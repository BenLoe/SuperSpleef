package me.BenLoe.SuperSpleef;

import java.util.ArrayList;
import java.util.List;

import me.BenLoe.SuperSpleef.Classes.BeastTamer;
import me.BenLoe.SuperSpleef.Classes.Bomber;
import me.BenLoe.SuperSpleef.Classes.Scout;
import me.BenLoe.SuperSpleef.Classes.Tank;
import me.BenLoe.SuperSpleef.Game.GameState;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;


public class Main extends JavaPlugin{

	Files files = new Files(this);
	Events events = new Events(this);
	
	public void onEnable(){
		Bukkit.getPluginManager().registerEvents(events, this);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
			public void run(){
				GameManager.manage();
			}
		}, 20l, 20l);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
			public void run(){
				Scout.checkCooldown();
				Scout.checkThird2();
				Bomber.checkCooldown();
				Bomber.checkSecond();
				Tank.checkCooldown();
				BeastTamer.checkCooldown();
			}
		}, 20l, 10l);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
			public void run(){
				Scout.checkSecond();
				Scout.checkThird1();
				Bomber.checkThird();
				Tank.checkFirst();
				BeastTamer.checkFirst();
			}
		}, 20l, 2l);
	}
	
	public void onDisable(){
		for (String s : Game.ingame){
			Player p = Bukkit.getPlayer(s);
			p.getInventory().clear();
			p.getInventory().setBoots(null);
			p.getInventory().setLeggings(null);
			p.getInventory().setChestplate(null);
			p.getInventory().setHelmet(null);
			p.updateInventory();
			p.getInventory().setContents(Game.invs.get(s));
			p.getInventory().setArmorContents(Game.armors.get(s));
			p.updateInventory();
			p.teleport(Game.getLocation("Lobby"));
		}
	}
	
	public static WorldEditPlugin getWorldEdit(){
		Plugin p = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
		if (p instanceof WorldEditPlugin) return (WorldEditPlugin) p;
		else return null;
	}
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	public boolean onCommand(CommandSender sender, Command cmd,
			String Label, String[] args){
		if (sender instanceof Player){
			Player p = (Player) sender;
			if (Label.equalsIgnoreCase("SS")){
				if (p.hasPermission("SS.Admin")){
				if (args.length == 0){
					p.sendMessage("§e§l§m----------------§e§l[§f§lSuperSpleef§e§l]§e§l§m----------------");
					p.sendMessage("   ");
					p.sendMessage("§b/SS spawn <1-8> §a- Set spawnpoint.");
					p.sendMessage("§b/SS save §a- Saves arena 50 blocks under.");
					p.sendMessage("§b/SS lobby §a- Sets lobby location.");
					p.sendMessage("   ");
					p.sendMessage("§e§l§m----------------------------------------");
					return true;
				}
				if (args.length == 1){
					if (args[0].equalsIgnoreCase("spawn")){
						p.sendMessage(ChatColor.RED + "Incorrect Syntax: /SS spawn <1-8>");
						return true;
					}
					if (args[0].equalsIgnoreCase("save")){
						Selection s = Main.getWorldEdit().getSelection(p);
						if (s == null){
							p.sendMessage(ChatColor.RED + "Please make a WorldEdit selection first.");
							return true;
						}
						Game.setLocation("Point1", s.getMaximumPoint());
						Game.setLocation("Point2", s.getMinimumPoint());
						p.sendMessage(ChatColor.GREEN + "Arena being copied...");
						int x1 = s.getMinimumPoint().getBlockX();
						int y1 = s.getMinimumPoint().getBlockY();
						int z1 = s.getMinimumPoint().getBlockZ();
						//MAXIMUM
						int x2 = s.getMaximumPoint().getBlockX();
						int y2 = s.getMaximumPoint().getBlockY();
						int z2 = s.getMaximumPoint().getBlockZ();
						List<Location> blocks1 = new ArrayList<Location>();
						for (int x = x1; x <= x2; x++) {
						    for (int y = y1; y <= y2; y++) {
						        for (int z = z1; z <= z2; z++) {
						           blocks1.add(s.getWorld().getBlockAt(x, y, z).getLocation()); 
						        }
						    }
						}
						for (int i = 0; i < blocks1.size(); i++){
							Location loc = blocks1.get(i);
							Block b = loc.getBlock();
							if (!b.getType().equals(Material.AIR)){
								b.getLocation().clone().subtract(0, 25, 0).getBlock().setType(b.getType());
								b.getLocation().clone().subtract(0, 25, 0).getBlock().setData(b.getData());
							}
						}
						p.sendMessage(ChatColor.GREEN + "Done!");
						return true;
					}
					if (args[0].equalsIgnoreCase("lobby")){
						p.sendMessage(ChatColor.GREEN + "Set lobby location.");
						Game.setLocation("Lobby", p.getLocation());
						return true;
					}
					if (args[0].equalsIgnoreCase("watching")){
						List<Vector> vectors = (List<Vector>) Files.getDataFile().getList("Watching");
						vectors.add(p.getLocation().getBlock().getLocation().toVector());
						Files.getDataFile().set("Watching", vectors);
						Files.saveDataFile();
						p.sendMessage(ChatColor.GREEN + "Added wathcing line.");
						return true;
					}
					p.sendMessage(ChatColor.RED + "Unknown Super Spleef command.");
				}
				if (args.length == 2){
					if (args[0].equalsIgnoreCase("save")){
						p.sendMessage(ChatColor.RED + "Inccorect syntax: /SS save");
						return true;
					}
					if (args[0].equalsIgnoreCase("lobby")){
						p.sendMessage(ChatColor.RED + "Inccorect syntax: /SS lobby");
						return true;
					}
					if (args[0].equalsIgnoreCase("spawn")){
						int number = Integer.parseInt(args[1]);
						p.sendMessage(ChatColor.GREEN + "Saved spawn location for spawnpoint" + number);
						Game.setLocation("Spawn" + number, p.getLocation());
						return true;
					}
				}
				if (args.length == 3){
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					return true;
				}
				}else{
					p.sendMessage(ChatColor.RED + "These commands are for admins only.");
				}
			}
			if (Label.equalsIgnoreCase("leave")){
				if (Game.ingame.contains(p.getName())){
					if (Game.gs == GameState.WARMUP || Game.gs == GameState.INGAME || Game.gs == GameState.WIN){
						p.getInventory().clear();
						p.getInventory().setBoots(null);
						p.getInventory().setLeggings(null);
						p.getInventory().setChestplate(null);
						p.getInventory().setHelmet(null);
						p.updateInventory();
						p.getInventory().setContents(Game.invs.get(p.getName()));
						p.getInventory().setArmorContents(Game.armors.get(p.getName()));
						p.updateInventory();
						p.teleport(Game.getLocation("Lobby"));
						Game.ingame.remove(p.getName());
						Game.watching.add(p.getName());
						Game.sendToAll(Game.tag + ChatColor.RED  + p.getName() + ChatColor.YELLOW  + " left the game.");
					}
				}else{
					p.sendMessage(ChatColor.RED + "You have nothing to leave.");
				}
			}
			return true;
		}
		return true;
	}
}
