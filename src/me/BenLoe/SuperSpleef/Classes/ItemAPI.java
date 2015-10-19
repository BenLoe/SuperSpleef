package me.BenLoe.SuperSpleef.Classes;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemAPI {

	public static ItemStack getItem(Material m, String DisplayName, List<String> Lore, int amount){
		@SuppressWarnings("deprecation")
		ItemStack item = new ItemStack(m.getId(), amount, (byte)0);
		ItemMeta itemm = item.getItemMeta();
		itemm.setDisplayName(DisplayName);
		itemm.setLore(Lore);
		item.setItemMeta(itemm);
		return item;
	}
	
}
