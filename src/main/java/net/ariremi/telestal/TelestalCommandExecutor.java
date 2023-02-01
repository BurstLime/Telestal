package net.ariremi.telestal;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
                        + "/ts clear [player|all] " + ChatColor.GREEN + "- " + plugin.getConfig().getString("command_clear").replace("&","§") + ChatColor.RESET + "\n"
                        + "/ts activate [<name>|all] <player>" + ChatColor.GREEN + "- " + plugin.getConfig().getString("command_activate").replace("&","§") + ChatColor.RESET + "\n"
                        + "/ts inactivate [<name>|all] <player>" + ChatColor.GREEN + "- " + plugin.getConfig().getString("command_inactivate").replace("&","§") + ChatColor.RESET + "\n";

                sender.sendMessage(HelpMessage);
            } else if (args[0].equalsIgnoreCase("activate")) {
                //activate
                if (args[1].equals("all")) {
                    sender.sendMessage("現在、未実装です。");
                    return true;
                }

                Player player;
                if (args.length == 3) {
                    player = plugin.getServer().getPlayer(args[2]);
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
                if (args[1].equals("all")) {
                    sender.sendMessage("現在、未実装です。");
                    return true;
                }

                Player player;
                if (args.length == 3) {
                    player = plugin.getServer().getPlayer(args[2]);
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
                Path data_path = Paths.get(plugin.getDataFolder().getPath()+"\\portal\\"+args[1]+".yml");
                if(Files.exists(data_path)){
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
                Path now_path = Paths.get(plugin.getDataFolder().getPath() + "\\portal\\" + args[1] + ".yml");
                Path new_path = Paths.get(plugin.getDataFolder().getPath() + "\\portal\\" + args[2] + ".yml");
                if(Files.exists(now_path) && !Files.exists((new_path))){
                    File now = new File(now_path.toUri());
                    File newFile = new File(plugin.getDataFolder().getPath() + "\\portal\\" + args[2] + ".yml");
                    if(now.renameTo(newFile)){
                        sender.sendMessage(prefix+plugin.getConfig().getString("rename_success").
                                replace("&","§").replace("<portal>",args[1]).replace("<new_portal>",args[2]));
                    } else {
                        sender.sendMessage(prefix+plugin.getConfig().getString("rename_fail").
                                replace("&","§").replace("<portal>",args[1]));
                    }
                } else if (Files.exists(now_path) && Files.exists(new_path)) {
                    sender.sendMessage(prefix+plugin.getConfig().getString("rename_duplication").
                            replace("&","§").replace("<new_portal>",args[2]));
                } else{
                    sender.sendMessage(prefix+plugin.getConfig().getString("portal_not_found").replace("&","§"));
                }
            }
        }
        return true;
    }

    private List getactivateplayer(CommandSender sender, String name){
        String prefix = plugin.getConfig().getString("prefix")+" ";
        prefix = prefix.replace("&","§");

        Path data_path = Paths.get(plugin.getDataFolder().getPath()+"\\portal\\"+name+".yml");
        if(!Files.exists(data_path)) {
            sender.sendMessage(prefix + plugin.getConfig().getString("portal_not_found").replace("&","§"));
            return null;
        }else {
            File newFile = new File(plugin.getDataFolder().getPath()+"\\portal\\"+name+".yml");
            InputStream inputStream;
            try {
                inputStream = new FileInputStream(newFile);
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

        Path File = Paths.get(plugin.getDataFolder().getPath() + "\\portal\\" + name + ".yml");
        if(Files.exists(File)){
            try{
                Files.delete(File);
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
}
