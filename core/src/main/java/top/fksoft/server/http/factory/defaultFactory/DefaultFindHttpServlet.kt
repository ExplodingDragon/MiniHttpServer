package top.fksoft.server.http.factory.defaultFactory

import top.fksoft.server.http.servlet.base.BaseHttpServlet
import top.fksoft.server.http.serverIO.HttpHeaderInfo
import top.fksoft.server.http.config.ServerConfig
import top.fksoft.server.http.servlet.FileHttpServlet
import top.fksoft.server.http.factory.FindHttpServletFactory

/**
 * @author ExplodingDragon
 * @version 1.0
 */
class DefaultFindHttpServlet(config: ServerConfig) : FindHttpServletFactory(config) {

    override fun findHttpExecute(info: HttpHeaderInfo): Class<out BaseHttpServlet> {
        val path = info.path.trim()
        val httpExecuteBinder = serverConfig.httpExecuteMap[path.substring(0, path.lastIndexOf('/')).trim()]
        httpExecuteBinder?.let {
            if (path.endsWith('/')) {
                if (it.bindDirectory) {
                    return it.servletClass
                }
            } else {
                if (!it.bindDirectory) {
                    return it.servletClass
                }
            }
        }
        return FileHttpServlet::class.java
    }
}
