package pers.shawxingkwok.mylibrary

import android.content.Context
import androidx.startup.Initializer
import pers.shawxingkwok.androidutil.KLog

class MyInitializer : Initializer<Unit>{
    override fun create(context: Context) {
        KLog.d("")
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}