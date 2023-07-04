package pers.shawxingkwok.androidutil.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.println(Log.ASSERT, "SF", "gfi")
        Log.wtf("SF", "f")
        Log.e("SF", "f")
    }
}