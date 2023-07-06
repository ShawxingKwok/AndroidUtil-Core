@file:Suppress("FunctionName")

package pers.shawxingkwok.androidutil

import android.content.pm.ApplicationInfo
import android.util.Log
import pers.shawxingkwok.ktutil.updateIf
import kotlin.reflect.KProperty0

private val AppOnDebug = (AppContext.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0

/**
 * A log util based on the official android log.
 * - Has a file name in the tag infix.
 * - Takes [id] as a global tag suffix.
 * - Has a trace link at the message end.
 * - Unimportant logs are automatically undone after released.
 *
 * For a released library on a debugging app, logs on level `V` and `D` are undone,
 * and others would be printed out to the console.
 *
 * For all code on a released app, logs on level `V`, `D`, and `I` are undone, and others would be passed to
 * [warnAfterRelease] and [errorAfterRelease] in which I suggest sending info to remote.
 *
 * If you are developing a
 * >
 * ### Usage example:
 *
 * Set your log class.
 * ```
 * internal object MLog : AndroidKLog(BuildConfig.DEBUG, "DEMO")
 * ```
 *
 * Log in MainActivity.kt
 * ```
 * // message arguments are all variable.
 * MLog.v(1, 2)
 * MLog(1) // this calls with level `DEBUG`
 * MLog.i(1, 2)
 * MLog.w(1, 3, 4)
 * MLog.e("Unexpected.")
 *
 * // tag prefixes are all assignable.
 * MLog("d", tagPrefix = "X")
 * MLog.i("i", tagPrefix = "Y")
 *
 * // `throwable` is assignable in `MLog.e`
 * try {
 *     error("Z")
 * }catch (e : Exception){
 *     MLog.e("Explanation", tr = e)
 * }
 * ```
 *
 * Logs:
 * ```
 * MainActivity DEMO    per...demo  V  1, 2 (MainActivity.kt:12)
 * MainActivity DEMO    per...demo  D  1 (MainActivity.kt:13)
 * MainActivity DEMO    per...demo  I  1, 2 (MainActivity.kt:14)
 * MainActivity DEMO    per...demo  W  1, 3, 4 (MainActivity.kt:15)
 * MainActivity DEMO    per...demo  E  Unexpected. (MainActivity.kt:16)
 * X MainActivity DEMO  per...demo  D  d (MainActivity.kt:19)
 * Y MainActivity DEMO  per...demo  I  i (MainActivity.kt:20)
 * MainActivity DEMO    per...demo  E  Explanation (MainActivity.kt:26)
 *                                          java.lang.IllegalStateException: Z
 *                                        	at pers.shawxingkwok.androidutil.core.demo.MainActivity.onCreate(MainActivity.kt:24)
 *                                          at ...
 *```
 */

/**
 * learning or junit test
 *      KLog
 *
 * system code
 *
 *
 * open library
 *
 * app submodules
 *
 */

public abstract class KLog(
    private val id: String,
    private val onDebug: Boolean,
) {
    public companion object : KLog("KLOG", AppOnDebug)

    private fun Any?.toMsg(): String =
        when(this){
            is KProperty0<*> -> "${name}: ${get().toMsg()}"
            is Array<*> ->  joinToString(prefix = "[", postfix = "]"){ it.toMsg() }
            is ByteArray -> joinToString(prefix = "[", postfix = "]")
            is ShortArray -> joinToString(prefix = "[", postfix = "]")
            is IntArray -> joinToString(prefix = "[", postfix = "]")
            is LongArray -> joinToString(prefix = "[", postfix = "]")
            is FloatArray -> joinToString(prefix = "[", postfix = "]")
            is DoubleArray -> joinToString(prefix = "[", postfix = "]")
            is BooleanArray -> joinToString(prefix = "[", postfix = "]")
            is CharArray -> joinToString(prefix = "[", postfix = "]")
            else -> toString()
        }

    protected fun log(
        level: Int,
        tagPrefix: String?,
        obj: Any?,
        tr: Throwable?,
    ){
        when{
            onDebug -> {}
            AppOnDebug -> if (level < Log.WARN) return
            else -> return
        }

        val traceElement = Thread.currentThread().stackTrace[4]

        val tag =
            listOfNotNull(
                tagPrefix,
                "(${traceElement.fileName}:${traceElement.lineNumber})",
                id,
            )
            .joinToString(" ")

        val trTraceOnNewLine = Log.getStackTraceString(tr).updateIf({ it.any() }){ "\n" + it }

        val msgWithTrace = "${obj.toMsg()}$trTraceOnNewLine"

        Log.println(level, tag, msgWithTrace)
    }

    public open fun v(obj: Any?){
        log(Log.VERBOSE, null, obj, null)
    }

    public open fun v(obj: Any?, tagPrefix: String){
        log(Log.VERBOSE, tagPrefix, obj, null)
    }

    public fun d(obj: Any?){
        log(Log.DEBUG, null, obj, null)
    }

    public fun d(obj: Any?, tagPrefix: String){
        log(Log.DEBUG, tagPrefix, obj, null)
    }

    public fun i(obj: Any?){
        log(Log.INFO, null, obj, null)
    }

    public fun i(obj: Any?, tagPrefix: String){
        log(Log.INFO, tagPrefix, obj, null)
    }

    public fun w(obj: Any?){
        log(Log.WARN, null, obj, null)
    }

    public fun w(obj: Any?, tagPrefix: String){
        log(Log.WARN, tagPrefix, obj, null)
    }

    public fun e(obj: Any?){
        log(Log.ERROR, null, obj, null)
    }

    public fun e(obj: Any?, tagPrefix: String){
        log(Log.ERROR, tagPrefix, obj, null)
    }

    public fun e(obj: Any?, tr: Throwable){
        log(Log.ERROR, null, obj, tr)
    }

    public fun e(obj: Any?, tagPrefix: String, tr: Throwable){
        log(Log.ERROR, tagPrefix, obj, tr)
    }
}