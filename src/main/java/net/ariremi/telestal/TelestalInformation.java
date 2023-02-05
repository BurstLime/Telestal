package net.ariremi.telestal;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

public class TelestalInformation {
    Telestal plugin;
    public TelestalInformation(Telestal plugin){
        this.plugin = plugin;
    }

    //ポータル情報
    public void SendPortalInfo(CommandSender sender, String portal){
        String prefix = plugin.getConfig().getString("prefix")+" ";
        prefix = prefix.replace("&","§");

        File File = new File(plugin.getDataFolder().getPath(),"portal");
        File = new File(File,portal+".yml");
        if(!Files.exists(File.toPath())) {
            sender.sendMessage(prefix + plugin.getConfig().getString("portal_not_found").replace("&", "§"));
        }else {
            InputStream inputStream;
            try {
                inputStream = new FileInputStream(File);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(inputStream);
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            List activated = (List) data.get("player");
            Integer activatedcount = activated.size();
            String bar = "\n§3";
            for (int i = 0; i < plugin.getConfig().getString("info_portal_start_line").replace("<portal>",portal).length()-6; i++){
                bar = bar + "-";
            }

            sender.sendMessage(plugin.getConfig().getString("info_portal_start_line").replace("<portal>",portal).replace("&","§")+"\n"+
                    plugin.getConfig().getString("info_portal_content").replace("&","§").
                            replace("<world>",data.get("world").toString()).
                            replace("<x>",data.get("x").toString()).replace("<y>",data.get("y").toString()).replace("<z>",data.get("z").toString()).
                            replace("<activated>",activatedcount.toString())+bar);
        }
    }

    //プレイヤー情報
    public void SendPlayerInfo(CommandSender sender, Player player){
        String prefix = plugin.getConfig().getString("prefix")+" ";
        prefix = prefix.replace("&","§");

        String[] filelist = new TelestalList(plugin).PortalList();
        Integer count = 0;
        for (String s : filelist) {
            List<String> activate_player = new TelestalCommandExecutor(plugin).getactivateplayer(sender,s);
            if (activate_player == null) {
                continue;
            }

            if (activate_player.contains(player.getUniqueId().toString())) {
                count++;
            }
        }

        String bar = "\n§3";
        for (int i = 0; i < plugin.getConfig().getString("info_portal_start_line").replace("<player>",player.getName()).length()-6; i++){
            bar = bar + "-";
        }

        Integer chance;
        if(filelist.length == 0){
            chance = 100;
        }else{
            Double before_chance = (double)count/filelist.length;
            chance = (int) (before_chance*100);
        }
        Integer inactivate = Integer.valueOf(filelist.length);

        sender.sendMessage(plugin.getConfig().getString("info_player_start_line").replace("<player>",player.getName()).replace("&","§")+"\n"+
                plugin.getConfig().getString("info_player_content").replace("&","§").
                        replace("<activated>",count.toString()).replace("<inactivate>",inactivate.toString()).replace("<chance>",chance.toString())+bar);

    }
}
