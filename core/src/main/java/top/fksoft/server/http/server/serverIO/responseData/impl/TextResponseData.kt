package top.fksoft.server.http.server.serverIO.responseData.impl

import top.fksoft.server.http.config.ResponseCode
import top.fksoft.server.http.server.serverIO.responseData.BaseResponseData
import java.io.*

/**
 * @author ExplodingDragon
 * @version 1.0
 */

open abstract class TextResponseData() : BaseResponseData {
    override var responseCode: ResponseCode = ResponseCode.HTTP_OK

    protected val header = HashMap<String,String>()

    override var length: Long = -1L
    // 忽略长度测量

    private val byteArrayOutputStream = ByteArrayOutputStream()
    private val output = PrintWriter(OutputStreamWriter(byteArrayOutputStream, Charsets.UTF_8), true)


    override fun header(): Map<String, String> {
        return header
    }

    override fun writeBody(output: OutputStream): Boolean {
        ByteArrayInputStream(byteArrayOutputStream.toByteArray()).copyTo(output)
        return true
    }

    fun println(str: String) {
        output.println(str)
    }

    fun print(str: String) {
        output.print(str)

    }

    fun printf(format: String, vararg args: Any) {
        output.printf(format, args)
    }

    override fun close() {
        output.close()
    }

}