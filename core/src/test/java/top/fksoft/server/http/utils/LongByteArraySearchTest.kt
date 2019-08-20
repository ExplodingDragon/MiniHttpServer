package top.fksoft.server.http.utils

import org.junit.Assert.assertEquals
import org.junit.Test
import top.fksoft.server.http.utils.longByte.LongByteArray
import top.fksoft.server.http.utils.longByte.LongByteArraySearch
import java.util.*

/**
 * @author ExplodingDragon
 * @version 1.0
 */
class LongByteArraySearchTest {
    @Test
    fun spitAll() {
        val bytes = byteArrayOf(1, 2, 3, 3, 1, 5, 5, 7, 8, 4, 4, 2, 3, 12, 1, 4, 5, 6, 2, 2, 3, 5, 5, 3)
        val pat = byteArrayOf(2,3)
        val autoByteArray = LongByteArray(arraySize = bytes.size.toLong())
        autoByteArray.openOutputStream().write(bytes)
        val search = autoByteArray.openSearch()
        val spitAll = search.spitAll(pat)
        assert(Arrays.equals(spitAll, longArrayOf(1, 3, 11, 13)))
    }

    @Test
    fun search() {
        val str = "BBC ABCDAB AACDABABCDABCDABD"
        val pat = "ABCDABD"
        val byteArray = LongByteArray(arraySize = str.length.toLong() * 2)
        byteArray.openOutputStream().write(str.toByteArray())
        val search = LongByteArraySearch(byteArray)
        val search1 = search.search(pat.toByteArray())
        assertEquals(search1, 21)
    }
}