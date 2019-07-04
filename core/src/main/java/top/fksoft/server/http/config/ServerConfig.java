package top.fksoft.server.http.config;

/**
 * @author ExplodingDragon
 * @version 1.0
 */
public class ServerConfig {

    /**
     * <p>指定 Socket 超時時間，單位 （ms）
     * </p>
     */
    private int socketTimeout = 3000;

    public int getSocketTimeout() {
        return socketTimeout;
    }
}
