package top.fksoft.server.http.utils

import org.junit.Assert
import org.junit.Test
import top.fksoft.server.http.utils.autoByteArray.AutoByteArrayOutputStream

/**
 * @author ExplodingDragon
 * @version 1.0
 */
class AutoByteArrayOutputStreamTest{

    @Test
    fun run() {
        val outputStream = AutoByteArrayOutputStream()

        outputStream.write(ByteArray(10024))

        Assert.assertTrue(outputStream.size == 10024L)
    }
}