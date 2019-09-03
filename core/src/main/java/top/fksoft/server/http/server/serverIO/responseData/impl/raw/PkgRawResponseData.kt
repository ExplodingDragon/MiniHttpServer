package top.fksoft.server.http.server.serverIO.responseData.impl.raw

import top.fksoft.server.http.config.ResponseCode
import top.fksoft.server.http.server.serverIO.responseData.BaseResponseData
import top.fksoft.server.http.utils.ContentTypeUtils
import java.io.ByteArrayOutputStream
import java.io.OutputStream

/**
 * @author ExplodingDragon
 * @version 1.0
 */
class PkgRawResponseData (private val path:String): BaseResponseData(){
    override val contentType: String = ContentTypeUtils.extension2Application(path)
    private val inputStream = PkgRawResponseData::class.java.getResourceAsStream(path)
    private var bytes:ByteArray = ByteArray(0)
    init {
        val output = ByteArrayOutputStream()
        val b = ByteArray(4096)
        while (true) {
            val i = inputStream.read(b)
            if (i == -1) {
                break
            }
            output.write(b, 0, i)
            output.flush()
             bytes = output.toByteArray()
            output.close()
        }
    }

    override val length: Long = bytes.size.toLong()
    override val responseCode: ResponseCode = ResponseCode.HTTP_OK



    override fun writeBody(output: OutputStream): Boolean {
        output.write(bytes)
        return true
    }

    override fun close() {
        inputStream.close()
        bytes = ByteArray(0)
    }

}
