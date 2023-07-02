package pers.shawxingkwok.androidutil.core.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // message arguments are all variable.
        MLog.v(1, 2)
        MLog(1) // this calls with level `DEBUG`
        MLog.i(1, 2)
        MLog.w(1, 3, 4)
        MLog.e("Unexpected.")

        // tag prefixes are all assignable.
        MLog("d", tagPrefix = "X")
        MLog.i("i", tagPrefix = "Y")

        // `throwable` is assignable in `MLog.e`
        try {
            error("Z")
        }catch (e : Exception){
            MLog.e("Explanation", tr = e)
        }
    }
}