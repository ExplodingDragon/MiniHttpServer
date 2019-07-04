package top.fksoft.server.http.utils;

import java.io.Closeable;

/**
 * @author ExplodingDragon
 * @version 1.0
 */
public class CloseUtils {
    public static void close(Closeable ... close){
        for (Closeable closeable : close) {
            try {
                closeable.close();
            }catch (Exception ignored){
            }
        }

    }
}
