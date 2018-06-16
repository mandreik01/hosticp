package de.codeapp.hosticp.user;

import de.codeapp.hosticp.main.HostiCP;
import de.lheinrich.lhdf.security.Crypter;
import de.lheinrich.lhdf.sql.ClientSQL;

import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class UserTools {

    private static Map<String, String> sessions = new TreeMap<>();

    public static boolean checkLoggedIn(String session, String id) {
        return session != null && id != null && sessions.containsKey(session) && sessions.get(session).equals(id);
    }

    public static String getId(String session) {
        return sessions.get(String.valueOf(session));
    }

    public static String createSession(String id) {
        var session = generateSessionString();
        while (sessions.containsKey(session))
            session = generateSessionString();

        sessions.put(session, id);
        return session;
    }

    private static String generateSessionString() {
        return UUID.randomUUID().toString() + UUID.randomUUID().toString();
    }

    public static String[] login(String mail, String password) {
        var hash = Crypter.hash("SHA3-512", "HostiCP_" + Crypter.hash("SHA3-512", password));
        var result = HostiCP.getInstance().doQuerySQL(ClientSQL.getQuery("user_login"), mail, hash);

        try {
            if (result.next()) {
                var id = String.valueOf(result.getInt("id"));
                return new String[]{createSession(id), id};
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}