@file:Suppress("FunctionName")

package pers.shawxingkwok.androidutil

import android.util.Log
import pers.shawxingkwok.ktutil.updateIf
import java.io.PrintWriter
import java.io.StringWriter
import java.net.UnknownHostException

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
public abstract class KLog private constructor(
    private val id: String?,
    private val isDebug: Boolean,
    private val warnAfterRelease: ((String) -> Unit)?,
    private val errorAfterRelease: ((String) -> Unit)?,
) {
    public companion object : KLog(
        isDebug = AppOnDebug,
        id = AppContext
            .packageName
            .split(".")
            .joinToString(""){ it.first().uppercase() },
    )

    public constructor(isDebug: Boolean, id: String?) : this(id, isDebug, null, null)

    public constructor(
        isDebug: Boolean,
        id: String?,
        warnAfterRelease: (String) -> Unit,
        errorAfterRelease: (String) -> Unit,
    ) :
        this(id, isDebug, warnAfterRelease, errorAfterRelease)

    private fun log(
        level: Int,
        customedTag: String?,
        messages: Array<out Any?>,
        tr: Throwable?,
    ){
        val printOut: (String, String) -> Unit =
            when{
                isDebug -> { tag, tracedMsg ->
                    Log.println(level, tag, tracedMsg)
                }

                // as a released library on a debugging app
                AppOnDebug ->
                    if (level >= Log.INFO)
                        { tag, tracedMsg ->
                            Log.println(level, tag, tracedMsg)
                        }
                    else
                        return

                // on a released app

                level == Log.WARN && warnAfterRelease != null ->
                    { tag, tracedMsg ->
                        warnAfterRelease.invoke("$tag | $tracedMsg")
                    }

                level == Log.ERROR && errorAfterRelease != null ->
                    { tag, tracedMsg ->
                        errorAfterRelease.invoke("$tag | $tracedMsg")
                    }

                else -> return
            }

        val traceElement = Thread.currentThread().stackTrace[4]

        val trace = "(${traceElement.fileName}:${traceElement.lineNumber})"

        val tag =
            listOfNotNull(
                customedTag,
                traceElement.fileName.substringBeforeLast("."),
                id
            )
            .joinToString(" ")

        val tracedMsg = messages.joinToString(postfix = " $trace")
            .updateIf({ tr != null }){
                var t: Throwable? = tr

                while (t != null) {
                    if (t is UnknownHostException)
                        return@updateIf it

                    t = t.cause
                }

                val sw = StringWriter()
                val pw = PrintWriter(sw)
                tr!!.printStackTrace(pw)
                pw.flush()

                it + "\n" + sw.toString()
            }

        printOut(tag, tracedMsg)
    }

    public fun v(vararg messages: Any?){
        log(Log.VERBOSE, null, messages, null)
    }

    public fun v(vararg messages: Any?, tagPrefix: String){
        log(Log.VERBOSE, tagPrefix, messages, null)
    }

    public operator fun invoke(vararg messages: Any?){
        log(Log.DEBUG, null, messages, null)
    }

    public operator fun invoke(vararg messages: Any?, tagPrefix: String){
        log(Log.DEBUG, tagPrefix, messages, null)
    }

    public fun i(vararg messages: Any?){
        log(Log.INFO, null, messages, null)
    }

    public fun i(vararg messages: Any?, tagPrefix: String){
        log(Log.INFO, tagPrefix, messages, null)
    }

    public fun w(vararg messages: Any?){
        log(Log.WARN, null, messages, null)
    }

    public fun w(vararg messages: Any?, tagPrefix: String){
        log(Log.WARN, tagPrefix, messages, null)
    }

    public fun e(vararg messages: Any?){
        log(Log.ERROR, null, messages, null)
    }

    public fun e(vararg messages: Any?, tagPrefix: String){
        log(Log.ERROR, tagPrefix, messages, null)
    }

    public fun e(vararg messages: Any?, tr: Throwable){
        log(Log.ERROR, null, messages, tr)
    }

    public fun e(vararg messages: Any?, tagPrefix: String, tr: Throwable){
        log(Log.ERROR, tagPrefix, messages, tr)
    }
}