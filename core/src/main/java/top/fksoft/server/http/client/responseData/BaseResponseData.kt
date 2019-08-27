package top.fksoft.server.http.client.responseData

import top.fksoft.server.http.config.ResponseCode

import java.io.Closeable
import java.io.OutputStream
import java.io.PrintWriter

/**
 * @author ExplodingDragon
 * @version 1.0
 */
interface BaseResponseData : Closeable {

    val responseCode: ResponseCode

    fun writeHeader(writer: PrintWriter)

    fun writeBody(output: OutputStream)

}
