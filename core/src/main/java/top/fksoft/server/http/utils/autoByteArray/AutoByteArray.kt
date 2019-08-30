package top.fksoft.server.http.utils.autoByteArray

import java.io.Closeable
import java.io.InputStream
import java.nio.charset.Charset

/**
 * @author ExplodingDragon
 * @version 1.0
 */
interface AutoByteArray : Closeable {

    val size: Long
    operator fun get(l: Long): Byte
    fun openInputStream(): InputStream
    fun toString(offset: Long = 0, length: Int = size.toInt(), charset: Charset = Charsets.UTF_8): String {
        return String(toByteArray2(offset, length), charset)
    }

    fun toByteArray(start: Long, end: Long) = toByteArray2(start, (end - start).toInt())
    fun toByteArray2(start: Long, length: Int): ByteArray {
        if (length == 0) {
            return ByteArray(0)
        }
        if (length < 0 || start >= size) {
            throw IndexOutOfBoundsException("length < 0 or start >= size length = $length ,start = $start ,size = $size.")
        }
        val inputStream = openInputStream()
        inputStream.skip(start)
        val array = ByteArray(length)
        inputStream.read(array)
        return array
    }

    val search: AutoByteArraySearch
        get() = openSearch()

    fun openSearch(): AutoByteArraySearch {
        return AutoByteArraySearch(this)
    }

}