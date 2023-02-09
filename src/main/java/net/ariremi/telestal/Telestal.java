package net.ariremi.telestal;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.file.Files;

public final class Telestal extends JavaPlugin {
    @Override
    public void onEnable() {
        // Plugin startup logic
        if(!getDataFolder().exists()) getDataFolder().mkdir();
        saveDefaultConfig();

        File portal_file = new File(getDataFolder(), "portal");
        if(!Files.exists(portal_file.toPath())){
            portal_file.mkdir();
        }

        if(!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")){
            getLogger().severe("HolographicDisplays is not installed or not enabled.");
            getLogger().severe("*** This plugin will be disabled. ***");
            this.setEnabled(false);
            return;
        }
        if(!Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")){
            getLogger().severe("ProtocolLib is not installed or not enabled.");
            getLogger().severe("*** This plugin will be disabled. ***");
            this.setEnabled(false);
            return;
        }

        getLogger().info("起動しました！");
        getCommand("telestal").setExecutor(new TelestalCommandExecutor(this));
        getCommand("telestal").setTabCompleter(new TelestalTabComplete(this));
        getServer().getPluginManager().registerEvents(new TelestalItem(this),this);
        getServer().getPluginManager().registerEvents(new TelestalPortal(this),this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
