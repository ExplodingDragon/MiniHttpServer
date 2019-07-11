package top.fksoft.server.http.logcat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public abstract class Log {


    public enum LogId {
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
        WARN, ERROR
    }

    public abstract void info(String message);

    public void info(String message, Object... t) {
        info(String.format(message, t));
    }

    public void info(String message, Throwable t,Object ... obj) {
        info(formatLogcat(String.format(message,obj), t));
    }

    public abstract void debug(String message);
    public void debug(String message, Object... t) {
        debug(String.format(message, t));
    }
    public void debug(String message, Throwable t,Object ... obj) {
        debug(formatLogcat(String.format(message,obj), t));
    }


    public abstract void warn(String message);

    public void warn(String message, Throwable t,Object ... obj) {
        warn(formatLogcat(String.format(message,obj), t));
    }

    public abstract void error(String message);

    public void error(String message, Throwable t, Object... obj) {
        error(formatLogcat(String.format(message,obj), t));
    }

    private String formatLogcat(String message, Throwable t) {
        return String.format("%s\n%s", message, throwable2Str(t));
    }

    private String throwable2Str(Throwable t) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintWriter printWriter = new PrintWriter(outputStream);
        t.printStackTrace(printWriter);
        printWriter.flush();
        printWriter.close();
        String result = new String(outputStream.toByteArray());
        try {
            outputStream.close();
        } catch (IOException ignored) {
        }
        return result;
    }
}
