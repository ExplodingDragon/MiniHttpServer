package top.fksoft.server.http.config

import top.fksoft.server.http.servlet.BaseHttpServlet
import top.fksoft.server.http.servlet.annotation.ServletBinder
import java.io.IOException
import java.lang.annotation.AnnotationFormatError

/**
 * @author ExplodingDragon
 * @version 1.0
 * @property ignorePath String 默认的路径
 * @property servletClass KClass<BaseHttpServlet> 可用的实例
 * @property bindDirectory Boolean 是否绑定为路径
 * @property path String  最终绑定的路径
 */
data class HttpServletBinder( val servletClass:Class<out BaseHttpServlet>,private var ignorePath:String = getPath(servletClass) , var bindDirectory: Boolean = isBindDirectory(servletClass)){

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
    companion object{
        fun getPath(clazz: Class<out BaseHttpServlet>): String{
            val annotation = clazz.getAnnotation(ServletBinder::class.java) ?: throw AnnotationFormatError("未标记注解.")
            return annotation.path
        }
        fun isBindDirectory(clazz: Class<out BaseHttpServlet>): Boolean{
            val annotation = clazz.getAnnotation(ServletBinder::class.java) ?: throw AnnotationFormatError("未标记注解.")
            return annotation.bindDirectory
        }
    }

}