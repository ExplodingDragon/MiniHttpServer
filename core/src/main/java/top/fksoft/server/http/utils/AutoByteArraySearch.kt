package top.fksoft.server.http.utils


/**
 * @author ExplodingDragon
 * @version 1.0
 */
class AutoByteArraySearch(private val autoByteArray: AutoByteArray) {

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

//    fun spitAll(pat: ByteArray, index: Long = 0): LongArray {
//
//    }

    private fun getSundayIndex(pat: ByteArray, byte: Byte): Int {
        for (i in pat.size - 1 downTo 0) {
            if (pat[i] === byte)
                return i
        }
        return -1
    }


    @Throws(IndexOutOfBoundsException::class)
    private fun sundaySearch(pat: ByteArray, index: Long = 0): Long {
        val M = autoByteArray.length()
        val N = pat.size
        if (index >= M) {
            throw IndexOutOfBoundsException()
        }
        var i = index
        var j: Int
        var skip = -1
        i = 0
        while (i <= M - N) {
            j = 0
            while (j < N) {
                if (autoByteArray[i + j] !== pat[j]) {
                    if (i === M - N)
                        break
                    skip = N - getSundayIndex(pat, autoByteArray[i + N])
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
