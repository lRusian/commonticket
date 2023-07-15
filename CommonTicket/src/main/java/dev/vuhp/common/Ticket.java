/*
 * Copyright (UniXSystem) 2023.
 *
 * Plugin: CommonTicket
 * Author: Vuhp (Discord: vuhp)
 * Team: UniXSystem and Vanity Proyect
 * Website: unixsystem.com.ar or vanityproyect.com
 * Date: "2023"
 */

package dev.vuhp.common;

import dev.vuhp.common.command.TicketCommand;
import dev.vuhp.common.listener.TicketListener;
import dev.vuhp.common.mysql.DatabaseManager;
import dev.vuhp.common.mysql.MySQLConnect;
import dev.vuhp.common.mysql.MySQLManager;
import dev.vuhp.common.util.FileConfig;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Getter
@Setter
public final class Ticket extends JavaPlugin {

    FileConfig ymlConfig;
    MySQLConnect mySQLInfo;
    DatabaseManager databaseManager;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        loadConfigs();
        if (connectToMySQL()) {
            getLogger().info("Common-Ticket [*] Successfully connected to MySQL Server.");
            databaseManager = new MySQLManager(this);
                if (databaseManager.createCratesTable(mySQLInfo.getConnection(), this.ymlConfig)) {

                } else {
                    getLogger().severe("Common-Ticket [*] Failed to create table in MySQL server! Will not work!");
                }

        } else {
            getLogger().severe("Common-Ticket [*] SQL Authentication Failed!");
            getLogger().severe("Common-Ticket [*] Will not work without either a) switching to YAML or b) providing proper MySQL credentials. Please make sure your settings are correct in config.yml");
        }


        loadListener();

        this.getCommand("ticket").setExecutor(new TicketCommand());




    }


    @Override
    public void onDisable() {
    }

    private void loadListener() {
        getServer().getPluginManager().registerEvents(new TicketListener(), this);
    }

    private void loadConfigs() {
        this.ymlConfig = new FileConfig(this, "config.yml");

    }

    public MySQLConnect getMySQLInfo() {
        return mySQLInfo;
    }


    public DatabaseManager getMySQLManager() {
        return databaseManager;
    }

    private boolean connectToMySQL() {
        String address = getConfig().getString("CONNECT.MYSQL.HOST");
        String port = getConfig().getString("CONNECT.MYSQL.PORT");
        String databaseName = getConfig().getString("CONNECT.MYSQL.DATABASE");
        String username = getConfig().getString("CONNECT.MYSQL.USER");
        String password = getConfig().getString("CONNECT.MYSQL.PASSWD");
        final String DB_URL = "jdbc:mysql://" + address + ":" + port + "/" + databaseName + "?characterEncoding=utf8";
        try {
            Connection connection = DriverManager.getConnection(DB_URL, username, password);
            mySQLInfo = new MySQLConnect(connection, username, password);
            return true;
        }catch(SQLException e) {
            getLogger().severe("Common-Ticket [*] It is expected for the plugin to fail epically if this is your first time loading the plugin." +
                    "Just make sure you add the correct MySQL information in the config.yml, then restart your server. " +
                    "If you still see errors like this one, then there is a problem.");
            return false;
        }
    }

    public static Ticket get() {
        return Ticket.getPlugin(Ticket.class);
    }

}