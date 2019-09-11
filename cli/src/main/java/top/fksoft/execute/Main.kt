package top.fksoft.execute

import top.fksoft.execute.config.Config
import top.fksoft.execute.servlet.InfoServlet
import top.fksoft.server.http.HttpServer
import top.fksoft.server.http.logcat.Logger
import java.io.File

object Main {
    private val logger = Logger.getLogger(Main::class.java)

    @JvmStatic
    fun main(args: Array<String>) {

        val config = Config.newInstance()
        config.initConfig(args)
        config.printConfig()
        try {
            val httpServer = HttpServer(8080)
            val serverConfig = httpServer.serverConfig
            serverConfig.workDirectory = File("D:\\")
            serverConfig.addAutoHttpServlet(InfoServlet::class)
            httpServer.start()
        } catch (e: Exception) {
            logger.error("启动服务器时出现问题 ！", e)
        }

    }
}
