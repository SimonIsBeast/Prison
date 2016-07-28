/*
 * Prison - A plugin for the Minecraft Bukkit mod
 * Copyright (C) 2016  SirFaizdat
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package tech.mcprison.prison.core;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import tech.mcprison.prison.Prison;

/**
 * Basic colorized logger.
 *
 * @author SirFaizdat
 */
public class PrisonLogger {

    String coloredLogPrefix = "&7[&6Prison&7]";

    /**
     * Logs a message to the server.
     *
     * @param level   The log level from Logger.Level
     * @param message The log message.
     */
    public void log(Level level, String message) {
        ConsoleCommandSender sender = Bukkit.getConsoleSender();

        message = Prison.color(coloredLogPrefix + level.prefix + level.color + message);
        if (sender == null) {
            Bukkit.getLogger().info(ChatColor.stripColor(message));
        } else {
            sender.sendMessage(message);
        }

    }

    /**
     * Logs a message with Level INFO.
     *
     * @param message The message you wish to send.
     */
    public void info(String message) {
        log(Level.INFO, message);
    }

    /**
     * Logs a message with Level WARN.
     *
     * @param message The message you wish to send.
     */
    public void warning(String message) {
        log(Level.WARN, message);
    }

    /**
     * Logs a message with Level SEVERE.
     *
     * @param message The message you wish to send.
     */
    public void severe(String message) {
        log(Level.SEVERE, message);
    }

    public enum Level {
        INFO("&7[&3Info&7]&r ", "&7"), WARN("&7[&eWarning&7]&r ", "&e"), SEVERE(
            "&7[&4Severe &7]&r ", "&e");

        String prefix, color;

        Level(String prefix, String color) {
            this.prefix = prefix;
            this.color = color;
        }
    }

}
