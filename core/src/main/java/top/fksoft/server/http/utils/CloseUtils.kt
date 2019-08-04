package top.fksoft.server.http.utils

import top.fksoft.server.http.logcat.Logger

/**
 * @author ExplodingDragon
 * @version 1.0
 */
object CloseUtils {
    @JvmStatic
    fun close(vararg close: Closeable) {
        for (closeable in close) {
            try {
                closeable.close()
            }catch (e:Exception){
                val logger = Logger.getLogger(closeable)
                logger.error("执行关闭方法时发生不可预知的异常！",e)
            }
        }

    }
    
    @JvmStatic
    fun close(vararg close: java.io.Closeable) {
        for (closeable in close) {
            try {
                closeable.close()
            }catch (e:Exception){
                val logger = Logger.getLogger(closeable)
                logger.error("执行关闭方法时发生不可预知的异常！",e)
            }
        }

    }

    interface Closeable {
        @Throws(Exception::class)
        fun close()


    }
}
