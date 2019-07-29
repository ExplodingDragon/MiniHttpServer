package top.fksoft.server.http.factory

import top.fksoft.server.http.factory.defaultFactory.DefaultLog
import kotlin.reflect.KClass

/**
 * @author ExplodingDragon
 * @version 1.0
 */
interface LogFactory {
    fun outputLog(millisecond: Long,level:Int,clazz: KClass<*>,message: String, exception: Throwable?)
    fun isDebug():Boolean
    companion object {
        val default = DefaultLog()
    }
}