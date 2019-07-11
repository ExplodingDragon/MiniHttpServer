package top.fksoft.server.http.client;

import top.fksoft.server.http.HttpServer;
import top.fksoft.server.http.config.NetworkInfo;
import top.fksoft.server.http.http2utils.BaseHttpHeaderFactory;
import top.fksoft.server.http.logcat.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * <p>HTTP 異步處理客戶端發送的請求
 * </p>
 *
 * @version 1.0
 * @author ExplodingDragon
 */
public class ClientRunnable extends BaseClientRunnable{
    private static Logger logger = Logger.getLogger(ClientRunnable.class);


    public ClientRunnable(HttpServer httpServer, Socket client, NetworkInfo info) {
        super(httpServer, client, info);
    }

    @Override
    void readData() throws Exception {
        InputStream input = getInputStream();
        Class<? extends BaseHttpHeaderFactory> findUtil = httpServer.getHttpHeaderFactory();
    }

    @Override
    void clear() throws IOException {

    }


}
