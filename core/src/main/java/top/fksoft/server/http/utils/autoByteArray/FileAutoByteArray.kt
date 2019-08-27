package top.fksoft.server.http.utils.autoByteArray

import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream

/**
 * @author ExplodingDragon
 * @version 1.0
 */
class FileAutoByteArray(private val tempFile:File) :AutoByteArray{
    var isClosed = false
    private set
    override val size: Long by lazy {
        tempFile.length()
    }
    init {
       if (tempFile.isFile.not()) {
           throw FileNotFoundException()
       }
   }
    @Synchronized
    override fun openInputStream(): InputStream {
    }

    @Synchronized
    override fun close() {
    }

    @Synchronized
    override fun get(l: Long) {
    }

}