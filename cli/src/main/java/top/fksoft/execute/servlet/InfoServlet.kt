package top.fksoft.execute.servlet

import top.fksoft.server.http.server.serverIO.HttpHeaderInfo
import top.fksoft.server.http.servlet.annotation.ServletBinder
import top.fksoft.server.http.servlet.binder.BaseHttpServletBinder
import top.fksoft.server.http.servlet.impl.HtmlHttpServlet

/**
 * @author ExplodingDragon
 * @version 1.0
 */

@ServletBinder(path = "/i.md")
class InfoServlet(headerInfo: HttpHeaderInfo) : HtmlHttpServlet(headerInfo) {
    init {
        printAsResource("/InfoHtml.md")
        val serverConfig = headerInfo.serverConfig
        replaceNode("PORT", serverConfig.serverPort.toString())
        val sysProperties = StringBuilder()
        System.getProperties().forEach { t, u ->
            sysProperties.append("key=$t,value=$u;").append('\n')
        }
        replaceNode("PROPERTY", sysProperties.toString())
        replaceNode("IP",headerInfo.remoteInfo.ip)
        val binder = StringBuilder()
        serverConfig.httpServletMap.forEach { (t: String, _: BaseHttpServletBinder) ->
            binder.append(t).append("\n")
        }
        replaceNode("BINDER",binder.toString())


    }

    override fun doGet(headerInfo: HttpHeaderInfo) {
        val binder = StringBuilder()
        val field = HttpHeaderInfo::class.java.getDeclaredField("formArray")
        field.isAccessible = true
        val get = field.get(headerInfo)
        if (get is HashMap<* , *>){
            get.forEach { (key, value) ->
                binder.append("|$key|$value|").append('\n')
            }
            replaceNode("GET_ARRAY",binder.toString())
        }
    }

    override fun doPost(headerInfo: HttpHeaderInfo) {

    }
}
