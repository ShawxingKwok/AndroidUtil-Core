package pers.shawxingkwok.androidutil

import android.annotation.SuppressLint
import android.content.Context
import androidx.startup.Initializer

internal class AppContextInitializer : Initializer<Unit> {
    companion object{
        @SuppressLint("StaticFieldLeak")
        var context: Context? = null
            private set
    }

    override fun create(context: Context) {
        AppContextInitializer.context = context
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}