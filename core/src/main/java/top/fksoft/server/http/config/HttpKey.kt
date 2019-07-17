package top.fksoft.server.http.config


/**
 *
 * 一些属性的预设封装
 *
 *
 * @author ExplodingDragon
 * @version 1.0
 */
object HttpKey {


    enum class PROPERTIES_KEY {
        /**
         * 服务器端口号
         */
        SERVER_PORT

    }


    /**
     * Http 下的GET 请求表示方法
     */
    const val METHOD_GET = "GET"

    /**
     * Http 下 POST 请求的表示方法
     */
    const val METHOD_POST = "POST"


    /**
     *  一个系统属性值，可得到系统临时目录位置
     */
    const val PROPERTY_SYSTEM_TEMP_DIR = "java.io.tmpdir"

    /**
     * 项目名称，随意啦
     */
    const val PKG_NAME = "MiniHttpServer"



    const val CHARSET_GBK = "GBK"
    const val CHARSET_UTF_8 = "UTF-8"


    const val HEADER_USER_AGENT = "User-Agent"
    const val HEADER_HOST = "Host"
    const val HEADER_ACCEPT_LANGUAGE = "Accept-Language"
    const val HEADER_ACCEPT = "Accept"
    const val HEADER_CONTENT_LENGTH = "Content-Length"
    const val HEADER_CONTENT_TYPE = "Content-Type"

    const val HEADER_CHARSET_KEY = "charset="
    const val HEADER_BOUNDARY_KEY = "boundary="

    const val HEADER_RANGE_KEY2 = "Range:"

    const val TYPE_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded"
    const val TYPE_FORM_DATA = "multipart/form-data"

    const val HTTP_MAX_HEADER_LEN = 5200


    fun getValue(src: String, key: String, spit: String = ";"): String? {
        for (line in src.split(spit.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            if (line.contains(key)) {
                return line.substring(line.indexOf(key) + key.length)
            }
        }
        return null
    }


}
