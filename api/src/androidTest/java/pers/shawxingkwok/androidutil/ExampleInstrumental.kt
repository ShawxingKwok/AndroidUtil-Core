package pers.shawxingkwok.androidutil

import android.content.pm.ApplicationInfo
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class LogTest {

    val tr = Exception("fJ")

    @Test
    fun a() {
        KLog("")

        object {
            init {
                val tr = java.lang.Exception()
                KLog("d")
                KLog.i("i")
                KLog.e("e")
                KLog.e("et", tr = tr)
            }
        }
    }
}