package pers.shawxingkwok.androidutil

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