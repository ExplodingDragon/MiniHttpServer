import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * @author ExplodingDragon
 * @version 1.0
 */
public class Test {
    public static void main(String[] args) throws IOException {
        Field[] declaredFields = FileInputStream.class.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            System.err.println(declaredField.getName());
        }
    }
}
