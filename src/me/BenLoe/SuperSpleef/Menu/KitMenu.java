package me.BenLoe.SuperSpleef.Menu;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class KitMenu {

	public static List<String> ininv = new ArrayList<String>();
	
	public static void open(Player p){
		Inventory inv = Bukkit.createInventory(null, 9 * 4, ChatColor.BLUE+ "Kit Menu");
		MenuItem.createMenu(inv, p);
		if (!ininv.contains(p.getName())){
			ininv.add(p.getName());
		}
		p.openInventory(inv);
	}
	
}
