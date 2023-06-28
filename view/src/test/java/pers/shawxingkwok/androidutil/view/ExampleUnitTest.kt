package pers.shawxingkwok.androidutil.view

import androidx.viewbinding.ViewBinding
import org.junit.Test

import org.junit.Assert.*
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.ParameterizedType

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    abstract class TypeHelper<T>

    class TypeHelperImpl<T> : TypeHelper<T>()

    @Test
    fun withGenericBindingClass() {
        val genericOwner = TypeHelperImpl<String>()
        val type = genericOwner.javaClass.genericSuperclass as ParameterizedType
        type.let(::println)
        type.actualTypeArguments.forEach { println(it) }
        type.actualTypeArguments.first().let(::println)
    }
}