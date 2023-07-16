package pers.shawxingkwok.androidutil

import org.junit.Test

internal class KLogTest {
    @Test
    fun start(){
        foo {
            KLog.d("") // safe inline blocks
        }

        bar {
            KLog.d("", tagPrefix = "(KLogTest.kt)") // crossinline blocks
        }
    }
}