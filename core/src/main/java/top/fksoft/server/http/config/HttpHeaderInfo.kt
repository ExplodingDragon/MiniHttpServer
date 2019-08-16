package top.fksoft.server.http.config

import jdkUtils.data.StringUtils
import top.fksoft.server.http.config.bean.NetworkInfo
import top.fksoft.server.http.logcat.Logger
import top.fksoft.server.http.utils.LongByteArray
import top.fksoft.server.http.utils.CloseUtils
import java.io.File
import java.nio.charset.Charset
import kotlin.random.Random

/**
 *
 * # HTTP 头信息
 *
 *
 * 用于保存HTTP Header 的所有信息
 *
 *
 * @author ExplodingDragon
 * @version 1.0
 */
class HttpHeaderInfo(val remoteInfo: NetworkInfo, val serverConfig: ServerConfig) : CloseUtils.Closeable {
    var charset: Charset = Charsets.UTF_8
    private val logger = Logger.getLogger(this)
    private val edit = Edit()

    /**
     * 服务器下的请求类型
     */
    var method = HttpConstant.METHOD_GET
        private set
    private var formArray = HashMap<String, String>()
    private var headerArray = HashMap<String, String>()

    /**
     * 请求的服务器文件路径（去除GET后缀）
     */
    var path: String = "/"
        private set

    /**
     *  HTTP 协议的版本，默认 1.0
     */
    var httpVersion: Float = 1.0f
        private set

    /**
     * 当前实例的唯一 Token
     */
    val headerSession = StringUtils.sha1Encryption("$remoteInfo${System.currentTimeMillis()}${Random.nextDouble()}")!!

    /**
     * # 得到表单数据
     *  得到GET 或 POST 下
     * @param key String 键值对
     * @param defaultValue String
     * @return String
     */
    fun getForm(key: String, defaultValue: String = ""): String {
        if (formArray.containsKey(key)) {
            return formArray[key]!!
        } else {
            return defaultValue
        }
    }


    var rawPostArray = LongByteArray(0)
        private set


    /**
     * # 得到POST 指定的文件信息
     *
     * @param key String 键值对
     * @return PostFileItem? 数据
     */
    fun getFormFile(key: String): PostFileItem? {
        var value = formArray["$POST_HEADER_STR$key"]
        return if (value != null) PostFileItem.formString(value) else null
    }

    /**
     * # 判断是否为 POST 请求
     *
     * @return Boolean 判断是否为POST 请求
     */
    fun isPost() = method == HttpConstant.METHOD_POST

    /**
     * # 得到 header 键值对
     *
     * 得到 Http Header 下键值对，如果键值对不存在则返回``` ```；
     * 可自定义默认返回的字符串
     *
     * @param key String 键值对
     * @param defaultValue String 如果不存在返回的数据
     * @return String 返回的数据
     */
    fun getHeader(key: String, defaultValue: String = ""): String {
        var header = headerArray[key]
        return if (header == null) defaultValue else header
    }

    @Throws(Exception::class)
    override fun close() {
        for (key in formArray.keys) {
            if (key.indexOf(POST_HEADER_STR) != -1) {
                PostFileItem.formString(formArray[key]!!).close()
            }
        }
        rawPostArray.close()
        formArray.clear()
        headerArray.clear()
    }


    fun edit(): Edit {
        return edit
    }


    inner class Edit() {

        /**
         * # 指定请求的类型
         * @param method String
         */
        fun setMethod(method: String) {
            this@HttpHeaderInfo.method = method;
        }

        /**
         *  # 添加表单
         *
         *
         * 将 GET 的表单和POST下以``` application/x-www-formArray-urlencoded```
         * 表单添加到MAP 中。
         *
         * 格式如下：
         * ``` bash
         * title=test&sub%5B%5D=1&sub%5B%5D=2&sub%5B%5D=3
         * ```
         * 但是需要将转义字符回转
         *
         * @param formArray String 原始表单数据
         * @param delimiter String 分隔符
         */
        fun addForms(formArray: String, delimiter: String = "&") {
            for (formStr in formArray.split(delimiter.toRegex()).filter { it != "" }) {
                var index = formStr.indexOf('=')
                if (index != -1) {
                    addForm(formStr.substring(0, index), formStr.substring(index + 1))
                } else {
                    logger.debug("无法格式化此字段：$formStr .")
                }

            }
        }

        /**
         * 添加一个表单到 map 中
         * @param key String 键值对
         * @param value String 数值
         */
        fun addForm(key: String, value: String) {
            if (key.trim().isEmpty())
                return
            formArray[key] = value.trim()
        }

        /**
         * # 添加一个 POST 上传文件的表单
         * @param key String POST 键值对
         * @param item PostFileItem 保存的临时文件位置
         */
        fun addFormFile(key: String, item: PostFileItem) {
            formArray["$POST_HEADER_STR$key"] = item.toString()
        }

        /**
         * # 指定请求的路径
         * @param path String
         */
        fun setPath(path: String) {
            this@HttpHeaderInfo.path = path

        }

        /**
         * # 得到只读header对象
         * @return HttpHeaderInfo 绑定的 Header 对象
         */
        fun getReader() = this@HttpHeaderInfo

        /**
         * # 添加一个Header Header 属性
         *
         * @param key String 键值对
         * @param value String 数值
         */
        fun addHeader(key: String, value: String) {
            headerArray[key] = value.trim()
        }

        /**
         * # 仅用于打印 DEBUG 信息 调试  ...
         */
        fun printDebug() {
            for (key in headerArray.keys) {
                logger.debug("header's Key=$key,value=${headerArray[key]};")
            }
            for (key in formArray.keys) {
                logger.debug("form's Key=$key,value=${formArray[key]};")
            }
        }

        fun setHttpVersion(httpVersion: Float) {
            this@HttpHeaderInfo.httpVersion = httpVersion
        }

        /**
         * 指定原始post 数据
         * @param input File
         */
        fun setRawPostByteArray(array: LongByteArray) {
            rawPostArray = array
        }


    }


    /**
     * # POST 上传文件的实体类
     *
     * @property key String 文件键值对
     * @property path File 保存的位置
     * @property contentType String 文件类型
     * @constructor
     */
    data class PostFileItem(val key: String,private val path: File, val contentType: String) : CloseUtils.Closeable {

        override fun close() {
            path.delete()
        }



        override fun toString(): String = StringBuilder()
                .append("<key>").append(key).append("</key>").append('\n')
                .append("<path>").append(path).append("</path>").append('\n')
                .append("<contentType>").append(contentType).append("</contentType>").toString()

        companion object {
            fun formString(str: String): PostFileItem {
                val key = StringUtils.subString(str, "<key>", "</key>")
                val path = StringUtils.subString(str, "<path>", "</path>")
                val contentType = StringUtils.subString(str, "<contentType>", "</contentType>")
                if (key == null || path == null || contentType == null) {
                    throw ClassFormatError()
                }
                return PostFileItem(key, File(path), contentType)
            }
        }

    }

    companion object {
        private const val POST_HEADER_STR = "@POST_FILE_"
    }
}
