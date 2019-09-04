package top.fksoft.execute.servlet;

import org.jetbrains.annotations.NotNull;
import top.fksoft.server.http.server.serverIO.HttpHeaderInfo;
import top.fksoft.server.http.server.serverIO.responseData.impl.text.HtmlResponseData;
import top.fksoft.server.http.servlet.BaseHttpServlet;

import java.io.IOException;

/**
 * @author ExplodingDragon
 * @version 1.0
 */
public class InfoServlet extends BaseHttpServlet {
    public InfoServlet(@NotNull HttpHeaderInfo headerInfo) {
        super(headerInfo);
        setHasPost(true);
    }


    @Override
    public void doGet(@NotNull HttpHeaderInfo headerInfo) throws Exception {
        HtmlResponseData responseData = new HtmlResponseData();
        responseData.println("Shit!");
        setResponseData(responseData);

    }


    @Override
    public void close() throws IOException {

    }
}
