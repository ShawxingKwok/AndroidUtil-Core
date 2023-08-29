package pers.shawxingkwok.androidutil

import android.view.View
import android.widget.Button
import androidx.core.view.isVisible
import org.junit.Test

internal class KLogTest {
    @Test
    fun start(){
        foo {
            KLog.d(intArrayOf(1, 2, 3)) // safe inline blocks
            KLog.d(arrayOf(2)) // safe inline blocks
            KLog.d(listOf(1, 2)) // safe inline blocks
        }

        bar {
            KLog.d("", "(KLogTest.kt)") // crossinline blocks
        }
    }
}