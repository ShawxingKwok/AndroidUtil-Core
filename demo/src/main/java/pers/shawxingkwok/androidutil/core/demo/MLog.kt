package pers.shawxingkwok.androidutil.core.demo

import pers.shawxingkwok.androidutil.KLog

// use `internal` in a library
object MLog : KLog(
    isDebug = BuildConfig.DEBUG,
    id = "DEMO",
)