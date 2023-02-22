package net.ariremi.telestal;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class TelestalBannedEvent implements Listener {
    Telestal plugin;
    public TelestalBannedEvent(Telestal plugin){
        this.plugin = plugin;
    }

    //Anvil Stop Event
    @EventHandler
    public void AnvilEvent(InventoryClickEvent e){
        if(e.getInventory().getType() != InventoryType.ANVIL){return;}
        ItemStack item = e.getCurrentItem();
        ArrayList<String> telestal_lore = new ArrayList<>();
        telestal_lore.add(ChatColor.GREEN+"右クリック"+ChatColor.WHITE+"で特定の場所へ転移する結晶");
        telestal_lore.add(ChatColor.DARK_RED+"発見済みポータルにのみ転移可能");
        try {
            if(item.getType()!= Material.DIAMOND){return;}
            if(item.getItemMeta().getDisplayName().contains("§b§l転移結晶 §a- ")&&item.getLore().equals(telestal_lore)){
                e.setCancelled(true);
            }
        }catch (NullPointerException ignored){
        }

    }
}
