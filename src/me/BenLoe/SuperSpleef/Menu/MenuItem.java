package me.BenLoe.SuperSpleef.Menu;

import java.util.ArrayList;
import java.util.List;

import me.BenLoe.SuperSpleef.Stats;
import me.BenLoe.SuperSpleef.Classes.KitType;

import org.Prison.Main.Currency.MoneyAPI;
import org.Prison.Main.Menu.MenuType;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public enum MenuItem {
	
	BUY_TANK(11), BUY_BEASTTAMER(15), SHOW_STATS(22);
	
	private final int i;
	
	private MenuItem(int i){
		this.i = i;
	}
	
	public void wasClicked(Player p){
		switch(this){
		case BUY_BEASTTAMER:
			if ((MoneyAPI.getMoney(p) >= 350000) && (Stats.getStats(p).getGamesPlayed() >= 50) && (Stats.getStats(p).getWins() >= 25) && !KitType.BEASTTAMER.ownsKit(p)){
				KitType.BEASTTAMER.buyKit(p);
				p.sendMessage("§aYou bought the §bBeastTamer §akit.");
				p.closeInventory();
				MoneyAPI.removeMoney(p, 350000);
			}else{
				p.playSound(p.getLocation(), Sound.NOTE_BASS, 1f, 1f);
			}
			break;
		case BUY_TANK:
			if ((MoneyAPI.getMoney(p) >= 250000) && (Stats.getStats(p).getGamesPlayed() >= 30) && (Stats.getStats(p).getWins() >= 15) && !KitType.TANK.ownsKit(p)){
				KitType.TANK.buyKit(p);
				p.sendMessage("§aYou bought the §bTank §akit.");
				p.closeInventory();
				MoneyAPI.removeMoney(p, 250000);
			}else{
				p.playSound(p.getLocation(), Sound.NOTE_BASS, 1f, 1f);
			}
			break;
		case SHOW_STATS:
			break;
		}
	}
	
	public ItemStack getItemFor(Player p){
		switch(this){
		case BUY_BEASTTAMER: {
			List<String> lore = new ArrayList<String>();
			lore.add("§fClick to buy the §bBeastTamer §fkit.");
			lore.add("");
			lore.add("§e§lCost:");
			lore.add("§7Must have played §b50 §7games.");
			lore.add("§7Must have won §b25 §7games.");
			lore.add("§a350,000$");
			lore.add("  ");
			if (!KitType.BEASTTAMER.ownsKit(p)){
			if (!(MoneyAPI.getMoney(p) >= 350000) || !(Stats.getStats(p).getGamesPlayed() >= 50) || !(Stats.getStats(p).getWins() >= 25)){
				lore.add("§cYou can't afford to buy this.");
			}
			}else{
				lore.add("§aYou already own this kit.");
			}
			return ItemAPI.getItem(Material.PORK, "§a§lBuy BeastTamer Kit", lore);
		}
		case BUY_TANK: {
			List<String> lore = new ArrayList<String>();
			lore.add("§fClick to buy the §bTank §fkit.");
			lore.add("");
			lore.add("§e§lCost:");
			lore.add("§7Must have played §b30 §7games.");
			lore.add("§7Must have won §b15 §7games.");
			lore.add("§a250,000$");
			lore.add("  ");
			if (!KitType.TANK.ownsKit(p)){
			if (!(MoneyAPI.getMoney(p) >= 250000) || !(Stats.getStats(p).getGamesPlayed() >= 30) || !(Stats.getStats(p).getWins() >= 13)){
				lore.add("§cYou can't afford to buy this.");
			}
			}else{
				lore.add("§aYou already own this kit.");
			}
			return ItemAPI.getItem(Material.CHAINMAIL_CHESTPLATE, "§a§lBuy Tank Kit", lore);
		}
		case SHOW_STATS: {
			List<String> lore = new ArrayList<String>();
			lore.add("§7 Money: §a" + MoneyAPI.getMoney(p));
			lore.add("§7 Games Played: §9" + Stats.getStats(p).getGamesPlayed());
			lore.add("§7 Wins: §b" + Stats.getStats(p).getWins());
			return ItemAPI.getItem(Material.BOOK, "§e§lPersonal Information:", lore);
		}
		}
		return null;
	}
	
	public int getSlot(){
		return this.i;
	}
	
	public static MenuItem getItemFromSlot(int slot){
		for (MenuItem m : values()){
				if (slot == m.i){
					return m;
				}
		}
		return MenuItem.SHOW_STATS;
	}
	
	public static boolean wasAItem(int slot){
		for (MenuItem m : values()){
				if (slot == m.i){
					return true;
				}
		}
		return false;
	}
	
	public static Inventory createMenu(Inventory inv, Player p){
		for (MenuItem m : values()){
				inv.setItem(m.getSlot(), m.getItemFor(p));
		}
		return inv;
	}
}
