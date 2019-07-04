package top.fksoft.server.http;

import top.fksoft.server.http.config.ServerConfig;
import top.fksoft.server.http.logcat.Logger;
import top.fksoft.server.http.server.HttpRunnable;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ThreadFactory;

/**
 * @author Explo
 */
public class HttpServer {
    private static Logger logger = Logger.getLogger(HttpServer.class);
    private final HttpRunnable runnable;
    /**
     * <p>服务器的所有配置信息,
     * </p>
     */
    private final ServerConfig serverConfig = new ServerConfig();
    private Thread httpThread = null;

    /**
     * 将 HTTP Server 绑定到一个端口上
     *
     * @param port 绑定端口
     * @throws IOException 如果端口绑定失败
     */
    public HttpServer(int port) throws IOException {
        this(port, ServerSocketFactory.getDefault());
    }

    /**
     * <p>将 HttpServer通过自定义工厂类绑定到指定端口
     * </p>
     *
     * 有时候需要对httpServer做一些特殊操作，
     * 例如积压 ... 则可通过此构造方法进行初始化参数
     *
     *
     * @param port 绑定的端口
     * @param factory 工厂类
     * @throws IOException 如果发生绑定错误
     */
    public HttpServer(int port, ServerSocketFactory factory) throws IOException {
        ServerSocket serverSocket = factory.createServerSocket(port);
        if (serverSocket == null) {
            throw new NullPointerException();
        }
        runnable = new HttpRunnable(this,serverSocket);
    }

    public void start() {
        if (httpThread == null) {
            httpThread = new HttpThreadFactory().newThread(runnable);
            httpThread.start();
            logger.info("监听线程已开启");
        } else {
            logger.warn("已开启过监听线程");
        }

    }

    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    private class HttpThreadFactory implements ThreadFactory {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, "#HTTP ACCEPT THREAD");
            thread.setPriority(Thread.MAX_PRIORITY);
            thread.setUncaughtExceptionHandler((t, e) -> {
                Logger.getLogger(r.getClass())
                        .error("[%s] 发生未捕获的错误，可将错误信息通过issues告诉开发者!", e, t.getName());
            });
            return thread;
        }
    }
}
