package top.fksoft.server.http.logcat

import jdkUtils.data.StringUtils
import top.fksoft.server.http.HttpServer
import kotlin.reflect.KClass



/**
 *
 * # 自定义日志工具
 *
 *s
 * 打印所有日志
 *
 *
 * @author ExplodingDragon
 * @version 1.0
 */
class Logger private constructor(private val kClass: KClass<*>) : Log() {
    override fun info(message: String, exception: Throwable?) {
        logCallback(LOGGER_INFO,message,exception)
    }

    override fun debug(message: String, exception: Throwable?) {
        if (HttpServer.loggerFactory.isDebug()) {
            logCallback(LOGGER_DEBUG,message,exception)
        }
    }

    override fun warn(message: String, exception: Throwable?) {
        logCallback(LOGGER_WARN,message,exception)
    }

    override fun error(message: String, exception: Throwable?) {
        logCallback(LOGGER_ERROR,message,exception)
    }


   private fun logCallback(level:Int,message: String, exception: Throwable?){
       HttpServer.loggerFactory.outputLog(System.currentTimeMillis(),level,kClass,message,exception)
   }

    /**
     * # 判断是否为DEBUG 模式
     */
    val debug:Boolean
    get() = HttpServer.loggerFactory.isDebug()

    companion object {
        init {
            printLogo("/res/Logo.txt")
        }

        @JvmStatic
        fun getLogger(clazz: Class<*>): Logger {
            return Logger(clazz.kotlin)
        }
        @JvmStatic
        fun getLogger(clazz: KClass<*>): Logger {
            return Logger(clazz)
        }

        private fun printLogo(path: String) {
            try {
                val logo = StringUtils.inputStreamToString(Logger::class.java.getResourceAsStream(path), "UTF-8")
                System.err.println("\n$logo\n")
            } catch (e: Exception) {
                System.err.println("Logo 打印失败！")
            }

        }
        @JvmStatic
        fun getLogger(any: Any) = getLogger(any::class)
    }
}
