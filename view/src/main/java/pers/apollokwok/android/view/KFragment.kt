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
 * See TODO(website)
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
    protected val binding: VB get() = _binding!!
    private var bindingAlive: Boolean = false

    init {
        actionsOnCreateView += { inflater, container, savedInstanceState ->
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
     * val adapter by bind{ Adapter() }
     */
    protected fun <T> withBinding(initialize: () -> T): KReadWriteProperty<KFragment<VB>, T> =
        object  : KReadWriteProperty<KFragment<VB>, T>{
            var t: T? = null

            fun requireSafe(propName: String) =
                require(bindingAlive){
                    "In ${javaClass.canonicalName}, " +
                    "'$propName' should be called between 'onCreateView' and 'onDestroyView'."
                }

            override fun checkProperty(thisRef: KFragment<VB>, property: KProperty<*>) {
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
     */
    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.FUNCTION)
    @SuppressLint("NonConstantResourceId")
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