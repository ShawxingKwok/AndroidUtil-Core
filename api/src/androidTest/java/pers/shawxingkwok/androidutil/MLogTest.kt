@file:Suppress("NO_EXPLICIT_VISIBILITY_IN_API_MODE_WARNING")

package pers.shawxingkwok.androidutil
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MLogTest {
    @Test
    fun start(){
        MLog.d("hello")
    }
}