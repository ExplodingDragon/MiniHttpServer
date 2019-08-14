package top.fksoft.server.http.utils

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author ExplodingDragon
 * @version 1.0
 */
class AutoByteArraySearchTest {
    @Test
    fun search() {
        val str = "BBC ABCDAB AACDABABCDABCDABD"
        val pat = "ABCDABD"
        val byteArray = AutoByteArray(str.length.toLong() * 2)
        byteArray.openOutputStream().write(str.toByteArray())
        val search = AutoByteArraySearch(byteArray)
        val search1 = search.search(pat.toByteArray())
        assertEquals(search1,21)
    }
}