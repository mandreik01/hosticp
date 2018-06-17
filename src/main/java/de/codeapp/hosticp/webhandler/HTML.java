package de.codeapp.hosticp.webhandler;

import java.util.HashMap;
import java.util.Map;

public class HTML {

    // TODO title
    public static String head = "<!doctype html><head><title>HostiCP</title><meta charset='utf-8'><link href='/style.css' rel='stylesheet'><meta name='viewport' content='width=device-width, initial-scale=1.0'></head><body>";
    private static final Map<String, String> contentTypes;

    static {
        contentTypes = new HashMap<>();
        contentTypes.put("html", "text/html");
        contentTypes.put("css", "text/css");
        contentTypes.put("dashboard", "application/javascript");
    }

    public static String getContentType(String name) {
        String[] rawEnding = name.split("\\.");
        String ending = rawEnding[rawEnding.length - 1];

        if (contentTypes.containsKey(ending))
            return contentTypes.get(ending);
        else
            return "text/plain";
    }

    public static String format(String content) {
        return head + content + "</body></html>";
    }

    public static String redirect(String url) {
        return "<meta http-equiv=\"refresh\" content=\"0; url=" + url + "\" />";
    }

    public static final String navbar(String active) {
        String[] links = {"groups", "wrapper"};
        String[] right_links = {"logout"};
        StringBuilder builder = new StringBuilder();

        for (String link : links) {
            builder.append("<li class='navbar'><a href='" + link + (link.equalsIgnoreCase(active) ? ".html' class='active'>" : ".html'>") + /* TODO */ link + "</a></li>");
        }
        for (String right : right_links) {
            builder.append("<li class='navbar nav-right'><a href='" + right + (right.equalsIgnoreCase(active) ? ".html' class='active'>" : ".html'>") + /* TODO */ right + "</a></li>");
        }

        return "<ul class='navbar'><li><a href='dashboard.html'" + (active.equalsIgnoreCase("dashboard") ? " class='active'>LotusCloud</a></li>" : ">LotusCloud</a></li>") + builder.toString() + "</ul>";
    }

}