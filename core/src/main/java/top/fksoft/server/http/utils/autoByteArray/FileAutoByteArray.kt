package top.fksoft.server.http.utils.autoByteArray

import java.io.*
import java.util.*

/**
 * @author ExplodingDragon
 * @version 1.0
 */
class FileAutoByteArray(private val tempFile: File) : AutoByteArray {
    var isClosed = false
        private set
    private val closeableList = LinkedList<Closeable>()
    override val size: Long by lazy {
        tempFile.length()
    }
    private val randomAccessFile by lazy {
        RandomAccessFile(tempFile, "r")

    }

    init {
        if (tempFile.isFile.not()) {
            throw FileNotFoundException()
        }
    }

    @Synchronized
    override fun openInputStream(): InputStream {
        if (isClosed) {
            throw RuntimeException("This is already closed.")
        }
        val fileInputStream = FileInputStream(tempFile)
        closeableList.add(fileInputStream)
        return fileInputStream
    }

    @Synchronized
    override fun get(l: Long): Byte {
        if (isClosed) {
            throw RuntimeException("This is already closed.")
        }
        if (l >= size) {
            throw IndexOutOfBoundsException("l >= size :$l >= $size .")
        }
        randomAccessFile.seek(l)
        return randomAccessFile.readByte()
    }

    @Synchronized
    override fun close() {
        if (isClosed.not()) {
            isClosed = true
        }
        closeableList.forEach {
            try {
                it.close()
            } catch (ignore: Exception) {
            }
        }
        randomAccessFile.close()
        tempFile.delete()

    }

}