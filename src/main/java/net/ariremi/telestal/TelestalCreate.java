package net.ariremi.telestal;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class TelestalCreate {
    Telestal plugin;
    String name;
    Location loc;
    public TelestalCreate(Telestal plugin){
        this.plugin = plugin;
    }


    public void CreateFile(String name, Location loc) throws FileNotFoundException, UnsupportedEncodingException {
        File newFile = new File(plugin.getDataFolder().getPath()+"\\portal\\"+name+".yml");
        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(newFile), "UTF-8");
        Map<String, Object> dataMap = new HashMap<>();
        List<String> playerlist = new ArrayList<>();
        dataMap.put("world", loc.getWorld().getName());
        dataMap.put("x", loc.getBlockX());
        dataMap.put("y", loc.getBlockY());
        dataMap.put("z", loc.getBlockZ());
        dataMap.put("yaw", loc.getYaw());
        dataMap.put("pitch", loc.getPitch());
        dataMap.put("player", playerlist);
        PrintWriter writer = new PrintWriter(newFile);

        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        Yaml yaml = new Yaml(options);
        yaml.dump(dataMap, writer);
        try {
            writer.close();
            osw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}