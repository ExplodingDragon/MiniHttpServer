package top.fksoft.execute;

import top.fksoft.execute.config.Config;
import top.fksoft.execute.config.LogListener;
import top.fksoft.server.http.HttpServer;
import top.fksoft.server.http.logcat.LogCat;
import top.fksoft.server.http.logcat.Logger;

public class Main {
    private static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        LogCat.setListener(new LogListener());
        Config config = Config.newInstance();
        config.initConfig(args);
        config.printConfig();
        try {
            new HttpServer(8080).start();
        }catch (Exception e){
            logger.error("无法绑定端口 ！",e);
        }
    }
}
