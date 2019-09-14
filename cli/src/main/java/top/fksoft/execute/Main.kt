package top.fksoft.execute

import jdkUtils.ModConfig
import jdkUtils.logcat.Logger
import top.fksoft.execute.servlet.InfoServlet
import top.fksoft.server.http.HttpServer
import top.fksoft.server.http.servlet.binder.impl.ResServletBinder
import java.io.File

object Main {
    private val logger = Logger.getLogger(Main::class.java)

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            val httpServer = HttpServer(8080)
            val serverConfig = httpServer.serverConfig
            ModConfig.debug = true
            serverConfig.workDirectory = File("D:\\")
            serverConfig.addAutoHttpServlet(InfoServlet::class)
            serverConfig.addHttpServletBinder(ResServletBinder("/","/InfoHtml.html"))
            serverConfig.addHttpServletBinder(ResServletBinder("/marked.min.js","/marked.min.js"))

            httpServer.start()
        } catch (e: Exception) {
            logger.error("启动服务器时出现问题 ！", e)
        }

    }
}
