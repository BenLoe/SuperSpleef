package me.BenLoe.SuperSpleef.Classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import me.BenLoe.SuperSpleef.Game;
import me.BenLoe.SuperSpleef.Main;
import me.BenLoe.SuperSpleef.ParticleEffect;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Tank {

	public static HashMap<String,Double> cooldown1 = new HashMap<String,Double>();
	public static HashMap<String,Double> cooldown2 = new HashMap<String,Double>();
	public static HashMap<String,Double> cooldown3 = new HashMap<String,Double>();
	public static List<FallingBlock> fallings = new ArrayList<FallingBlock>();
	
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
	
	@SuppressWarnings("deprecation")
	public static void attemptFirst(Player p){
		if (cooldown1.containsKey(p.getName())){
			p.sendMessage(ChatColor.RED + "Cooldown: " + ChatColor.YELLOW + cooldown1.get(p.getName()) + ChatColor.RED + ".");
			return;
		}
		cooldown1.put(p.getName(), 2.0);
		Location location = p.getLocation().toVector().add(p.getLocation().getDirection().multiply(0.5)).toLocation(p.getWorld()).clone().add(0, 1.5, 0);
		final FallingBlock f = p.getWorld().spawnFallingBlock(location, Material.COBBLESTONE, (byte)0);
		f.setDropItem(false);
		f.setFallDistance(1000f);
		f.setVelocity(p.getLocation().getDirection().multiply(1.8));
		Game.playSound(Sound.DIG_STONE, p.getLocation(), 1f, 0.5f);
		Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), new Runnable(){
			public void run(){
				fallings.add(f);
			}
		}, 3l);
	}
	
	public static void checkFirst(){
		if (!fallings.isEmpty()){
			List<FallingBlock> remove = new ArrayList<FallingBlock>();
			for (FallingBlock f : fallings){
				f.setFallDistance(1000f);
				boolean bo = false;
				for (Entity e : f.getNearbyEntities(1, 1, 1)){
					if (e instanceof Player){
						bo = true;
						ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(Material.COBBLESTONE, (byte) 0), 0.4f, 0.5f, 0.4f, 0.1f, 100, e.getLocation().clone().add(0, 1.3, 0), 100);
						e.setVelocity(f.getVelocity().multiply(0.5).setY(0.2));
						((Player) e).damage(0.0);
						f.getLocation().getBlock().setType(Material.AIR);
						remove.add(f);
						f.remove();
					}
					}
				if (bo == false){
					if (f.getLocation().clone().subtract(0, 0.5, 0).getBlock().getType() != Material.AIR){
						ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(Material.COBBLESTONE, (byte) 0), 0.4f, 0.5f, 0.4f, 0.1f, 100, f.getLocation().clone().add(0, 1.3, 0), 100);
						if (Game.isBreakable(f.getLocation().clone().subtract(0, 0.5, 0).getBlock().getType())){
						f.getLocation().clone().subtract(0, 0.5, 0).getBlock().setType(Material.AIR);
						}
						f.getLocation().getBlock().setType(Material.AIR);
						f.remove();
						remove.add(f);
					}
				}
			}
			fallings.removeAll(remove);
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void attemptSecond(final Player p){
		if (cooldown2.containsKey(p.getName())){
			p.sendMessage(ChatColor.RED + "Cooldown: " + ChatColor.YELLOW + cooldown2.get(p.getName()) + ChatColor.RED + ".");
			return;
		}
		cooldown2.put(p.getName(), 10.0);
		final List<Location> blocks = new ArrayList<Location>();
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
						if (loc.getBlock().getType() == Material.AIR){
							blocks.add(loc);
						}
					}
				}
			}
		}
		List<Location> up1 = new ArrayList<Location>();
		final List<Location> up2 = new ArrayList<Location>();
		final List<Location> up3 = new ArrayList<Location>();
		final List<Location> up4 = new ArrayList<Location>();
		for (Location loc : blocks){
			if (loc.getBlock().getType() == Material.AIR){
				Random r = new Random();
				int i = r.nextInt(4) + 1;
				switch(i){
				case 1: up1.add(loc);
				break;
				case 2: up3.add(loc);
				break;
				case 3: up2.add(loc);
				break;
				case 4: up4.add(loc);
				}
			}
		}
		final HashMap<Location,Material> Materials1 = new HashMap<Location,Material>();
		final List<FallingBlock> falls = new ArrayList<FallingBlock>();
		for (Location loc : up1){
			for (int i = 1; i < 10; i++){
				Location locu = loc.clone().subtract(0, i, 0);
				if (locu.getBlock().getType() != Material.AIR && Game.isBreakable(locu.getBlock().getType())){
					Materials1.put(loc, locu.getBlock().getType());
					FallingBlock f = locu.getWorld().spawnFallingBlock(locu.clone().add(0, 1, 0), locu.getBlock().getType(), (byte)0);
					f.setVelocity(new Vector(0, loc.distance(locu) * 0.145, 0));
					f.setFallDistance(1000f);
					f.setDropItem(false);
					falls.add(f);
					break;
				}
			}
		}
		Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), new Runnable(){
			public void run(){
				for (FallingBlock f : falls){
					f.teleport(f.getLocation().subtract(0, 500, 0));
				}
				falls.clear();
				for (Location loc : Materials1.keySet()){
					Material m = Materials1.get(loc);
					loc.getBlock().setType(m);
					ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(m, (byte)0), 0.3f, 0.3f, 0.3f, 0.3f, 40, loc, 20);
				}
				for (Player p1 : Bukkit.getOnlinePlayers()){
					p1.playSound(p.getLocation(), Sound.DIG_STONE, 1f, 1f);
				}
				Materials1.clear();
				for (Location loc : up2){
					for (int i = 1; i < 10; i++){
						Location locu = loc.clone().subtract(0, i, 0);
						if (locu.getBlock().getType() != Material.AIR && Game.isBreakable(locu.getBlock().getType())){
							Materials1.put(loc, locu.getBlock().getType());
							FallingBlock f = locu.getWorld().spawnFallingBlock(locu.clone().add(0, 1, 0), locu.getBlock().getType(), (byte)0);
							f.setVelocity(new Vector(0, loc.distance(locu) * 0.145, 0));
							f.setFallDistance(1000f);
							f.setDropItem(false);
							falls.add(f);
							break;
						}
					}
				}
				Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), new Runnable(){
					public void run(){
						for (FallingBlock f : falls){
							f.teleport(f.getLocation().subtract(0, 500, 0));
						}
						falls.clear();
						for (Location loc : Materials1.keySet()){
							Material m = Materials1.get(loc);
							loc.getBlock().setType(m);
							if (m != Material.AIR){
							ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(m, (byte)0), 0.3f, 0.3f, 0.3f, 0.3f, 40, loc, 20);
							}
						}
						for (Player p1 : Bukkit.getOnlinePlayers()){
							p1.playSound(p.getLocation(), Sound.DIG_STONE, 1f, 1f);
						}
						Materials1.clear();
						for (Location loc : up3){
							for (int i = 1; i < 10; i++){
								Location locu = loc.clone().subtract(0, i, 0);
								if (locu.getBlock().getType() != Material.AIR && Game.isBreakable(locu.getBlock().getType())){
									Materials1.put(loc, locu.getBlock().getType());
									FallingBlock f = locu.getWorld().spawnFallingBlock(locu.clone().add(0, 1, 0), locu.getBlock().getType(), (byte)0);
									f.setVelocity(new Vector(0, loc.distance(locu) * 0.145, 0));
									f.setFallDistance(1000f);
									f.setDropItem(false);
									falls.add(f);
									break;
								}
							}
						}
						Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), new Runnable(){
							public void run(){
								for (FallingBlock f : falls){
									f.teleport(f.getLocation().subtract(0, 500, 0));
								}
								falls.clear();
								for (Location loc : Materials1.keySet()){
									Material m = Materials1.get(loc);
									loc.getBlock().setType(m);
									if (m != Material.AIR){
									ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(m, (byte)0), 0.3f, 0.3f, 0.3f, 0.3f, 40, loc, 20);
									}
								}
								for (Player p1 : Bukkit.getOnlinePlayers()){
									p1.playSound(p.getLocation(), Sound.DIG_STONE, 1f, 1f);
								}
								Materials1.clear();
								for (Location loc : up4){
									for (int i = 1; i < 10; i++){
										Location locu = loc.clone().subtract(0, i, 0);
										if (locu.getBlock().getType() != Material.AIR && Game.isBreakable(locu.getBlock().getType())){
											Materials1.put(loc, locu.getBlock().getType());
											FallingBlock f = locu.getWorld().spawnFallingBlock(locu.clone().add(0, 1, 0), locu.getBlock().getType(), (byte)0);
											f.setVelocity(new Vector(0, loc.distance(locu) * 0.145, 0));
											f.setFallDistance(1000f);
											f.setDropItem(false);
											falls.add(f);
											break;
										}
									}
								}
								Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), new Runnable(){
									public void run(){
										for (FallingBlock f : falls){
											f.teleport(f.getLocation().subtract(0, 500, 0));
										}
										falls.clear();
										for (Location loc : Materials1.keySet()){
											Material m = Materials1.get(loc);
											loc.getBlock().setType(m);
											if (m != Material.AIR){
											ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(m, (byte)0), 0.3f, 0.3f, 0.3f, 0.3f, 40, loc, 20);
											}
										}
										for (Player p1 : Bukkit.getOnlinePlayers()){
											p1.playSound(p.getLocation(), Sound.DIG_STONE, 1f, 1f);
										}
										Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), new Runnable(){
											public void run(){
												for (Location loc : blocks){
													if (loc.getBlock().getType() != Material.AIR){
													ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(loc.getBlock().getType(), (byte)0), 0.2f, 0.2f, 0.2f, 0.3f, 14, loc, 20);
													}
													loc.getBlock().setType(Material.AIR);
												}
											}
										}, 4 * 20l);
									}
								}, 6l);
							}
						}, 6l);
					}
				}, 6l);
			}
		}, 6l);
	}
	
	@SuppressWarnings("deprecation")
	public static void attemptThird(final Player p){
		if (cooldown3.containsKey(p.getName())){
			p.sendMessage(ChatColor.RED + "Cooldown: " + ChatColor.YELLOW + cooldown3.get(p.getName()) + ChatColor.RED + ".");
			return;
		}
		cooldown3.put(p.getName(), 5.0);
		p.setVelocity(new Vector(0, 0, 0));
		Location ploc = p.getLocation().clone();
		Vector vector1 = getDirection(ploc, 0, 90);
		Vector vector2 = getDirection(ploc, 180, 90);
		final Location loc1 = ploc.toVector().add(vector1.multiply(0.4)).toLocation(p.getWorld()).add(0, 1.8, 0);
		Location loc2 = ploc.toVector().add(vector2.multiply(0.4)).toLocation(p.getWorld()).add(0, 1.8, 0);
	    Location locu1 = ploc.toVector().add(vector1.multiply(0.2)).toLocation(p.getWorld()).add(0, 1.8, 0);
		Location locu2 = ploc.toVector().add(vector2.multiply(0.2)).toLocation(p.getWorld()).add(0, 1.8, 0);
		List<Location> util = new ArrayList<Location>();
		for (int i = 1; i < 6; i++){
			Location loc3 = loc1.toVector().add(getDirection(ploc, 90, 90).multiply(i)).toLocation(p.getWorld());
			Location loc4 = loc2.toVector().add(getDirection(ploc, 90, 90).multiply(i)).toLocation(p.getWorld());
			Location loc5 = ploc.toVector().add(getDirection(ploc, 90, 90).multiply(i)).toLocation(p.getWorld()).add(0, 1.8, 0);
			Location loc6 = locu1.toVector().add(getDirection(ploc, 90, 90).multiply(i)).toLocation(p.getWorld());
			Location loc7 = locu2.toVector().add(getDirection(ploc, 90, 90).multiply(i)).toLocation(p.getWorld());
			util.add(loc3);
			util.add(loc4);
			util.add(loc5);
			util.add(loc6);
			util.add(loc7);
		}
		List<Location> util2 = new ArrayList<Location>();
		for (Location locutil : util){
			for (int i = 0; i < 7; i++){
				Location utilloc = locutil;
				utilloc.setY(p.getLocation().getY() + 1.8);
				utilloc.subtract(0, i, 0);
				if (utilloc.getBlock().getType() != Material.AIR && Game.isBreakable(utilloc.getBlock().getType()) && !util2.contains(utilloc) && utilloc.clone().add(0, 1, 0).getBlock().getType() == Material.AIR){
					util2.add(utilloc);
					break;
				}
			}
		}
		List<Location> ses1 = new ArrayList<Location>();
		final List<Location> ses2 = new ArrayList<Location>();
		final List<Location> ses3 = new ArrayList<Location>();
		for (Location loc : util2){
			double distance = loc.distance(p.getLocation());
			if (distance < 3){
				ses1.add(loc);
			}else if (distance < 6){
				ses2.add(loc);
			}else if (distance < 100){
				ses3.add(loc);
			}
		}
		final List<FallingBlock> falls = new ArrayList<FallingBlock>();
		for (Player p1 : Bukkit.getOnlinePlayers()){
			p1.playSound(loc1, Sound.DIG_STONE, 1f, 1f);
		}
		final Vector throwback = p.getLocation().getDirection().add(new Vector(0, 0.6, 0)).multiply(0.8);
		for (Location loc : ses1){
			FallingBlock b = p.getWorld().spawnFallingBlock(loc.clone().add(0, 0.2, 0), loc.getBlock().getType(), loc.getBlock().getData());
			b.setVelocity(new Vector(0, 0.18, 0));
			b.setFallDistance(1000f);
			falls.add(b);
			if (loc.getBlock().getType() != Material.AIR){
				ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(loc.getBlock().getType(), loc.getBlock().getData()), 0.3f, 0.3f, 0.3f, 0.2f, 40, loc.clone().add(0, 1.4, 0), 20);
			}
			Random r = new Random();
			int i = r.nextInt(3) + 1;
			if (i == 1 || i == 2){
				loc.getBlock().setType(Material.AIR);
			}
			for (Entity e : b.getNearbyEntities(1.2, 1.2, 1.2)){
				if (e instanceof Player && ((Player)e).getName() != p.getName()){
					((Player) e).damage(0);
					e.setVelocity(throwback);
				}
			}
		}
		Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), new Runnable(){
			public void run(){
				for (FallingBlock f : falls){
					f.teleport(f.getLocation().subtract(0, 500, 0));
				}
				falls.clear();
				for (Player p1 : Bukkit.getOnlinePlayers()){
					p1.playSound(loc1, Sound.DIG_STONE, 1f, 1f);
				}
				for (Location loc : ses2){
					FallingBlock b = p.getWorld().spawnFallingBlock(loc.clone().add(0, 0.2, 0), loc.getBlock().getType(), loc.getBlock().getData());
					b.setVelocity(new Vector(0, 0.18, 0));
					b.setFallDistance(1000f);
					if (loc.getBlock().getType() != Material.AIR){
						ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(loc.getBlock().getType(), loc.getBlock().getData()), 0.3f, 0.3f, 0.3f, 0.2f, 40, loc.clone().add(0, 1.4, 0), 20);
					}					
					Random r = new Random();
					int i = r.nextInt(3) + 1;
					if (i == 1 || i == 2){
						loc.getBlock().setType(Material.AIR);
					}
					falls.add(b);
					for (Entity e : b.getNearbyEntities(1.2, 1.2, 1.2)){
						if (e instanceof Player && ((Player)e).getName() != p.getName()){
							((Player) e).damage(0);
							e.setVelocity(throwback);
						}
					}
				}
				Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), new Runnable(){
					public void run(){
						for (FallingBlock f : falls){
							f.teleport(f.getLocation().subtract(0, 500, 0));
						}
						for (Player p1 : Bukkit.getOnlinePlayers()){
							p1.playSound(loc1, Sound.DIG_STONE, 1f, 1f);
						}
						falls.clear();
						for (Location loc : ses3){
							FallingBlock b = p.getWorld().spawnFallingBlock(loc.clone().add(0, 0.2, 0), loc.getBlock().getType(), loc.getBlock().getData());
							b.setVelocity(new Vector(0, 0.18, 0));
							b.setFallDistance(1000f);
							if (loc.getBlock().getType() != Material.AIR){
								ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(loc.getBlock().getType(), loc.getBlock().getData()), 0.3f, 0.3f, 0.3f, 0.2f, 40, loc.clone().add(0, 1.4, 0), 20);
							}							
							Random r = new Random();
							int i = r.nextInt(3) + 1;
							if (i == 1 || i == 2){
								loc.getBlock().setType(Material.AIR);
							}
							falls.add(b);
							for (Entity e : b.getNearbyEntities(1.2, 1.2, 1.2)){
								if (e instanceof Player && ((Player)e).getName() != p.getName()){
									((Player) e).damage(0);
									e.setVelocity(throwback);
								}
							}
						}
						Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), new Runnable(){
							public void run(){
								for (FallingBlock f : falls){
									f.teleport(f.getLocation().subtract(0, 500, 0));
								}
								falls.clear();
							}
						}, 3l);
					}
				}, 3l);
			}
		}, 3l);
	}
	
	public static void giveItems(Player p){
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "Punch someone with this to knock them");
		lore.add(ChatColor.GRAY + "away. Right-Click to throw a stone that");
		lore.add(ChatColor.GRAY + "can break the ground.");
		ItemStack first = ItemAPI.getItem(Material.STICK, ChatColor.GREEN + "Knockback Stick - Stone Throw", lore, 1);
		p.getInventory().addItem(first);
		lore.clear();
		lore.add(ChatColor.GRAY + "Right-Click to create a defense sphere around");
		lore.add(ChatColor.GRAY + "you.");
		p.getInventory().addItem(ItemAPI.getItem(Material.BLAZE_ROD, ChatColor.GREEN + "Defense Sphere", lore, 1));
		lore.clear();
		lore.add(ChatColor.GRAY + "Right-Click to punch the ground and launch");
		lore.add(ChatColor.GRAY + "a seismic wave.");
		p.getInventory().addItem(ItemAPI.getItem(Material.DIAMOND_HOE, ChatColor.GREEN + "Seismic Wave", lore, 1));
		p.updateInventory();
	}
	
	public static Vector getDirection(Location loc, int yawadd, int pitchadd){
		double pitch = ((0 + pitchadd) * Math.PI) / 180;
		double yaw  = ((loc.getYaw() + yawadd)  * Math.PI) / 180;
		double x = Math.sin(pitch) * Math.cos(yaw);
		double z = Math.sin(pitch) * Math.sin(yaw);
		double y = Math.cos(pitch);
		return new Vector(x, y, z).multiply(1.5);
	}
	
}
