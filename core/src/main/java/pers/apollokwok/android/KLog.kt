@file:Suppress("FunctionName")

package pers.apollokwok.android

import android.util.Log
import android.util.Log.getStackTraceString
import pers.apollokwok.ktutil.updateIf

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
    public operator fun invoke(
        vararg info: Any?,
        tag: String = defaultTag,
        e: Exception? = null,
    ){
        if(!isDebug) return

        val position = when (e) {
            null -> {
                val trace = Exception().stackTrace[2].toString()
                val header = trace.substringBefore("$")
                val tail = "(" + trace.substringAfter("(")
                "$header$tail"
            }

            else -> getStackTraceString(e)
        }

        val msg = info.joinToString(postfix = "\nat $position")

        Log.println(Log.DEBUG, tag, msg)
    }

    private fun log(
        priority: Int,
        info: Array<out Any?>,
        tag: String,
        e: Exception?,
    ) {
        val msg = info.joinToString()
            .updateIf({ e != null }) {
                it + "\nat" + getStackTraceString(e)
            }

        Log.println(priority, tag, msg)
    }

    public fun v(vararg info: Any?, tag: String = defaultTag, e: Exception? = null){
        if (isDebug)
            log(Log.VERBOSE, info, tag, e)
    }

    public fun i(vararg info: Any?, tag: String = defaultTag, e: Exception? = null){
        log(Log.INFO, info, tag, e)
    }

    public fun w(vararg info: Any?, tag: String = defaultTag, e: Exception? = null){
        log(Log.WARN, info, tag, e)
    }

    public fun e(vararg info: Any?, tag: String = defaultTag, e: Exception? = null){
        log(Log.ERROR, info, tag, e)
    }
}