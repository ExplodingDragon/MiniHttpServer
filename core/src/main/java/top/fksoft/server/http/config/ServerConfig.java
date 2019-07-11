package top.fksoft.server.http.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ExplodingDragon
 * @version 1.0
 */
public class ServerConfig {
    private Map<String,Object> mapKey = new ConcurrentHashMap<>();
    /**
     * <p>指定 Socket 超時時間，單位 （ms）
     * </p>
     */
    private int socketTimeout = 3000;

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public <T> T getHttpProperties(String key) {
        if (!mapKey.containsKey(key)){
            throw new PropertiesNotFoundException(key + " not found.");
        }
        return (T) mapKey.get(key);
    }

    public static class PropertiesNotFoundException extends RuntimeException{
        public PropertiesNotFoundException(String message) {
            super(message);
        }
    }
}
