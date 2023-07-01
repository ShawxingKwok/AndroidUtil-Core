package pers.shawxingkwok.androidutil.demos.core

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import pers.shawxingkwok.androidutil.KLog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MLog.v(1,2,3,4, tag = "Main")
        MLog(1)
        MLog.i(1, 2)
        MLog.w(1,3,4)
        MLog.e("Some unexpected.")

        try {
            error("Some error.")
        }catch (e : Exception){
            MLog.e("Explanation", tag = "Main", tr = e)
        }
    }
}