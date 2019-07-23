package top.fksoft.server.http.httpHeaderReader

import top.fksoft.server.http.config.HttpHeader
import top.fksoft.server.http.config.HttpKey
import top.fksoft.server.http.logcat.Logger

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URLDecoder

/**
 *
 *
 * # 默认的 HTTP 解析类
 *
 *
 * 注意：
 * 此类存在严重的安全问题，受数据方式的影响，
 * 如果攻击者制造一个畸形的HTTP 请求，
 * 可能会造成内存泄漏的问题,切勿用于
 * 生产环境
 *
 *
 * @author ExplodingDragon
 * @version 1.0
 */
@Deprecated(message = "存在严重的安全问题！",level = DeprecationLevel.WARNING)
class DefaultHttpHeaderReader : BaseHttpHeaderReader() {
    private val logger = Logger.getLogger(DefaultHttpHeaderReader::class)

    @Throws(Exception::class)
    override fun readHeaderInfo(edit: HttpHeader.Edit): Boolean {
        val headerReader = BufferedReader(InputStreamReader(inputStream))
        val httpType = headerReader.readLine().trim()
        // 读取HTTP第一行的数据
        val typeArray = httpType.split(" ")
        /*
        在标准 HTTP 协议下 ，请求头一般为：
            GET /index.html HTTP/1.1
            由此可利用切割来取出数据

         */
        if (typeArray.size != 3) {
            logger.warn("未知协议：$httpType")
            return false
        }
        var location = typeArray[1]
        location = URLDecoder.decode(location, HttpKey.CHARSET_UTF_8)
        //还原 URL 中的转义字符
        while (true){
            val line = headerReader.readLine().trim()
            if (line == "") {
                //达到HTTP HEADER 第一个末尾
                break;
            }
            val spitIndex = line.indexOf(":")
            if (spitIndex == -1) {
                throw IndexOutOfBoundsException("非法header 头！")
            }
            edit.addHeader(line.substring(0,spitIndex),line.substring(spitIndex + 1))
            //添加完成所有 Header 信息
        }


        val i = location.indexOf('?')
        //开始判断请求类型
        if (httpType.startsWith(HttpKey.METHOD_GET)) {
            edit.setMethod(HttpKey.METHOD_GET)
            //GET
            if (i != -1) {
                var methodGetData = location.substring(i + 1).trim()
                //得到 在GET 请求下附加的数据
                edit.addForms(methodGetData)
            }
        } else if (httpType.startsWith(HttpKey.METHOD_POST)) {
            edit.setMethod(HttpKey.METHOD_POST)
            if (edit.getReader().getHeader(HttpKey.HEADER_KEY_CONTENT_TYPE,HttpKey.UNKNOWN_VALUE) == HttpKey.UNKNOWN_VALUE  ){
                //请求为 POST 但是不存在 Content-Type ，判定为畸形 http 请求
                logger.warn("请求为 POST 但是不存在 Content-Type.")
             return false
            }
        } else {
            //暂无法解析 除 GET 和 POST 以外的其他方法
            logger.warn("未知请求协议：$httpType")
            return false
        }
        edit.setPath(if (i != -1) location.substring(0, i) else location)
        //指定请求路径
        return true
    }

    override fun readHeaderPostData(httpHeader: HttpHeader.Edit) {
        
    }

    @Throws(Exception::class)
    override fun close() {

    }

}
