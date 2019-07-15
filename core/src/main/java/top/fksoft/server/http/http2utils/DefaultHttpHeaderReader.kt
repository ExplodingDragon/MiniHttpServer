package top.fksoft.server.http.http2utils

import top.fksoft.server.http.config.HttpHeader
import top.fksoft.server.http.config.HttpKey
import top.fksoft.server.http.logcat.Logger

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URLDecoder

/**
 *
 *
 * 默认的 HTTP 解析类
 *
 *
 * 注意：
 * 此类存在严重的安全问题，受取值的影响，
 * 如果攻击者制造一个畸形的HTTP 请求，
 * 可能会造成内存泄漏的问题,切勿用于
 * 生产环境
 *
 *
 * @author ExplodingDragon
 * @version 1.0
 */
class DefaultHttpHeaderReader : BaseHttpHeaderReader() {

    @Throws(Exception::class)
    override fun readHeaderData(httpHeader: HttpHeader.Edit): Boolean {
        val headerReader = BufferedReader(InputStreamReader(inputStream))
        val httpType = headerReader.readLine().trim { it <= ' ' }
        // 读取HTTP第一行的数据
        val array = httpType.split(" ".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        /*
        在标准 HTTP 协议下 ，请求头一般为：
            GET /index.html HTTP/1.1
            由此可利用切割来取出数据

         */
        if (array.size != 3) {
            logger.warn("未知协议：$httpType")
            return false
        }
        var location = array[1]
        val i = location.indexOf('?')
        if (i != -1) {

        }
        location = URLDecoder.decode(location, HttpKey.CHARSET_UTF_8)
        //还原 URL 中的转义字符

        if (httpType.startsWith(HttpKey.METHOD_GET)) {
            httpHeader.setMethod(HttpKey.METHOD_GET)
            //GET

        } else if (httpType.startsWith(HttpKey.METHOD_POST)) {
            httpHeader.setMethod(HttpKey.METHOD_POST)
        } else {
            //暂无法解析 除 GET 和 POST 以外的其他方法
            logger.warn("未知请求协议：$httpType")
            return false
        }

        return false
    }

    @Throws(Exception::class)
    override fun close() {

    }

    companion object {
        private val logger = Logger.getLogger(DefaultHttpHeaderReader::class)
    }
}
