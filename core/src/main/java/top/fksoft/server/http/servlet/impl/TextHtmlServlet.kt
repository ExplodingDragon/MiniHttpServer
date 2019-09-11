package top.fksoft.server.http.servlet.impl

import top.fksoft.server.http.server.serverIO.HttpHeaderInfo
import top.fksoft.server.http.server.serverIO.responseData.impl.TextResponseData
import top.fksoft.server.http.servlet.BaseHttpServlet
import java.io.IOException

/**
 * @author ExplodingDragon
 * @version 1.0
 */
open class TextHtmlServlet(headerInfo: HttpHeaderInfo) : BaseHttpServlet(headerInfo) {
    init {
        responseData = TextResponseData()
    }

    var contentType:String
    set(value) {
        responseData.contentType = contentType
    }
    get() = responseData.contentType

    fun replace(old: String, newValues: String) {
            (responseData as TextResponseData).replace(old,newValues)
    }

    fun println(str: String) {
        (responseData as TextResponseData).println(str)
    }

    fun print(str: String) {
        (responseData as TextResponseData).print(str)
    }

    fun printf(format: String, vararg args: Any) {
        (responseData as TextResponseData).printf(format,args)
    }

    override fun doGet(headerInfo: HttpHeaderInfo) {
    }

    @Throws(IOException::class)
    override fun close() {
    }
}
