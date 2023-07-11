package pers.shawxingkwok.androidutil

import android.content.pm.ApplicationInfo
import android.util.Log
import pers.shawxingkwok.ktutil.updateIf
import kotlin.reflect.KProperty0

private val AppOnDebug = (AppContext.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0

/**
 * **See** [doc](https://shawxingkwok.github.io/ITWorks/docs/android/util-core/#klog)
 */
public abstract class KLog(
    private val onDebug: Boolean,
    private val id: String,
) {
    public companion object : KLog(AppOnDebug, "KLOG")

    public abstract class InApp(onDebug: Boolean, id: String) : KLog(onDebug, "$id APP")

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
        when{
            onDebug -> {}
            AppOnDebug -> if (level < Log.WARN) return
            else -> return
        }

        val trace = Thread.currentThread().stackTrace
        // stackTrace may be disturbed by `crossinline`
        var i = 4
        while ("$\$inlined" in trace[i].className){ i++ }

        val traceElement = trace[i]
        val tag =
            listOfNotNull(
                tagPrefix,
                if (i == 4)
                    "(${traceElement.fileName}:${traceElement.lineNumber})"
                else
                    "(${traceElement.fileName})",
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