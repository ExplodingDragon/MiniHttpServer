package top.fksoft.execute;

import top.fksoft.server.http.HttpServer;
import top.fksoft.server.http.logcat.Logger;

import java.io.IOException;

public class Main {
    private static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        try {
            new HttpServer(8080).start();

        }catch (Exception e){
            logger.error("无法绑定端口 ！",e);
            logger.error("无法绑定端口 ！",e);
            logger.error("无法绑定端口 ！",e);
            logger.error("无法绑定端口 ！",e);
        }
    }
}
