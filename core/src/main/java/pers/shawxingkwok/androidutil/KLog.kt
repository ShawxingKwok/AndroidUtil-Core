@file:Suppress("FunctionName")

package pers.shawxingkwok.androidutil

import android.util.Log
import java.io.PrintWriter
import java.io.StringWriter
import java.net.UnknownHostException
import kotlin.reflect.full.staticProperties

/**
 * A log util extended from android logcat.
 *
 * Usage example:
 * ```
 * // Set your log class.
 * object MLog : AndroidKLog("SXK", BuildConfig.DEBUG)
 *
 * // Log somewhere.
 * MLog("Jack")
 * MLog.i(20, 1.8)
 *
 * // Logs:
 * D    Jack (xx.kt:10)
 * I    20, 1.8 (xx.kt:11)
 *```
 *
 * Note: for released android libraries, on level 'VERBOSE' and 'DEBUG', its logs are not visible;
 * and on level 'INFO' and 'DEBUG', traces are not visible.
 *
 * If you are developing an application, remember to set
 * '-assumenosideeffects class pers.shawxingkwok.android.KLog{ *; }' in 'proguard-rules.pro' in the app module to
 * remove logs in the released apk.
 */
public abstract class KLog(
    private val isDebug: Boolean,
    private val defaultTagHeader: String? = null,
) {
    public companion object : KLog(
        isDebug = Class.forName(AppContext.packageName + "." + "BuildConfig")
            .kotlin
            .staticProperties
            .first { it.name == "DEBUG" }.get() as Boolean,

        defaultTagHeader = "SXK",
    )

    private fun log(level: Int, customedTag: String?, messages: Array<out Any?>, tr: Throwable?){
        if (!isDebug && level < Log.WARN)
            return

        val traceElement = Thread.currentThread().stackTrace[4]
        val trace = "(${traceElement.fileName}:${traceElement.lineNumber})"

        val tag = when{
            customedTag != null -> customedTag
            defaultTagHeader != null -> "$defaultTagHeader ${traceElement.fileName.substringBeforeLast(".")}"
            else -> traceElement.fileName.substringBeforeLast(".")
        }

        var tracedMsg = messages.joinToString() + " " + trace

        if (tr != null) run{
            var t: Throwable? = tr

            while (t != null) {
                if (t is UnknownHostException)
                    return@run

                t = t.cause
            }

            val sw = StringWriter()
            val pw = PrintWriter(sw)
            tr.printStackTrace(pw)
            pw.flush()
            tracedMsg += "\n" + sw.toString()
        }

        Log.println(level, tag, tracedMsg)
    }

    public fun v(vararg messages: Any?){
        log(Log.VERBOSE, null, messages, null)
    }

    public fun v(vararg messages: Any?, tag: String){
        log(Log.VERBOSE, tag, messages, null)
    }

    public operator fun invoke(vararg messages: Any?){
        log(Log.DEBUG, null, messages, null)
    }

    public operator fun invoke(vararg messages: Any?, tag: String){
        log(Log.DEBUG, tag, messages, null)
    }

    public fun i(vararg messages: Any?){
        log(Log.INFO, null, messages, null)
    }

    public fun i(vararg messages: Any?, tag: String){
        log(Log.INFO, tag, messages, null)
    }

    public fun w(vararg messages: Any?){
        log(Log.WARN, null, messages, null)
    }

    public fun w(vararg messages: Any?, tag: String){
        log(Log.WARN, tag, messages, null)
    }

    public fun e(vararg messages: Any?){
        log(Log.ERROR, null, messages, null)
    }

    public fun e(vararg messages: Any?, tag: String){
        log(Log.ERROR, tag, messages, null)
    }

    public fun e(vararg messages: Any?, tr: Throwable){
        log(Log.ERROR, null, messages, tr)
    }

    public fun e(vararg messages: Any?, tag: String, tr: Throwable){
        log(Log.ERROR, tag, messages, tr)
    }
}