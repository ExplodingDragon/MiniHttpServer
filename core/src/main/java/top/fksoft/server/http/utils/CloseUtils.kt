package top.fksoft.server.http.utils

/**
 * @author ExplodingDragon
 * @version 1.0
 */
object CloseUtils {
    fun close(vararg close: Closeable?) {
        for (closeable in close) {
            if (closeable == null) {
                continue
            }
            try {
                closeable.close()
            } catch (ignored: Exception) {
            }

        }

    }

    fun close(vararg close: java.io.Closeable) {
        for (closeable in close) {
            try {
                closeable.close()
            } catch (ignored: Exception) {
            }

        }

    }

    interface Closeable {

        @Throws(Exception::class)
        fun close()
    }
}
