package top.fksoft.server.http.utils.longByte

import jdkUtils.data.StringUtils
import jdkUtils.io.FileUtils
import top.fksoft.server.http.config.HttpConstant
import top.fksoft.server.http.logcat.Logger
import java.io.*
import java.nio.charset.Charset
import java.util.*
import kotlin.random.Random

/**
 *# 自动且临时的大数据保存容器
 *
 * 不具有线程安全特性 !!!
 *
 *
 * @author ExplodingDragon
 * @version 1.0
 */
public class LongByteArray(private val tempFile: File = getTempPath(), private val arraySize: Long = tempFile.length()) : Closeable {


    private val logger = Logger.getLogger(LongByteArray::class)
    private val list = LinkedList<Closeable>()
    /**
     * 表示是否使用本地临时文件
     */
    val useTempFile: Boolean
    /**
     * 临时文件
     */
    private var tempRandomAccessFile: RandomAccessFile? = null
    private var byteArray = ByteArray(0)

    init {
        useTempFile = arraySize > MAX_RAM_SIZE || tempFile.isFile
        if (useTempFile || arraySize == -1L) {
            logger.debug("使用临时文件；文件位置：[${tempFile.absolutePath}]")
            tempRandomAccessFile = RandomAccessFile(tempFile, "rws")
            if (!tempFile.isFile) {
                tempRandomAccessFile!!.setLength(arraySize)
            }
        } else {
            if (arraySize > 0) {
                logger.debug("使用数组；数组大小:[$arraySize]")
            }
            byteArray = ByteArray(arraySize.toInt())
        }
    }


    /**
     *
     * @param i Long
     * @param value Byte
     * @throws IndexOutOfBoundsException
     */
    @Synchronized
    @Throws(IndexOutOfBoundsException::class)
    operator fun set(i: Long, value: Byte) {
        if (i >= arraySize) {
            throw IndexOutOfBoundsException()
        }
        if (useTempFile) {
            tempRandomAccessFile?.let {
                synchronized(it) {
                    it.seek(i)
                    it.writeByte(value.toInt())
                }

            }
        } else {
            synchronized(byteArray) {
                byteArray[i.toInt()] = value
            }
        }

    }


    operator fun get(i: Long): Byte {
        if (i >= arraySize) {
            throw IndexOutOfBoundsException()
        }
        if (useTempFile) {
            tempRandomAccessFile?.let {
                synchronized(it) {
                    it.seek(i)
                    return it.readByte()
                }
            }
            throw NullPointerException()
        } else {
            return byteArray[i.toInt()]
        }
    }

    @Throws(Exception::class)
    fun toByteArray(index: Long = 0, length: Int = arraySize.toInt() - 1): ByteArray {
        if (length > 2 * MAX_RAM_SIZE) {
            throw OutOfMemoryError()
        }
        if (index + length >= arraySize) {
            throw IndexOutOfBoundsException()
        }
        val result = ByteArray(length.toInt())
        if (useTempFile) {
            tempRandomAccessFile?.let {
                synchronized(it) {
                    it.seek(index)
                    it.read(result, 0, length)
                }
            }
        } else {
            synchronized(byteArray) {
                System.arraycopy(byteArray, index.toInt(), result, 0, length)
            }
        }
        return result
    }

    /**
     * # 得到其InputStream对象
     *
     * @return InputStream
     */
    fun openInputStream(): InputStream {
        if (useTempFile) {
            synchronized(useTempFile) {
                val fileInputStream = FileInputStream(tempFile)
                list.add(fileInputStream)
                return fileInputStream
            }

        } else {
            return ByteArrayInputStream(byteArray)
        }
    }

    fun openOutputStream(): OutputStream {
        if (useTempFile) {
            synchronized(useTempFile) {
                val fileOutputStream = FileOutputStream(tempFile)
                list.add(fileOutputStream)
                return fileOutputStream
            }
        } else {
            return LongByteArrayOutputStream(byteArray)
        }
    }

    override fun close() {
        if (useTempFile) {
            tempRandomAccessFile?.let {
                synchronized(it) {
                    it.close()
                }
                for (outputStream in list) {
                    outputStream.close()
                }
                list.clear()
                logger.debug("删除临时文件结果：${tempFile.delete()}")
            }
        } else {
            byteArray = ByteArray(0)
        }
    }

    fun length(): Long = if (useTempFile) tempRandomAccessFile!!.length() else byteArray.size.toLong()

    val size: Long
        get() = length()

    fun openSearch() = LongByteArraySearch(this)

    override fun toString(): String {
        return toString(Charsets.UTF_8)
    }


    fun toString(charset: Charset): String {
        if (!useTempFile) {
            return String(byteArray, charset)
        }
        return super.toString()
    }

    fun copyOfNewArray(fileIndex: Long = 0, fileEnd: Long = size, tempFile: File = getTempPath()): LongByteArray {
        if (fileIndex > fileEnd) {
            throw IndexOutOfBoundsException("fileEnd - fileIndex = ${fileEnd - fileIndex} .")
        }
        val inputStream = openInputStream()
        inputStream.skip(fileIndex)
        FileUtils.delete(tempFile)
        tempFile.createNewFile()
        val outputStream = FileOutputStream(tempFile)
        var size = fileEnd - fileIndex
        val array = ByteArray(1024)
        while (true) {
            if (inputStream.available() == 0) {
                break
            }
            var readSize = inputStream.read(array, 0, array.size)
            if (readSize <= size) {
                outputStream.write(array)
                size -= readSize
            } else {
                outputStream.write(array, 0, size.toInt())
                break
            }
            outputStream.flush()
        }
        outputStream.close()
        inputStream.close()
        return LongByteArray(tempFile)
    }


    companion object {


        /**
         * 指定最大占用内存大小，超过则建立临时文件
         */
        @JvmStatic
        val MAX_RAM_SIZE = 8 * 1024

        /**
         * # 根据各种因素生成一个临时文件
         *
         * @return File 临时文件绝对位置
         */
        @Synchronized
        fun getTempPath(): File {
            val prefix = StringUtils.md5Encryption("${Random.nextDouble()}${System.nanoTime()}")
            return File(HttpConstant.PROPERTY_SYSTEM_TEMP_DIR, "$prefix.tmp")
        }


    }
}
