package pers.shawxingkwok.androidutil

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
    object MLog : KLog(BuildConfig.DEBUG)

    val tr = Exception("fJ")

    @Test
    fun a() {
        KLog("")
        KLog(null, " (ExampleInstrumental.kt:23)")

        MLog("on Debug")

        class E{
            init {
                KLog.e("F", 1)
                KLog.e("F", 1, tr = tr)
            }
        }

        val e = E()
    }
}