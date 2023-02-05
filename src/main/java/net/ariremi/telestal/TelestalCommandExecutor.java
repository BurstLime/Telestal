package net.ariremi.telestal;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

public class TelestalCommandExecutor implements CommandExecutor {
    Telestal plugin;

    public TelestalCommandExecutor(Telestal plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(!(sender instanceof Player)){
            plugin.getLogger().info(plugin.getConfig().getString("console_execute"));
            return true;
        }

        String prefix = plugin.getConfig().getString("prefix")+" ";
        prefix = prefix.replace("&","§");

        if(command.getName().equalsIgnoreCase("telestal")){
            if(args.length == 0 || args[0].equalsIgnoreCase("help")){
                String HelpMessage = ChatColor.BLUE + "<=== Telestal Help ===>" + ChatColor.RESET + "\n"
                        + "/ts help " + ChatColor.GREEN + "- " + plugin.getConfig().getString("command_help").replace("&","§") + ChatColor.RESET + "\n"
                        + "/ts give <name> <任意:使用回数> " + ChatColor.GREEN + "- " + plugin.getConfig().getString("command_give").replace("&","§") + ChatColor.RESET + "\n"
                        + "/ts create <name> " + ChatColor.GREEN + "- " + plugin.getConfig().getString("command_create").replace("&","§") + ChatColor.RESET + "\n"
                        + "/ts remove <name> " + ChatColor.GREEN + "- " + plugin.getConfig().getString("command_remove").replace("&","§") + ChatColor.RESET + "\n"
                        + "/ts set <name> " + ChatColor.GREEN + "- " + plugin.getConfig().getString("command_set").replace("&","§") + ChatColor.RESET + "\n"
                        + "/ts rename <name> <new_name>" + ChatColor.GREEN + "- " + plugin.getConfig().getString("command_rename").replace("&","§") + ChatColor.RESET + "\n"
                        + "/ts info [portal|player] <name> " + ChatColor.GREEN + "- " + plugin.getConfig().getString("command_info").replace("&","§") + ChatColor.RESET + "\n"
                        + "/ts list " + ChatColor.GREEN + "- " + plugin.getConfig().getString("command_list").replace("&","§") + ChatColor.RESET + "\n"
                        + "/ts reset <player> " + ChatColor.GREEN + "- " + plugin.getConfig().getString("command_reset").replace("&","§") + ChatColor.RESET + "\n"
                        + "/ts activate <name> <player>" + ChatColor.GREEN + "- " + plugin.getConfig().getString("command_activate").replace("&","§") + ChatColor.RESET + "\n"
                        + "/ts inactivate <name> <player>" + ChatColor.GREEN + "- " + plugin.getConfig().getString("command_inactivate").replace("&","§") + ChatColor.RESET + "\n";

                sender.sendMessage(HelpMessage);
            } else if (args[0].equalsIgnoreCase("activate")) {
                //activate
                Player player;
                if (args.length == 3) {
                    try {
                        player = plugin.getServer().getPlayer(args[2]);
                        player.getUniqueId().toString();
                    }catch (Exception e){
                        try{
                            player = plugin.getServer().getPlayer(String.valueOf(plugin.getServer().getOfflinePlayer(plugin.getServer().getPlayerUniqueId(args[2]))));
                            player.getUniqueId().toString();
                        }catch (Exception f){
                            sender.sendMessage(prefix+plugin.getConfig().getString("player_not_found").replace("&","§"));
                            return true;
                        }
                    }
                } else {
                    player = (Player) sender;
                }

                List<String> activate_player = this.getactivateplayer(player, args[1]);
                if (activate_player == null) {
                    return true;
                }

                if (activate_player.contains(player.getUniqueId().toString())) {
                    sender.sendMessage(prefix + plugin.getConfig().getString("activate_already").
                            replace("<player>", player.getName()).replace("<portal>", args[1]).replace("&","§"));
                } else {
                    activate_player.add(player.getUniqueId().toString());
                    try {
                        new TelestalActivate(plugin).PlayerActivate(args[1], activate_player);
                        sender.sendMessage(prefix + plugin.getConfig().getString("activate_success").
                                replace("<portal>", args[1]).replace("<player>", player.getName()).replace("&","§"));
                    } catch (FileNotFoundException e) {
                        sender.sendMessage(prefix + plugin.getConfig().getString("activate_fail").
                                replace("<portal>", args[1]).replace("&","§"));
                        throw new RuntimeException(e);
                    }
                }
            } else if (args[0].equalsIgnoreCase("inactivate")) {
                //inactivate
                Player player;
                if (args.length == 3) {
                    try {
                        player = plugin.getServer().getPlayer(args[2]);
                        player.getUniqueId().toString();
                    }catch (Exception e){
                        try{
                            player = plugin.getServer().getPlayer(String.valueOf(plugin.getServer().getOfflinePlayer(plugin.getServer().getPlayerUniqueId(args[2]))));
                            player.getUniqueId().toString();
                        }catch (Exception f){
                            sender.sendMessage(prefix+plugin.getConfig().getString("player_not_found").replace("&","§"));
                            return true;
                        }
                    }
                } else {
                    player = (Player) sender;
                }

                List<String> activate_player = this.getactivateplayer(player, args[1]);
                if (activate_player == null) {
                    return true;
                }

                if (!activate_player.contains(player.getUniqueId().toString())) {
                    sender.sendMessage(prefix + plugin.getConfig().getString("inactivate_already").
                            replace("<player>", player.getName()).replace("<portal>", args[1]).replace("&","§"));
                } else {
                    activate_player.remove(player.getUniqueId().toString());
                    try {
                        new TelestalActivate(plugin).PlayerActivate(args[1], activate_player);
                        sender.sendMessage(prefix + plugin.getConfig().getString("inactivate_success").
                                replace("<portal>", args[1]).replace("<player>", player.getName()).replace("&","§"));
                    } catch (FileNotFoundException e) {
                        sender.sendMessage(prefix + plugin.getConfig().getString("inactivate_fail").
                                replace("<portal>", args[1]).replace("&","§"));
                        throw new RuntimeException(e);
                    }
                }
            } else if (args[0].equalsIgnoreCase("create") && args.length == 2) {
                //create
                File File = new File(plugin.getDataFolder().getPath(),"portal");
                File = new File(File,args[1]+".yml");
                if(Files.exists(File.toPath())){
                    sender.sendMessage(prefix+plugin.getConfig().getString("create_duplication").replace("&","§"));
                }else{
                    Player player = (Player) sender;
                    Location location = player.getLocation();
                    try {
                        new TelestalCreate(plugin).CreateFile(args[1],location);
                        sender.sendMessage(prefix+plugin.getConfig().getString("create_success").
                                replace("<portal>",args[1]).replace("&","§"));
                    } catch (FileNotFoundException e) {
                        sender.sendMessage(prefix+plugin.getConfig().getString("create_fail").
                                replace("<portal>",args[1]).replace("&","§"));
                    } catch (UnsupportedEncodingException e) {
                        sender.sendMessage(prefix+plugin.getConfig().getString("create_fail").
                                replace("<portal>",args[1]).replace("&","§"));
                        throw new RuntimeException(e);
                    }
                }
            } else if (args[0].equalsIgnoreCase("remove") && args.length == 2) {
                //remove
                this.RemoveFile(sender,args[1]);
            } else if (args[0].equalsIgnoreCase("rename") && args.length == 3) {
                //rename
                File now = new File(plugin.getDataFolder().getPath(),"portal");
                now = new File(now,args[1]+".yml");
                File newFile = new File(plugin.getDataFolder().getPath(),"portal");
                newFile = new File(newFile,args[2]+".yml");
                if(Files.exists(now.toPath()) && !Files.exists((newFile.toPath()))){
                    if(now.renameTo(newFile)){
                        sender.sendMessage(prefix+plugin.getConfig().getString("rename_success").
                                replace("&","§").replace("<portal>",args[1]).replace("<new_portal>",args[2]));
                    } else {
                        sender.sendMessage(prefix+plugin.getConfig().getString("rename_fail").
                                replace("&","§").replace("<portal>",args[1]));
                    }
                } else if (Files.exists(now.toPath()) && Files.exists(newFile.toPath())) {
                    sender.sendMessage(prefix+plugin.getConfig().getString("rename_duplication").
                            replace("&","§").replace("<new_portal>",args[2]));
                } else{
                    sender.sendMessage(prefix+plugin.getConfig().getString("portal_not_found").replace("&","§"));
                }
            } else if (args[0].equalsIgnoreCase("set") && args.length == 2) {
                //set
                File File = new File(plugin.getDataFolder().getPath(),"portal");
                File = new File(File,args[1]+".yml");
                if(Files.exists(File.toPath())) {
                    Player player = (Player) sender;
                    Location location = player.getLocation();
                    try {
                        this.SetLocation(args[1], location);
                        sender.sendMessage(prefix+plugin.getConfig().getString("set_success").
                                replace("&","§").replace("<portal>",args[1]));
                    } catch (FileNotFoundException e) {
                        sender.sendMessage(prefix+plugin.getConfig().getString("set_fail").
                                replace("&","§").replace("<portal>",args[1]));
                    }
                } else {
                    sender.sendMessage(prefix+plugin.getConfig().getString("portal_not_found").replace("&","§"));
                }
            } else if (args[0].equalsIgnoreCase("list")){
                //list
                Integer pages = new TelestalList(plugin).GetPages();
                Integer p;
                if(args.length == 2){
                    try {
                        p = Integer.valueOf(args[1]);
                    } catch (Exception e){
                        sender.sendMessage(prefix+plugin.getConfig().getString("invalid_value").replace("&","§"));
                        return true;
                    }
                }else {
                    p = 1;
                }

                if(args.length == 2 && 0 < p && p <= pages){
                    sender.spigot().sendMessage(new TelestalList(plugin).PageContent(p-1));
                } else if (pages == 0) {
                    sender.sendMessage(prefix+plugin.getConfig().getString("list_nothing").replace("&","§"));
                } else {
                    sender.spigot().sendMessage(new TelestalList(plugin).PageContent(0));
                }
            } else if (args[0].equalsIgnoreCase("reset")){
                //reset
                Player player;
                if (args.length == 2) {
                    try {
                        player = plugin.getServer().getPlayer(args[1]);
                        player.getUniqueId().toString();
                    }catch (Exception e){
                        sender.sendMessage(prefix+plugin.getConfig().getString("player_not_found").replace("&","§"));
                        return true;
                    }
                } else {
                    player = (Player) sender;
                }
                try {
                    ResetPlayer(sender,player);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } else if (args[0].equalsIgnoreCase("info") && args.length == 3) {
                //info
                if(args[1].equalsIgnoreCase("portal")){
                    new TelestalInformation(plugin).SendPortalInfo(sender,args[2]);
                } else if (args[1].equalsIgnoreCase("player")) {
                    Player player;
                    try {
                        player = plugin.getServer().getPlayer(args[2]);
                        player.getUniqueId().toString();
                    }catch (Exception e){
                        try{
                            player = plugin.getServer().getPlayer(String.valueOf(plugin.getServer().getOfflinePlayer(plugin.getServer().getPlayerUniqueId(args[2]))));
                            player.getUniqueId().toString();
                        }catch (Exception f){
                            sender.sendMessage(prefix+plugin.getConfig().getString("player_not_found").replace("&","§"));
                            return true;
                        }
                    }
                    new TelestalInformation(plugin).SendPlayerInfo(sender, player);
                }
            }
        }
        return true;
    }

    //アクティベートプレイヤーをリストで取得
    public List getactivateplayer(CommandSender sender, String portal){
        String prefix = plugin.getConfig().getString("prefix")+" ";
        prefix = prefix.replace("&","§");

        File File = new File(plugin.getDataFolder().getPath(),"portal");
        File = new File(File,portal+".yml");
        if(!Files.exists(File.toPath())) {
            sender.sendMessage(prefix + plugin.getConfig().getString("portal_not_found").replace("&","§"));
            return null;
        }else {
            InputStream inputStream;
            try {
                inputStream = new FileInputStream(File);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            Yaml yaml = new Yaml();
            Map<String, Object> load_data = yaml.load(inputStream);
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return (List) load_data.get("player");
        }
    }

    private void RemoveFile(CommandSender sender, String name){
        String prefix = plugin.getConfig().getString("prefix")+" ";
        prefix = prefix.replace("&","§");

        File File = new File(plugin.getDataFolder().getPath(),"portal");
        File = new File(File,name+".yml");
        if(Files.exists(File.toPath())){
            try{
                Files.delete(File.toPath());
                sender.sendMessage(prefix+plugin.getConfig().getString("remove_success").
                        replace("<portal>",name).replace("&","§"));
            } catch (IOException e) {
                sender.sendMessage(prefix+plugin.getConfig().getString("remove_fail").
                        replace("<portal>",name).replace("&","§"));
            }
        }else {
            sender.sendMessage(prefix+plugin.getConfig().getString("portal_not_found").replace("&","§"));
        }
    }

    private void SetLocation(String name, Location loc) throws FileNotFoundException {
        File File = new File(plugin.getDataFolder().getPath(),"portal");
        File = new File(File,name+".yml");
        InputStream inputStream;
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

        data.put("world", loc.getWorld().getName());
        data.put("x", loc.getBlockX());
        data.put("y", loc.getBlockY());
        data.put("z", loc.getBlockZ());
        data.put("yaw", loc.getYaw());
        data.put("pitch", loc.getPitch());
        PrintWriter writer = new PrintWriter(File);
        yaml.dump(data, writer);
        try {
            writer.close();
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void ResetPlayer(CommandSender sender,Player player) throws FileNotFoundException {
        String prefix = plugin.getConfig().getString("prefix")+" ";
        prefix = prefix.replace("&","§");

        String[] filelist = new TelestalList(plugin).PortalList();
        Integer count = 0;
        Integer error = 0;
        for (String s : filelist) {

            List<String> activate_player = this.getactivateplayer(player, s);
            if (activate_player == null) {
                continue;
            }

            if (activate_player.contains(player.getUniqueId().toString())) {
                activate_player.remove(player.getUniqueId().toString());
                try {
                    new TelestalActivate(plugin).PlayerActivate(s, activate_player);
                    count++;
                } catch (FileNotFoundException e) {
                    error++;
                    throw new RuntimeException(e);
                }
            }
        }

        sender.sendMessage(prefix+plugin.getConfig().getString("reset_success").
                replace("&","§").replace("<count>",count.toString()).replace("<player>",player.getName()));
        if (0 < error){
            sender.sendMessage(prefix+plugin.getConfig().getString("reset_error").
                    replace("&","§").replace("<count>",error.toString()));
        }
    }
}
