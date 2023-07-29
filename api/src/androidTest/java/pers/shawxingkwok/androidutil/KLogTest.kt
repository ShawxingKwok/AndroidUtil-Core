package pers.shawxingkwok.androidutil

import android.view.View
import android.widget.Button
import androidx.core.view.isVisible
import org.junit.Test

internal class KLogTest {
    @Test
    fun start(){
        foo {
            KLog.d("") // safe inline blocks
        }

        bar {
            KLog.d("", "(KLogTest.kt)") // crossinline blocks
        }
    }
}