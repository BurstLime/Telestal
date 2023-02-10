package net.ariremi.telestal;

import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class TelestalItem implements Listener {
    Telestal plugin;
    public TelestalItem(Telestal plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void ClickTelestal(PlayerInteractEvent e) {
        String prefix = plugin.getConfig().getString("prefix")+" ";
        prefix = prefix.replace("&","§");

        Player player = e.getPlayer();
        Action action = e.getAction();
        ItemStack item = e.getItem();

        ArrayList<String> telestal_lore = new ArrayList<>();
        telestal_lore.add(ChatColor.GREEN+"右クリック"+ChatColor.WHITE+"で特定の場所へ転移する結晶");
        telestal_lore.add(ChatColor.DARK_RED+"発見済みポータルにのみ転移可能");

        if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
            if (!(item==null)&&item.hasItemMeta()&&
                    item.getItemMeta().getDisplayName().contains("§b§l転移結晶 §a- ")&&item.getType() == Material.DIAMOND&&item.getLore().equals(telestal_lore)) {
                String portalname = item.getItemMeta().getDisplayName().replace("§b§l転移結晶 §a- §d","");
                List getPortal = new TelestalCommandExecutor(plugin).getactivateplayer(player,portalname);
                if(getPortal.contains(player.getUniqueId().toString())){
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,25,8,true,false));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,15,255,true,false));
                    player.spawnParticle(Particle.ENCHANTMENT_TABLE,player.getLocation(),500,0.5,1,0.5,0.2);
                    player.spawnParticle(Particle.PORTAL,player.getLocation(),500,0.5,1,0.5,0.5);
                    player.teleport(getLocation(e.getPlayer(),portalname));
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT,SoundCategory.MASTER, 0.3f,1);
                    item.setAmount(item.getAmount()-1);
                }else{
                    player.sendMessage(prefix+plugin.getConfig().getString("portal_not_activate").replace("&","§"));
                }
            }
        }
    }

    //転移結晶の入手
    public void GiveTelestal(CommandSender sender,String portal,Player player,Integer amount){
        String prefix = plugin.getConfig().getString("prefix")+" ";
        prefix = prefix.replace("&","§");

        File File = new File(plugin.getDataFolder().getPath(),"portal");
        File = new File(File,portal+".yml");
        if(!Files.exists(File.toPath())) {
            sender.sendMessage(prefix + plugin.getConfig().getString("portal_not_found").replace("&", "§"));
        }else {
            ItemStack telestal = new ItemStack(Material.DIAMOND, amount);
            ItemMeta telestal_meta = telestal.getItemMeta();

            telestal_meta.setDisplayName("§b§l転移結晶 §a- "+ChatColor.LIGHT_PURPLE+portal);

            ArrayList<String> telestal_lore = new ArrayList<>();
            telestal_lore.add(ChatColor.GREEN+"右クリック"+ChatColor.WHITE+"で特定の場所へ転移する結晶");
            telestal_lore.add(ChatColor.DARK_RED+"発見済みポータルにのみ転移可能");
            telestal_meta.setLore(telestal_lore);

            telestal.setItemMeta(telestal_meta);
            player.getInventory().addItem(telestal);

            sender.sendMessage(prefix+plugin.getConfig().getString("give_success").replace("&","§").
                    replace("<player>",player.getName()).replace("<portal>",portal).replace("<amount>",amount.toString()));
        }

    }

    //ポータルの位置取得
    public Location getLocation(Player player,String portal) {
        String prefix = plugin.getConfig().getString("prefix")+" ";
        prefix = prefix.replace("&","§");

        File File = new File(plugin.getDataFolder().getPath(), "portal");
        File = new File(File, portal + ".yml");
        if (!Files.exists(File.toPath())) {
            player.sendMessage(prefix + plugin.getConfig().getString("portal_not_found").replace("&", "§"));
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
            return new Location(world,x,y,z,yaw,pitch);
        }
    }
}