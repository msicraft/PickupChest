package com.msicraft.pickupchest;

import com.msicraft.pickupchest.Command.MainCommand;
import com.msicraft.pickupchest.Command.TabComplete;
import com.msicraft.pickupchest.Event.ChestInteractEvent;
import com.msicraft.pickupchest.Event.ChestRelateEvent;
import com.msicraft.pickupchest.Event.PreventDropEvent;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class PickupChest extends JavaPlugin {

    private static PickupChest plugin;

    public static String getPrefix() {
        return ChatColor.GREEN + "[Pickup Chest]";
    }

    public static PickupChest getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        final int configVersion = plugin.getConfig().contains("config-version", true) ? plugin.getConfig().getInt("config-version") : -1;
        if (configVersion != 2) {
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + getPrefix() + ChatColor.RED + " You are using the old config");
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + getPrefix() + ChatColor.RED + " Created the latest config.yml after replacing the old config.yml with config_old.yml");
            replaceconfig();
            createFiles();
        } else {
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + getPrefix() + " You are using the latest version of config.yml");
        }
        getServer().getPluginCommand("pickupchest").setExecutor(new MainCommand());
        getServer().getPluginCommand("pickupchest").setTabCompleter(new TabComplete());
        getServer().getPluginManager().registerEvents(new ChestInteractEvent(), this);
        getServer().getPluginManager().registerEvents(new PreventDropEvent(), this);
        getServer().getPluginManager().registerEvents(new ChestRelateEvent(), this);
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + getPrefix() + " Plugin Enable");
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + getPrefix() + ChatColor.RED +" Plugin Disable");
    }

    public void FilesReload() {
        plugin.reloadConfig();
        ChestInteractEvent.reloadVariables();
    }

    protected FileConfiguration config;

    private void createFiles() {
        File configf = new File(getDataFolder(), "config.yml");

        if (!configf.exists()) {
            configf.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }
        FileConfiguration config = new YamlConfiguration();

        try {
            config.load(configf);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void replaceconfig() {
        File file = new File(getDataFolder(), "config.yml");
        this.config = YamlConfiguration.loadConfiguration(file);
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        File config_old = new File(getDataFolder(),"config_old-" + dateFormat.format(date) + ".yml");
        file.renameTo(config_old);
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + getPrefix() + " Plugin replaced the old config.yml with config_old.yml and created a new config.yml");
    }

}
