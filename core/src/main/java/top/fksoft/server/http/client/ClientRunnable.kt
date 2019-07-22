package top.fksoft.server.http.client

import top.fksoft.server.http.HttpServer
import top.fksoft.server.http.config.HttpHeader
import top.fksoft.server.http.config.NetworkInfo
import top.fksoft.server.http.httpHeaderReader.BaseHttpHeaderReader
import top.fksoft.server.http.logcat.Logger
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
class ClientRunnable(httpServer: HttpServer, client: Socket, info: NetworkInfo) : BaseClientRunnable(httpServer, client, info) {
    private val httpHeader: HttpHeader = HttpHeader(info)
    private var headerReader: BaseHttpHeaderReader? = null

    @Throws(Exception::class)
    override fun execute() {
        var httpHeaderReader = BaseHttpHeaderReader.createHttpHeaderReader(super.httpServer.httpHeaderReader)
        this.headerReader = httpHeaderReader
        httpHeaderReader.onCreate(httpServer.serverConfig, this)
        //初始化 HTTP 解析类
        if (httpHeaderReader.readHeaderInfo(httpHeader.edit())) {
            if(httpHeader.isPost())
                httpHeaderReader.readHeaderPostData(httpHeader.edit())
        } else {
            throw IOException("无法解析 HTTP Header 的数据.具体信息查看上一条日志！")
        }
        logger.debug("${httpHeader.method} ${httpHeader.path}")
    }

    @Throws(IOException::class)
    internal override fun clear() {
        CloseUtils.close(headerReader, httpHeader)
    }

    companion object {
        private val logger = Logger.getLogger(ClientRunnable::class.java)
    }


}
