package top.fksoft.server.http.runnable

import top.fksoft.server.http.HttpServer
import top.fksoft.server.http.client.ClientResponse
import top.fksoft.server.http.config.HttpHeaderInfo
import top.fksoft.server.http.config.ResponseCode.Companion.HTTP_OK
import top.fksoft.server.http.config.bean.NetworkInfo
import top.fksoft.server.http.factory.HeaderReaderFactory
import top.fksoft.server.http.logcat.Logger
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
    private val httpHeaderInfo = HttpHeaderInfo(info,serverConfig)
    private var clientResponse: ClientResponse = ClientResponse(httpHeaderInfo, client)
    private val headerReader = HeaderReaderFactory.createHttpHeaderReader(super.httpServer.httpHeaderReader)
    private val logger = Logger.getLogger(ClientAcceptRunnable::class)
    @Throws(Exception::class)
    override fun execute() {
        headerReader.onCreate(httpServer.serverConfig, this)
        //初始化 HTTP 解析类
        val code = headerReader.readHeaderInfo(httpHeaderInfo.edit())
        clientResponse.responseCode = code
        if (code.equals(HTTP_OK)) {
            //协议识别 再放行tcp 连接维持时间
            headerReader.readHeaderBody(httpHeaderInfo.edit())
            val findHttpExecute = httpServer.findHttpExecute.findHttpExecute(httpHeaderInfo)
            val declaredConstructor = findHttpExecute.getDeclaredConstructor(HttpHeaderInfo::class.java, ClientResponse::class.java)
            declaredConstructor.isAccessible = true
            try {
                declaredConstructor.newInstance(httpHeaderInfo,clientResponse)
            }catch (e:Exception){
                logger.error("在$remoteAddress 下发生不可预知的异常！",e)
            }
        }
        clientResponse.flashResponse()


        logger.debug("method:${httpHeaderInfo.method}    path:${httpHeaderInfo.path}    version:${httpHeaderInfo.httpVersion}")
    }

    @Throws(IOException::class)
    internal override fun clear() {
        CloseUtils.close(headerReader, httpHeaderInfo, clientResponse)
    }


}
