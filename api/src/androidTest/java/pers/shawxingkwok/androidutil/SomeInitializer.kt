@file:Suppress("NO_EXPLICIT_VISIBILITY_IN_API_MODE_WARNING", "NO_EXPLICIT_RETURN_TYPE_IN_API_MODE_WARNING")

package pers.shawxingkwok.androidutil

import android.content.Context
import androidx.startup.Initializer

class SomeInitializer : Initializer<Unit>{
    override fun create(context: Context) {
        // type your own pre-start task
    }

    override fun dependencies() = listOf(
        AppContextInitializer::class.java,
        // type other needed initializer classes if any
    )
}