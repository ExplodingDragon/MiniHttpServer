package top.fksoft.server.http.utils.autoByteArray

import java.io.Closeable
import java.io.InputStream

/**
 * @author ExplodingDragon
 * @version 1.0
 */
interface AutoByteArray:Closeable {

    operator fun get(l: Long)
    fun openInputStream():InputStream
    val size:Long
}