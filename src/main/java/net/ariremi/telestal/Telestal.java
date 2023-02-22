package net.ariremi.telestal;

import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.file.Files;

public final class Telestal extends JavaPlugin implements Listener {
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

        getCommand("telestal").setExecutor(new TelestalCommandExecutor(this));
        getCommand("telestal").setTabCompleter(new TelestalTabComplete(this));
        getServer().getPluginManager().registerEvents(new TelestalItem(this),this);
        getServer().getPluginManager().registerEvents(new TelestalPortal(this),this);
        getServer().getPluginManager().registerEvents(new TelestalBannedEvent(this),this);
        getServer().getPluginManager().registerEvents(this,this);
        getLogger().info("起動しました！");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        new TelestalPortal(this).PortalLoad();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        HolographicDisplaysAPI holo_api = HolographicDisplaysAPI.get(this);
        holo_api.deleteHolograms();
    }
}
