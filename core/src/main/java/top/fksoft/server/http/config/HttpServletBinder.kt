package top.fksoft.server.http.config

import top.fksoft.server.http.servlet.BaseHttpServlet
import java.io.IOException

/**
 * @author ExplodingDragon
 * @version 1.0
 * @property ignorePath String 默认的路径
 * @property servletClass KClass<BaseHttpServlet> 可用的实例
 * @property bindDirectory Boolean 是否绑定为路径
 * @property path String  最终绑定的路径
 */
data class HttpServletBinder(private var ignorePath:String, val servletClass:Class<BaseHttpServlet>, var bindDirectory: Boolean = false){

    val path:String
        get() = ignorePath

    init {
        ignorePath = ignorePath.trim()
        if (!ignorePath.startsWith("/")){
            throw IOException("不是合法的绑定路径结构.")
        }
        if (ignorePath.endsWith('/')){
            bindDirectory = true
            ignorePath = ignorePath.substring(0,ignorePath.lastIndexOf('/')).trim()
        }
    }

}