package pers.shawxingkwok.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import pers.shawxingkwok.androidutil.KLog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        KLog.d("")
    }
}