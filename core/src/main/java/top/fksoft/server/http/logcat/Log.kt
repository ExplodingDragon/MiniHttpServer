package top.fksoft.server.http.logcat

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.PrintWriter
import java.nio.charset.Charset

abstract class Log {


    enum class LogId {
        /**
         * 标准日志，用于打印服务器工作状态
         */
        INFO,
        /**
         * 调试日志，用于打印内部调试日志
         */
        DEBUG,
        /**
         * 警告日志
         */
        WARN,
        ERROR
    }

    abstract fun info(message: String)

    fun info(message: String, vararg t: Any) {
        info(String.format(message, *t))
    }

    fun info(message: String, t: Throwable, vararg obj: Any) {
        info(formatLogcat(String.format(message, *obj), t))
    }

    abstract fun debug(message: String)
    fun debug(message: String, vararg t: Any) {
        debug(String.format(message, *t))
    }

    fun debug(message: String, t: Throwable, vararg obj: Any) {
        debug(formatLogcat(String.format(message, *obj), t))
    }


    abstract fun warn(message: String)

    fun warn(message: String, t: Throwable, vararg obj: Any) {
        warn(formatLogcat(String.format(message, *obj), t))
    }

    abstract fun error(message: String)

    fun error(message: String, t: Throwable, vararg obj: Any) {
        error(formatLogcat(String.format(message, *obj), t))
    }

    private fun formatLogcat(message: String, t: Throwable): String {
        return String.format("%s\n%s", message, throwable2Str(t))
    }

    private fun throwable2Str(t: Throwable): String {
        val outputStream = ByteArrayOutputStream()
        val printWriter = PrintWriter(outputStream)
        t.printStackTrace(printWriter)
        printWriter.flush()
        printWriter.close()
        val result = String(outputStream.toByteArray(), Charset.defaultCharset())
        try {
            outputStream.close()
        } catch (ignored: IOException) {
        }

        return result
    }
}
