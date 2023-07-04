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
        val isDebug = (AppContext.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        println(isDebug)

        Log.d("SX", "1")
        Log.wtf("SX", "2")
        Log.e("SX", "3")
        Log.d("SX", "4")

        KLog("")
        KLog(null, " (ExampleInstrumental.kt:23)")


        class E{
            init {
                KLog.e("F", 1)
                KLog.e("F", 1, tr = tr)
            }
        }

        val e = E()
    }
}