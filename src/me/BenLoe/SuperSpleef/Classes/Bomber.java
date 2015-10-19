package me.BenLoe.SuperSpleef.Classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import me.BenLoe.SuperSpleef.Game;
import me.BenLoe.SuperSpleef.Main;
import me.BenLoe.SuperSpleef.ParticleEffect;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Bomber {
	
	public static HashMap<String,Double> cooldown1 = new HashMap<String,Double>();
	public static HashMap<String,Double> cooldown2 = new HashMap<String,Double>();
	public static HashMap<String,Double> cooldown3 = new HashMap<String,Double>();
	public static List<UUID> tnts = new ArrayList<UUID>();
	public static List<String> ultimate = new ArrayList<String>();
	
	public static void attemptFirst(Player p){
		if (cooldown1.containsKey(p.getName())){
			p.sendMessage(ChatColor.RED + "Cooldown: " + ChatColor.YELLOW + cooldown1.get(p.getName()) + ChatColor.RED + ".");
			return;
		}
		cooldown1.put(p.getName(), 5.0);
		ParticleEffect.EXPLOSION_LARGE.display(0.5f, 2f, 0.5f, 0.1f, 10, p.getLocation(), 100);
		ParticleEffect.LAVA.display(0.2f, 0.5f, 0.3f, 0.1f, 10, p.getLocation(), 100);
		Game.playSound(Sound.EXPLODE, p.getLocation(), 1f, 0.2f);
		p.setVelocity(p.getLocation().getDirection().multiply(0.9).setY(1.1));
	}
	
	public static void attemptSecond(Player p){
		if (cooldown2.containsKey(p.getName())){
			p.sendMessage(ChatColor.RED + "Cooldown: " + ChatColor.YELLOW + cooldown2.get(p.getName()) + ChatColor.RED + ".");
			return;
		}
		cooldown2.put(p.getName(), 6.0);
		Item i = p.getWorld().dropItem(p.getLocation().clone().add(0, 5, 0), new ItemStack(Material.TNT));
		i.setPickupDelay(1000);
		i.teleport(i.getLocation().clone().subtract(0, 1.8, 0));
		i.setVelocity(p.getLocation().getDirection().multiply(0.5));
		Game.playSound(Sound.FIZZ, p.getLocation(), 0.6f, 0.3f);
		tnts.add(i.getUniqueId());
	}
	public static void checkSecond(){
		for (Item i : Bukkit.getWorld("PrisonMap").getEntitiesByClass(Item.class)){
			if (tnts.contains(i.getUniqueId())){
				ParticleEffect.SMOKE_NORMAL.display(0.25f, 0.25f, 0.25f, 0.0001f, 5, i.getLocation().clone().add(0, 0.5, 0), 100);
				if (i.getTicksLived() > 50){
					ParticleEffect.EXPLOSION_HUGE.display(1f, 1f, 1f, 0.1f, 20, i.getLocation().clone().add(0, 1, 0), 100);
					for (Entity e : i.getNearbyEntities(4, 4, 4)){
						if (e instanceof Player){
							Location midPoint = i.getLocation();
							Vector direction = e.getLocation().toVector().subtract(midPoint.toVector()).normalize();
							direction.multiply(1.1).setY(0.7);
							e.setVelocity(direction);
							((Player) e).damage(0.0);
						}
					}
					final List<Location> blocks = new ArrayList<Location>();
					int radius = 3;
					int bX = i.getLocation().getBlockX();
					int bY = i.getLocation().getBlockY();
					int bZ = i.getLocation().getBlockZ();
					for (int x = bX - radius; x <= bX + radius; x++){
						for (int y = bY - radius; y <= bY + radius; y++){
							for (int z = bZ - radius; z <= bZ + radius; z++){
								double distance = ((bX-x)*(bX-x) + ((bZ-z)*(bZ-z)) + ((bY-y)*(bY-y)));
								if (distance < radius*radius){
									Location loc = new Location(i.getWorld(), x, y, z);
									if (loc.getBlock().getType() != Material.AIR){
										blocks.add(loc);
									}
								}
							}
						}
					}
					for (Location loc : blocks){
						Random r = new Random();
						int i1 = r.nextInt(2) + 1;
						if (Game.isBreakable(loc.getBlock().getType()) && i1 == 1){
							loc.getBlock().setType(Material.AIR);
						}
					}
					Game.playSound(Sound.EXPLODE, i.getLocation(), 1f, 1f);
					tnts.remove(i.getUniqueId());
					i.teleport(i.getLocation().subtract(0, 500, 0));
				}
			}
		}
	}
	
	public static void attemptThird(final Player p ){
		if (cooldown3.containsKey(p.getName())){
			p.sendMessage(ChatColor.RED + "Cooldown: " + ChatColor.YELLOW + cooldown3.get(p.getName()) + ChatColor.RED + ".");
			return;
		}
		cooldown3.put(p.getName(), 12.0);
		Game.playSound(Sound.ENDERDRAGON_GROWL, p.getLocation(), 1f, 0.3f);
		ultimate.add(p.getName());
		Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), new Runnable(){
			public void run(){
				ultimate.remove(p.getName());
			}
		}, 3 * 20l);
	}
	
	public static void checkThird(){
		for (Player p : Bukkit.getOnlinePlayers()){
			if (Game.playerInGame(p) && ultimate.contains(p.getName())){
				if (p.getLocation().clone().subtract(0, 1, 0).getBlock().getType() != Material.AIR){
					int radius = 4;
					int bX = p.getLocation().getBlockX();
					int bY = p.getLocation().getBlockY();
					int bZ = p.getLocation().getBlockZ();
					for (int x = bX - radius; x <= bX + radius; x++){
						for (int y = bY - radius; y <= bY + radius; y++){
							for (int z = bZ - radius; z <= bZ + radius; z++){
								double distance = ((bX-x)*(bX-x) + ((bZ-z)*(bZ-z)) + ((bY-y)*(bY-y)));
								if (distance < radius*radius && !(distance < ((radius - 1) * (radius - 1)))){
									Location loc = new Location(p.getWorld(), x, y, z);
									if (Game.isBreakable(loc.getBlock().getType())){
										ParticleEffect.EXPLOSION_LARGE.display(0.1f, 0.1f, 0.1f, 0.1f, 3, loc.clone().add(0, 0.2, 0), 100);
									}
								}
							}
						}
					}
					for (Entity e : p.getNearbyEntities(3, 2, 3)){
						if (e instanceof Player){
							Location midPoint = p.getLocation();
							Vector direction = e.getLocation().toVector().subtract(midPoint.toVector()).normalize();
							direction.multiply(0.5).setY(0.3);
							e.setVelocity(direction);
							((Player) e).damage(0.0);
						}
					}
				}
			}
		}
	}
	
	public static void checkCooldown(){
		for (Player p : Bukkit.getOnlinePlayers()){
			if (cooldown1.containsKey(p.getName())){
				double newtime = cooldown1.get(p.getName());
				newtime = newtime - 0.500000;
				cooldown1.remove(p.getName());
				if (newtime != 0.0){
					cooldown1.put(p.getName(), newtime);
				}
			}
			if (cooldown2.containsKey(p.getName())){
				double newtime = cooldown2.get(p.getName());
				newtime = newtime - 0.500000;
				cooldown2.remove(p.getName());
				if (newtime != 0.0){
					cooldown2.put(p.getName(), newtime);
				}
			}
			if (cooldown3.containsKey(p.getName())){
				double newtime = cooldown3.get(p.getName());
				newtime = newtime - 0.500000;
				cooldown3.remove(p.getName());
				if (newtime != 0.0){
					cooldown3.put(p.getName(), newtime);
				}
			}
		}
	}
	
	public static void giveItems(Player p){
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "Punch someone with this to knock them");
		lore.add(ChatColor.GRAY + "away. Right-Click to ignite a small explosion");
		lore.add(ChatColor.GRAY + "under you, launching you up.");
		ItemStack first = ItemAPI.getItem(Material.STICK, ChatColor.GREEN + "Knockback Stick - Explosive Feet", lore, 1);
		p.getInventory().addItem(first);
		lore.clear();
		lore.add(ChatColor.GRAY + "Right-Click to shoot a small tnt that");
		lore.add(ChatColor.GRAY + "will ignite, then explode after a few");
		lore.add(ChatColor.GRAY + "seconds.");
		p.getInventory().addItem(ItemAPI.getItem(Material.BLAZE_ROD, ChatColor.GREEN + "Mini Tnt", lore, 1));
		lore.clear();
		lore.add(ChatColor.GRAY + "Right-Click to create a small aura around");
		lore.add(ChatColor.GRAY + "you launching players away.");
		p.getInventory().addItem(ItemAPI.getItem(Material.DIAMOND_HOE, ChatColor.GREEN + "Explosive Aura", lore, 1));
		p.updateInventory();
	}
	
}
