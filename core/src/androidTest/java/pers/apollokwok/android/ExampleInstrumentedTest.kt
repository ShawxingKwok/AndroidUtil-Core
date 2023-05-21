package pers.apollokwok.android

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class MLog : KLog("Apollo", BuildConfig.DEBUG) {
    @Test
    fun useAppContext() {
        d(1,2)
        _d("s", 1, 2, 3)

        i(3)
    }
}