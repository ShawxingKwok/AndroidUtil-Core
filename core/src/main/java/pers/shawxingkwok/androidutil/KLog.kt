@file:Suppress("FunctionName")

package pers.shawxingkwok.androidutil

import android.util.Log
import pers.shawxingkwok.ktutil.TraceUtil
import pers.shawxingkwok.ktutil.updateIf

/**
 *
 * A log util extended from android logcat.
 *
 * Usage example:
 * ```
 * object MLog : AndroidKLog("Shawxing", BuildConfig.DEBUG)
 *
 * // somewhere
 * MLog("Jack")
 * MLog.i(20, 1.8)
 *```
 * Logs:
 *
 * **D**    Jack
 *
 * at ...
 *
 * **I**    20, 1.8
 *
 * at ...
 *
 * Note: for released android libraries, on level 'VERBOSE' and 'DEBUG', its logs are not visible;
 * and on level 'INFO' and 'DEBUG', traces are not visible.
 *
 * If you are developing an application, remember to set
 * '-assumenosideeffects class pers.shawxingkwok.android.KLog{ *; }' in 'proguard-rules.pro' in the app module to
 * remove logs in the released apk.
 */
public abstract class KLog(
    private val defaultTag: String,
    private val isDebug: Boolean,
) {
    private fun getContractedMsg(messages: Array<*>, addTrace: Boolean): String =
        messages.joinToString()
        .updateIf({ addTrace }){
            it + "\n" + TraceUtil.getTrace(3)
        }

    /**
     * Log out [messages] with level [Log.VERBOSE] and [defaultTag].
     * **Trace** would be appended when [isDebug].
     */
    public fun v(vararg messages: Any?){
        if (isDebug) {
            val msg = getContractedMsg(messages, true)
            Log.v(defaultTag, msg)
        }
    }

    /**
     * Log out [messages] with level [Log.VERBOSE] and [tag].
     * **Trace** would be appended when [isDebug].
     */
    public fun v(vararg messages: Any?, tag: String){
        if (isDebug) {
            val msg = getContractedMsg(messages, true)
            Log.v(tag, msg)
        }
    }

    /**
     * Log out [messages] with level [Log.DEBUG] and [defaultTag].
     * **Trace** would be appended when [isDebug].
     */
    public operator fun invoke(vararg messages: Any?){
        if (isDebug) {
            val msg = getContractedMsg(messages, true)
            Log.d(defaultTag, msg)
        }
    }

    /**
     * Log out [messages] with level [Log.DEBUG] and [tag].
     * **Trace** would be appended when [isDebug].
     */
    public operator fun invoke(vararg messages: Any?, tag: String){
        if (isDebug) {
            val msg = getContractedMsg(messages, true)
            Log.d(tag, msg)
        }
    }

    /**
     * Log out [messages] with level [Log.INFO] and [defaultTag].
     * **Trace** would be appended when [isDebug].
     */
    public fun i(vararg messages: Any?){
        val msg = getContractedMsg(messages, isDebug)
        Log.i(defaultTag, msg)
    }

    /**
     * Log out [messages] with level [Log.INFO] and [tag].
     * **Trace** would be appended when [isDebug].
     */
    public fun i(vararg messages: Any?, tag: String){
        val msg = getContractedMsg(messages, isDebug)
        Log.i(tag, msg)
    }

    /**
     * Log out [messages] with level [Log.WARN] and [defaultTag].
     * **Trace** would be appended when [isDebug].
     */
    public fun w(vararg messages: Any?){
        val msg = getContractedMsg(messages, isDebug)
        Log.w(defaultTag, msg)
    }

    /**
     * Log out [messages] with level [Log.WARN] and [tag].
     * **Trace** would be appended when [isDebug].
     */
    public fun w(vararg messages: Any?, tag: String){
        val msg = getContractedMsg(messages, isDebug)
        Log.w(tag, msg)
    }

    /**
     * Log out [messages] with level [Log.ERROR] and [defaultTag].
     * **Trace** would be always appended.
     */
    public fun e(vararg messages: Any?){
        val msg = getContractedMsg(messages, true)
        Log.e(defaultTag, msg)
    }

    /**
     * Log out [messages] with level [Log.ERROR] and [tag].
     * **Trace** would be always appended.
     */
    public fun e(vararg messages: Any?, tag: String){
        val msg = getContractedMsg(messages, true)
        Log.e(tag, msg)
    }

    /**
     * Log out [messages] with level [Log.ERROR], [defaultTag] and [tr].
     * **Trace** would be always appended.
     */
    public fun e(vararg messages: Any?, tr: Throwable){
        val msg = getContractedMsg(messages, true)
        Log.e(defaultTag, msg + "\n" + TraceUtil.getTraces(tr))
    }

    /**
     * Log out [messages] with level [Log.ERROR], [tag] and [tr].
     * **Trace** would be always appended.
     */
    public fun e(vararg messages: Any?, tag: String, tr: Throwable){
        val msg = getContractedMsg(messages, true)
        Log.e(tag, msg + "\n" + TraceUtil.getTraces(tr))
    }
}