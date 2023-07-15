/*
 * Copyright (UniXSystem) 2023.
 *
 * Plugin: CommonTicket
 * Author: Vuhp (Discord: vuhp)
 * Team: UniXSystem and Vanity Proyect
 * Website: unixsystem.com.ar or vanityproyect.com
 * Date: "2023"
 */

package dev.vuhp.common.util;

import dev.vuhp.common.Ticket;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.WordUtils;

import java.util.List;
import java.util.stream.Collectors;

public class CC {
    public static String translate(String in) {
        return ChatColor.translateAlternateColorCodes('&', in);
    }

    public static List<String> translate(List<String> in) {
        return in.stream().map(CC::translate).collect(Collectors.toList());
    }
    public static String capitalize(String input) {
        return WordUtils.capitalize(input);
    }

    public static String prefix(String input) {
        return input
                .replace("<prefix>", "&7[&9CommonTicket&7] &b➤ ")
                .replace("<arrow>", "»")
                .replace("<arrow_bold>", "➤")
                .replace("<sa>", "↴")
                .replace("<symbol>", "\u26C3");

    }

}
