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
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Scout {
	
	public static HashMap<String,Double> cooldown1 = new HashMap<String,Double>();
	public static HashMap<String,Double> cooldown2 = new HashMap<String,Double>();
	public static HashMap<String,Double> cooldown3 = new HashMap<String,Double>();
	public static List<UUID> potionItems  = new ArrayList<UUID>();
	public static List<UUID> potionItems2 = new ArrayList<UUID>();
	public static final List<Location> third = new ArrayList<Location>();
	
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
	
		public static void attemptFirst(Player p){
			if (cooldown1.containsKey(p.getName())){
				p.sendMessage(ChatColor.RED + "Cooldown: " + ChatColor.YELLOW + cooldown1.get(p.getName()) + ChatColor.RED + ".");
				return;
			}
			cooldown1.put(p.getName(), 15.0);
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 4, 1));
			ParticleEffect.CRIT_MAGIC.display(1f, 1f, 1f, 0.1f, 50, p.getLocation().add(0, 0.5, 0), 100);
			p.playSound(p.getLocation(), Sound.BAT_TAKEOFF, 1f, 0.5f);
		}
		
		@SuppressWarnings("deprecation")
		public static void attemptSecond(Player p){
			if (cooldown2.containsKey(p.getName())){
				p.sendMessage(ChatColor.RED + "Cooldown: " + ChatColor.YELLOW + cooldown2.get(p.getName()) + ChatColor.RED + ".");
				return;
			}
			cooldown2.put(p.getName(), 1.0);
			ItemStack potion = new ItemStack(373, 1, (byte) 16418);
			Location location = p.getLocation().toVector().add(p.getLocation().getDirection().multiply(1.2)).toLocation(p.getWorld()).add(0, 1.2, 0);
			final Item item1 = p.getWorld().dropItem(location, potion);
			final Item item2 = p.getWorld().dropItem(location, potion);
			final Item item3 = p.getWorld().dropItem(location, potion);
			final Item item4 = p.getWorld().dropItem(location, potion);
			item1.setPickupDelay(1000);
			item2.setPickupDelay(1000);
			item3.setPickupDelay(1000);
			item4.setPickupDelay(1000);
			Random r = new Random();
			int i1 = r.nextInt(10) + 85;
			int i2 = r.nextInt(10) + 85;
			int i3 = r.nextInt(10) + 85;
			int i4 = r.nextInt(10) + 85;
			int i5 = r.nextInt(10) + 85;
			int i6 = r.nextInt(10) + 85;
			int i7 = r.nextInt(10) + 85;
			int i8 = r.nextInt(10) + 85;
			item1.setVelocity(getDirection(p, i1, i2));
			item2.setVelocity(getDirection(p, i3, i4));
			item3.setVelocity(getDirection(p, i5, i6));
			item4.setVelocity(getDirection(p, i7, i8));
			Game.playSound(Sound.DIG_SNOW, p.getLocation(), 1f, 1f);
			Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), new Runnable(){
				public void run(){
					potionItems.add(item1.getUniqueId());
					potionItems.add(item2.getUniqueId());
					potionItems.add(item3.getUniqueId());
					potionItems.add(item4.getUniqueId());
				}
			}, 5l);
		}
		
		@SuppressWarnings("deprecation")
		public static void checkSecond(){
			for (Item i : Bukkit.getWorld("PrisonMap").getEntitiesByClass(Item.class)){
				if (potionItems.contains(i.getUniqueId())){
					boolean bo = false;
					for (Entity e : i.getNearbyEntities(1, 1, 1)){
						if (e instanceof Player){
							Player p = (Player) e;
							p.damage(0.0);
							p.setVelocity(i.getVelocity().multiply(0.7));
							potionItems.remove(i.getUniqueId());
							ParticleEffect.CRIT_MAGIC.display(0.2f, 1f, 0.2f, 0.1f, 6, p.getLocation().add(0, 0.2, 0), 100);
							i.teleport(i.getLocation().subtract(0, 500, 0));
						}
						if (!bo && i.getLocation().subtract(0, 0.5, 0).getBlock().getType() != Material.AIR){
							if (Game.isBreakable(i.getLocation().subtract(0, 0.5, 0).getBlock().getType())){				
							ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(Material.SEA_LANTERN, (byte) 0), 0.3f, 0.2f, 0.3f, 0.1f, 15, i.getLocation(), 100);
							Block b = i.getLocation().subtract(0, 0.5, 0).getBlock();
							Material m = b.getType();
							Byte by = b.getData();
							b.setType(Material.AIR);
							b.getWorld().spawnFallingBlock(b.getLocation(), m, by);
							Game.playSound(Sound.GLASS, i.getLocation(), 1f, 1f);
							}
							potionItems.remove(i.getUniqueId());
							i.teleport(i.getLocation().subtract(0, 500, 0));
					}
				}
				if (i.getTicksLived() > 2 * 20){
					potionItems.remove(i.getUniqueId());
					i.remove();
				}
			}
			}
		}
		
		@SuppressWarnings("deprecation")
		public static void attemptThird(Player p){
			if (cooldown3.containsKey(p.getName())){
				p.sendMessage(ChatColor.RED + "Cooldown: " + ChatColor.YELLOW + cooldown3.get(p.getName()) + ChatColor.RED + ".");
				return;
			}
			cooldown3.put(p.getName(), 12.0);
			ItemStack potion = new ItemStack(373, 1, (byte) 16418);
			Location location = p.getLocation().toVector().add(p.getLocation().getDirection().multiply(0.5)).toLocation(p.getWorld()).add(0, 1.2, 0);
			Item item = p.getWorld().dropItem(location, potion);
			item.setVelocity(getDirection(p, 90, 90));
			potionItems2.add(item.getUniqueId());
		}
		
		public static void checkThird1(){
			for (final Item i : Bukkit.getWorld("PrisonMap").getEntitiesByClass(Item.class)){
				if (potionItems2.contains(i.getUniqueId())){
					if (i.getLocation().clone().subtract(0, 0.5, 0).getBlock().getType() != Material.AIR){
						if (Game.isBreakable(i.getLocation().clone().subtract(0, 0.5, 0).getBlock().getType())){
							final Location loc = i.getLocation().getBlock().getLocation().clone(); 
							ParticleEffect.CRIT_MAGIC.display(1f, 0.1f, 1f, 0.1f, 50, i.getLocation(), 100);
							third.add(loc);
							Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), new Runnable(){
								public void run(){
									third.remove(loc);
								}
							}, 65l);
						}
						i.teleport(i.getLocation().subtract(0, 500, 0));
					}
				}
			}
		}
		
		@SuppressWarnings("deprecation")
		public static void checkThird2(){
			if (!third.isEmpty()){
				for (Location loc : third){
					ParticleEffect.CRIT_MAGIC.display(2f, 0.1f, 2f, 0.1f, 400, loc, 100);
					final List<Location> blocks = new ArrayList<Location>();
					int radius = 4;
					int bX = loc.getBlockX();
					int bY = loc.getBlockY();
					int bZ = loc.getBlockZ();
					for (int x = bX - radius; x <= bX + radius; x++){
						for (int y = bY - radius; y <= bY + radius; y++){
							for (int z = bZ - radius; z <= bZ + radius; z++){
								double distance = ((bX-x)*(bX-x) + ((bZ-z)*(bZ-z)) + ((bY-y)*(bY-y)));
								if (distance < radius*radius){
									Location loc1 = new Location(loc.getWorld(), x, y, z);
									if (Game.isBreakable(loc1.getBlock().getType())){
										blocks.add(loc1);
									}
								}
							}
						}
					}
					Random r = new Random();
					int i = r.nextInt(blocks.size());
					Block b = blocks.get(i).getBlock();
					Material m = b.getType();
					Byte by = b.getData();
					b.setType(Material.AIR);
					FallingBlock fb = b.getWorld().spawnFallingBlock(b.getLocation(), m, by);
					fb.setVelocity(new Vector(0, 0.3, 0));
				}
			}
		}

		public static Vector getDirection(Player p, int yawadd, int pitchadd){
			double pitch = ((p.getLocation().getPitch() + pitchadd) * Math.PI) / 180;
			double yaw  = ((p.getLocation().getYaw() + yawadd)  * Math.PI) / 180;
			double x = Math.sin(pitch) * Math.cos(yaw);
			double z = Math.sin(pitch) * Math.sin(yaw);
			double y = Math.cos(pitch);
			return new Vector(x, y, z).multiply(1.6);
		}
		
		public static void giveItems(Player p){
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.GRAY + "Punch someone with this to knock them");
			lore.add(ChatColor.GRAY + "away. Right-Click to give yourself 4 seconds");
			lore.add(ChatColor.GRAY + "of speed I.");
			ItemStack first = ItemAPI.getItem(Material.STICK, ChatColor.GREEN + "Knockback Stick - Speed Boost", lore, 1);
			p.getInventory().addItem(first);
			lore.clear();
			lore.add(ChatColor.GRAY + "Right-Click to shoot a shotgun of speed potions");
			lore.add(ChatColor.GRAY + "speed potions that throwback people and break");
			lore.add(ChatColor.GRAY + "blocks they hit.");
			p.getInventory().addItem(ItemAPI.getItem(Material.BLAZE_ROD, ChatColor.GREEN + "Speed Shotgun", lore, 1));
			lore.clear();
			lore.add(ChatColor.GRAY + "Right-Click to create a small circle that");
			lore.add(ChatColor.GRAY + "breaks a few blocks.");
			p.getInventory().addItem(ItemAPI.getItem(Material.DIAMOND_HOE, ChatColor.GREEN + "Scout Circle", lore, 1));
			p.updateInventory();
		}
}
