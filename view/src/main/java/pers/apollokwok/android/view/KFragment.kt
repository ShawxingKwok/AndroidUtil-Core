package pers.apollokwok.android.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.viewbinding.ViewBinding
import pers.apollokwok.ktutil.KReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.isAccessible

/**
 * Owns [binding] of type [VB], function [withView], and annotation [OnClick].
 *
 * Usage example:
 * ```
 * class MainFragment : KFragment<FragmentMainBinding>(FragmentMainBinding::class) {
 *
 *     private val topAdapter: SingleItemAdapter by withView {
 *         SingleItemAdapter(
 *             scope = lifecycleScope,
 *             initialItems = (0..20).map(::User),
 *         )
 *         .also { binding.rvTop.adapter = it }
 *     }
 *
 *     private val bottomAdapter: MultipleItemAdapter by withView {
 *         MultipleItemAdapter(
 *             scope = lifecycleScope,
 *             initialItems = (100..120).map(::User)
 *         )
 *         .also { binding.rvBottom.adapter = it }
 *     }
 *
 *     @SuppressLint("NonConstantResourceId")
 *     @OnClick(R.id.btn)
 *     private fun onClick1(){
 *         val newTopUserId = topAdapter.currentList.last().id + 1
 *         val newTopList = topAdapter.currentList + User(newTopUserId)
 *         val newBottomList = newTopList.map { User(it.id + 100) }
 *         topAdapter.submitList(newTopList)
 *         bottomAdapter.submitList(newBottomList)
 *     }
 * }
 */
public abstract class KFragment<VB: ViewBinding>(private val bindingKClass: KClass<VB>) : Fragment() {
    private var _binding: VB? = null
    protected val binding: VB get() = _binding!!

    private val functionIDPairs = this::class
        .declaredMemberFunctions
        .mapNotNull { function ->
            val id = function.findAnnotation<OnClick>()?.viewId ?: return@mapNotNull null
            require(function.typeParameters.none() && function.parameters.size == 1){
                "${function.name} is annotated with OnClick and should own no parameters " +
                "except its owner instance."
            }
            function.isAccessible = true
            function to id
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // set binding
        @Suppress("UNCHECKED_CAST")
        _binding = bindingKClass
            .java
            .getMethod(
                "inflate",
                LayoutInflater::class.java,
                ViewGroup::class.java,
                Boolean::class.java
            )
            .invoke(null, inflater, container, false) as VB

        // set onClick
        functionIDPairs.forEach {(function, id) ->
            val view = binding.root.findViewById<View>(id)

            view.setOnClickListener {
                function.call(this)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actionsOnViewCreated.forEach { it() }
        viewObjectsInitialized = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        actionsOnDestroyView.forEach { it() }
        viewObjectsInitialized = false
    }

    private val actionsOnViewCreated: MutableList<() -> Unit> = mutableListOf()
    private val actionsOnDestroyView: MutableList<() -> Unit> = mutableListOf()
    private var viewObjectsInitialized: Boolean = false

    private fun requireSafe(propName: String) =
        require(viewObjectsInitialized){
            "In ${javaClass.canonicalName}, '$propName' should be called between 'onViewCreated' and 'onDestroyView'."
        }

    /**
     * The background value would be [init] every [onViewCreated], and freed every [onDestroyView].
     *
     * Usage example:
     *
     * ```
     * val adapter by withView{ Adapter() }
     */
    protected fun <T> withView(init: () -> T): KReadWriteProperty<KFragment<VB>, T> =
        object  : KReadWriteProperty<KFragment<VB>, T>{
            var t: T? = null

            override fun checkProperty(thisRef: KFragment<VB>, property: KProperty<*>) {
                actionsOnViewCreated += { t = init() }
                actionsOnDestroyView += { t = null }
            }

            override fun getValue(thisRef: KFragment<VB>, property: KProperty<*>): T {
                requireSafe(property.name)
                @Suppress("UNCHECKED_CAST")
                return t as T
            }

            override fun setValue(thisRef: KFragment<VB>, property: KProperty<*>, value: T) {
                requireSafe(property.name)
                t = value
            }
        }

    /**
     * View of [viewId] would be set [View.OnClickListener] which calls the annotated function.
     */
    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.FUNCTION)
    @SuppressLint("NonConstantResourceId")
    public annotation class OnClick(@IdRes val viewId: Int)
}