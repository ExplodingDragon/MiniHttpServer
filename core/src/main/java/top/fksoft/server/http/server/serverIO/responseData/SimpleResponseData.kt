package top.fksoft.server.http.server.serverIO.responseData

import top.fksoft.server.http.config.ResponseCode
import top.fksoft.server.http.server.serverIO.responseData.impl.text.PkgHtmlResponseData

/**
 * @author ExplodingDragon
 * @version 1.0
 */
object SimpleResponseData {
    val NOT_FOUND = PkgHtmlResponseData(ResponseCode.HTTP_NOT_FOUND, "/res/resultHtml/404.html")
    val BAD_REQUEST = PkgHtmlResponseData(ResponseCode.HTTP_BAD_REQUEST, "/res/resultHtml/400.html")
    val WELCOME = PkgHtmlResponseData(packagePath = "/res/resultHtml/HelloWorld.html")

}