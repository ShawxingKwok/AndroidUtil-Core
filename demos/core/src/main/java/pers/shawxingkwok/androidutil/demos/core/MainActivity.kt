package pers.shawxingkwok.androidutil.demos.core

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MLog(1)
        MLog.i(1, 2)
        MLog.e("Some unexpected.")

        try {
            error("Some error.")
        }catch (e : Exception){
            MLog.e("Explanation", tr = e)
        }
    }
}