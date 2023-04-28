package kr.co.testapp0501

import android.util.Log

object Logger {
    var debugMode = true
    fun d(tag: String?, `object` : Any) {
        if(debugMode) Log.d(tag, buildMessage(`object`))
    }
    fun e(tag: String?, `object` : Any) {
        if(debugMode) Log.e(tag, buildMessage(`object`))
    }
    private fun buildMessage(`object` : Any) : String {
        val ste = Thread.currentThread().stackTrace[4]
        val sb = StringBuilder()
        sb.append("Logger::")
        sb.append(ste.fileName.replace(".java", ""))
        sb.append("::")
        sb.append(`object`.toString())
        return sb.toString()
    }
}