package pers.shawxingkwok.androidutil.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import pers.shawxingkwok.androidutil.KLog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        KLog.d("hello", "JACK")

        KLog.d("hello", "ShawxingKwok")
    }
}