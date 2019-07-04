package top.fksoft.server.http.client;

import top.fksoft.server.http.HttpServer;
import top.fksoft.server.http.config.NetworkInfo;
import top.fksoft.server.http.utils.CloseUtils;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * <p>HTTP 異步處理客戶端發送的請求
 * </p>
 *
 * @version 1.0
 * @author ExplodingDragon
 */
public class ClientRunnable implements Runnable, Closeable {

    private final InetSocketAddress remote;
    private final Socket client;
    private final HttpServer httpServer;

    public ClientRunnable(Socket client, InetSocketAddress remote, HttpServer httpServer) {
        this.client = client;
        this.remote = remote;
        this.httpServer = httpServer;
    }

    @Override
    public void run() {

    }

    public NetworkInfo getRemoteAddress() {
        return null;
    }

    @Override
    public void close() throws IOException {
        CloseUtils.close(client);
    }
}
