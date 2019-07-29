package top.fksoft.server.http.factory.defaultFactory

import top.fksoft.server.http.factory.LogFactory
import top.fksoft.server.http.logcat.Log
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.KClass

/**
 * @author ExplodingDragon
 * @version 1.0
 */
class DefaultLog :LogFactory{
    private val format = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
    override fun outputLog(millisecond: Long, level: Int, clazz: KClass<*>, message: String, exception: Throwable?) {
        val output = StringBuilder()
        output.append("${format.format(millisecond)} - ${String.format("%-5s",Log.logIdName(level))} - ${clazz.java.name} : $message")
        exception?.let {
            output.append("\r\n")
            output.append(Log.exceptionToString(exception))
        }
        if (level >= Log.LOGGER_WARN){
            System.err.println(output)
        }else{
            println(output)
        }
    }



    override fun isDebug(): Boolean  = true


}