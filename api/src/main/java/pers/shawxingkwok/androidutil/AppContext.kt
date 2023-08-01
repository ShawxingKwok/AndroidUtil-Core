package pers.shawxingkwok.androidutil

import android.content.Context
import androidx.startup.Initializer

/**
 * It is the static applicationContext for you to get anywhere. In this way,
 * your `Database`, `DAO`, `SharedPreferences`, `DataStore` and some other tools
 * could be static, of which the profit is much more considerable than the
 * additional resource memory.
 */
public lateinit var AppContext: Context
    private set

internal class AppContextInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        AppContext = context
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}