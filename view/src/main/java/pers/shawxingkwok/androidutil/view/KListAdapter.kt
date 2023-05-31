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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.reflect.Method
import kotlin.reflect.KClass

/**
 * A simplified [ListAdapter] with easier usage and better performance.
 *
 * Also supports multiple kinds of [ViewHolder]s in [KListAdapter.Multiple].
 *
 * Usage example:
 *
 * ```
 * class ItemAdapter(
 *     scope: CoroutineScope,
 *     initialItems: List<User>,
 * )
 *     : KListAdapter<User, ItemTealBinding>(
 *         ItemTealBinding::class,
 *         scope,
 *         initialItems
 *     )
 * {
 *     override fun onViewHolderCreated(
 *         holder: ViewBindingHolder<ItemTealBinding>
 *     ) {
 *         ...
 *     }
 *
 *     override fun onBindViewHolder(
 *         holder: ViewBindingHolder<ItemTealBinding>,
 *         position: Int
 *     ) {
 *         holder.binding.tv.text = currentList[position].toString()
 *     }
 *
 *     override fun areItemsTheSame(
 *         oldItem: User,
 *         newItem: User
 *     ): Boolean {
 *         return newItem == oldItem
 *     }
 * }
 */
public abstract class KListAdapter<T, VB: ViewBinding>(
    private val bindingKClass: KClass<VB>,
    private val scope: CoroutineScope,
    initialItems: List<T>,
)
    : RecyclerView.Adapter<KListAdapter.ViewBindingHolder<VB>>()
{
    private val me = this

    public var currentList: List<T> = initialItems
        private set

    private val mUpdateCallback = AdapterListUpdateCallback(this)

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

    final override fun getItemCount(): Int = currentList.size

    protected open fun onCurrentListChanged(previousList: List<T>, currentList: List<T>){}

    // Max generation of currently scheduled runnable
    @Volatile private var mMaxScheduledGeneration = 0

    public open fun submitList(newList: List<T>, actionOnDone: (() -> Unit)? = null) {
        // incrementing generation means any currently-running diffs are discarded when they finish
        val runGeneration = ++mMaxScheduledGeneration

        if (newList == currentList) return

        val previousList = currentList

        fun execute(action: () -> Unit){
            currentList = newList.toList()
            action()
            onCurrentListChanged(previousList, currentList)
            actionOnDone?.invoke()
        }

        when{
            // fast simple remove all
            newList.isEmpty() -> {
                execute {
                    mUpdateCallback.onRemoved(0, previousList.size)
                }
                return
            }

            // fast simple insert first
            previousList.isEmpty() -> {
                execute {
                    mUpdateCallback.onInserted(0, newList.size)
                }
                return
            }
        }

        val callback = object : DiffUtil.Callback() {
            override fun getOldListSize() = previousList.size

            override fun getNewListSize(): Int = newList.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                me.areItemsTheSame(previousList[oldItemPosition], newList[newItemPosition])

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                me.areContentsTheSame(previousList[oldItemPosition], newList[newItemPosition])

            override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? =
                me.getChangePayload(previousList[oldItemPosition], newList[newItemPosition])
        }

        // compute in another thread.
        scope.launch(Dispatchers.Default) {
            val result = DiffUtil.calculateDiff(callback)

            // switch to the main thread and update UI if there is no submitted new list.
            if (mMaxScheduledGeneration == runGeneration)
                scope.launch {
                    execute {
                        result.dispatchUpdatesTo(mUpdateCallback)
                    }
                }
        }
    }

    internal fun <V: ViewBinding> getGetBinding(bindingKClass: KClass<V>) =
        bindingKClass.java
            .getMethod(
                "inflate",
                LayoutInflater::class.java,
                ViewGroup::class.java,
                Boolean::class.java
            )

    private val getBinding: Method by lazy(LazyThreadSafetyMode.NONE) { getGetBinding(bindingKClass) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewBindingHolder<VB> {
        val layoutInflater = LayoutInflater.from(parent.context)
        @Suppress("UNCHECKED_CAST")
        val binding = getBinding(null, layoutInflater, parent, false) as VB
        val holder = ViewBindingHolder(binding)
        onViewHolderCreated(holder)
        return holder
    }

    protected abstract fun onViewHolderCreated(holder: ViewBindingHolder<VB>)

    /**
     * Extends from [KListAdapter] and supports multiple kinds of [ViewHolder]s.
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
    public abstract class Multiple<T>(
        scope: CoroutineScope,
        initialItems: List<T>,
    )
        : KListAdapter<T, ViewBinding>(ViewBinding::class, scope, initialItems)
    {
        private val viewBindingHolderBuilders = mutableListOf<ViewBindingHolderBuilder<ViewBinding>>()

        protected abstract fun arrange(position: Int): ViewBindingHolderBuilder<ViewBinding>

        final override fun getItemViewType(position: Int): Int {
            val builder = arrange(position)
            return viewBindingHolderBuilders.indexOf(builder)
        }

        final override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ViewBindingHolder<ViewBinding> {
            val layoutInflater = LayoutInflater.from(parent.context)
            return viewBindingHolderBuilders[viewType].buildViewHolder(parent, layoutInflater)
        }

        final override fun onBindViewHolder(holder: ViewBindingHolder<ViewBinding>, position: Int) {
            val builder = arrange(position)
            builder.onBindViewBindingHolder(holder, position)
        }

        final override fun onViewHolderCreated(holder: ViewBindingHolder<ViewBinding>) {}

        public abstract inner class ViewBindingHolderBuilder<out VB : ViewBinding>(
            bindingKClass: KClass<VB>,
        ) {
            init {
                viewBindingHolderBuilders += this
            }

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
                onViewBindingHolderCreated(holder)
                return holder
            }

            public abstract fun onViewBindingHolderCreated(holder: ViewBindingHolder<@UnsafeVariance VB>)

            public abstract fun onBindViewBindingHolder(
                holder: ViewBindingHolder<@UnsafeVariance VB>,
                position: Int,
            )
        }
    }

    public class ViewBindingHolder<out VB : ViewBinding>(public val binding: VB) : ViewHolder(binding.root)
}