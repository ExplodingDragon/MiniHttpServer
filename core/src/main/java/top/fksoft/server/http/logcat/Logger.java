package top.fksoft.server.http.logcat;

import jdkUtils.data.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;

import static top.fksoft.server.http.logcat.Log.LogId.*;

public class Logger extends Log {
    static {
        printLogo("/res/Logo.txt");
    }
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss",Locale.getDefault());
    private final String name;

    private Logger(String name) {
        this.name = name;
    }

    public static Logger getLogger(Class clazz) {
        return new Logger(clazz.getName());
    }

    @Override
    public void info(String message) {
        callback(INFO, message);
    }

    @Override
    public void debug(String message) {
        callback(DEBUG, message);
    }

    @Override
    public void warn(String message) {
        callback(WARN, message);
    }

    @Override
    public void error(String message) {
        callback(ERROR, message);
    }

    private void callback(LogId info, String message) {

        LogCat.listener.callback(info, String.format("%s - %-5S - %s - %s",
                format.format(System.currentTimeMillis()),
                info.name(),
                name,
                message));

    }
    public static void printLogo(String path){
        try {
            String logo = StringUtils.inputStreamToString(Logger.class.getResourceAsStream(path)
                    , "UTF-8");
            LogCat.listener.callback(ERROR,"\n" + logo );
        }catch (Exception e){
            System.err.println("Logo 打印失败！");
        }
    }
}
