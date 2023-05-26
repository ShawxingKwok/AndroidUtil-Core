@file:Suppress("FunctionName")

package pers.apollokwok.android

import android.util.Log
import android.util.Log.getStackTraceString
import androidx.core.app.NotificationCompatSideChannelService
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
    ){
        if (!isDebug) return

        val trace = Exception().stackTrace[2].toString()
        val header = trace.substringBefore("$")
        val tail = "(" + trace.substringAfter("(")
        val positionSuffix = "\nat $header$tail"

        val msg = info.joinToString() + positionSuffix

        Log.d(tag, msg)
    }

    public fun v(vararg info: Any?, tag: String = defaultTag){
        if (isDebug)
            Log.v(tag, info.joinToString())
    }

    public fun i(vararg info: Any?, tag: String = defaultTag){
        Log.i(tag, info.joinToString())
    }

    public fun w(vararg info: Any?, tag: String = defaultTag){
        Log.w(tag, info.joinToString())
    }

    public fun e(vararg info: Any?, tag: String = defaultTag, ex: Exception? = null){
        if (ex != null)
            Log.e(tag, info.joinToString(), ex)
        else {
            val newEx = Exception(info.joinToString())
            val trace = getStackTraceString(newEx)
            Log.e(tag, trace)
        }
    }
}