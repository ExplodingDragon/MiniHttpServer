package top.fksoft.server.http.utils;

public class ClassUtils {

    public static String getClassName(int fun) {
        return getElement(fun).getClassName();
    }

    private static StackTraceElement getElement(int fun) {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
         return stacktrace[2 + fun];
    }


    public static String getMethodName(int fun) {
        return getElement(fun).getMethodName();
    }
    public static String getFileName(int fun) {
        return getElement(fun).getFileName();
    }
    public static int getLineNumber(int fun) {
        return getElement(fun).getLineNumber();
    }

}
