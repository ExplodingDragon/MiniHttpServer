package top.fksoft.server.http.client.responseData

import top.fksoft.server.http.config.ResponseCode
import java.io.OutputStream
import java.io.PrintWriter

/**
 * @author ExplodingDragon
 * @version 1.0
 */
abstract class DefaultResponseData : BaseResponseData {
    override val responseCode: ResponseCode
        get() = ResponseCode.HTTP_OK


    override fun writeHeader(writer: PrintWriter) {
    }

    override fun writeBody(output: OutputStream) {
    }

    override fun close() {
    }
}
