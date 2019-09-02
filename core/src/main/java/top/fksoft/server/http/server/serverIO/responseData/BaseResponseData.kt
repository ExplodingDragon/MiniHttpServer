package top.fksoft.server.http.server.serverIO.responseData

/**
 * @author ExplodingDragon
 * @version 1.0
 */

import top.fksoft.server.http.config.ResponseCode
import java.io.Closeable
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author ExplodingDragon
 * @version 1.0
 */
interface BaseResponseData : Closeable {

    var length:Long

    var responseCode: ResponseCode

    var contentType:String

    fun header():Map<String,String>

    fun writeBody(output: OutputStream):Boolean

    val webDateFormat: SimpleDateFormat
        get() = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH)


}
