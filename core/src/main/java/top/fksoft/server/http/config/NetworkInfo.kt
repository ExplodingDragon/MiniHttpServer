package top.fksoft.server.http.config

class NetworkInfo {
    private var hostName: String? = null
    var address: String? = null
    var port = -1

    constructor(address: String, port: Int) {
        update(address, port)
    }

    constructor() {}

    fun update(address: String, port: Int) {
        this.address = address
        this.port = port
    }

    fun update(info: NetworkInfo) {
        this.address = info.address
        this.port = info.port
        this.hostName = info.hostName
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as NetworkInfo?
        return if (port != that!!.port) false else address == that.address
    }

    override fun hashCode(): Int {
        var result = address!!.hashCode()
        result = 31 * result + port
        return result
    }

    fun setHostName(hostName: String) {
        this.hostName = hostName
    }

    fun getHostName(): String? {
        return if (hostName == null) address else hostName
    }

    override fun toString(): String {
        return getHostName() + ":" + port
    }
}
