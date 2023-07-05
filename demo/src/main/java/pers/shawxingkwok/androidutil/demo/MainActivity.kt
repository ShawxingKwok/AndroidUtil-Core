package pers.shawxingkwok.androidutil.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.apkfuns.logutils.LogUtils

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
// 输出参数
        LogUtils.d("12%s3%d45", "a", 0)

        class Person(val age: Int, val name: String, val score: Float)
        val person = Person(11, "pengwei", 37.5f)
        LogUtils.d(person)

// 对象为空
        LogUtils.d(null)

// 输出json（json默认debug打印）
        val json = "{'a':'b','c':{'aa':234,'dd':{'az':12}}}"
        LogUtils.json(json)

// 打印数据集合
        val list1: MutableList<Person> = ArrayList<Person>()
        for (i in 0..3) {
            list1.add(person)
        }
        LogUtils.d(list1)

// 打印数组
        val doubles = arrayOf(
            doubleArrayOf(1.2, 1.6, 1.7, 30.0, 33.0),
            doubleArrayOf(1.2, 1.6, 1.7, 30.0, 33.0),
            doubleArrayOf(1.2, 1.6, 1.7, 30.0, 33.0),
            doubleArrayOf(1.2, 1.6, 1.7, 30.0, 33.0)
        )
        LogUtils.d(doubles)

// 自定义tag
        LogUtils.tag("我是自定义tag").d("我是打印内容")

// 其他用法

// 其他用法
        LogUtils.v("12345")
        LogUtils.i("12345")
        LogUtils.w("12345")
        LogUtils.e("12345")
        LogUtils.wtf("12345")

        // KLog.v()
        // KLog(savedInstanceState)
        // KLog.i(savedInstanceState)
        // KLog.w(savedInstanceState)
        // KLog.e(savedInstanceState)
    }
}