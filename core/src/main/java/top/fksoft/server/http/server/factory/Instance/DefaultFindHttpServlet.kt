package top.fksoft.server.http.server.factory.Instance

import top.fksoft.server.http.config.ServerConfig
import top.fksoft.server.http.server.factory.FindHttpServletFactory
import top.fksoft.server.http.server.serverIO.HttpHeaderInfo
import top.fksoft.server.http.servlet.BaseHttpServlet
import top.fksoft.server.http.servlet.Instance.FileHttpServlet

/**
 * @author ExplodingDragon
 * @version 1.0
 */
class DefaultFindHttpServlet(config: ServerConfig) : FindHttpServletFactory(config) {

    override fun findHttpServlet(info: HttpHeaderInfo): Class<out BaseHttpServlet> {
        val path = info.path.trim().toLowerCase()
        val httpExecuteBinder = serverConfig.httpExecuteMap[path]
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
