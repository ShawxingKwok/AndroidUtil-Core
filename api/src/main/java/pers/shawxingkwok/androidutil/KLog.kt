@file:Suppress("FunctionName")

package pers.shawxingkwok.androidutil

import android.content.pm.ApplicationInfo
import android.util.Log
import pers.shawxingkwok.ktutil.updateIf

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
    private val onDebug: Boolean,
    private val id: String,
) {
    public companion object : KLog(AppOnDebug, "KLOG")

    private fun log(
        level: Int,
        tagPrefix: String?,
        msg: Array<out Any?>,
        tr: Throwable?,
    ){
        when{
            onDebug -> {}
            AppOnDebug -> if (level < Log.INFO) return
            else -> return
        }

        val traceElement = Thread.currentThread().stackTrace[4]

        val trace = "(${traceElement.fileName}:${traceElement.lineNumber})"

        val tag =
            listOfNotNull(
                tagPrefix,
                traceElement.fileName.substringBeforeLast("."),
                id
            )
            .joinToString(" ")

        val trTrace =  Log.getStackTraceString(tr)

        val msgWithTrace = msg.joinToString(postfix = " $trace")
            .updateIf({ trTrace.any() }){ it + "\n" + trTrace }

        Log.println(level, tag, msgWithTrace)
    }

    public fun v(vararg msg: Any?){
        log(Log.VERBOSE, null, msg, null)
    }

    public fun v(vararg msg: Any?, tagPrefix: String){
        log(Log.VERBOSE, tagPrefix, msg, null)
    }

    public operator fun invoke(vararg msg: Any?){
        log(Log.DEBUG, null, msg, null)
    }

    public operator fun invoke(vararg msg: Any?, tagPrefix: String){
        log(Log.DEBUG, tagPrefix, msg, null)
    }

    public fun i(vararg msg: Any?){
        log(Log.INFO, null, msg, null)
    }

    public fun i(vararg msg: Any?, tagPrefix: String){
        log(Log.INFO, tagPrefix, msg, null)
    }

    public fun w(vararg msg: Any?){
        log(Log.WARN, null, msg, null)
    }

    public fun w(vararg msg: Any?, tagPrefix: String){
        log(Log.WARN, tagPrefix, msg, null)
    }

    public fun e(vararg msg: Any?){
        log(Log.ERROR, null, msg, null)
    }

    public fun e(vararg msg: Any?, tagPrefix: String){
        log(Log.ERROR, tagPrefix, msg, null)
    }

    public fun e(vararg msg: Any?, tr: Throwable){
        log(Log.ERROR, null, msg, tr)
    }

    public fun e(vararg msg: Any?, tagPrefix: String, tr: Throwable){
        log(Log.ERROR, tagPrefix, msg, tr)
    }
}