package pers.shawxingkwok.androidutil

import android.content.pm.ApplicationInfo
import android.util.Log
import pers.shawxingkwok.ktutil.updateIf
import kotlin.reflect.KProperty0

/**
 * See [doc](https://shawxingkwok.github.io/ITWorks/docs/android/util-core/#klog).
 */
public abstract class KLog(
    private val id: String,
    private val onDebug: Boolean,
) {
    public companion object : KLog("KLOG", true)

    public abstract class InApp(id: String, onDebug: Boolean) : KLog("$id APP", onDebug)

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
        obj: Any?,
        tagPrefix: String?,
        tr: Throwable?,
    ){
        if (!onDebug && level < Log.WARN) return

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
        log(Log.VERBOSE, obj, null, null)
    }

    public open fun v(obj: Any?, tagPrefix: String){
        log(Log.VERBOSE, obj, tagPrefix, null)
    }

    public open fun d(obj: Any?){
        log(Log.DEBUG, obj,null, null)
    }

    public open fun d(obj: Any?, tagPrefix: String){
        log(Log.DEBUG, obj, tagPrefix, null)
    }

    public open fun i(obj: Any?){
        log(Log.INFO, obj, null, null)
    }

    public open fun i(obj: Any?, tagPrefix: String){
        log(Log.INFO, obj, tagPrefix, null)
    }

    public open fun w(obj: Any?){
        log(Log.WARN, obj, null, null)
    }

    public open fun w(obj: Any?, tagPrefix: String){
        log(Log.WARN, obj, tagPrefix, null)
    }

    public open fun e(obj: Any?){
        log(Log.ERROR, obj, null, null)
    }

    public open fun e(obj: Any?, tagPrefix: String){
        log(Log.ERROR, obj, tagPrefix, null)
    }

    public open fun e(obj: Any?, tr: Throwable){
        log(Log.ERROR, obj, null, tr)
    }

    public open fun e(obj: Any?, tagPrefix: String, tr: Throwable){
        log(Log.ERROR, obj, tagPrefix, tr)
    }
}