package top.fksoft.server.http.thread

import top.fksoft.server.http.HttpServer
import top.fksoft.server.http.config.ResponseCode
import top.fksoft.server.http.config.ResponseCode.Companion.HTTP_OK
import top.fksoft.server.http.config.bean.NetworkInfo
import top.fksoft.server.http.factory.HeaderReaderFactory
import top.fksoft.server.http.logcat.Logger
import top.fksoft.server.http.serverIO.ClientResponse
import top.fksoft.server.http.serverIO.HttpHeaderInfo
import top.fksoft.server.http.serverIO.base.BaseResponse
import top.fksoft.server.http.thread.base.BaseClientRunnable
import top.fksoft.server.http.utils.CloseableUtils
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
class ClientAcceptRunnable(httpServer: HttpServer, client: Socket, private val networkInfo: NetworkInfo) : BaseClientRunnable(httpServer, client, networkInfo) {
    private val httpHeaderInfo = HttpHeaderInfo(networkInfo, serverConfig)
    // 返回
    private val response: BaseResponse by lazy {
        ClientResponse(httpHeaderInfo, client.getOutputStream())
    }
    //读取Header 头
    private val headerReader: HeaderReaderFactory by lazy {
        HeaderReaderFactory.createHttpHeaderReader(super.httpServer.httpHeaderReader, httpServer.serverConfig, client.getInputStream())
    }
    private val logger = Logger.getLogger(ClientAcceptRunnable::class)
    @Throws(Exception::class)
    override fun execute() {
        //初始化 HTTP 解析类
        val code = headerReader.readHeaderInfo(httpHeaderInfo.edit())
        response.responseCode = code
        if (code == HTTP_OK) {
            //协议识别 再放行tcp 连接维持时间
            val findHttpExecute = httpServer.findHttpExecute.findHttpExecute(httpHeaderInfo)
            val declaredConstructor = findHttpExecute.getDeclaredConstructor(HttpHeaderInfo::class.java, ClientResponse::class.java)
            declaredConstructor.isAccessible = true
            try {
                val execute = declaredConstructor.newInstance(httpHeaderInfo, response)
                if (execute.hasPost) {
                    response.responseCode = headerReader.readHeaderBody(httpHeaderInfo.edit())
                }

                if (httpHeaderInfo.isPost() && !execute.hasPost) {
                    response.responseCode = ResponseCode.HTTP_BAD_METHOD
                } else if (response.responseCode == HTTP_OK) {
                    execute.execute()
                }

            } catch (e: Exception) {
                logger.error("在$remoteAddress 下发生不可预知的异常！", e)
                response.responseCode = ResponseCode.HTTP_UNAVAILABLE
            }
        }

        if (response.flashResponse()) {
            logger.info("请求类型:[${httpHeaderInfo.method}]; 请求路径:[${httpHeaderInfo.path}]; 返回状态码:[${response.responseCode}]")
        }else{
            logger.warn("在返回来自[$networkInfo]的连接中失败了.")
        }

    }

    @Throws(IOException::class)
    override fun clear() {
        CloseableUtils.close(headerReader, httpHeaderInfo, response)
    }


}
