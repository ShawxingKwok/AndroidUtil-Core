package pers.apollokwok.android

import android.util.Log
import android.util.Log.e
import android.util.Log.w
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Exception

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class LogTest {
    val MLog = object : KLog("Apollo", BuildConfig.DEBUG){}

    val tr = Exception("fJ")

    @Test
    fun a() {
        MLog("on Debug")

        class E{
            init {
                MLog.e("F", 1)
                MLog.e("F", 1, tr = tr)
            }
        }
        val e =E()
    }
}