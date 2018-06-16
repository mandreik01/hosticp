package de.codeapp.hosticp.main;

import de.codeapp.hosticp.webhandler.Dashboard;
import de.codeapp.hosticp.webhandler.Index;
import de.codeapp.hosticp.webhandler.NotFound;
import de.lheinrich.lhdf.sql.ClientPostgreSQL;
import de.lheinrich.lhdf.sql.DriverSQL;
import de.lheinrich.lhdf.sql.TransactionSQL;
import de.lheinrich.lhdf.tools.Config;
import de.lheinrich.lhdf.tools.FileTools;
import de.lheinrich.lhdf.webserver.Webserver;

import java.io.File;

public class HostiCP extends ClientPostgreSQL {

    public static HostiCP instance;
    private Config config;
    private Webserver webserver;
    private Config language;

    private HostiCP(boolean setup) throws Exception {
        this.config = new Config(FileTools.loadConfigFile("config.db"), FileTools.loadResourceFile("config.db"));
        this.webserver = new Webserver(this.config.getInt("webserver.port"), this.config.getInt("webserver.portssl"), config.getInt("webserver.threads"), this.config.getInt("webserver.timeout"), this.config.getInt("webserver.maxconnections"), this.config.getInt("webserver.portssl") == 0 ? null : Webserver.generateKeyStore(new File(this.config.get("webserver.ssl.certificate")), new File(this.config.get("webserver.ssl.key"))));
        registerWebserverHandler();
        this.language = new Config(FileTools.loadConfigFile("language.db"), FileTools.loadResourceFile("language.db"));
        loginSQL(DriverSQL.POSTGRESQL, this.config.get("postgresql.host"), this.config.getInt("postgresql.port"), this.config.get("postgresql.database"), this.config.get("postgresql.username"), this.config.get("postgresql.password"));
        connectSQL();

        if (!setup) {
            var setupQueries = getQuery("setup").split(System.lineSeparator() + System.lineSeparator());
            var setupTransaction = new TransactionSQL(this);
            for (var setupQuery : setupQueries) {
                setupTransaction.add(setupQuery);
            }
            setupTransaction.commit();
        }
    }

    public void registerWebserverHandler() {
        this.webserver.registerHandler("not found", new NotFound());
        this.webserver.registerHandler("", new Index());
        this.webserver.registerHandler("dashboard.html", new Dashboard());
    }

    public static Config getConfig() {
        return instance.config;
    }

    public static Webserver getWebserver() {
        return instance.webserver;
    }

    public static String getLanguage(String key) {
        return instance.language.get(key);
    }

    public static void main(String[] args) throws Exception {
        instance = new HostiCP(args.length == 1 && args[0].equals("setup"));
    }

    public static HostiCP getInstance() {
        return instance;
    }
}
