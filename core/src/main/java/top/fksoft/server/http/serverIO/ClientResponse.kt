package top.fksoft.server.http.serverIO

import jdkUtils.data.StringUtils
import top.fksoft.server.http.config.HttpConstant
import top.fksoft.server.http.config.ResponseCode
import top.fksoft.server.http.serverIO.base.BaseResponse
import top.fksoft.server.http.utils.ContentTypeUtils
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.ArrayList

/**
 * # 响应客户端的信息
 *
 * @author ExplodingDragon
 * @version 1.0
 */


class ClientResponse(private val headerInfo: HttpHeaderInfo, private val outputStream: OutputStream): BaseResponse(headerInfo, outputStream) {
    var responseContentType: String = HttpConstant.HEADER_VALUE_TEXT_HTML
    var responseInputStream: InputStream? = null
    private val responseMap = ConcurrentHashMap<String, String>()
    private var isPrintHeader = false

    val printWriter by lazy {
        PrintWriter(OutputStreamWriter(outputStream, headerInfo.charset), true)
    }


    init {
        addResponseHeader("Server", HttpConstant.LIB_NAME)
    }


    /**
     * # 输出字符到客户端（实时）
     *
     *
     *
     * @param line String
     */
    fun println(line: String) = run {
        printHeader()
        printWriter.println(line)
    }

    private fun printHeader() {
        if (!isPrintHeader) {
            var writer = printWriter
            writer.println("HTTP/1.1 ${responseCode.responseCode}  ${responseCode.codeMessage}")
            addResponseHeader(HttpConstant.HEADER_KEY_CONTENT_TYPE, responseContentType)
            for (entry in responseMap.iterator()) {
                writer.println("${entry.key}: ${entry.value}")
            }
            writer.println()
            isPrintHeader = true
        }
    }


    @Throws(Exception::class)
    override fun close() {
    }

    fun writeFile(file: File) = if (file.isFile) {
        var application = ContentTypeUtils.file2Application(file)
        responseContentType = application
        addResponseHeader(HttpConstant.HEADER_KEY_CONTENT_LENGTH, file.length().toString())
        responseInputStream = FileInputStream(file)
        logger.debug("输出文件： ${file.absolutePath}.")
        setValidation(Date(file.lastModified()), StringUtils.md5Encryption("${file.lastModified()}-${file.length()}"))
    } else {
        responseCode = ResponseCode.HTTP_NOT_FOUND
        logger.debug("文件[$file]不存在！")
        responseContentType = HttpConstant.HEADER_VALUE_TEXT_HTML
    }


    override fun flashResponse(): Boolean {
        val available = responseInputStream?.let {
            if (responseInputStream is FileInputStream) {
                val declaredField = FileInputStream::class.java.getDeclaredField("path")
                declaredField.isAccessible = true
                File(declaredField.get(responseInputStream).toString()).length()
            } else {
                responseInputStream!!.available().toLong()
            }
        }.let {
            it ?: 0
        }
        val rangeList = ArrayList<String>()
        responseInputStream?.let {
            if (headerInfo.containsHeader("Range")) {
                val allRange = headerInfo.getHeader("Range")
                logger.debug("发现断点续传请求！$allRange")

                if (headerInfo.containsHeader("If-Range")) {
                    val ifRange = headerInfo.getHeader("If-Range")
                    val range1 = responseMap["Last-Modified"].let {
                        it ?: HttpConstant.UNKNOWN_VALUE
                    }
                    val range2 = responseMap["ETag"] ?: HttpConstant.UNKNOWN_VALUE
                    if (ifRange == range1 || ifRange == range2 && responseInputStream is FileInputStream) {
                        val rangeData = allRange.split("=")[1]
                        val ranges = rangeData.split(",")
                        for (range in ranges) {
                            val i = range.indexOf("-")
                            when (i) {
                                0 -> rangeList.add("${available - 1 - range.substring(1).toLong()}-${available - 1} ")
                                range.length - 1 -> {
                                    val toLong = range.substring(0, i).toLong()
                                    rangeList.add("$toLong-${available - 1}")
                                }
                                else -> rangeList.add(range)
                            }
                        }
                        if (logger.debug) {
                            logger.debug("发现断点续传请求！${rangeList.toTypedArray().contentToString()}")
                        }
                        responseCode = ResponseCode.HTTP_PARTIAL
                        var length: Long = 0
                        for (range in rangeList) {
                            val split = range.split('-')
                            val start = split[0].toLong()
                            val end = split[1].toLong()
                            length += end - start
                        }
                        addResponseHeader(HttpConstant.HEADER_KEY_CONTENT_LENGTH, "$length")

                        addResponseHeader("Content-Range", "bytes ${rangeData}/$available")
                    } else {
                        logger.debug("断点续传格式错误改为标准返回！")
                    }
                }

            }
        }
        if (!isPrintHeader) {
            printHeader()
            var output = outputStream
            responseInputStream?.let {


                if (rangeList.size != 0 && responseInputStream is FileInputStream) {
                    val declaredField = FileInputStream::class.java.getDeclaredField("path")
                    declaredField.isAccessible = true
                    val accessFile = RandomAccessFile(File(declaredField.get(responseInputStream).toString()), "r")
                    val b = ByteArray(1024)
                    for (range in rangeList) {
                        val split = range.split('-')
                        val start = split[0].toLong()
                        val end = split[1].toLong()
                        accessFile.seek(start)
                        var len = end - start
                        while (true) {
                            if (len >= b.size) {
                                len -= accessFile.read(b, 0, b.size)
                                output.write(b)
                            } else {
                                accessFile.read(b, 0, len.toInt())
                                output.write(b, 0, len.toInt())
                                break
                            }
                            output.flush()
                        }
                    }
                } else {
                    logger.debug("输出流大小:$available")
                    var b = ByteArray(4096)
                    var read: Int
                    while (true) {
                        read = it.read(b, 0, b.size)
                        if (-1 == read) {
                            break
                        }
                        output.write(b, 0, read)
                        output.flush()

                    }
                }
            }

        }
        return true
    }

    private val format = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH)

    /**
     * # 指定校验值
     *
     * @param lastModified String
     * @param eTag String
     */
    fun setValidation(lastModified: Date, eTag: String) {
        addResponseHeader("Last-Modified", "${format.format(lastModified)} GMT")
        addResponseHeader("ETag", eTag)
    }

    fun addResponseHeader(key2: String, value: String) {
        responseMap[key2.trim()] = value
    }

}