package top.fksoft.server.http.http2utils

import top.fksoft.server.http.client.ClientRunnable
import top.fksoft.server.http.config.HttpHeader
import top.fksoft.server.http.config.HttpKey
import top.fksoft.server.http.config.ServerConfig
import top.fksoft.server.http.utils.CloseUtils

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

/**
 *
 * 解析 http header 的抽象方法
 *
 *
 * 用于解析http header的抽象方法，用于 http 头的所有内容，
 * 包括 HTTP 协议、请求的类型、请求头的所有信息，以及包括POST请求的
 * 表单信息，如果是文件上传则保存到指定的临时目录
 *
 * @see DefaultHttpHeaderReader 默认实现
 *
 *
 * @author ExplodingDragon
 * @version 1.0
 */
abstract class BaseHttpHeaderReader : CloseUtils.Closeable, HttpKey {
    private var runnable: ClientRunnable? = null
    private var config: ServerConfig? = null

    protected val inputStream: InputStream
        @Throws(IOException::class)
        get() = runnable!!.inputStream

    protected val outputStream: OutputStream
        @Throws(IOException::class)
        get() = runnable!!.outputStream

    /**
     *
     * HTTP Header 预读取的初始化构造方法
     *
     *
     * 此方法将在初始化类后由子线程调用此方法进行初始化
     *
     * @param config
     * @param runnable
     */
    fun onCreate(config: ServerConfig, runnable: ClientRunnable) {
        this.config = config
        this.runnable = runnable
    }


    /**
     *
     * 解析HTTP Header 信息并归档
     *
     *
     * 解析HTTP Header 中的信息，并将信息归档到 [HttpHeader] 中
     *
     * @param httpHeader
     * @return
     */
    @Throws(Exception::class)
    abstract fun readHeaderData(httpHeader: HttpHeader.Edit): Boolean

    companion object {


        @Throws(IllegalAccessException::class, InstantiationException::class)
        fun createHttpHeaderReader(headerFactory: Class<out BaseHttpHeaderReader>): BaseHttpHeaderReader {
            return headerFactory.newInstance()
        }


        val default: BaseHttpHeaderReader
            get() = DefaultHttpHeaderReader()
    }
}
