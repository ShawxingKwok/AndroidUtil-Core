@file:Suppress("FunctionName")

package pers.apollokwok.android

import android.util.Log

/**
 * A log util extended from android logcat.
 *
 * Usage example:
 * ```
 * object MLog : KLog("Apollo", BuildConfig.DEBUG)
 *
 * // somewhere
 * MLog.d(name, age)
 */
public abstract class KLog(
    private val defaultTag: String,
    private val isDebug: Boolean,
) {
    private fun log(priority: Int, tag: String, info: Array<out Any?>){
        if (isDebug)
            Log.println(priority, tag, info.joinToString(", "))
    }

    public fun v(vararg info: Any?){
        log(Log.VERBOSE, defaultTag, info)
    }

    public fun d(vararg info: Any?){
        log(Log.DEBUG, defaultTag, info)
    }

    public fun i(vararg info: Any?){
        log(Log.INFO, defaultTag, info)
    }

    public fun w(vararg info: Any?){
        log(Log.WARN, defaultTag, info)
    }

    public fun e(vararg info: Any?){
        log(Log.ERROR, defaultTag, info)
    }

    public fun _v(tag: String, vararg info: Any?){
        log(Log.VERBOSE, tag, info)
    }

    public fun _d(tag: String, vararg info: Any?){
        log(Log.DEBUG, tag, info)
    }

    public fun _i(tag: String, vararg info: Any?){
        log(Log.INFO, tag, info)
    }

    public fun _w(tag: String, vararg info: Any?){
        log(Log.WARN, tag, info)
    }

    public fun _e(tag: String, vararg info: Any?){
        log(Log.ERROR, tag, info)
    }
}