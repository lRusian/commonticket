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
import java.util.UUID;

import dev.vuhp.common.Ticket;
import dev.vuhp.common.util.FileConfig;
import org.bukkit.entity.Player;

public abstract class DatabaseManager {
	
	protected Ticket plugin;

	public DatabaseManager(Ticket pl) {
		plugin = pl;
	}

	public abstract boolean createCratesTable(Connection connection, FileConfig config);

	public abstract void addPlayerToDatabase(Connection connection, Player player);
	public abstract void getUserDB(Connection connection, Player player);
	public abstract void addTicket(Connection connection, Player player);
	public abstract void addRank(Connection connection, Player player, String arg, String user);
	public abstract int getTicket(Connection connection, UUID playerUUID);

}
