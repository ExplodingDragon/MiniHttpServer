package top.fksoft.server.http.server.serverIO.responseData.impl

import jdkUtils.data.StringUtils
import top.fksoft.server.http.config.ResponseCode
import java.nio.charset.Charset
import kotlin.text.Charsets.UTF_8

/**
 * @author ExplodingDragon
 * @version 1.0
 */
class PkgHtmlResponseData(override var responseCode: ResponseCode = ResponseCode.HTTP_OK, packagePath: String, charset: Charset = UTF_8):HtmlResponseData(){
    init {
        println(StringUtils.inputStreamToString(javaClass.getResourceAsStream("/res/resultHtml/400.html"),charset.name()))
    }
}
