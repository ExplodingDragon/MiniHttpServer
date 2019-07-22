package top.fksoft.server.http.utils

import java.io.IOException

/**
 * @author ExplodingDragon
 * @version 1.0
 */
object CloseUtils {

    @Throws(Exception::class)
    fun close(vararg close: Closeable?) {
        for (closeable in close) {
            if (closeable == null) {
                continue
            }
            closeable.close()
        }

    }

    @Throws(IOException::class)
    fun close(vararg close: java.io.Closeable) {
        for (closeable in close) {
                closeable.close()

        }

    }

    interface Closeable {

        @Throws(Exception::class)
        fun close()

    }
}
