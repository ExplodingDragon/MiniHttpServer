package top.fksoft.server.http.utils.longByte

import top.fksoft.server.http.utils.CalculateUtils
import java.nio.charset.Charset
import java.util.*


/**
 * @author ExplodingDragon
 * @version 1.0
 */
class LongByteArraySearch(private val longByteArray: LongByteArray) {

    /**
     * # 序列搜索
     *
     * 使用 Sunday算法进行搜索
     *
     * @param pat ByteArray 匹配数组
     * @param index Long 开始位置
     * @return Long 第一次出现的位置
     * @throws IndexOutOfBoundsException 如果索引异常
     */
    @Throws(IndexOutOfBoundsException::class)
    fun search(pat: ByteArray, index: Long = 0): Long = sundaySearch(pat, index)

    /**
     * # 数据切割
     * @param pat ByteArray
     * @param index Long
     * @return LongArray
     */
    fun spitAll(pat: ByteArray, index: Long = 0, addEndIndex:Boolean = true): LongArray {
        val patSize = pat.size
        val list = LinkedList<Long>()

        var startIndex :Long = index
        var endIndex:Long

        while (true){
            startIndex = search(pat,startIndex)
            if (startIndex!=-1L){
                endIndex = startIndex + patSize
                list.add(startIndex)
                if (addEndIndex){
                    list.add(endIndex)
                }
                startIndex+=patSize

            }else{
                break
            }
        }

        return list.toLongArray()
    }
    fun readLine(index: Long,charset: Charset = Charsets.UTF_8):String?{
        val lineEndIndex = lineEndIndex(index)
        if (lineEndIndex == -1L )
            return null
        return String(longByteArray.toByteArray(index, (lineEndIndex - index).toInt()), charset)
    }

    fun lineEndIndex(index: Long) :Long{
        if (index >=  longByteArray.size){
            throw IndexOutOfBoundsException("index > array.size")
        }
        for (startIndex in index until longByteArray.size){
            val char = longByteArray[startIndex].toChar()
            if (char == '\r' || char == '\n'){
                return startIndex
            }
        }
        return -1
    }
    fun nextLineStartIndex(index: Long) :Long{
        if (index >=  longByteArray.size){
            throw IndexOutOfBoundsException("index > array.size")
        }
        for ((index ,value) in (index until longByteArray.size).withIndex()){
            val char = longByteArray[value].toChar()
            val nextChar = longByteArray[value + 1].toChar()

            if (char == '\r'){
                if (nextChar == '\n'){
                    return value + 2
                }else{
                    return value + 1
                }
            }
            if (char == '\n'){
                if (nextChar == '\r'){
                    return value + 2
                }else{
                    return value + 1
                }
            }
        }
        return -1
    }
    fun getNextLineIndex(index: Long,lineSize:Int = 0):Long {
        if (index >=  longByteArray.size){
            throw IndexOutOfBoundsException("index > array.size")
        }
        var result:Long = index
        for (i in 0 .. lineSize){
            result = nextLineStartIndex(result)
        }
        return result
    }
    private fun getSundayIndex(pat: ByteArray, byte: Byte): Int {
        for (i in pat.size - 1 downTo 0) {
            if (pat[i] === byte)
                return i
        }
        return -1
    }


    @Throws(IndexOutOfBoundsException::class)
    private fun sundaySearch(pat: ByteArray, index: Long = 0): Long {
        val M = longByteArray.length() - index
        val N = pat.size
        if (M < 0) {
            throw IndexOutOfBoundsException()
        }
        var i = index
        var j: Int
        var skip = -1
        while (i <= M - N) {
            j = 0
            while (j < N) {
                if (longByteArray[i + j] !== pat[j]) {
                    if (i === M - N)
                        break
                    skip = N - getSundayIndex(pat, longByteArray[i + N])
                    break
                }
                j++
            }
            if (j === N)
                return i
            i += skip
        }
        return -1

    }

    fun calculate(method: String): String = CalculateUtils.getMD5(longByteArray.openInputStream(),method)


}
