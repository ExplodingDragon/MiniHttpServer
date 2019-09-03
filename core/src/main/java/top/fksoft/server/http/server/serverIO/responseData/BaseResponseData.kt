package top.fksoft.server.http.server.serverIO.responseData

/**
 * @author ExplodingDragon
 * @version 1.0
 */

import top.fksoft.server.http.config.ResponseCode
import top.fksoft.server.http.logcat.Logger
import java.io.Closeable
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

/**
 * @author ExplodingDragon
 * @version 1.0
 */
abstract class BaseResponseData : Closeable {

    protected val logger = Logger.getLogger(javaClass)

    private val header = HashMap<String, String>()

    abstract val length:Long

    abstract val responseCode: ResponseCode

    abstract val contentType:String

    fun header():HashMap<String,String> = header

    protected fun addHeader(key: String, value: String) {
        header[key.trim()] = value.trim()
    }

    abstract fun writeBody(output: OutputStream):Boolean

    val webDateFormat: SimpleDateFormat = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH)


}
