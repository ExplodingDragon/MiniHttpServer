package top.fksoft.server.http.http2utils;

import top.fksoft.server.http.client.ClientRunnable;
import top.fksoft.server.http.config.ServerConfig;

/**
 * <p>解析 http header 的抽象方法
 * </p>
 *
 *  用于解析http header的抽象方法，用于 http 头的所有内容，
 *  包括 HTTP 协议、请求的类型、请求头的所有信息，以及包括POST请求的
 *  表单信息，如果是文件上传则保存到指定的临时目录
 *
 * @see DefaultHttpHeaderFactory 默认实现
 *
 * @author ExplodingDragon
 * @version 1.0
 */
public abstract class BaseHttpHeaderFactory {
    private ClientRunnable runnable;
    private ServerConfig config;

    /**
     * <p>HTTP Header 预读取的初始化构造方法
     * </p>
     *
     * 此方法将在初始化类后由子线程
     *
     * @param config
     * @param runnable
     */
    final public void init(ServerConfig config, ClientRunnable runnable){
        this.config = config;
        this.runnable = runnable;
    }
}
