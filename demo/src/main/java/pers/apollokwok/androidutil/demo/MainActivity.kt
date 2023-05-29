package pers.apollokwok.androidutil.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MLog(javaClass.simpleName)
        MLog.v(javaClass.simpleName)
        MLog.i(javaClass.simpleName)
        MLog.w(javaClass.simpleName)
        MLog.e(javaClass.simpleName)
        MLog.e(javaClass.simpleName, Exception())
    }
}