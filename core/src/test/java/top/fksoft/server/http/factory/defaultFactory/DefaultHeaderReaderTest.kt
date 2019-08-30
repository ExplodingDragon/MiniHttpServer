package top.fksoft.server.http.factory.defaultFactory

import org.junit.Test
import kotlin.text.Charsets.UTF_8

/**
 * @author ExplodingDragon
 * @version 1.0
 */
class DefaultHeaderReaderTest{

    @Test
    fun test() {
        val toByteArray = "打野\r\n".toByteArray(UTF_8)
        println(toByteArray.toString(UTF_8))
    }
}