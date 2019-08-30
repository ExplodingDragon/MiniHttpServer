package top.fksoft.server.http.utils.autoByteArray

import top.fksoft.server.http.utils.CalculateUtils
import java.nio.charset.Charset
import java.util.*

/**
 * @author ExplodingDragon
 * @version 1.0
 */
class AutoByteArraySearch(private val autoByteArray: AutoByteArray) {


    /**
     * # 使用Java 实现的算法进行数据搜索
     *
     * 在Byte数组中进行快速搜索，
     * 现已改用 Sunday算法进行搜索，弃用了KMP
     *
     * @param pat ByteArray 搜索源
     * @param index Long 开始位置
     * @return Long 第一次出现的位置
     * @throws IndexOutOfBoundsException 如果传入数据错误
     */
    @Throws(IndexOutOfBoundsException::class)
    fun search(pat: ByteArray, index: Long = 0): Long = sundaySearch(pat, index)

    /**
     *
     * # 数据切割
     *
     * > Test pass.
     *
     *  对数据进行预切割，返回对应的切割数据的位置
     *
     * @param pat ByteArray 切割条件
     * @param index Long 切割起始位置
     * @return LongArray 得到的切割位置
     */
    fun spit(pat: ByteArray, index: Long = 0): LongArray {
        if (index < 0 || pat.isEmpty()) {
            throw IndexOutOfBoundsException("index < 0 || pat.isEmpty() => index = $index")
        }

        val patSize = pat.size
        val list = LinkedList<Long>()
        var startIndex = index
        while (true) {
            val search = search(pat, startIndex)
            if (search == -1L) {
                break
            }
            when {
                search == index -> list.add(search + patSize)
                index == startIndex -> {
                    list.add(0)
                    list.add(search - 1)
                    list.add(search + patSize)
                }
                else -> {
                    list.add(search - 1)
                    list.add(search + patSize)
                }
            }
            startIndex += search + patSize
        }
        if (list.last == (autoByteArray.size - 1)) {
            list.removeLast()
        } else {
            list.add(autoByteArray.size)
        }

        return list.toLongArray()
    }


    fun readLine(index: Long, charset: Charset = Charsets.UTF_8): String? {
        val lineEndIndex = lineEndIndex(index)
        if (lineEndIndex == -1L)
            return null
        return autoByteArray.toString(index, (lineEndIndex - index).toInt(), charset)
    }

    fun lineEndIndex(index: Long): Long {
        if (index >= autoByteArray.size) {
            throw IndexOutOfBoundsException("index > array.size")
        }
        for (startIndex in index until autoByteArray.size) {
            val char = autoByteArray[startIndex].toChar()
            if (char == '\r' || char == '\n') {
                return startIndex
            }
        }
        return -1
    }

    fun nextLineStartIndex(index: Long): Long {
        if (index >= autoByteArray.size) {
            throw IndexOutOfBoundsException("index > array.size")
        }
        for (value in (index until autoByteArray.size)) {
            val char = autoByteArray[value].toChar()
            val nextChar = autoByteArray[value + 1].toChar()

            if (char == '\r') {
                if (nextChar == '\n') {
                    return value + 2
                } else {
                    return value + 1
                }
            }
            if (char == '\n') {
                if (nextChar == '\r') {
                    return value + 2
                } else {
                    return value + 1
                }
            }
        }
        return -1
    }

    fun getNextLineIndex(index: Long, lineSize: Int = 0): Long {
        if (index >= autoByteArray.size) {
            throw IndexOutOfBoundsException("index > array.size")
        }
        var result: Long = index
        for (i in 0..lineSize) {
            result = nextLineStartIndex(result)
        }
        return result
    }

    private fun getSundayIndex(pat: ByteArray, byte: Byte): Int {
        for (i in pat.size - 1 downTo 0) {
            if (pat[i] == byte) {
                return i
            }
        }
        return -1
    }


    @Throws(IndexOutOfBoundsException::class)
    private fun sundaySearch(pat: ByteArray, index: Long = 0): Long {
        val m = autoByteArray.size - index
        val n = pat.size
        if (m < 0) {
            throw IndexOutOfBoundsException()
        }
        var i = index
        var j: Int
        var skip = -1
        while (i <= m - n) {
            j = 0
            while (j < n) {
                if (autoByteArray[i + j] != pat[j]) {
                    if (i == m - n) {
                        break
                    }
                    skip = n - getSundayIndex(pat, autoByteArray[i + n])
                    break
                }
                j++
            }
            if (j == n) {
                return i
            }
            i += skip
        }
        return -1

    }

    fun calculate(method: String): String = CalculateUtils.getMD5(autoByteArray.openInputStream(), method)


}
