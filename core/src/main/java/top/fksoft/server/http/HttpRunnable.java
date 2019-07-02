package top.fksoft.server.http;

import top.fksoft.server.http.logcat.Logger;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;

public class HttpRunnable implements  Runnable ,Closeable {
    private static Logger logger = Logger.getLogger(HttpRunnable.class);
    private final ServerSocket serverSocket;

    public HttpRunnable(ServerSocket serverSocket) throws IOException {
        if (serverSocket == null || serverSocket.isClosed()||!serverSocket.isBound()){
            throw new IOException("套接字错误！");
        }
         this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        int localPort = serverSocket.getLocalPort();
        logger.info("HTTP 服务器启动正常，绑定端口为：%d",localPort);
        throw new RuntimeException("sss");
    }

    @Override
    public void close() throws IOException {
        serverSocket.close();
    }
}
