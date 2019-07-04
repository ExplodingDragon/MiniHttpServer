package top.fksoft.server.http.config;

public class NetworkInfo {
    private String hostName = null;
    private String address = null;
    private int port = -1;

    public NetworkInfo(String address, int port) {
        update(address,port);
    }

    public NetworkInfo() {
    }

    public void update(String address, int port) {
        this.address = address;
        this.port = port;
    }
    public void update(NetworkInfo info) {
        this.address = info.address;
        this.port = info.port;
        this.hostName = info.hostName;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NetworkInfo that = (NetworkInfo) o;
        if (port != that.port) return false;
        return address.equals(that.address);
    }

    @Override
    public int hashCode() {
        int result = address.hashCode();
        result = 31 * result + port;
        return result;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getHostName() {
        return hostName == null?address:hostName;
    }

    @Override
    public String toString() {
        return getHostName() + ":" + port;
    }
}
