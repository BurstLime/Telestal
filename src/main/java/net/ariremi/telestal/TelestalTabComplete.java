package net.ariremi.telestal;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TelestalTabComplete implements TabCompleter {
    Telestal plugin;
    public TelestalTabComplete(Telestal plugin){
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
        String[] portal_list = new TelestalList(plugin).PortalList();
        String[] command_list = {"help","give","create","remove","set","info","list","reset","reload","tp","activate","inactivate"};
        if(command.getName().equalsIgnoreCase("telestal")) {
            if(args.length == 1){
                if(args[0].length() == 0){
                    return Arrays.asList(command_list);
                }else{
                    List<String> searchlist = new ArrayList<>();
                    for (String s : command_list) {if (s.startsWith(args[0])) {searchlist.add(s);}}
                    return Arrays.asList(searchlist.toArray(new String[searchlist.size()]));
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("info")) {
                    return Arrays.asList("portal", "player");
                } else if (args[0].equalsIgnoreCase("list")) {
                    List<String> pagelist = new ArrayList<>();
                    for(int i = 0; i < new TelestalList(plugin).GetPages(); i++){
                        pagelist.add(Integer.toString(i+1));
                    }
                    return Arrays.asList(pagelist.toArray(new String[pagelist.size()]));
                } else if (args[0].equalsIgnoreCase("remove")||args[0].equalsIgnoreCase("set")
                        ||args[0].equalsIgnoreCase("activate")||args[0].equalsIgnoreCase("inactivate")
                        ||args[0].equalsIgnoreCase("give")||args[0].equalsIgnoreCase("tp")){
                    if(args[1].length() == 0){
                        return Arrays.asList(portal_list);
                    }else{
                        List<String> searchlist = new ArrayList<>();
                        for (String s : portal_list) {if (s.startsWith(args[1])) {searchlist.add(s);}}
                        return Arrays.asList(searchlist.toArray(new String[searchlist.size()]));
                    }
                } else if (args[0].equalsIgnoreCase("create")) {
                    return Arrays.asList("");
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("give")){
                    return Arrays.asList("");
                }else if (args[0].equalsIgnoreCase("info")&&args[1].equalsIgnoreCase("portal")) {
                    if(args[2].length() == 0){
                        return Arrays.asList(portal_list);
                    }else{
                        List<String> searchlist = new ArrayList<>();
                        for (String s : portal_list) {if (s.startsWith(args[2])) {searchlist.add(s);}}
                        return Arrays.asList(searchlist.toArray(new String[searchlist.size()]));
                    }
                }
            }
        }
        return null;
    }
}
