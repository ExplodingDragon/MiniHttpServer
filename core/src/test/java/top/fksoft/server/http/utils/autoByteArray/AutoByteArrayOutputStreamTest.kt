package top.fksoft.server.http.utils.autoByteArray

import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import kotlin.random.Random

/**
 * @author ExplodingDragon
 * @version 1.0
 */
class AutoByteArrayOutputStreamTest {
    private val auto by lazy {
        AutoByteArrayOutputStream(maxByteArraySize = 4096)
    }


    @Before
    fun before(): Unit {
        for (i in 0 until 10) {
            auto.write(Random.nextBytes(1024))
        }
    }

    @Test
    fun testSize(): Unit {
        assertFalse("写入文件问题.",auto.useByteArray)
        assertEquals("写入大小问题 .",auto.size, 10 * 1024L)
        assertTrue(auto.autoByteArray is FileAutoByteArray)
    }


    @After
    fun after(): Unit {
        auto.autoByteArray.close()
        auto.close()

    }
}