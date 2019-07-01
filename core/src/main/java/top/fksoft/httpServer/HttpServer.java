package top.fksoft.httpServer;

import top.fksoft.httpServer.logcat.Logger;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.util.concurrent.ThreadFactory;

public class HttpServer {
    private static Logger logger = Logger.getLogger(HttpServer.class);
    private final HttpRunnable runnable;
    private Thread httpThread = null;

    public HttpServer(int port) throws IOException {
        this(port, ServerSocketFactory.getDefault());
    }

    public HttpServer(int port, ServerSocketFactory factory) throws IOException {
        runnable = new HttpRunnable(factory.createServerSocket(port));
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

    private class HttpThreadFactory implements ThreadFactory {

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, "#HTTP ACCEPT THREAD");
            thread.setPriority(Thread.MAX_PRIORITY);
            thread.setUncaughtExceptionHandler((t, e) -> {
                Logger.getLogger(r.getClass())
                        .error("[%s] 发生未捕获的错误，可将错误信息通过issues告诉开发者!", e,t.getName());
            });
            return thread;
        }
    }
}
