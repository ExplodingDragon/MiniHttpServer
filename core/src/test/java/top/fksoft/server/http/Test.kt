package top.fksoft.server.http

import org.junit.Test
import top.fksoft.server.http.server.serverIO.responseData.impl.raw.FileResponseData.Range

/**
 * @author ExplodingDragon
 * @version 1.0
 */
class Test {
    @Test
    fun test() {
        val s = 500L
        val d = 42854723L

        println(Range(d - 500, d).size)
    }

}