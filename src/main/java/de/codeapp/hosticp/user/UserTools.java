package de.codeapp.hosticp.user;

import de.codeapp.hosticp.main.HostiCP;
import de.lheinrich.lhdf.security.Crypter;
import de.lheinrich.lhdf.sql.ClientSQL;

import java.sql.SQLException;

public class UserTools {

    public static boolean checkLoggedIn(String cookie) {
        return false;
    }

    public static boolean login(String mail, String password) {
        var hash = Crypter.hash("SHA3-512", "HostiCP_" + Crypter.hash("SHA3-512", password));
        var result = HostiCP.getInstance().doQuerySQL(ClientSQL.getQuery("user_login"), mail, hash);

        try {
            if (result.next())
                return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}