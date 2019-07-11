package top.fksoft.server.http.logcat;

import top.fksoft.server.http.logcat.Log.LogId;

/**
 * <p>日志的管理类，用于日志的全局管理</p>
 * @author Explo
 */
public class LogCat {
    static LogcatListener listener = (id, message) -> {
        switch (id) {
            case INFO:
            case DEBUG:
            case WARN:
                System.out.println(message);
                break;
            case ERROR:
                System.err.println(message);
                break;
                default:
        }
    };

    /**
     * <p>设置日志的输出和返回的监听器
     * </p>
     * @param listener 监听器
     */
    public static void setListener(LogcatListener listener) {
        LogCat.listener = listener;
    }

    public interface LogcatListener {
        /**
         * <p>Log 的回调方法，用于在http服务器中自定义输出
         * </p>
         * @param id
         * @param message
         */
        void callback(LogId id, String message);
    }
}
