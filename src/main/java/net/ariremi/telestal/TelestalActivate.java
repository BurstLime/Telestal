package net.ariremi.telestal;

import org.bukkit.Location;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;

public class TelestalActivate {
    Telestal plugin;

    public TelestalActivate(Telestal plugin){
        this.plugin = plugin;
    }

    public void PlayerActivate(String name, List<String> playerlist) throws FileNotFoundException {
        File File = new File(plugin.getDataFolder().getPath()+"\\portal\\"+name+".yml");
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(File);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        Yaml yaml = new Yaml(options);
        Map<String, Object> data = yaml.load(inputStream);

        data.put("player",playerlist);
        PrintWriter writer = new PrintWriter(File);
        yaml.dump(data, writer);
    }
}
