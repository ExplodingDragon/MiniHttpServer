package top.fksoft.server.http.runnable

import top.fksoft.server.http.HttpServer
import top.fksoft.server.http.config.HttpHeaderInfo
import top.fksoft.server.http.config.NetworkInfo
import top.fksoft.server.http.logcat.Logger
import top.fksoft.server.http.reader.BaseHttpHeaderReader
import top.fksoft.server.http.runnable.base.BaseClientRunnable
import top.fksoft.server.http.utils.CloseUtils
import java.io.IOException
import java.net.Socket

/**
 *
 * HTTP 異步處理客戶端發送的請求
 *
 *
 * @version 1.0
 * @author ExplodingDragon
 */
class ClientAcceptRunnable(httpServer: HttpServer, client: Socket, info: NetworkInfo) : BaseClientRunnable(httpServer, client, info) {
    private val httpHeaderInfo: HttpHeaderInfo = HttpHeaderInfo(info)
    private var headerReader: BaseHttpHeaderReader? = null
    private val logger = Logger.getLogger(ClientAcceptRunnable::class)

    @Throws(Exception::class)
    override fun execute() {
        var httpHeaderReader = BaseHttpHeaderReader.createHttpHeaderReader(super.httpServer.httpHeaderReader)
        this.headerReader = httpHeaderReader
        httpHeaderReader.onCreate(httpServer.serverConfig, this)
        //初始化 HTTP 解析类
        if (httpHeaderReader.readHeaderInfo(httpHeaderInfo.edit())) {
            if(httpHeaderInfo.isPost())
                httpHeaderReader.readHeaderPostData(httpHeaderInfo.edit())
        } else {
            throw IOException("无法解析 HTTP Header 的数据.具体信息查看上一条日志！")
        }
        logger.debug("method:${httpHeaderInfo.method}    path:${httpHeaderInfo.path}    version:${httpHeaderInfo.httpVersion}")
    }

    @Throws(IOException::class)
    internal override fun clear() {
        CloseUtils.close(headerReader, httpHeaderInfo)
    }



}
