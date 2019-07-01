package top.fksoft.httpServer.logcat;

import top.fksoft.httpServer.logcat.Log.LogId;

/**
 * <p>日志的管理类，用于日志的全局管理</p>
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
        void callback(LogId id, String message);
    }
}
