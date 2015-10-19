package me.BenLoe.SuperSpleef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.BenLoe.SuperSpleef.Classes.BeastTamer;
import me.BenLoe.SuperSpleef.Classes.Bomber;
import me.BenLoe.SuperSpleef.Classes.KitType;
import me.BenLoe.SuperSpleef.Classes.Scout;
import me.BenLoe.SuperSpleef.Classes.Tank;
import net.md_5.bungee.api.ChatColor;

import org.Prison.Main.Currency.MoneyAPI;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Game {
	
	public enum GameState {
		WAITING, COUNTDOWN, WARMUP, INGAME, WIN;
		
		public void setGameState(){
			gs = this;
		}
	}
	
	public static GameState gs = GameState.WAITING;
	public static List<String> ingame = new ArrayList<String>();
	public static List<String> inqueue = new ArrayList<String>();
	public static List<String> watching = new ArrayList<String>();
	public static HashMap<String,ItemStack[]> invs = new HashMap<String,ItemStack[]>();
	public static HashMap<String,ItemStack[]> armors = new HashMap<String,ItemStack[]>();
	public static String tag = "§b[§9SuperSpleef§b]: ";
	
	public static GameState getGameState(){
	return gs;
	}
	
	public static Location getLocation(String type){
		Location loc = new Location(Bukkit.getWorld(Files.getDataFile().getString(type + ".world")), Files.getDataFile().getInt(type + ".x"), Files.getDataFile().getInt(type + ".y"), Files.getDataFile().getInt(type + ".z"));
		return loc;
	}
	
	public static void setLocation(String type, Location loc){
		Files.getDataFile().set(type + ".world", loc.getWorld().getName());
		Files.getDataFile().set(type + ".x", loc.getBlockX());
		Files.getDataFile().set(type + ".y", loc.getBlockY());
		Files.getDataFile().set(type + ".z", loc.getBlockZ());
		Files.saveDataFile();
	}
	
	public static void addToQueue(Player p){
		if (inqueue.contains(p.getName())){
			for (int i = 1; i <= inqueue.size(); i++){
				if (inqueue.get(i - 1) == p.getName()){
				p.sendMessage(Game.tag + "§eYou are §b" + i + "§e in queue.");
				}
			}
		}else{
		inqueue.add(p.getName());
		p.sendMessage(tag + "§eYou are now §b" + inqueue.size() + "§e in queue.");
		if (Game.gs == GameState.COUNTDOWN && inqueue.size() <= 8){
			p.sendMessage(Game.tag + "§eThe countdown has already started. Game starting in §b" + GameManager.time + " §eseconds.");
		}
		}
	}
	
	public static void sendToAllInQueue(String message){
		for (int i = 1; i <= 8; i++){
			if (i > inqueue.size()){
				break;
			}
			Bukkit.getPlayer(inqueue.get(i - 1)).sendMessage(message);
		}
	}
	
	public static void soundToAllInQueue(Sound sound){
		for (int i = 1; i <= 8; i++){
			if (i > inqueue.size()){
				break;
			}
			Player p = Bukkit.getPlayer(inqueue.get(i - 1));
			p.playSound(p.getLocation(), sound, 1f, 1f);
		}
	}
	
	public static void sendToAll(String message){
		for (String s : ingame){
			Bukkit.getPlayer(s).sendMessage(message);
		}
	}
	
	public static void sendToAllPlus(String message){
		for (String s : ingame){
			Bukkit.getPlayer(s).sendMessage(message);
		}
		for (String s : watching){
			Bukkit.getPlayer(s).sendMessage(message);
		}
	}
	
	public static void startGame(){
		for (int i = 1; i <= 8; i++){
			if (i > inqueue.size()){
				break;
			}
			Player p = Bukkit.getPlayer(inqueue.get(i - 1));
			p.setAllowFlight(false);
			p.setFlying(false);
			if (p.getGameMode() != GameMode.SURVIVAL){
				p.setGameMode(GameMode.SURVIVAL);
			}
			Location loc = Game.getLocation("Spawn" + i);
			p.teleport(loc);
			p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 2));
			invs.put(p.getName(), p.getInventory().getContents());
			armors.put(p.getName(), p.getInventory().getArmorContents());
			p.getInventory().clear();
			p.getInventory().setBoots(null);
			p.getInventory().setLeggings(null);
			p.getInventory().setChestplate(null);
			p.getInventory().setHelmet(null);
			p.updateInventory();
			ParticleEffect.FIREWORKS_SPARK.display(0.5f, 1f, 0.5f, 0.1f, 20, loc.add(0, 0.5, 0), 100);
			ingame.add(p.getName());
		}
		inqueue.removeAll(ingame);
		sendToAll(Game.tag + "§eGame starting.");
		for (int i = 1; i <= inqueue.size(); i++){
			Player p = Bukkit.getPlayer(inqueue.get(i--));
			p.sendMessage(Game.tag + "§eA game has started, you have been moved to §b" + i + "§e in queue.");
		}
	}
	
	public static void trueStartGame(){
		for (String s : ingame){
			Player p = Bukkit.getPlayer(s);
			if (KitType.getKit(p).equals(KitType.SCOUT)){
				Scout.giveItems(p);
			}
			if (KitType.getKit(p).equals(KitType.BOMBER)){
				Bomber.giveItems(p);
				Bomber.cooldown3.put(s, 10.0);
			}
			if (KitType.getKit(p).equals(KitType.TANK)){
				Tank.giveItems(p);
				Tank.cooldown3.put(s, 10.0);
			}
			if (KitType.getKit(p).equals(KitType.BEASTTAMER)){
				BeastTamer.giveItems(p);
			}
			p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1f, 1f);
		}
	}
	
	public static void Win(Player p){
		Game.gs = GameState.WIN;
		sendToAllPlus("§b✦§a-----------------------§b✦");
		sendToAllPlus("");
		sendToAllPlus("  §a§lWinner:");
		if (p == null){
			sendToAllPlus("  §eWell... no-one won.");
		}else{
			sendToAllPlus("  §l" + p.getName());
		}
		sendToAllPlus("");
		sendToAllPlus("§b✦§a-----------------------§b✦");
		if (p != null){
			p.sendMessage(ChatColor.GREEN + "+25,000$");
			MoneyAPI.addMoney(p, 25000);
			Stats.getStats(p.getName()).addWins(1).addGamesPlayed(1);
		}
	}
	
	public static void Die(Player p){
		sendToAllPlus(Game.tag + "§c" + p.getName() + " §ehas fallen and burned to death. §c" + (Game.ingame.size() - 1) + " §eplayers remain.");
		Game.ingame.remove(p.getName());
		Game.watching.add(p.getName());
		p.teleport(getLocation("Lobby"));
		p.getInventory().clear();
		p.getInventory().clear();
		p.getInventory().setBoots(null);
		p.getInventory().setLeggings(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setHelmet(null);
		p.updateInventory();
		p.getInventory().setContents(Game.invs.get(p.getName()));
		p.getInventory().setArmorContents(Game.armors.get(p.getName()));
		p.updateInventory();
		Stats.getStats(p.getName()).addGamesPlayed(1);
		for (String s : Game.ingame){
			Player p1 = Bukkit.getPlayer(s);
			p1.sendMessage(ChatColor.GREEN + "+1000$");
			MoneyAPI.addMoney(p1, 1000);
		}
		if (Bomber.ultimate.contains(p.getName())){
			Bomber.ultimate.remove(p.getName());
		}
	}
	
	public static boolean playerInGame(Player p){
		if (ingame.contains(p.getName())){
			return true;
		}
		return false;
	}
	
	public static void playSound(Sound s, Location loc, float pitch, float level){
		for (String s1 : Game.ingame){
			Player p = Bukkit.getPlayer(s1);
			p.playSound(loc, s, level, pitch);
		}
		for (String s2 : Game.watching){
			Player p = Bukkit.getPlayer(s2);
			p.playSound(loc, s, level, pitch);
		}
	}
	
	public static boolean isBreakable(Material m){
		List<Material> ms = new ArrayList<Material>();
		ms.add(Material.STAINED_CLAY);
		ms.add(Material.SEA_LANTERN);
		ms.add(Material.GLOWSTONE);
		if (ms.contains(m)){
			return true;
		}else{
			return false;
		}
	}
}
