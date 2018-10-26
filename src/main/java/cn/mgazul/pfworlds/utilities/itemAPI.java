package cn.mgazul.pfworlds.utilities;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class itemAPI
{
    public static ItemStack doItem(Material material, int menge, String name, ArrayList<String> lore) {
        final ItemStack item = new ItemStack(material, menge);
        final ItemMeta meta = item.getItemMeta();
        meta.setLore((ArrayList<String>)lore);
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }
    
    public static ItemStack doItemHead(final Material material, final int menge, final String owner, final String name, final ArrayList<String> lore) {
        final ItemStack item = new ItemStack(material, menge);
        final SkullMeta meta = (SkullMeta)item.getItemMeta();
        meta.setLore((ArrayList<String>)lore);
        meta.setOwningPlayer(Bukkit.getPlayer(owner));
        meta.setDisplayName(name);
        item.setItemMeta((ItemMeta)meta);
        return item;
    }
}
