package top.fksoft.server.http.thread.base

import top.fksoft.server.http.HttpServer
import top.fksoft.server.http.config.ServerConfig
import top.fksoft.server.http.config.bean.NetworkInfo
import top.fksoft.server.http.logcat.Logger
import top.fksoft.server.http.utils.CloseableUtils
import java.io.Closeable
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket
import java.net.SocketException
import java.net.SocketTimeoutException

/**
 *
 * HTTP 異步處理客戶端發送的請求
 *
 *
 * @author ExplodingDragon
 * @version 1.0
 */
abstract class BaseClientRunnable(protected val httpServer: HttpServer, protected val client: Socket, val remoteAddress: NetworkInfo)
    : Runnable, Closeable {
    private val logger = Logger.getLogger(BaseClientRunnable::class)

    protected val serverConfig: ServerConfig = httpServer.serverConfig
    val inputStream: InputStream
        @Throws(IOException::class)
        get() = client.getInputStream()
    val outputStream: OutputStream
        @Throws(IOException::class)
        get() = client.getOutputStream()

    override fun run() {
        try {
            execute()
        } catch (e: Exception) {
            when (e) {
                is SocketTimeoutException -> logger.warn("$remoteAddress 发送未知请求，已强制断开！")
                is SocketException -> logger.warn("$remoteAddress 远程主机强制断开连接！",e)
                else -> logger.warn("在处理来自%s的Http请求中发生错误.", e, remoteAddress)
            }
        }

        try {
            //销毁
            CloseableUtils.close(this)
        } catch (e: Exception) {
            logger.warn("在销毁来自%s的Http请求中发生错误.", e, remoteAddress)
        }

    }

    /**
     *
     * 从TCP 连接中读取 http 头信息
     *
     *
     * @throws Exception 如果发生异常直接抛出，自动销毁实例
     */
    @Throws(Exception::class)
    protected abstract fun execute()

    @Throws(Exception::class)
    override fun close() {
        clear()
        CloseableUtils.close(client)
        logger.debug("已完全关闭$remoteAddress 的连接.")
    }

    /**
     *
     * 清空所有用于客户端请求的数据
     *
     *
     * @throws IOException
     */
    @Throws(IOException::class)
    internal abstract fun clear()

}
