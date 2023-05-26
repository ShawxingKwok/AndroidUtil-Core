package pers.apollokwok.android

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
class MLog : KLog("Apollo", BuildConfig.DEBUG) {
    @Test
    fun a() {
        e("e", ex = Exception("FAM"))
        e("d")
    }
}