package pers.shawxingkwok.androidutil.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import pers.shawxingkwok.androidutil.view.KFragment.OnClick
import pers.shawxingkwok.ktutil.KReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.isAccessible

/**
 * Sets [binding] via [VB] and [bindingKClass].
 * Also supports [withBinding] and [OnClick].
 */
public abstract class KFragment<VB: ViewBinding>(private val bindingKClass: KClass<VB>) : Fragment() {
    private val actionsOnCreateView: MutableList<(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) -> Unit> = mutableListOf()
    private val actionsOnDestroyView: MutableList<() -> Unit> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        actionsOnCreateView.forEach { it(inflater, container, savedInstanceState) }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        actionsOnDestroyView.forEach { it() }
    }

    //region binding
    private var _binding: VB? = null

    /**
     * Alive between [onCreateView] and [onDestroyView].
     */
    protected val binding: VB get() = _binding!!
    private var bindingAlive: Boolean = false

    init {
        actionsOnCreateView += { inflater, container, _ ->
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

            bindingAlive = true
        }

        actionsOnDestroyView += {
            _binding = null
            bindingAlive = false
        }
    }
    //endregion

    /**
     * The background value is alive with [binding].
     *
     * Usage example:
     *
     * ```
     * val adapter by withBinding{ Adapter() }
     */
    protected fun <T> withBinding(initialize: () -> T): KReadWriteProperty<KFragment<VB>, T> =
        object  : KReadWriteProperty<KFragment<VB>, T> {
            var t: T? = null

            fun requireSafe(propName: String) =
                require(bindingAlive){
                    "In ${javaClass.canonicalName}, " +
                    "'$propName' should be called between 'onCreateView' and 'onDestroyView'."
                }

            override fun onDelegate(thisRef: KFragment<VB>, property: KProperty<*>) {
                actionsOnCreateView += { _, _, _ -> t = initialize() }
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

    //region OnClick
    /**
     * View of [viewId] would be set [View.OnClickListener] which calls the annotated function.
     *
     * Usage example:
     *
     * ```
     * @OnClick(R.id.btn_confirm)
     * private fun onClick1(){
     *     ...
     * }
     *
     * @OnClick(R.id.btn_cancel)
     * private fun onClick2(){
     *     ...
     * }
     *```
     *
     * Remember to suppress 'unused' warning if annotated by [OnClick].
     */
    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.FUNCTION)
    public annotation class OnClick(@IdRes val viewId: Int)

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

    init {
        actionsOnCreateView += { _, _, _ ->
            functionIDPairs.forEach { (function, id) ->
                val view = binding.root.findViewById<View>(id)

                view.setOnClickListener {
                    function.call(this)
                }
            }
        }
    }
    //endregion
}