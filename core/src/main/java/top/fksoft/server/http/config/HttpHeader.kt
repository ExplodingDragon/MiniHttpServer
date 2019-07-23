package top.fksoft.server.http.config

import top.fksoft.server.http.logcat.Logger
import top.fksoft.server.http.utils.CloseUtils
import java.io.File

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
class HttpHeader(private val remoteInfo: NetworkInfo) : CloseUtils.Closeable {
    private val logger = Logger.getLogger(this)

    private val edit = Edit()
    var method = HttpKey.METHOD_GET
    private set
    private var formArray = HashMap<String, String>()
    private var headerArray = HashMap<String, String>()


    var path:String = "/"
    private set

    /**
     * # 得到表单数据
     *  得到GET 或 POST 下
     * @param key String 键值对
     * @param defaultValue String
     * @return String
     */
    fun getForm(key:String, defaultValue: String = ""):String{
        if (formArray.containsKey(key)){
            return formArray[key]!!
        }else{
            return defaultValue
        }
    }



    @Throws(Exception::class)
    override fun close() {

    }



    fun edit(): Edit {
        return edit
    }

    /**
     * # 判断是否为 POST 请求
     *
     * @return Boolean 判断是否为POST 请求
     */
    fun isPost() = method == HttpKey.METHOD_POST

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
    fun getHeader(key: String, defaultValue: String= ""): String {
        var header = getHeader(key)
        return  if (header == null) defaultValue else header
    }




    inner class Edit() {

        fun setMethod(method: String) {
            this@HttpHeader.method = method;
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
                    addForm(formStr.substring(0, index),formStr.substring(index + 1))
                }else{
                    logger.debug("无法格式化此字段：$formStr .")
                }

            }
        }

        /**
         * 添加一个表单到 map 中
         * @param key String 键值对
         * @param value String 数值
         */
        fun addForm(key:String,value:String){
            formArray[key] = value
        }
        fun addFormFile(key:String,path:File){
            formArray["@POST_FILE_$key"] = path.absolutePath
        }
        /**
         * # 指定请求的路径
         * @param path String
         */
        fun setPath(path: String) {
            this@HttpHeader.path = path

        }

        /**
         * # 得到只读header对象
         * @return HttpHeader 绑定的 Header 对象
         */
        fun getReader() = this@HttpHeader

        /**
         * # 添加一个Header Header 属性
         *
         * @param key String 键值对
         * @param value String 数值
         */
        fun addHeader(key: String, value: String) {
            headerArray[key] = value
        }

    }

    data class PostItem(val key:String,val path:File)

}
