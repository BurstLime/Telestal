package net.ariremi.telestal;

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

        getLogger().info("起動しました！");
        getCommand("telestal").setExecutor(new TelestalCommandExecutor(this));
        getCommand("telestal").setTabCompleter(new TelestalTabComplete(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
