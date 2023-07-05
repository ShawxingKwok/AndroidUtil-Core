package pers.shawxingkwok.androidutil

import android.content.Context
import androidx.startup.Initializer
import java.lang.Exception

public lateinit var AppContext: Context
    private set

internal class MyInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        AppContext = context
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}