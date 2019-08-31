package top.fksoft.server.http.serverIO.base

import top.fksoft.server.http.config.ResponseCode
import top.fksoft.server.http.logcat.Logger
import top.fksoft.server.http.serverIO.HttpHeaderInfo
import java.io.Closeable
import java.io.OutputStream

/**
 * @author ExplodingDragon
 * @version 1.0
 */
abstract class BaseResponse(private val httpHeaderInfo: HttpHeaderInfo, private val outputStream: OutputStream):Closeable{
    open val logger = Logger.getLogger(javaClass)

    /**
     * 请求状态码
     */
    open var responseCode:ResponseCode = ResponseCode.HTTP_OK

    /**
     * # 推送缓冲区内容
     */
    abstract fun flashResponse():Boolean


}