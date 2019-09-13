package top.fksoft.execute.servlet

import jdkUtils.data.StringUtils
import top.fksoft.server.http.server.serverIO.HttpHeaderInfo
import top.fksoft.server.http.servlet.annotation.ServletBinder
import top.fksoft.server.http.servlet.impl.HtmlHttpServlet

/**
 * @author ExplodingDragon
 * @version 1.0
 */

@ServletBinder(path = "/i")
class InfoHtmlServlet(headerInfo: HttpHeaderInfo) : HtmlHttpServlet(headerInfo) {

    init {
        println(StringUtils.readInputStream(javaClass.getResourceAsStream("/InfoHtml.html")))
    }

}
