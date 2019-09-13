package top.fksoft.execute

import jdkUtils.logcat.Logger
import top.fksoft.execute.servlet.InfoHtmlServlet
import top.fksoft.execute.servlet.InfoServlet
import top.fksoft.server.http.HttpServer
import java.io.File

object Main {
    private val logger = Logger.getLogger(Main::class.java)

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            val httpServer = HttpServer(8080)
            val serverConfig = httpServer.serverConfig
            serverConfig.workDirectory = File("D:\\")
            serverConfig.addAutoHttpServlet(InfoServlet::class)
            serverConfig.addAutoHttpServlet(InfoHtmlServlet::class)
            httpServer.start()
        } catch (e: Exception) {
            logger.error("启动服务器时出现问题 ！", e)
        }

    }
}
