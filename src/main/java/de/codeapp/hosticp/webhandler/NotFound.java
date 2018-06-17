package de.codeapp.hosticp.webhandler;

import de.lheinrich.lhdf.tools.FileTools;
import de.lheinrich.lhdf.webserver.WebserverHandler;

import java.util.HashMap;
import java.util.Map;

public class NotFound extends WebserverHandler {

    private final Map<String, String[]> files = new HashMap<>();

    @Override
    public String[] process(String name, Map<String, String> get, Map<String, String> head, Map<String, String> post_put, Map<String, String> cookies, String clientIp) {
        if (files.containsKey(name))
            return files.get(name);
        else {
            String[] file = new String[]{HTML.getContentType(name), FileTools.loadResourceFile("web/" + name, false)};
            files.put(name, file);
            return file;
        }
    }
}
