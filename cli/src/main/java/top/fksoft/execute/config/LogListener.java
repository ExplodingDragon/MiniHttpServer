package top.fksoft.execute.config;

import top.fksoft.server.http.logcat.LogCat;

import static top.fksoft.server.http.logcat.Log.LogId;

public class LogListener implements LogCat.LogcatListener {
    @Override
    public void callback(LogId id, String message) {
        if (Config.newInstance().isDebug()){
            switch (id) {
                case INFO:
                case DEBUG:
                    System.out.println(message);
                    break;
                case WARN:
                case ERROR:
                    System.err.println(message);
                    break;
                default:
            }
        }
    }
}
