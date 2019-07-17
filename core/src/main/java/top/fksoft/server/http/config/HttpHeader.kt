package top.fksoft.server.http.config

import top.fksoft.server.http.logcat.Logger
import top.fksoft.server.http.utils.CloseUtils

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
    private var method = HttpKey.METHOD_GET
    private var form = HashMap<String, String>()





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

        /**
         *  # 添加表单
         *
         *
         * 将 GET 的表单和POST下以``` application/x-www-form-urlencoded```
         * 表单添加到MAP 中。
         *
         * 格式如下：
         * ```
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
            form[key] = value
        }
    }

}
