package top.fksoft.server.http.server;

import top.fksoft.server.http.HttpServer;
import top.fksoft.server.http.client.ClientRunnable;
import top.fksoft.server.http.config.NetworkInfo;
import top.fksoft.server.http.config.ServerConfig;
import top.fksoft.server.http.logcat.Logger;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>HTTP 監控綫程，監聽每一個遠程鏈接
 * </p>
 *
 * @author ExplodingDragon
 * @version 1.0
 * @see java.io.Closeable
 * @see java.lang.Runnable
 */
public class HttpRunnable implements Runnable, Closeable {
    private static Logger logger = Logger.getLogger(HttpRunnable.class);
    private final ServerSocket serverSocket;
    private final HttpServer httpServer;
    private final ServerConfig serverConfig;
    private final ExecutorService cacheThreadPool ;

    public HttpRunnable(HttpServer httpServer, ServerSocket serverSocket) throws IOException {
        this.httpServer = httpServer;
        this.serverConfig = httpServer.getServerConfig();
        if (serverSocket == null || serverSocket.isClosed() || !serverSocket.isBound()) {
            throw new IOException("套接字错误！");
        }
        this.serverSocket = serverSocket;
        cacheThreadPool =  new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                serverConfig.getSocketTimeout() * 4, TimeUnit.MILLISECONDS,
                new SynchronousQueue<>(), r -> {
            Thread thread = new Thread(r);
            if (r.getClass() == ClientRunnable.class) {
                ClientRunnable runnable = (ClientRunnable) r;
                // 为每一个http线程打TAG
                thread.setName("#Http Client - " + runnable.getRemoteAddress().toString());
            }
            return thread;
        });
    }

    @Override
    public void run() {
        int localPort = serverSocket.getLocalPort();
        logger.info("HTTP 服务器启动正常，绑定端口为：%d .", localPort);
        while (!serverSocket.isClosed()) {
            NetworkInfo remoteInfo = new NetworkInfo();
            try {
                Socket client = serverSocket.accept();
                InetSocketAddress remote = (InetSocketAddress) client.getRemoteSocketAddress();
                String remoteAddress = remote.getAddress().getHostAddress();
                int remotePort = remote.getPort();
                remoteInfo.update(remoteAddress, remotePort);
                remoteInfo.setHostName(remote.getHostName());
                // 得到远程服务器信息
                logger.debug(String.format("接受到一条来自%s远程连接.", remoteInfo.toString()));
                client.setSoTimeout(serverConfig.getSocketTimeout());
                cacheThreadPool.execute(new ClientRunnable(httpServer, client, remoteInfo));
            } catch (Exception e) {
                logger.warn("在处理%s的过程中出现异常.", e, remoteInfo.toString());
            }
        }
    }


    @Override
    public void close() throws IOException {
        serverSocket.close();
    }
}
