package pers.shawxingkwok.androidutil

import android.content.Context
import androidx.startup.Initializer

/**
 * See [docs](https://shawxingkwok.github.io/ITWorks/docs/android/util-core/#appcontext).
 */
public lateinit var AppContext: Context
    private set

/**
 * See [docs](https://shawxingkwok.github.io/ITWorks/docs/android/util-core/#appcontext).
 */
public class AppContextInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        AppContext = context
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}