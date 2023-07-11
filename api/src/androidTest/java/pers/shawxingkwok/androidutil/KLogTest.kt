package pers.shawxingkwok.androidutil

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import pers.shawxingkwok.ktutil.mutableLazy


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class KLogTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        KLog.d("")
        bar {
            KLog.d("bar")
        }
        var x by mutableLazy {
            foo {
                KLog.d("foo")
            }

            KLog.d("")
        }
        x
    }
}