package de.codeapp.hosticp.webhandler;

import de.codeapp.hosticp.main.HostiCP;
import de.codeapp.hosticp.user.UserTools;
import de.lheinrich.lhdf.webserver.WebserverHandler;

import java.util.Map;

public class Index extends WebserverHandler {

    @Override
    public String[] process(String name, Map<String, String> get, Map<String, String> head, Map<String, String> post_put, Map<String, String> cookies, String clientIp) {
        if (UserTools.checkLoggedIn(getCookies().get("hosticp_session"), getCookies().get("hosticp_id"))) {
            return toReturn(HTML.redirect("/dashboard.html"));
        } else if (post_put.containsKey("mail") && post_put.containsKey("password")) {
            var login = UserTools.login(post_put.get("mail"), post_put.get("password"));
            if (login != null) {
                setCookie("hosticp_session", login[0]);
                setCookie("hosticp_id", login[1]);
                return toReturn(HTML.redirect("/dashboard.html"));
            } else {
                return toReturn(loginBox(HostiCP.getLanguage("user.login.wrong")));
            }
        } else {
            return toReturn(loginBox(HostiCP.getLanguage("login")));
        }
    }

    private String loginBox(String title) {
        return "<form method='post'><div class='login'><h3>" + title + "</h3><input placeholder='" + HostiCP.getLanguage("mail") + "' name='mail' required><input placeholder='" + HostiCP.getLanguage("password") + "' name='password' type='password' required><br><br><button type='submit'>" + HostiCP.getLanguage("login") + "</button></form>";
    }

    private String[] toReturn(String content) {
        return new String[]{"text/html", HTML.format(content)};
    }
}