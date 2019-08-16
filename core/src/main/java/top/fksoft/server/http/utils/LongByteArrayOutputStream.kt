package top.fksoft.server.http.utils

import java.io.IOException
import java.io.OutputStream
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset


/**
 * @author ExplodingDragon
 */
class LongByteArrayOutputStream(private val bytes: ByteArray) : OutputStream() {
    val arraySize:Int = bytes.size

    private var count: Int = 0


    @Synchronized
    override fun write(b: Int) {
        if (count >= arraySize){
            throw IndexOutOfBoundsException()
        }
        bytes[count] = b.toByte()
        count += 1
    }


    @Synchronized
    override fun write(b: ByteArray, off: Int, len: Int) {
        if (off < 0 || off > b.size || len < 0 ||
                off + len - b.size > 0) {
            throw IndexOutOfBoundsException()
        }
        System.arraycopy(b, off, bytes, count, len)
        count += len
    }



    @Synchronized
    fun reset() {
        count = 0
    }


    @Synchronized
    fun toByteArray(): ByteArray {
        return bytes.copyOf(count)
    }


    @Synchronized
    fun size(): Int {
        return count
    }


    @Synchronized
    override fun toString(): String {
        return String(bytes, 0, count)
    }


    @Synchronized
    @Throws(UnsupportedEncodingException::class)
    fun toString(charsetName: Charset): String {
        return String(bytes, 0, count, charsetName)
    }



    @Throws(IOException::class)
    override fun close() {
    }



}
