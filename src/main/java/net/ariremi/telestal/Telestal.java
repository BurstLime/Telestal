package net.ariremi.telestal;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Telestal extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        if(!getDataFolder().exists()) getDataFolder().mkdir();
        saveDefaultConfig();

        Path data_path = Paths.get(getDataFolder().getPath()+"\\portal");
        if(!Files.exists(data_path)){
            File portal_file = new File(getDataFolder().getPath()+"\\portal");
            portal_file.mkdir();
        }

        getLogger().info("起動しました！");
        getCommand("telestal").setExecutor(new TelestalCommandExecutor(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
