package de.codeapp.hosticp.webhandler;

import de.lheinrich.lhdf.tools.FileTools;
import de.lheinrich.lhdf.webserver.WebserverHandler;

import java.util.Map;

public class Dashboard extends WebserverHandler {

    @Override
    public String[] process(String name, Map<String, String> get, Map<String, String> head, Map<String, String> post, Map<String, String> cookies, String clientIp) {
        String[] file = new String[]{HTML.getContentType(name), FileTools.loadResourceFile("web/dashboard.html", false)};
        return file;
    }

    private String[] toReturn(String content) {
        return new String[]{"text/html", HTML.format(content)};
    }
}
