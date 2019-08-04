package top.fksoft.server.http.client

import top.fksoft.server.http.config.HttpHeaderInfo
import top.fksoft.server.http.logcat.Logger
import top.fksoft.server.http.utils.CloseUtils

/**
 * @author ExplodingDragon
 * @version 1.0
 */
abstract class BaseHttpExecute protected constructor(private val headerInfo: HttpHeaderInfo,private val  response: ClientResponse) : CloseUtils.Closeable {
    protected var logger = Logger.getLogger(javaClass.kotlin)

    var hasPost = false

    /**
     *
     *# 初始化方法
     *
     * 初始化方法，用于在接受 HTTP 连接后
     * 逻辑处理前进行前进行一些操作
     *
     * @param headerInfo 客户端信息
     * @return 是否通过初始化
     * @throws Exception
     */
    @Throws(Exception::class)
    protected fun init(headerInfo: HttpHeaderInfo): Boolean {
        return true
    }


    /**
     *# HTTP 下的 GET请求执行的方法
     *
     * @param headerInfo HttpHeaderInfo
     * @param response ClientResponse
     * @throws Exception
     */
    @Throws(Exception::class)
    protected abstract fun doGet(headerInfo: HttpHeaderInfo, response: ClientResponse)

    /**
     *# HTTP 下的 POST请求执行的方法
     *
     * @param headerInfo HttpHeaderInfo
     * @param response ClientResponse
     * @throws Exception
     */
    @Throws(Exception::class)
    protected open fun doPost(headerInfo: HttpHeaderInfo, response: ClientResponse) {
        doGet(headerInfo, response)
    }

    /**
     * # 生命周期结束的方法
     *
     * @throws Exception
     */
    @Throws(Exception::class)
    override fun close() {

    }

    @Throws(Exception::class)
    fun execute(){
        try {
            if (!init(headerInfo)) {
                throw RuntimeException("无法通过init")
            }
            if (headerInfo.isPost()) {
                doPost(headerInfo, response)
            } else {
                doGet(headerInfo, response)
            }
        } catch (e: Exception) {
            logger.warn("在 ${headerInfo.remoteInfo} 中发生异常！",e)
        } finally {
            try {
                CloseUtils.close(this)
            } catch (e: Exception) {
                logger.warn("在 ${headerInfo.remoteInfo} 结束生命周期中发生异常！",e)
            }

        }
    }
}
