package top.fksoft.execute.config;

import top.fksoft.server.http.logcat.Logger;

public class Config {
    private Logger logger = Logger.getLogger(Config.class);
    private static Config config = null;
    private boolean debug = false,
            printConfig = false;

    public static Config newInstance() {
        if (config == null) {
            Config.config = new Config();
        }
        return config;
    }

    public boolean isDebug() {
        return debug;
    }

    public void initConfig(String[] args) {
        for (String arg : args) {
            if (isValue(arg, "--enable-debug", "-d")) {
                debug = true;
            }
            if (isValue(arg, "--print-config", "-c")) {
                printConfig = true;
            }
        }
    }

    private boolean isValue(String arg, String... values) {
        if (values == null) {
            return false;
        }
        for (String value : values) {
            if (value.equals(arg)) {
                return true;
            }
        }
        return false;
    }

    public void printConfig() {
        if (debug){
            logger.debug("已开启DEBUG模式,可能会输出隐私信息，如非调试，请勿打开！");
        }
        if (printConfig){

        }
    }
}
