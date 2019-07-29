package top.fksoft.server.http.factory

import top.fksoft.server.http.client.BaseHttpExecute
import top.fksoft.server.http.config.HttpHeaderInfo
import top.fksoft.server.http.config.ServerConfig
import top.fksoft.server.http.factory.defaultFactory.DefaultFindHttpExecute

/**
 * @author ExplodingDragon
 * @version 1.0
 */
abstract class FindHttpExecuteFactory protected constructor(config: ServerConfig) {
    protected val serverConfig = config
    /**
     * # 得到当前 HTTP  连接可用的 HTTP 解析类
     *
     * @param info
     * @return
     */
    abstract fun findHttpExecute(info: HttpHeaderInfo): Class<out BaseHttpExecute>


    companion object{
        fun getDefault(config: ServerConfig): FindHttpExecuteFactory = default.getDeclaredConstructor(ServerConfig::class.java).newInstance(config)
        @JvmStatic
        val default = DefaultFindHttpExecute::class.java
    }
}
