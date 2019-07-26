package top.fksoft.server.http.config

import top.fksoft.server.http.utils.CloseUtils
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.Socket

/**
 * # 响应客户端的信息
 *
 * @author ExplodingDragon
 * @version 1.0
 */


class ClientResponse(val headerInfo: HttpHeaderInfo, val client: Socket) : CloseUtils.Closeable {


    val printWriter: PrintWriter = PrintWriter(OutputStreamWriter(client.getOutputStream(),headerInfo.charset),true)


    /**
     * 返回的请求状态码
     */
    var responseCode:Int = 200;

    /**
     * # 输出字符到客户端（实时）
     *
     *
     *
     * @param line String
     */
    fun println(line:String) = run {
        printHeader()
        printWriter.println(line)
    }

    /**
     * 在
     */
    private fun printHeader() {

    }


    @Throws(Exception::class)
    override fun close() {
    }

}