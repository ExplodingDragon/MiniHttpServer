package top.fksoft.server.http.factory.defaultFactory

import top.fksoft.server.http.client.BaseHttpExecute
import top.fksoft.server.http.config.HttpHeaderInfo
import top.fksoft.server.http.config.ServerConfig
import top.fksoft.server.http.example.FileHttpExecute
import top.fksoft.server.http.factory.FindHttpExecuteFactory

/**
 * @author ExplodingDragon
 * @version 1.0
 */
class DefaultFindHttpExecute(config: ServerConfig) : FindHttpExecuteFactory(config) {

    override fun findHttpExecute(info: HttpHeaderInfo): Class<out BaseHttpExecute> {
        val path = info.path.trim()
        val httpExecuteBinder = serverConfig.httpExecuteMap[path.substring(0, path.lastIndexOf('/')).trim()]
        httpExecuteBinder?.let {
            if (path.endsWith('/')) {
                if (it.bindDirectory) {
                    return it.executeClass
                }
            } else {
                if (!it.bindDirectory) {
                    return it.executeClass
                }
            }
        }
        return FileHttpExecute::class.java
    }
}
