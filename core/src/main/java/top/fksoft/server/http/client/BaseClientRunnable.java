package top.fksoft.server.http.client;

import top.fksoft.server.http.HttpServer;
import top.fksoft.server.http.config.HttpKey;
import top.fksoft.server.http.config.NetworkInfo;
import top.fksoft.server.http.config.ServerConfig;
import top.fksoft.server.http.logcat.Logger;
import top.fksoft.server.http.utils.CloseUtils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * <p>HTTP 異步處理客戶端發送的請求
 * </p>
 *
 * @author ExplodingDragon
 * @version 1.0
 */
public abstract class BaseClientRunnable implements Runnable, Closeable, HttpKey {
    private static Logger logger = Logger.getLogger(BaseClientRunnable.class);


    protected final HttpServer httpServer;
    protected final NetworkInfo info;
    protected final Socket client;
    protected final ServerConfig serverConfig;

    public BaseClientRunnable(HttpServer httpServer, Socket client, NetworkInfo info) {
        this.client = client;
        this.info = info;
        this.httpServer = httpServer;
        this.serverConfig = httpServer.getServerConfig();
    }

    @Override
    public void run() {
        try {
            readData();
        } catch (Exception e) {
            logger.warn("在处理来自%s的Http请求中发生错误.", e, info);
        }
        try {
            //销毁
            close();
        } catch (Exception e) {
            logger.warn("在销毁来自%s的Http请求中发生错误.", e, info);
        }
    }

    /**
     * <p>从TCP 连接中读取 http 头信息
     * </p>
     *
     * @throws Exception 如果发生异常直接抛出，自动销毁实例
     */
    abstract void readData() throws Exception;

    public NetworkInfo getRemoteAddress() {
        return info;
    }

    @Override
    public void close() throws IOException {
        clear();
        CloseUtils.close(client);
    }

    /**
     * <p>清空所有用于客户端请求的数据
     * </p>
     *
     * @throws IOException
     */
    abstract void clear() throws IOException;

    public InputStream getInputStream() throws IOException {
        return  client.getInputStream();
    }
    public OutputStream getOutputStream() throws IOException {
        return  client.getOutputStream();
    }
}
