package top.fksoft.server.http.servlet

import jdkUtils.data.StringUtils
import jdkUtils.io.FileUtils
import top.fksoft.server.http.servlet.base.BaseHttpServlet
import top.fksoft.server.http.serverIO.ClientResponse
import top.fksoft.server.http.config.HttpConstant
import top.fksoft.server.http.serverIO.HttpHeaderInfo
import java.io.File
import java.util.*

/**
 * @author ExplodingDragon
 * @version 1.0
 */
open class FileHttpServlet protected constructor(headerInfo: HttpHeaderInfo, response: ClientResponse) : BaseHttpServlet(headerInfo, response) {
    init {
        hasPost = true
    }

    @Throws(Exception::class)
    override fun doGet(headerInfo: HttpHeaderInfo, response: ClientResponse) {
        val workDirectory = headerInfo.serverConfig.workDirectory
        var acceptFile = File(workDirectory, headerInfo.path.substring(1))
        if (headerInfo.path.endsWith('/')) {
            var string = StringUtils.inputStreamToString(javaClass.getResource(HttpConstant.EXAMPLE_FILE_PATH_1).openStream(), FileUtils.UTF_8)
            string = string.replace("%PATH%", headerInfo.path)
            var stringBuilder = StringBuilder()
            if (acceptFile.isDirectory) {
                var listFiles = acceptFile.listFiles().asList()
                FileUtils.sort(listFiles)
                for (file in listFiles) {
                    var name = file.name
                    var length = file.length()
                    var lengthStr = FileUtils.bytesToString(length.toDouble(), 2)
                    var date = String.format("%20s", Date(file.lastModified()).toString())
                    if (file.isDirectory) {
                        name = "$name/"
                        lengthStr = "-"
                    }
                    stringBuilder.append("<tr><td><a href=\"$name\">$name</a><td>$date</td><td>$lengthStr</td></tr>")
                }
                string = string.replace("%TABLE%", stringBuilder.toString())
                response.println(string)
            }
        } else {
            response.writeFile(acceptFile)
        }
    }

}
