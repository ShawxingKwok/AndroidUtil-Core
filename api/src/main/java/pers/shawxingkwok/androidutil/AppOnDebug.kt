package pers.shawxingkwok.androidutil

import kotlin.reflect.full.staticProperties

internal val AppOnDebug = Class.forName(AppContext.packageName + "." + "BuildConfig")
    .kotlin
    .staticProperties
    .first { it.name == "DEBUG" }.get() as Boolean