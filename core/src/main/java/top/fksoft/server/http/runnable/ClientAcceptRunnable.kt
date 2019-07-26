package top.fksoft.server.http.runnable

import top.fksoft.server.http.HttpServer
import top.fksoft.server.http.config.ClientResponse
import top.fksoft.server.http.config.HttpHeaderInfo
import top.fksoft.server.http.config.NetworkInfo
import top.fksoft.server.http.config.ResponseCode.Companion.HTTP_OK
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
    private val httpHeaderInfo = HttpHeaderInfo(info)
    private var clientResponse: ClientResponse? = null
    private val headerReader = BaseHttpHeaderReader.createHttpHeaderReader(super.httpServer.httpHeaderReader)
    private val logger = Logger.getLogger(ClientAcceptRunnable::class)

    @Throws(Exception::class)
    override fun execute() {
        headerReader.onCreate(httpServer.serverConfig, this)
        //初始化 HTTP 解析类
        val responseCode = headerReader.readHeaderInfo(httpHeaderInfo.edit())
        if (responseCode.equals(HTTP_OK)) {
            client.soTimeout = serverConfig.socketTimeout
            //协议识别 再放行tcp 连接维持时间
            clientResponse = ClientResponse(httpHeaderInfo, client)
            headerReader.readHeader(httpHeaderInfo.edit())
        } else {
            throw IOException("无法解析 HTTP Header 的数据.具体信息查看上一条日志！")
        }
        logger.debug("method:${httpHeaderInfo.method}    path:${httpHeaderInfo.path}    version:${httpHeaderInfo.httpVersion}")
    }

    @Throws(IOException::class)
    internal override fun clear() {
        CloseUtils.close(headerReader, httpHeaderInfo, clientResponse)
    }


}
