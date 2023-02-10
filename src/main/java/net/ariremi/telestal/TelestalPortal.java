package net.ariremi.telestal;

import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.hologram.Hologram;
import me.filoghost.holographicdisplays.api.hologram.VisibilitySettings;
import me.filoghost.holographicdisplays.api.hologram.line.ItemHologramLine;
import me.filoghost.holographicdisplays.api.hologram.line.TextHologramLine;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class TelestalPortal implements Listener {
    Telestal plugin;

    public TelestalPortal(Telestal plugin){
        this.plugin = plugin;
    }


    public void PortalLoad(){
        HolographicDisplaysAPI api = HolographicDisplaysAPI.get(plugin);
        String[] portal_list = new TelestalList(plugin).PortalList();
        api.deleteHolograms();
        for (int i = 0; i < portal_list.length; i++){
            Hologram hologram = api.createHologram(Objects.requireNonNull(getLocation(portal_list[i])));
            VisibilitySettings visibilitySettings = hologram.getVisibilitySettings();
            visibilitySettings.setGlobalVisibility(VisibilitySettings.Visibility.VISIBLE);
            List<String> active_player = getactivateplayer(portal_list[i]);
            for (int a = 0; a < active_player.size(); a++){
                UUID uuid = UUID.fromString(active_player.get(a));
                Player player = plugin.getServer().getPlayer(uuid);
                visibilitySettings.setIndividualVisibility(player, VisibilitySettings.Visibility.HIDDEN);
            }
            TextHologramLine textLine = hologram.getLines().appendText(plugin.getConfig().getString("portal_hologram_top").
                    replace("&","§").replace("<portal>",portal_list[i]));
            ItemHologramLine itemLine = hologram.getLines().appendItem(new ItemStack(Material.DIAMOND));
            TextHologramLine textLine2 = hologram.getLines().appendText(plugin.getConfig().getString("portal_hologram_bottom").replace("&","§"));
        }
    }

    //ポータルのアクティベートイベント
    @EventHandler
    public void PortalActivate(PlayerMoveEvent e) throws FileNotFoundException {
        String prefix = plugin.getConfig().getString("prefix")+" ";
        prefix = prefix.replace("&","§");

        Player player = e.getPlayer();
        String[] portal_list = new TelestalList(plugin).PortalList();
        for (int i = 0; i < portal_list.length; i++){
            if(Objects.requireNonNull(getLocation(portal_list[i])).getBlockX() == player.getLocation().getBlockX() &&
                    Objects.requireNonNull(getLocation(portal_list[i])).getBlockZ() == player.getLocation().getBlockZ()){
                List<String> activate_player = new TelestalCommandExecutor(plugin).getactivateplayer(player,portal_list[i]);
                if(!activate_player.contains(player.getUniqueId().toString())){
                    activate_player.add(player.getUniqueId().toString());
                    new TelestalActivate(plugin).PlayerActivate(portal_list[i], activate_player);
                    player.sendMessage(prefix+plugin.getConfig().getString("portal_discover").replace("&","§").replace("<portal>",portal_list[i]));
                    player.sendTitle(plugin.getConfig().getString("portal_discover_title").replace("&","§").replace("<portal>",portal_list[i]),
                            plugin.getConfig().getString("portal_discover_subtitle").replace("&","§"),5,60, 5);
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5f, 1);
                    PortalLoad();
                }else{
                    player.sendActionBar(plugin.getConfig().getString("portal_visit_actionbar").
                            replace("&","§").replace("<portal>",portal_list[i]));
                }
            }
        }
    }

    private Location getLocation(String portal) {
        String prefix = plugin.getConfig().getString("prefix")+" ";
        prefix = prefix.replace("&","§");

        File File = new File(plugin.getDataFolder().getPath(), "portal");
        File = new File(File, portal + ".yml");
        if (!Files.exists(File.toPath())) {
            return null;
        } else {
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
            World world = plugin.getServer().getWorld(data.get("world").toString());
            double x = Double.valueOf(data.get("x").toString());
            double y = Double.valueOf(data.get("y").toString());
            double z = Double.valueOf(data.get("z").toString());
            float yaw = Float.valueOf(data.get("yaw").toString());
            float pitch = Float.valueOf(data.get("pitch").toString());
            return new Location(world,x+0.5,y+2,z+0.5,yaw,pitch);
        }
    }

    private List getactivateplayer(String portal){
        File File = new File(plugin.getDataFolder().getPath(),"portal");
        File = new File(File,portal+".yml");
        if(!Files.exists(File.toPath())) {
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

}
