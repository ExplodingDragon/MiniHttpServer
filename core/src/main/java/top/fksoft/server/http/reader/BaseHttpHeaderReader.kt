package top.fksoft.server.http.reader

import top.fksoft.server.http.config.HttpHeaderInfo
import top.fksoft.server.http.config.ServerConfig
import top.fksoft.server.http.runnable.ClientAcceptRunnable
import top.fksoft.server.http.utils.CloseUtils
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import kotlin.reflect.KClass

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
abstract class BaseHttpHeaderReader : CloseUtils.Closeable {
    private var runnable: ClientAcceptRunnable? = null
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
    fun onCreate(config: ServerConfig, runnable: ClientAcceptRunnable) {
        this.config = config
        this.runnable = runnable
    }


    /**
     *
     * # 解析HTTP Header 信息并归档
     *
     *
     * 解析HTTP Header 中的信息，并将信息归档到 [HttpHeaderInfo] 中;
     * 在此方法中针对HTTP 1.1 头中的配置信息进行处理，但不处理POST表单
     * 信息（如果有）
     *
     * example:
     * ``` html
     * POST http://www.example.com HTTP/1.1
     * Content-Type: application/x-www-form-urlencoded;charset=utf-8
     *
     * ```
     *
     * @param edit 有效信息存放的位置
     * @return 是否为标准的 HTTP 请求（如果不是则会中断继续）
     */
    @Throws(Exception::class)
    abstract fun readHeaderInfo(edit: HttpHeaderInfo.Edit): Boolean


    /**
     * # 解析HTTP POST 下发送的表单数据
     *
     * 此方法只会在 POST  请求的情况下被调用，
     *
     *
     * @param httpHeader Edit
     * @throws Exception
     */
    @Throws(Exception::class)
    abstract fun readHeaderPostData(httpHeader: HttpHeaderInfo.Edit): Boolean


    companion object {

        @Throws(IllegalAccessException::class, InstantiationException::class)
        fun createHttpHeaderReader(headerFactory: KClass<out BaseHttpHeaderReader>): BaseHttpHeaderReader {
            return headerFactory.java.newInstance()
        }

    }
}
