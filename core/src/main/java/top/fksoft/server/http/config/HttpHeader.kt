package top.fksoft.server.http.config

import top.fksoft.server.http.utils.CloseUtils

/**
 *
 * HTTP 头信息
 *
 *
 * 用于保存HTTP Header 的所有信息
 *
 *
 * @author ExplodingDragon
 * @version 1.0
 */
class HttpHeader(private val remoteInfo: NetworkInfo) : HttpKey, CloseUtils.Closeable {
    private val edit = Edit()
    private var method = HttpKey.METHOD_GET

    @Throws(Exception::class)
    override fun close() {

    }

    fun edit(): Edit {
        return edit
    }

     inner class Edit() {

        fun setMethod(method: String) {
           this@HttpHeader.method = method;
        }
    }

}
