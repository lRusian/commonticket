/*
 * Copyright (UniXSystem) 2023.
 *
 * Plugin: CommonTicket
 * Author: Vuhp (Discord: vuhp)
 * Team: UniXSystem and Vanity Proyect
 * Website: unixsystem.com.ar or vanityproyect.com
 * Date: "2023"
 */

package dev.vuhp.common.command;

import dev.vuhp.common.Ticket;
import dev.vuhp.common.util.CC;
import dev.vuhp.common.util.FileConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TicketCommand implements CommandExecutor {

    FileConfig config = Ticket.get().getYmlConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        if (args.length == 0) {

            String ttTicket = String.valueOf(Ticket.get().getMySQLManager().getTicket(Ticket.get().getMySQLInfo().getConnection(), player.getUniqueId()));

            player.sendMessage(CC.translate(Ticket.get().getYmlConfig().getString("MESSAGE.TICKET.LOG")
                    .replace("<ticket_totals>", ttTicket)));
            player.sendMessage(CC.translate("&9&lCommonTicket &7&o(by: UniXSystem and Vuhp)"));
            player.sendMessage(CC.translate("&7&m------------------------------"));
            player.sendMessage(CC.translate("/ticket register | For register in the website."));
            player.sendMessage(CC.translate("/ticket add | For creating Ticket."));
            player.sendMessage(CC.translate("/ticket account | For view you account Information."));
            if (player.hasPermission("commonticket.rank.add")) {
                player.sendMessage(CC.translate("&b  ➤ &9/ticket rank <rankname> <nickname>"));
                return true;
            }
            player.sendMessage(CC.translate("&7&m------------------------------"));
            player.sendMessage(CC.translate("&9&lCopyRight at Vuhp. (Contact Discord: vuhp)"));
            return true;
        }

        String cases = args[0];
        if(cases.equalsIgnoreCase("register")) {
            if (config.getBoolean("COMMAND.TICKET.REGISTER")) {
                Ticket.get().getMySQLManager().addPlayerToDatabase(Ticket.get().getMySQLInfo().getConnection(), player);
            } else {
                player.sendMessage(config.getString("COMMAND.TICKET.MESSAGE.COMMAND-DISABLED"));
            }
            return true;
        }
        if(cases.equalsIgnoreCase("account")) {

            Ticket.get().getMySQLManager().getUserDB(Ticket.get().getMySQLInfo().getConnection(), player);

            return true;
        }
        if(cases.equalsIgnoreCase("add")) {

            Ticket.get().getMySQLManager().addTicket(Ticket.get().getMySQLInfo().getConnection(), player);

            return true;
        }
        if(cases.equalsIgnoreCase("rank")) {

            if (player.hasPermission("commonticket.rank.add")) {
                player.sendMessage(CC.translate("&c[&l!!!&c] You have not permissions."));
                return true;
            }

            if (args.length < 2) {
                player.sendMessage(CC.translate("&c[!!!] You have not placed the text correctly."));
                return true;
            }
            if (args.length < 3) {
                player.sendMessage(CC.translate("&c[!!!] You have not placed the text correctly."));
                return true;
            }
            if (args[2].contains(" ") ||args[2].contains("'") || args[2].contains(";") || args[2].contains("-") || args[2].contains("=") || args[2].contains("!") || args[2].contains("#") || args[2].contains(".") || args[2].contains(",") || args[2].contains("[") || args[2].contains("]") || args[2].contains("{") || args[2].contains("}")) {

                player.sendMessage(CC.translate("&c[!!!] Signs placed are not allowed."));
                return true;
            }

            if (args[1].equals("Developer")) {
                Ticket.get().getMySQLManager().addRank(Ticket.get().getMySQLInfo().getConnection(), player, "Developer", args[2]);
                return true;
            } else if (args[1].equals("Admin")) {
                Ticket.get().getMySQLManager().addRank(Ticket.get().getMySQLInfo().getConnection(), player, "Admin", args[2]);
                return true;
            } else if (args[1].equals("Staff")) {
                Ticket.get().getMySQLManager().addRank(Ticket.get().getMySQLInfo().getConnection(), player, "Staff", args[2]);
                return true;
            } else if (args[1].equals("Member")) {
                Ticket.get().getMySQLManager().addRank(Ticket.get().getMySQLInfo().getConnection(), player, "Member", args[2]);
                return true;
            } else {
                player.sendMessage(CC.translate("&c[!!!] " + args[1] + " Rank does not exist!"));
                return true;
            }
        }

        return false;
    }
}
