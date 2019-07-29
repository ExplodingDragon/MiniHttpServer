package top.fksoft.server.http

import top.fksoft.server.http.config.ServerConfig
import top.fksoft.server.http.factory.FindHttpExecuteFactory
import top.fksoft.server.http.factory.HeaderReaderFactory
import top.fksoft.server.http.logcat.Logger
import top.fksoft.server.http.runnable.SocketListenerRunnable
import java.io.IOException
import java.util.concurrent.ThreadFactory
import javax.net.ServerSocketFactory
import kotlin.reflect.KClass

/**
 * @author ExplodingDragon
 *
 * @version 1.0
 */
class HttpServer

/**
 *
 * 将 HttpServer通过自定义工厂类绑定到指定端口
 *
 *
 * 有时候需要对httpServer做一些特殊操作，
 * 例如积压 ... 则可通过此构造方法进行初始化参数
 *
 *
 * @param port 绑定的端口
 * @param factory 工厂类
 * @throws IOException 如果发生绑定错误
 */
@Throws(IOException::class)
@JvmOverloads constructor(port: Int, factory: ServerSocketFactory = ServerSocketFactory.getDefault()) {
    private val logger = Logger.getLogger(HttpServer::class)
    private val runnable: SocketListenerRunnable
    /**
     *
     * 服务器的所有配置信息,
     *
     */
    val serverConfig: ServerConfig
    /**
     * 服务器路径查询方法
     */
    val findHttpExecute: FindHttpExecuteFactory
        get() =  FindHttpExecuteFactory.getDefault(serverConfig)
    private var httpThread: Thread? = null

    /**
     * 解析 HTTP请求头的处理类
     */
    var httpHeaderReader: KClass<out HeaderReaderFactory> = HeaderReaderFactory.getDefault()
    init {
        val serverSocket = factory.createServerSocket(port) ?: throw NullPointerException()
        serverConfig = ServerConfig(port)
        runnable = SocketListenerRunnable(this, serverSocket)
    }

    fun start() {
        if (httpThread == null) {
            if (!serverConfig.init()) {
                throw IOException("在初始化过程发生问题.")
            }
            httpThread = HttpThreadFactory().newThread(runnable)
            httpThread!!.start()
            logger.info("监听线程已开启")
        } else {
            logger.warn("已开启过监听线程")
        }
    }

    private inner class HttpThreadFactory : ThreadFactory {
        override fun newThread(r: Runnable): Thread {
            val thread = Thread(r, "AcceptThread")
            thread.priority = Thread.MAX_PRIORITY
            thread.setUncaughtExceptionHandler { t, e ->
                Logger.getLogger(r)
                        .error("[ ${t.name} ] 发生未捕获的错误，可将错误信息通过issues告诉开发者!", e)
            }
            return thread
        }
    }

}
