package top.fksoft.server.http.utils

object ClassUtils {

    fun getClassName(`fun`: Int): String {
        return getElement(`fun`).className
    }

    private fun getElement(`fun`: Int): StackTraceElement {
        val stacktrace = Thread.currentThread().stackTrace
        return stacktrace[2 + `fun`]
    }


    fun getMethodName(`fun`: Int): String {
        return getElement(`fun`).methodName
    }

    fun getFileName(`fun`: Int): String? {
        return getElement(`fun`).fileName
    }

    fun getLineNumber(`fun`: Int): Int {
        return getElement(`fun`).lineNumber
    }

}
