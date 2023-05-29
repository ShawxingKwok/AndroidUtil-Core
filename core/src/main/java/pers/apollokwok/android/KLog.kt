@file:Suppress("FunctionName")

package pers.apollokwok.android

import android.util.Log
import pers.apollokwok.ktutil.TraceUtil
import pers.apollokwok.ktutil.updateIf

/**
 * A log util extended from android logcat.
 *
 * Usage example:
 * ```
 * object MLog : AndroidKLog("Apollo", BuildConfig.DEBUG)
 *```
 *
 * // somewhere
 *
 * MLog(name)
 *
 * MLog.i(age, height)
 */
public abstract class KLog(
    private val defaultTag: String,
    private val isDebug: Boolean,
) {
    private fun getContractedMsg(messages: Array<*>): String {
        var msg = messages.joinToString()

        if (isDebug)
            msg += "\n" + TraceUtil.getTrace(2)

        return msg
    }

    public fun v(vararg messages: Any?){
        Log.v(defaultTag, getContractedMsg(messages))
    }

    public fun v(vararg messages: Any?, tag: String){
        Log.v(tag, getContractedMsg(messages))
    }

    public operator fun invoke(vararg messages: Any?){
        Log.d(defaultTag, getContractedMsg(messages))
    }

    public operator fun invoke(vararg messages: Any?, tag: String){
        Log.d(tag, getContractedMsg(messages))
    }

    public fun i(vararg messages: Any?){
        Log.i(defaultTag, getContractedMsg(messages))
    }

    public fun i(vararg messages: Any?, tag: String){
        Log.i(tag, getContractedMsg(messages))
    }

    public fun w(vararg messages: Any?){
        Log.w(defaultTag, getContractedMsg(messages))
    }

    public fun w(vararg messages: Any?, tag: String){
        Log.w(tag, getContractedMsg(messages))
    }

    public fun e(vararg messages: Any?){
        Log.e(defaultTag, getContractedMsg(messages))
    }

    public fun e(vararg messages: Any?, tag: String){
        Log.e(tag, getContractedMsg(messages))
    }

    public fun e(vararg messages: Any?, tr: Throwable){
        Log.e(defaultTag, getContractedMsg(messages) + "\n" + TraceUtil.getTraces(tr))
    }

    public fun e(vararg messages: Any?, tag: String, tr: Throwable){
        Log.e(tag, getContractedMsg(messages) + "\n" + TraceUtil.getTraces(tr))
    }
}