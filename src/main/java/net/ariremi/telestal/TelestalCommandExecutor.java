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
        String prefix = plugin.getConfig().getString("prefix")+" ";
        prefix = prefix.replace("&","§");

        if(command.getName().equalsIgnoreCase("telestal")){
            if(args.length == 0 || args[0].equalsIgnoreCase("help")){
                String HelpMessage = ChatColor.BLUE + "<=== Telestal Help ===>" + ChatColor.RESET + "\n"
                        + "/ts help " + ChatColor.GREEN + "- " + plugin.getConfig().getString("command_help") + ChatColor.RESET + "\n"
                        + "/ts give <name> <任意:使用回数> " + ChatColor.GREEN + "- " + plugin.getConfig().getString("command_give") + ChatColor.RESET + "\n"
                        + "/ts create <name> " + ChatColor.GREEN + "- " + plugin.getConfig().getString("command_create") + ChatColor.RESET + "\n"
                        + "/ts remove <name> " + ChatColor.GREEN + "- " + plugin.getConfig().getString("command_remove") + ChatColor.RESET + "\n"
                        + "/ts set <name> " + ChatColor.GREEN + "- " + plugin.getConfig().getString("command_set") + ChatColor.RESET + "\n"
                        + "/ts rename <name> <new_name>" + ChatColor.GREEN + "- " + plugin.getConfig().getString("command_rename") + ChatColor.RESET + "\n"
                        + "/ts info [portal|player] <name> " + ChatColor.GREEN + "- " + plugin.getConfig().getString("command_info") + ChatColor.RESET + "\n"
                        + "/ts list " + ChatColor.GREEN + "- " + plugin.getConfig().getString("command_list") + ChatColor.RESET + "\n"
                        + "/ts clear [player|all] " + ChatColor.GREEN + "- " + plugin.getConfig().getString("command_clear") + ChatColor.RESET + "\n"
                        + "/ts activate [<name>|all] <player>" + ChatColor.GREEN + "- " + plugin.getConfig().getString("command_activate") + ChatColor.RESET + "\n"
                        + "/ts inactivate [<name>|all] <player>" + ChatColor.GREEN + "- " + plugin.getConfig().getString("command_inactivate") + ChatColor.RESET + "\n";

                sender.sendMessage(HelpMessage);
            } else if (args[0].equalsIgnoreCase("activate")) {
                //activate
                Player player = (Player) sender;
                List<String> activate_player = this.getactivateplayer(sender,args[1]);
                if (activate_player == null){
                    return true;
                }

                if(activate_player.contains(player.getUniqueId().toString())){
                    sender.sendMessage(prefix+ChatColor.RED+plugin.getConfig().getString("activate_already").
                            replace("<player>",sender.getName()).replace("<portal>",args[1]));
                }else{
                    activate_player.add(player.getUniqueId().toString());
                    try {
                        new TelestalActivate(plugin).PlayerActivate(args[1],activate_player);
                        sender.sendMessage(prefix+ChatColor.GREEN+plugin.getConfig().getString("activate_success").
                                replace("<portal>",args[1]).replace("<player>",sender.getName()));
                    } catch (FileNotFoundException e) {
                        sender.sendMessage(prefix+ChatColor.RED+plugin.getConfig().getString("activate_fail").
                                replace("<portal>",args[1]));
                        throw new RuntimeException(e);
                    }
                }

            } else if (args[0].equalsIgnoreCase("create") && args.length == 2) {
                //create
                Path data_path = Paths.get(plugin.getDataFolder().getPath()+"\\portal\\"+args[1]+".yml");
                if(Files.exists(data_path)){
                    sender.sendMessage(prefix+ChatColor.RED+plugin.getConfig().getString("create_duplication"));
                }else{
                    Player player = (Player) sender;
                    Location location = player.getLocation();
                    try {
                        new TelestalCreate(plugin).CreateFile(args[1],location);
                        sender.sendMessage(prefix+ChatColor.GREEN+plugin.getConfig().getString("create_success").replace("<portal>",args[1]));
                    } catch (FileNotFoundException e) {
                        sender.sendMessage(prefix+ChatColor.RED+plugin.getConfig().getString("create_fail").replace("<portal>",args[1]));
                    } catch (UnsupportedEncodingException e) {
                        sender.sendMessage(prefix+ChatColor.RED+plugin.getConfig().getString("create_fail").replace("<portal>",args[1]));
                        throw new RuntimeException(e);
                    }
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
            sender.sendMessage(prefix + ChatColor.RED + plugin.getConfig().getString("portal_not_found"));
            return null;
        }else {
            File newFile = new File(plugin.getDataFolder().getPath()+"\\portal\\"+name+".yml");
            InputStream inputStream = null;
            try {
                inputStream = new FileInputStream(newFile);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            Yaml yaml = new Yaml();
            Map<String, Object> load_data = yaml.load(inputStream);
            return (List) load_data.get("player");
        }
    }
}
