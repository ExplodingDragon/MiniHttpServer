package top.fksoft.server.http.client

import top.fksoft.server.http.config.HttpConstant
import top.fksoft.server.http.config.HttpHeaderInfo
import top.fksoft.server.http.config.ResponseCode
import top.fksoft.server.http.logcat.Logger
import top.fksoft.server.http.utils.CloseUtils
import top.fksoft.server.http.utils.ContentTypeUtils
import java.io.*
import java.net.Socket
import java.util.concurrent.ConcurrentHashMap

/**
 * # 响应客户端的信息
 *
 * @author ExplodingDragon
 * @version 1.0
 */


class ClientResponse(private val headerInfo: HttpHeaderInfo, private val client: Socket) : CloseUtils.Closeable {
    val logger = Logger.getLogger(this)
    var responseContentType: String = HttpConstant.HEADER_VALUE_TEXT_HTML
    var responseInputStream: InputStream? = null
    private val responseMap = ConcurrentHashMap<String, String>()
    var isPrintHeader = false
    private var ignorePrintWriter: PrintWriter? = null
    val outputStream: OutputStream
        get() = client.getOutputStream()
    val printWriter: PrintWriter
        get() {
            if (ignorePrintWriter == null) {
                ignorePrintWriter = PrintWriter(OutputStreamWriter(outputStream, headerInfo.charset), true)
            }
            return ignorePrintWriter!!
        }


    init {
        addResponseHeader("Server", HttpConstant.LIB_NAME)
    }


    /**
     * 返回的请求状态码
     */
    var responseCode: ResponseCode = ResponseCode.HTTP_OK

    /**
     * # 输出字符到客户端（实时）
     *
     *
     *
     * @param line String
     */
    fun println(line: String) = run {
        printHeader()
        printWriter.println(line)
    }

    private fun printHeader() {
        if (!isPrintHeader) {
            var writer = printWriter
            writer.println("HTTP/1.1 ${responseCode.responseCode}  ${responseCode.codeMessage}")
            addResponseHeader(HttpConstant.HEADER_KEY_CONTENT_TYPE, responseContentType)
            for (entry in responseMap.iterator()) {
                writer.println("${entry.key}: ${entry.value}")
            }
            writer.println()
            isPrintHeader = true
        }
    }


    @Throws(Exception::class)
    override fun close() {
    }

    fun writeFile(file: File) = if (file.isFile) {
        var application = ContentTypeUtils.file2Application(file)
        responseContentType = application
        addResponseHeader(HttpConstant.HEADER_KEY_CONTENT_LENGTH, file.length().toString())
        responseInputStream = FileInputStream(file)
        logger.debug("输出文件： ${file.absolutePath}.")
    } else {
        responseCode = ResponseCode.HTTP_NOT_FOUND
        logger.debug("文件[$file]不存在！")
        responseContentType = HttpConstant.HEADER_VALUE_TEXT_HTML
        responseInputStream = javaClass.getResource("/res/resultHtml/404.html").openStream()
    }

    fun flashResponse() {
        if (!isPrintHeader) {
            printHeader()
            var output = outputStream
            if (responseInputStream != null) {
                logger.debug("输出流大小:" + responseInputStream!!.available())
                var b = ByteArray(4096)
                var read: Int
                while (true) {
                    read = responseInputStream!!.read(b, 0, b.size)
                    if (-1 == read) {
                        break
                    }
                    output.write(b, 0, read)
                    output.flush()
                }
            }
        }
    }

    fun addResponseHeader(key2: String, value: String) {
        responseMap[key2.trim()] = value
    }

}