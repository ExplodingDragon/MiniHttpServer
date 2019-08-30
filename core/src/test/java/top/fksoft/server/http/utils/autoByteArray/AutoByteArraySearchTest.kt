package top.fksoft.server.http.utils.autoByteArray

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author ExplodingDragon
 * @version 1.0
 */
class AutoByteArraySearchTest {
    private val str = "Hello world!My name is ExplodingFKL.\r\n but I like Chinese.\n\n\n\n\n"
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
        val array = ArrayAutoByteArray("ABCDEFGHIJKLNMOPQRSTUVWXYZ0123456789ABCDEFGHIJKLNMOPQRSTUVWXYZ0123456789ABCDEFGHIJKLNMOPQRSTUVWXYZ0123456789ABCDEFGHIJKLNMOPQRSTUVWXYZ0123456789ABCDEFGHIJKLNMOPQRSTUVWXYZ0123456789".toByteArray(Charsets.US_ASCII))
        println(array.size)
        println(array.search.search("123".toByteArray(Charsets.US_ASCII), 102))
        val spit = array.search.spit("123".toByteArray())
        for (l in spit.indices.step(2)) {
            val start = spit[l]
            val end = spit[l + 1]
            println(array.toByteArray(start, end).toString(Charsets.US_ASCII))
        }
    }

    @Test
    fun readLine() {
        assertEquals(search.readLine(),"Hello world!My name is ExplodingFKL.")

    }

    @Test
    fun toByteArray(){
        println(arrayAutoByteArray.toByteArray(0, 5).toString(Charsets.US_ASCII))
    }

    @After
    fun after() {
        arrayAutoByteArray.close()
    }


}