@file:Suppress("LeakingThis")

package pers.shawxingkwok.androidutil.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import pers.shawxingkwok.ktutil.lazyFast
import java.lang.reflect.Method
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

/**
 * A simplified [ListAdapter] with easier usage and better performance,
 * also supporting multiple kinds of items.
 *
 * Usage example:
 *
 * ```
 * class MultipleItemsAdapter(
 *     scope: CoroutineScope,
 *     initialItems: List<User>,
 * )
 *     : KListAdapter.Multiple<User>(scope, initialItems)
 * {
 *     private val tealBuilder =
 *         object : ViewBindingHolderBuilder<ItemTealBinding>(
 *             ItemTealBinding::class
 *         ){
 *             override fun onViewBindingHolderCreated(
 *                 holder: ViewBindingHolder<ItemTealBinding>
 *             ) {
 *                 ...
 *             }
 *
 *             override fun onBindViewBindingHolder(
 *                 holder: ViewBindingHolder<ItemTealBinding>,
 *                 position: Int,
 *             ) {
 *                 holder.binding.tv.text =
 *                     currentList[position].toString()
 *             }
 *         }
 *
 *     private val purpleBuilder =
 *         object : ViewBindingHolderBuilder<ItemPurpleBinding>(
 *             ItemPurpleBinding::class
 *         ){
 *             override fun onViewBindingHolderCreated(
 *                 holder: ViewBindingHolder<ItemPurpleBinding>
 *             ) {
 *                 ...
 *             }
 *
 *             override fun onBindViewBindingHolder(
 *                 holder: ViewBindingHolder<ItemPurpleBinding>,
 *                 position: Int,
 *             ) {
 *                 holder.binding.tv.text =
 *                     currentList[position].toString()
 *             }
 *         }
 *
 *     override fun areItemsTheSame(
 *         oldItem: User,
 *         newItem: User,
 *     ): Boolean {
 *         return oldItem == newItem
 *     }
 *
 *     override fun arrange(position: Int): ViewBindingHolderBuilder<ViewBinding> =
 *         when{
 *             position % 2 == 0 -> tealBuilder
 *             else -> purpleBuilder
 *         }
 * }
 */
public abstract class KListAdapter<T>(
    private val scope: CoroutineScope,
    initialItems: List<T>,
)
    : RecyclerView.Adapter<KListAdapter.ViewBindingHolder<ViewBinding>>()
{
    public var currentList: List<T> = initialItems
        private set

    private val updateCallback = AdapterListUpdateCallback(this)

    /**
     * @see DiffUtil.Callback.areItemsTheSame
     */
    protected abstract fun areItemsTheSame(oldItem: T, newItem: T): Boolean

    /**
     * @see DiffUtil.Callback.areContentsTheSame
     */
    protected open fun areContentsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem

    /**
     * @see DiffUtil.Callback.getChangePayload
     */
    protected open fun getChangePayload(oldItem: T, newItem: T): Any? = null

    /**
     * @suppress
     */
    final override fun getItemCount(): Int = arrangement.size

    protected open fun onCurrentListChanged(previousList: List<T>, currentList: List<T>) {}

    // Max generation of currently scheduled runnable
    @Volatile
    private var maxScheduledGeneration = 0

    private val diffDispatcher = run {
        val workQueue = LinkedBlockingQueue<Runnable>()
        val executor = ThreadPoolExecutor(2, 2, 0L, TimeUnit.MILLISECONDS, workQueue)
        executor.asCoroutineDispatcher()
    }

    public fun submitList(newList: List<T>, onFinish: (() -> Unit)? = null) {
        // incrementing generation means any currently-running diffs are discarded when they finish
        val runGeneration = ++maxScheduledGeneration

        if (newList == currentList) return

        val previousList = currentList

        fun execute(action: () -> Unit) {
            currentList = newList.toList()
            action()
            onCurrentListChanged(previousList, currentList)

            arrangement.clear()
            arrange(arrangement)
            allHolderBuilders.clear()
            allHolderBuilders += arrangement

            onFinish?.invoke()
        }

        when {
            // fast simple remove all
            newList.isEmpty() -> {
                execute {
                    updateCallback.onRemoved(0, previousList.size)
                }
                return
            }

            // fast simple insert first
            previousList.isEmpty() -> {
                execute {
                    updateCallback.onInserted(0, newList.size)
                }
                return
            }
        }

        val callback = object : DiffUtil.Callback() {
            override fun getOldListSize() = previousList.size

            override fun getNewListSize(): Int = newList.size

            private val me = this@KListAdapter

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                me.areItemsTheSame(previousList[oldItemPosition], newList[newItemPosition])

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                me.areContentsTheSame(previousList[oldItemPosition], newList[newItemPosition])

            override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? =
                me.getChangePayload(previousList[oldItemPosition], newList[newItemPosition])
        }

        // compute in another thread.
        scope.launch(diffDispatcher) {
            val result = DiffUtil.calculateDiff(callback)

            // switch to the main thread and update UI if there is no submitted new list.
            if (maxScheduledGeneration == runGeneration)
                scope.launch {
                    execute {
                        result.dispatchUpdatesTo(updateCallback)
                    }
                }
        }
    }

    private val arrangement by lazy {
        mutableListOf<HolderBuilder<ViewBinding>>().also(::arrange)
    }

    private val allHolderBuilders by lazyFast { arrangement.toMutableSet() }

    /**
     * @suppress
     */
    final override fun getItemViewType(position: Int): Int {
        val builder = arrangement[position]
        return allHolderBuilders.indexOf(builder)
    }

    /**
     * @suppress
     */
    final override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewBindingHolder<ViewBinding> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val builder = allHolderBuilders.elementAt(viewType)
        return builder.buildViewHolder(parent, layoutInflater)
    }

    /**
     * @suppress
     */
    final override fun onBindViewHolder(holder: ViewBindingHolder<ViewBinding>, position: Int) {
        val builder = arrangement[position]
        builder.onBindHolder(holder, position)
    }

    protected abstract fun arrange(builders: MutableList<HolderBuilder<ViewBinding>>)

    public abstract class HolderBuilder<out VB : ViewBinding>(
        bindingKClass: KClass<VB>,
    ) {
        private val getBinding: Method = getGetBinding(bindingKClass)

        @Suppress("UNCHECKED_CAST")
        internal fun buildViewHolder(
            parent: ViewGroup,
            layoutInflater: LayoutInflater,
        )
            : ViewBindingHolder<@UnsafeVariance VB>
        {
            val binding = getBinding(null, layoutInflater, parent, false) as VB
            val holder = ViewBindingHolder(binding)
            onHolderCreated(holder)
            return holder
        }

        public abstract fun onHolderCreated(holder: ViewBindingHolder<@UnsafeVariance VB>)

        public abstract fun onBindHolder(holder: ViewBindingHolder<@UnsafeVariance VB>, position: Int)
    }

    public class ViewBindingHolder<out VB : ViewBinding>(public val binding: VB) : ViewHolder(binding.root)

    private companion object{
        private val cache = mutableMapOf<KClass<out ViewBinding>, Method>()

        private fun getGetBinding(bindingKClass: KClass<out ViewBinding>): Method =
            cache.getOrPut(bindingKClass) {
                bindingKClass.java
                    .getMethod(
                        "inflate",
                        LayoutInflater::class.java,
                        ViewGroup::class.java,
                        Boolean::class.java
                    )
            }
    }
}