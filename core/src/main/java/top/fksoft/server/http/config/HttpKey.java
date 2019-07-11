package top.fksoft.server.http.config;

public interface HttpKey {
    String VERSION_KEY = "HTTP/";

    String CHARSET_GBK = "GBK";
    String CHARSET_UTF_8 = "UTF-8";

    String GET = "GET";
    String POST = "POST";


    String HEADER_USER_AGENT = "User-Agent";
    String HEADER_HOST = "Host";
    String HEADER_ACCEPT_LANGUAGE = "Accept-Language";
    String HEADER_ACCEPT = "Accept";
    String HEADER_CONTENT_LENGTH = "Content-Length";
    String HEADER_CONTENT_TYPE = "Content-Type";
    String HEADER_CHARSET_KEY = "charset=";
    String HEADER_BOUNDARY_KEY = "boundary=";

    String HEADER_RANGE_KEY2 = "Range:";

    String TYPE_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
    String TYPE_FORM_DATA = "multipart/form-data";

    int HTTP_MAX_HEADER_LEN = 5200;

    static String getValue(String src, String key, String spit) {
        for (String line : src.split(spit)) {
            if (line.contains(key)) {
                return line.substring(line.indexOf(key) + key.length());
            }
        }
        return null;
    }


    static String getValue(String src, String key) {
        return getValue(src, key, ";");
    }

    static String getValueException(String src, String key) throws IndexOutOfBoundsException {
        String value = getValue(src, key);
        if (value == null) {
            throw new IndexOutOfBoundsException();
        }
        return value;
    }
}
