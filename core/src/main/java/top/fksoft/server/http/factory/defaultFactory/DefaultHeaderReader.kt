package top.fksoft.server.http.factory.defaultFactory

import top.fksoft.server.http.config.HttpConstant
import top.fksoft.server.http.config.HttpHeaderInfo
import top.fksoft.server.http.config.ResponseCode
import top.fksoft.server.http.factory.HeaderReaderFactory
import top.fksoft.server.http.logcat.Logger
import top.fksoft.server.http.utils.LongByteArray
import top.fksoft.server.http.utils.DataReaderUtils
import java.io.File
import java.net.URLDecoder
import java.nio.charset.Charset

/**
 *
 *
 * # 默认的 HTTP 解析类
 *
 *
 * 注意：
 * 此类存在严重的安全问题，受取数据方式的影响，
 * 如果攻击者制造一个畸形的HTTP 请求，
 * 可能会造成内存泄漏的问题,切勿用于
 * 生产环境
 *
 *
 * @author ExplodingDragon
 * @version 1.0
 */
@Deprecated(message = "存在严重的安全问题！", level = DeprecationLevel.WARNING)
class DefaultHeaderReader : HeaderReaderFactory() {
    private val logger = Logger.getLogger(DefaultHeaderReader::class)

    @Throws(Exception::class)
    override fun readHeaderInfo(edit: HttpHeaderInfo.Edit): ResponseCode {
        val headerReader = DataReaderUtils(inputStream,Charsets.UTF_8)
//        val headerReader = BufferedReader(InputStreamReader(inputStream))
        val httpType = headerReader.readLine()!!.trim()
        // 读取HTTP第一行的数据
        val typeArray = httpType.split(" ")
        /*
        在标准 HTTP 协议下 ，请求头一般为：
            GET /index.html HTTP/1.1
            由此可利用切割来取出数据

         */
        if (typeArray.size != 3) {
            logger.warn("未知协议：$httpType")
            return ResponseCode.HTTP_BAD_REQUEST
            //第一行解析错误，无法得出协议，直接返回 400 错误
        }
        var location = typeArray[1]
        location = URLDecoder.decode(location, HttpConstant.CHARSET_UTF_8)
        //还原 URL 中的转义字符
        while (true) {
            val line = headerReader.readLine()!!.trim()
            if (line == "") {
                //达到HTTP HEADER 第一个末尾
                break
            }
            val spitIndex = line.indexOf(":")
            if (spitIndex == -1) {
                throw IndexOutOfBoundsException("非法header 头！")
            }
            edit.addHeader(line.substring(0, spitIndex), line.substring(spitIndex + 1))
            //添加完成所有 Header 信息
        }


        val i = location.indexOf('?')
        //开始判断请求类型
        val infoReader = edit.getReader()
        if (httpType.startsWith(HttpConstant.METHOD_GET)) {
            edit.setMethod(HttpConstant.METHOD_GET)
            //GET
            if (i != -1) {
                val methodGetData = location.substring(i + 1).trim()
                //得到 在GET 请求下附加的数据
                edit.addForms(methodGetData)
            }
        } else if (httpType.startsWith(HttpConstant.METHOD_POST)) {
            edit.setMethod(HttpConstant.METHOD_POST)
            if (infoReader.getHeader(HttpConstant.HEADER_KEY_CONTENT_TYPE, HttpConstant.UNKNOWN_VALUE) == HttpConstant.UNKNOWN_VALUE) {
                //请求为 POST 但是不存在 Content-Type ，判定为畸形 http 请求
                logger.warn("请求为 POST 但是不存在 Content-Type.")
                return  ResponseCode.HTTP_NOT_ACCEPTABLE
                //返回 406 错误

            }
            if (infoReader.getHeader(HttpConstant.HEADER_KEY_CONTENT_LENGTH, HttpConstant.UNKNOWN_VALUE) == HttpConstant.UNKNOWN_VALUE) {
                logger.warn("请求为 POST 但是不存在 Content-Length.")
                //请求为 POST 但是不存在 Content-Length ，判定为畸形 http 请求
                return ResponseCode.HTTP_LENGTH_REQUIRED
                //返回 411 错误
            }
        } else {
            //暂无法解析 除 GET 和 POST 以外的其他方法
            logger.warn("未知请求协议：$httpType")
            return ResponseCode.HTTP_BAD_METHOD
            //协议错误，返回 405 错误
        }
        edit.setPath(if (i != -1) location.substring(0, i) else location)
        //指定请求路径
        try {
            val httpVersion = typeArray[2].substringAfter("HTTP/").toFloat()
            edit.setHttpVersion(httpVersion)
        } catch (ignore: Exception) {
            logger.debug("在${infoReader.remoteInfo} 发现畸形HTTP 请求.", ignore)
            return ResponseCode.HTTP_UPGRADE_REQUIRED
        }
        if (infoReader.httpVersion > 1.1f) {
            logger.debug("无法处理HTTP版本大于1.1的 HTTP 连接：${infoReader.httpVersion}. -- ${infoReader.remoteInfo}")
            return ResponseCode.HTTP_UPGRADE_REQUIRED
        }

        return ResponseCode.HTTP_OK
    }


    override fun readHeaderPostData(edit: HttpHeaderInfo.Edit): ResponseCode {

        val headerInfo = edit.run { getReader() }
        val contentType = headerInfo.getHeader(HttpConstant.HEADER_KEY_CONTENT_TYPE)
        val contentLength = headerInfo.getHeader(HttpConstant.HEADER_KEY_CONTENT_LENGTH).toInt()
        val charset = HttpConstant.getValue(contentType,"charset=",defaultResult = "UTF-8")
        val readerUtils = DataReaderUtils(inputStream, Charset.forName(charset))


        val output = File(headerInfo.serverConfig.tempDirectory, "${headerInfo.headerSession}_RAW_POST")
        val array = LongByteArray(contentLength.toLong(), output)
        val stream = array.openOutputStream()
        if (!readerUtils.copy(stream,contentLength)) {
            return ResponseCode.HTTP_BAD_REQUEST
        }
        edit.setRawPostByteArray(array)
        if (HttpConstant.HEADER_CONTENT_TYPE_URLENCODED in contentType){
            //此为普通的POST 请求方式，可以用类似于GET 数据解析的方式来进行解析
            val line = String(array.toByteArray())
            logger.debug("raw Post data:$line")
            edit.addForms(URLDecoder.decode(line,charset))
        }else if (HttpConstant.HEADER_CONTENT_TYPE_FORM_DATA in contentType){
            //另一种标准POST请求的方式
            val boundary = HttpConstant.getValue(contentType, "boundary=", HttpConstant.UNKNOWN_VALUE)
            if (boundary == HttpConstant.UNKNOWN_VALUE){
                //不存在POST 分割字符串，所以此POST 请求异常
                return ResponseCode.HTTP_BAD_REQUEST
            }
            val pat = "--$boundary\r\n".toByteArray()
            val arraySearch = array.openSearch()
            val spitArray = arraySearch.spitAll(pat)


        }
        return ResponseCode.HTTP_OK

    }

    @Throws(Exception::class)
    override fun close() {
    }

}
