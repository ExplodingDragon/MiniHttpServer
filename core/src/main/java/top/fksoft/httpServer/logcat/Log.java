package top.fksoft.httpServer.logcat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public abstract class Log {


    public enum LogId {
        INFO, DEBUG, WARN, ERROR
    }

    public abstract void info(String message);

    public void info(String message, Object... t) {
        info(String.format(message, t));
    }

    public void info(String message, Throwable t) {
        info(formatLogcat(message, t));
    }

    public void i(String message, Throwable t) {
        info(message, t);
    }

    public void i(String message) {
        info(message);
    }

    public abstract void debug(String message);

    public void debug(String message, Throwable t) {
        debug(formatLogcat(message, t));
    }

    public void d(String message, Throwable t) {
        debug(message, t);
    }

    public void d(String message) {
        debug(message);
    }

    public abstract void warn(String message);

    public void warn(String message, Throwable t) {
        warn(formatLogcat(message, t));
    }

    public void w(String message, Throwable t) {
        warn(message, t);
    }

    public void w(String message) {
        warn(message);
    }

    public abstract void error(String message);

    public void error(String message, Throwable t) {
        error(formatLogcat(message, t));
    }
    public void error(String message, Throwable t, Object... obj) {
        error(formatLogcat(String.format(message,obj), t));
    }
    public void e(String message, Throwable t) {
        error(message, t);
    }

    public void e(String message) {
        error(message);
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
