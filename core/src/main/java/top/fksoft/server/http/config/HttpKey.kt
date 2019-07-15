package top.fksoft.server.http.config

import top.fksoft.server.http.config.HttpKey.Companion.getValue

/**
 *
 * 一些属性的预设封装
 *
 *
 * @author ExplodingDragon
 * @version 1.0
 */
interface HttpKey {


    enum class PROPERTIES_KEY {
        /**
         * 服务器端口号
         */
        SERVER_PORT

    }

    companion object {

        /**
         * Http 下的GET 请求表示方法
         */
        val METHOD_GET = "GET"

        /**
         * Http 下 POST 请求的表示方法
         */
        val METHOD_POST = "POST"


        val PROPERTY_SYSTEM_TEMP_DIR = "java.io.tmpdir"

        val PKG_NAME = "MiniHttpServer"


        val VERSION_KEY = "HTTP/"

        val CHARSET_GBK = "GBK"
        val CHARSET_UTF_8 = "UTF-8"


        val HEADER_USER_AGENT = "User-Agent"
        val HEADER_HOST = "Host"
        val HEADER_ACCEPT_LANGUAGE = "Accept-Language"
        val HEADER_ACCEPT = "Accept"
        val HEADER_CONTENT_LENGTH = "Content-Length"
        val HEADER_CONTENT_TYPE = "Content-Type"
        val HEADER_CHARSET_KEY = "charset="
        val HEADER_BOUNDARY_KEY = "boundary="

        val HEADER_RANGE_KEY2 = "Range:"

        val TYPE_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded"
        val TYPE_FORM_DATA = "multipart/form-data"

        val HTTP_MAX_HEADER_LEN = 5200

        @JvmOverloads
        fun getValue(src: String, key: String, spit: String = ";"): String? {
            for (line in src.split(spit.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
                if (line.contains(key)) {
                    return line.substring(line.indexOf(key) + key.length)
                }
            }
            return null
        }

        @Throws(IndexOutOfBoundsException::class)
        fun getValueException(src: String, key: String): String {
            return getValue(src, key) ?: throw IndexOutOfBoundsException()
        }
    }
}
