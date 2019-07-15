package top.fksoft.server.http.logcat

import top.fksoft.server.http.logcat.Log.LogId

/**
 *
 * 日志的管理类，用于日志的全局管理
 * @author Explo
 */
object LogCat {
    internal var listener:LogcatListener = object : LogcatListener {
        override fun callback(id: LogId, message: String) = when (id) {
            Log.LogId.INFO, Log.LogId.DEBUG, Log.LogId.WARN -> println(message)
            Log.LogId.ERROR -> System.err.println(message)
        }
    }

    /**
     *
     * 设置日志的输出和返回的监听器
     *
     * @param listen 监听器
     */
    @JvmStatic
    fun setListener(listen: LogcatListener) {
        LogCat.listener = listen
    }

    public interface LogcatListener {
        /**
         *
         * Log 的回调方法，用于在http服务器中自定义输出
         *
         * @param id
         * @param message
         */
        fun callback(id: LogId, message: String)
    }
}
