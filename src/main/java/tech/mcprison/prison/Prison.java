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

package tech.mcprison.prison;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import tech.mcprison.prison.core.*;
import tech.mcprison.prison.core.Updater.UpdateResult;
import tech.mcprison.prison.core.Updater.UpdateType;
import tech.mcprison.prison.mines.Mines;

import java.io.File;
import java.io.IOException;

/**
 * @author SirFaizdat
 */
public class Prison extends JavaPlugin implements Listener {

    ///
    /// SINGLETON
    ///

    private static Prison i = null;

    public static Prison i() {
        return i;
    }

    ///
    /// FIELDS
    ///

    public static PrisonLogger l = new PrisonLogger();
    public Config config;
    public ItemManager im;
    private ComponentManager componentManager;
    public Updater updater;

    private Economy economy = null;
    private Permission permissions = null;

    // Utility Methods
    public static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    // BEGIN ENABLE STUFF

    public void onEnable() {
        long startTime = System.currentTimeMillis();
        i = this;
        this.saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);

        bootstrap();
        initPermissions();
        initEconomy();
        initComponents();
        initCommands();

        initMetrics();

        l.info("&7Enabled &3Prison v" + getDescription().getVersion()
            + "&7. Made with <3 by &3SirFaizdat&7.");
        long endTime = System.currentTimeMillis();
        l.info("&8Enabled in " + (endTime - startTime) + " milliseconds.");

        // Post-enable tasks
        updateCheck();
        populateItemManagerLater();
    }

    private void bootstrap() {
        config = new Config();
        im = new ItemManager();
        componentManager = new ComponentManager();
        new MessageUtil();
        updater = new Updater(this, 76155, this.getFile(), UpdateType.NO_DOWNLOAD, true);
    }

    private void initComponents() {
        componentManager.addComponent(new Mines());
        componentManager.enableAll();
    }

    private void initCommands() {
        if (config.enableAutosmelt)
            new AutoSmelt();
        if (config.enableAutoblock)
            new BlockCommand();
    }

    private void initMetrics() {
        if (config.optOut)
            return;
        if(getDescription().getVersion().contains("SNAPSHOT"))
            return;

        try {
            Metrics metrics = new Metrics(this);

            if (getPermissions() != null) {
                Metrics.Graph permissionsPluginGraph = metrics.createGraph("Permissions Plugins");
                permissionsPluginGraph.addPlotter(new Metrics.Plotter(getPermissions().getName()) {
                    @Override public int getValue() {
                        return 1;
                    }
                });
            }

            if (getEconomy() != null) {
                Metrics.Graph economyPluginGraph = metrics.createGraph("Economy Plugins");
                economyPluginGraph.addPlotter(new Metrics.Plotter(getEconomy().getName()) {
                    @Override public int getValue() {
                        return 1;
                    }
                });
            }

            metrics.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateCheck() {
        if (config.checkUpdates && !getDescription().getVersion().contains("-SNAPSHOT")) {
            if (updater.getResult() == UpdateResult.UPDATE_AVAILABLE) {
                l.info(MessageUtil.get("general.updateAvailable", updater.getLatestName()));
                for (Player p : getServer().getOnlinePlayers()) {
                    if (p.isOp() || p.hasPermission("prison.manage")) {
                        p.sendMessage(
                            MessageUtil.get("general.updateAvailable", updater.getLatestName()));
                    }
                }
            }
        }
    }

    private void populateItemManagerLater() {
        Bukkit.getScheduler().runTaskLater(Prison.i(), new Runnable() {

            @Override public void run() {
                try {
                    im.populateLists();
                } catch (IOException e) {
                    l.severe("Could not load item list. Will now only support Item IDs.");
                    e.printStackTrace();
                }
            }
        }, 10L);
    }

    private void initEconomy() {
        if (!Bukkit.getPluginManager().isPluginEnabled("Vault"))
            return;

        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager()
            .getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
            return;
        }
        economy = null;
    }

    private void initPermissions() {
        if (!Bukkit.getPluginManager().isPluginEnabled("Vault"))
            return;

        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager()
            .getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permissions = permissionProvider.getProvider();
            return;
        }
        permissions = null;
    }

    // END ENABLE STUFF

    public void onDisable() {
        componentManager.disableAll();
        componentManager.components.clear();
    }

    public Permission getPermissions() {
        return permissions;
    }

    public Economy getEconomy() {
        return economy;
    }

    public File getFile() {
        return super.getFile();
    }

    // Listeners
    @EventHandler public void onPlayerJoin(PlayerJoinEvent e) {
        if (updater.getResult() == UpdateResult.UPDATE_AVAILABLE) {
            Player p = e.getPlayer();
            if (p.isOp() || p.hasPermission("prison.manage")) {
                p.sendMessage(MessageUtil.get("general.updateAvailable", updater.getLatestName()));
            }
        }
    }
}
