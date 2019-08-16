package top.fksoft.server.http.utils

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
    fun spitAll(pat: ByteArray, index: Long = 0): LongArray {
        val patSize = pat.size
        val list = LinkedList<Long>()

        var startIndex :Long = index
        var endIndex:Long

        while (true){
            startIndex = search(pat,startIndex)
            if (startIndex!=-1L){
                endIndex = startIndex + patSize
                list.add(startIndex)
                list.add(endIndex)
                startIndex+=patSize

            }else{
                break
            }
        }

        return list.toLongArray()
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

}
