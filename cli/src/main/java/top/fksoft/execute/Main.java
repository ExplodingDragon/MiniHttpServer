package top.fksoft.execute;

import top.fksoft.execute.config.Config;
import top.fksoft.server.http.HttpServer;
import top.fksoft.server.http.config.ServerConfig;
import top.fksoft.server.http.logcat.Logger;

import java.io.File;

public class Main {
    private static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {

        Config config = Config.newInstance();
        config.initConfig(args);
        config.printConfig();
        try {
            HttpServer httpServer = new HttpServer(8080);
            ServerConfig serverConfig = httpServer.getServerConfig();
            serverConfig.setWorkDirectory(new File("E:\\"));
            httpServer.start();
        }catch (Exception e){
            logger.error("启动服务器时出现问题 ！",e);
        }
    }
}
