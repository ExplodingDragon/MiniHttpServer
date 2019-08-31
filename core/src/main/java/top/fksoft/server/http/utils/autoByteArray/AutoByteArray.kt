package top.fksoft.server.http.utils.autoByteArray

import java.io.Closeable
import java.io.File
import java.io.InputStream
import java.nio.charset.Charset
import kotlin.random.Random

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

    fun toString(charset: Charset): String{
        return toString(0, size.toInt(), charset)
    }

    fun toByteArray(start: Long, end: Long) = toByteArray2(start, (end - start + 1).toInt())
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

    fun copyOf(tempFile: File = AutoByteArrayOutputStream.getTempFile(Random.nextDouble()), index: Long, end: Long): AutoByteArray {
        var length = end - index
        if ( end < index || end >= size || length > size){
            throw IndexOutOfBoundsException()
        }
        if (length == 0L){
            return ArrayAutoByteArray(byteArrayOf(get(end)))
        }
        val outputStream = AutoByteArrayOutputStream(tempFile)
        val inputStream = openInputStream()
        inputStream.skip(index)
        val array = ByteArray(1024)
        while (true){
            inputStream.read(array)
            if (length >= 1024){
                length-=1024
                outputStream.write(array)
            }else{
                outputStream.write(array,0,length.toInt())
                break
            }
        }
        return outputStream.autoByteArray
    }

}