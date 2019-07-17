package top.fksoft.server.http.logcat

import jdkUtils.data.StringUtils
import top.fksoft.server.http.logcat.Log.LogId.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.KClass

/**
 *
 * 此HTTP Server下的日志系统
 *
 *
 * 用于打印 HTTP 下的所有日志
 *
 * @author Explo
 */
class Logger private constructor(private val name: String) : Log() {

    override fun info(message: String) {
        callback(INFO, message)
    }

    override fun debug(message: String) {
        callback(DEBUG, message)
    }

    override fun warn(message: String) {
        callback(WARN, message)
    }

    override fun error(message: String) {
        callback(ERROR, message)
    }

    private fun callback(info: Log.LogId, message: String) {

        LogCat.listener.callback(info, String.format("%s - %-5S - %s - %s",
                format.format(System.currentTimeMillis()),
                info.name,
                name,
                message))

    }

    companion object {
        init {
            printLogo("/res/Logo.txt")
        }

        private val format = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        @JvmStatic
        fun getLogger(clazz: Class<*>): Logger {
            return Logger(clazz.name)
        }
        @JvmStatic
        fun getLogger(clazz: KClass<*>): Logger {
            return Logger(clazz.java.name)
        }

        private fun printLogo(path: String) {
            try {
                val logo = StringUtils.inputStreamToString(Logger::class.java.getResourceAsStream(path), "UTF-8")
                LogCat.listener.callback(ERROR, "\n" + logo)
            } catch (e: Exception) {
                System.err.println("Logo 打印失败！")
            }

        }
        @JvmStatic
        fun getLogger(any: Any) = getLogger(any::class)
    }
}
