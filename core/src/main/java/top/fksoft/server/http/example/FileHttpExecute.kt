package top.fksoft.server.http.example

import jdkUtils.data.StringUtils
import jdkUtils.io.FileUtils
import top.fksoft.server.http.client.BaseHttpExecute
import top.fksoft.server.http.client.ClientResponse
import top.fksoft.server.http.config.HttpHeaderInfo
import java.io.File
import java.util.*

/**
 * @author ExplodingDragon
 * @version 1.0
 */
open class FileHttpExecute protected constructor(headerInfo: HttpHeaderInfo, response: ClientResponse) : BaseHttpExecute(headerInfo, response) {
    @Throws(Exception::class)
    override fun doGet(headerInfo: HttpHeaderInfo, response: ClientResponse) {
        val workDirectory = headerInfo.serverConfig.workDirectory
        var acceptFile = File(workDirectory, headerInfo.path.substring(1))
        if (headerInfo.path.endsWith('/')) {
            var string = StringUtils.inputStreamToString(javaClass.getResource("<html>\n<head>\n    <title>index of %path%</title>\n    <style>\n        body {\n            padding: 10px;\n        }\n\n        th {\n            padding-left: 20px;\n            padding-right: 20px;\n        }\n\n        .name {\n            padding-left: 40px;\n            padding-right: 40px;\n        }\n    </style>\n</head>\n<body>\n<div style=\"text-align: left;\"><h1>index of %path%</h1></div>\n<hr>\n<div style=\"text-align: center;\"></div>\n<table border=\"0\" style=\"text-align: center\">\n    <tr>\n        <th class=\"name\">文件名称</th>\n        <th>文件大小</th>\n        <th>创建日期</th>\n        <th>访问</th>\n    </tr>\n    %table%\n</table>\n</body>\n</html>").openStream(), FileUtils.UTF_8)
            string = string.replace("%path%", headerInfo.path)
            var stringBuilder = StringBuilder()
            stringBuilder.append("<tr><td>上级目录</td><td>-</td><td>-</td><td><a href=\"..\"><button> 访问</button></a></td></tr>\n")

            if (acceptFile.isDirectory) {

                var listFiles = acceptFile.listFiles().asList()
                FileUtils.sort(listFiles)
                for (file in listFiles) {
                    var href = file.name

                    var length = file.length()
                    var date = String.format("%20s", Date(file.lastModified()).toString())
                    var name = file.name
                    if (file.isDirectory) {
                        href = "$href/"
                        name = "$name/"
                        length = 0
                    }
                    stringBuilder.append("<tr><td>$name</td><td>${
                    FileUtils.bytesToString(length.toDouble(),0)}</td><td>$date</td><td><a href=\" $href \"><button> 访问</button></a></td></tr>\n")
                }
            }
            string = string.replace("%table%", stringBuilder.toString())

            response.println(string)
        } else {
            response.writeFile(acceptFile)
        }

    }
}
