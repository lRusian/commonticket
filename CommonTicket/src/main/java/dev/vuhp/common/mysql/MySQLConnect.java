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

public class MySQLConnect {
	
	private Connection connection;
	private String username;
	private String password;

	public MySQLConnect(Connection connection, String username, String password) {
		this.connection = connection;
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public Connection getConnection() {
		return connection;
	}

}
