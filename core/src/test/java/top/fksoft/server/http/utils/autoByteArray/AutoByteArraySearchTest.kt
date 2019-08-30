package top.fksoft.server.http.utils.autoByteArray

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author ExplodingDragon
 * @version 1.0
 */
class AutoByteArraySearchTest {
    private val str = "Hello world!My name is ExplodingFKL."
    private val byteArray = str.toByteArray(Charsets.US_ASCII)
    private val arrayAutoByteArray = ArrayAutoByteArray(byteArray)
    private val search = arrayAutoByteArray.search
    private val pat = "world".toByteArray(Charsets.US_ASCII)

    @Test
    fun search() {
        assertEquals(search.search(pat,6),6)
        assertEquals(arrayAutoByteArray[6].toChar(),'w')
    }

    @Test
    fun spit() {
        val spit = search.spit(pat)
        assertEquals(arrayAutoByteArray.toString(spit[0], (spit[1] - spit[0]).toInt()),"Hello")
        assertEquals(arrayAutoByteArray.toByteArray(spit[0], spit[1] ).toString(Charsets.US_ASCII),"Hello")
    }

    @After
    fun after() {
        arrayAutoByteArray.close()
    }
}