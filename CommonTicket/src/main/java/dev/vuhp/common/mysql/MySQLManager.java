/*
 * Copyright (UniXSystem) 2023.
 *
 * Plugin: CommonTicket
 * Author: Vuhp (Discord: vuhp)
 * Team: UniXSystem and Vanity Proyect
 * Website: unixsystem.com.ar or vanityproyect.com
 * Date: "2023"
 */

package dev.vuhp.common.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import dev.vuhp.common.Ticket;
import dev.vuhp.common.util.FileConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MySQLManager extends DatabaseManager {
	
	private Ticket plugin;

	public MySQLManager(Ticket pl) {
		super(pl);
	}

	@Override
	public boolean createCratesTable(Connection connection, FileConfig config) {
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `uxs_user` (" +
					"`id` int(11) NOT NULL AUTO_INCREMENT," +
					"`username` varchar(16) NOT NULL," +
					"`password` varchar(64) NOT NULL," +
					"`role` varchar(60) NOT NULL DEFAULT 'Member'," +
					"`uuid` text NOT NULL," +
					"`ign` text NOT NULL," +
					"`last_update` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP," +
					"`created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP," +
					"PRIMARY KEY ( id ));");
			preparedStatement.executeUpdate();
			preparedStatement.close();

			PreparedStatement preparedStatement15 = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `site_use_license` (" +
					"`id` int(11) NOT NULL AUTO_INCREMENT," +
					"`code` text NOT NULL," +
					"PRIMARY KEY ( id ));");
			preparedStatement15.executeUpdate();
			preparedStatement15.close();

			PreparedStatement preparedStatement20 = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `category` (" +
					"`id` int(11) NOT NULL AUTO_INCREMENT," +
					"`name` varchar(96) NOT NULL," +
					"`description` varchar(256) NOT NULL," +
					"`priority` varchar(18) NOT NULL DEFAULT 'low'," +
					"`datetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP," +
					"PRIMARY KEY ( id ));");
			preparedStatement20.executeUpdate();
			preparedStatement20.close();

			PreparedStatement preparedStatement25 = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `chat` (" +
					"`id` int(11) NOT NULL AUTO_INCREMENT," +
					"`id-cat` varchar(20) NOT NULL," +
					"`id-user` varchar(20) NOT NULL," +
					"`uuid` text NOT NULL," +
					"`priority` varchar(20) NOT NULL DEFAULT 'Normal'," +
					"`description` text NOT NULL," +
					"`status` varchar(16) NOT NULL DEFAULT 'oppened'," +
					"`expire` bigint(20) NOT NULL," +
					"`datetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP," +
					"PRIMARY KEY ( id ));");
			preparedStatement25.executeUpdate();
			preparedStatement25.close();

			PreparedStatement preparedStatement30 = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `chat-contains` (" +
					"`id` int(11) NOT NULL AUTO_INCREMENT," +
					"`id-category` varchar(20) NOT NULL," +
					"`id-chat` varchar(20) NOT NULL," +
					"`id-user` varchar(20) NOT NULL," +
					"`id-sender` varchar(20) NOT NULL DEFAULT 'yes'," +
					"`descr` text NOT NULL," +
					"`datetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP," +
					"PRIMARY KEY ( id ));");
			preparedStatement30.executeUpdate();
			preparedStatement30.close();

			PreparedStatement preparedStatement35 = connection.prepareStatement(
					"SELECT * FROM `site_use_license` WHERE id='1';");
			ResultSet result = preparedStatement35.executeQuery();

			boolean found = false;

			if(result != null) {
				while(result.next()) {
					found = true;
				}
			}

			if(!found) {
				String sitekey = config.getString("CONNECT.SITE_KEY");
				PreparedStatement preparedStatement40 = connection.prepareStatement(
						"INSERT INTO `site_use_license`(`id`, `code`) VALUES ('1', '" + sitekey + "')");
				preparedStatement40.executeUpdate();
				preparedStatement40.close();
			}
			preparedStatement35.close();


			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}


	public void addPlayerToDatabase(Connection connection, Player player) {
		new BukkitRunnable() {
			@Override
			public void run() {
				try {

					UUID uuid = player.getUniqueId();

					PreparedStatement preparedStatement = connection.prepareStatement(
							"SELECT * FROM `uxs_user` WHERE uuid='" + uuid + "';");
					ResultSet result = preparedStatement.executeQuery();

					boolean found = false;

					if(result != null) {
						while(result.next()) {
							found = true;
							for (String string : Ticket.get().getYmlConfig().getStringList("COMMAND.TICKET.MESSAGE.REGISTERED")) {
								player.sendMessage(string);
							}
						}
					} else {
						for (String string : Ticket.get().getYmlConfig().getStringList("COMMAND.TICKET.MESSAGE.REGISTER")) {
							player.sendMessage(string.replace("<site_link>", Ticket.get().getYmlConfig().getString("CONNECT.SITE_LINK")));
						}
					}

					if(!found) {
						PreparedStatement preparedStatement2 = connection.prepareStatement("INSERT INTO `uxs_user`(`username`, `password`, `uuid`, `ign`) VALUES ('" + player.getName() + "', '123', '" + uuid + "', '"+ player.getName() +"')");
						preparedStatement2.executeUpdate();
						preparedStatement2.close();

					}


					preparedStatement.close();
				} catch (SQLException e) {
					Bukkit.getLogger().severe("Issue adding a player to database. Is your MySQL Server running properly?");
				}
			}

		}.runTaskAsynchronously(Ticket.get());
	}


	public void getUserDB(Connection connection, Player player) {
		new BukkitRunnable() {
			@Override
			public void run() {
				try {

					UUID uuid = player.getUniqueId();
					PreparedStatement preparedStatement = connection.prepareStatement(
							"SELECT id, username, password, uuid FROM `uxs_user` WHERE uuid='" + uuid + "';");
					ResultSet result = preparedStatement.executeQuery();

					while(result.next()) {
						for (String string : Ticket.get().getYmlConfig().getStringList("COMMAND.TICKET.MESSAGE.ACCOUNT")) {
							player.sendMessage(string
									.replace("<site_link>", Ticket.get().getYmlConfig().getString("CONNECT.SITE_LINK"))
									.replace("<username>", result.getString(2))
									.replace("<password>", result.getString(3))
									.replace("<userid>", result.getString(1))
									.replace("<uuid>", result.getString(4))
							);
						}
					}

					preparedStatement.close();

				} catch (SQLException e) {
					Bukkit.getLogger().severe("Issue view account on database. Is your MySQL Server running properly?");
				}
			}

		}.runTaskAsynchronously(Ticket.get());
	}
	public void addTicket(Connection connection, Player player) {
		new BukkitRunnable() {
			@Override
			public void run() {
				try {

					UUID uuid = player.getUniqueId();
					PreparedStatement preparedStatement = connection.prepareStatement(
							"SELECT id, uuid FROM `uxs_user` WHERE uuid='" + uuid + "';");
					ResultSet result = preparedStatement.executeQuery();

						String userid = "";
						while(result.next()) {
							userid = result.getString(1);
							for (String string : Ticket.get().getYmlConfig().getStringList("COMMAND.TICKET.MESSAGE.CREATED")) {
								player.sendMessage(string.replace("<site_link>", Ticket.get().getYmlConfig().getString("CONNECT.SITE_LINK")));
							}
						}


						PreparedStatement preparedStatement2 = connection.prepareStatement(
								"INSERT INTO `chat`(`id-cat`, `id-user`, `uuid`, `priority`, `description`, `status`, `expire`) VALUES ('" + Ticket.get().getYmlConfig().getString("CONNECT.CATEGORY_DEFAULT") + "', '" + userid + "', '" + uuid + "', 'low', 'Help me', 'oppened', '-1')");
						preparedStatement2.executeUpdate();
						preparedStatement2.close();



					preparedStatement.close();



				} catch (SQLException e) {
					Bukkit.getLogger().severe("Issue create ticket on database. Is your MySQL Server running properly?");
				}
			}

		}.runTaskAsynchronously(Ticket.get());
	}
	public void addRank(Connection connection, Player player, String arg, String user) {
		new BukkitRunnable() {
			@Override
			public void run() {
				try {

					UUID uuid = player.getUniqueId();
					PreparedStatement preparedStatement = connection.prepareStatement(
							"SELECT id, ign, uuid FROM `uxs_user` WHERE ign='" + user + "';");
					ResultSet result = preparedStatement.executeQuery();

						String userid = "";
						String useruuid = "";
						while(result.next()) {
							userid = result.getString(1);
							useruuid = result.getString(3);
							for (String string : Ticket.get().getYmlConfig().getStringList("COMMAND.TICKET.MESSAGE.ADD-RANK")) {
								player.sendMessage(string
										.replace("<site_link>", Ticket.get().getYmlConfig().getString("CONNECT.SITE_LINK"))
										.replace("<userid>", userid)
										.replace("<ign>", user)
										.replace("<rank>", arg)
										.replace("<uuid>", useruuid)
								);
							}
						}

							PreparedStatement preparedStatement2 = connection.prepareStatement("UPDATE `uxs_user` SET `role` = '" + arg + "' WHERE `uxs_user`.`id` = '" + userid + "';");
							preparedStatement2.executeUpdate();
							preparedStatement2.close();



					preparedStatement.close();



				} catch (SQLException e) {
					Bukkit.getLogger().severe("Issue create ticket on database. Is your MySQL Server running properly?");
				}
			}

		}.runTaskAsynchronously(Ticket.get());
	}


	@Override
	public int getTicket(Connection connection, UUID playerUUID) {
		try {
			String uuidStr = playerUUID.toString();
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(id) AS total FROM `chat` WHERE uuid='" + uuidStr + "';");
			ResultSet result = preparedStatement.executeQuery();

			int resultsCount = 0;
			int openedToday = 0;

			if(result != null) {
				while(result.next()) {
					if(resultsCount < 1) {
						openedToday = result.getInt("total");
					}
					resultsCount++;
				}
			}

			preparedStatement.close();

			if(resultsCount != 1) {
				return 0;
			} else {
				return openedToday;
			}

		} catch (SQLException e) {
			Bukkit.getLogger().severe("Failed to check coins Today. Is your MySQL Server running properly?");
			return 0;
		}
	}



}
