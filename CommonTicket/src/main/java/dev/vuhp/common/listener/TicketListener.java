/*
 * Copyright (UniXSystem) 2023.
 *
 * Plugin: CommonTicket
 * Author: Vuhp (Discord: vuhp)
 * Team: UniXSystem and Vanity Proyect
 * Website: unixsystem.com.ar or vanityproyect.com
 * Date: "2023"
 */

package dev.vuhp.common.listener;

import dev.vuhp.common.Ticket;
import dev.vuhp.common.util.CC;
import org.bukkit.event.player.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;

public class TicketListener implements Listener {

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        Player player = event.getPlayer();
        //Ticket.get().getMySQLManager().addPlayerToDatabase(Ticket.get().getMySQLInfo().getConnection(), player);
        if (Ticket.get().getYmlConfig().getBoolean("CONNECT.JOIN-SEND-MESSAGE")) {
            int ttTicket = Ticket.get().getMySQLManager().getTicket(Ticket.get().getMySQLInfo().getConnection(), player.getUniqueId());

            player.sendMessage(Ticket.get().getYmlConfig().getString("MESSAGE.TICKET.LOG")
                    .replace("<ticket_totals>", String.valueOf(ttTicket)));
        }
    }
}