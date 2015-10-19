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
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class BeastTamer {
	public static HashMap<String,Double> cooldown1 = new HashMap<String,Double>();
	public static HashMap<String,Double> cooldown2 = new HashMap<String,Double>();
	public static HashMap<String,Double> cooldown3 = new HashMap<String,Double>();
	public static List<UUID> creeps = new ArrayList<UUID>();
	public static List<UUID> pigs = new ArrayList<UUID>();

	public static void attemptFirst(Player p){
		if (cooldown1.containsKey(p.getName())){
			p.sendMessage(ChatColor.RED + "Cooldown: " + ChatColor.YELLOW + cooldown1.get(p.getName()) + ChatColor.RED + ".");
			return;
		}
		cooldown1.put(p.getName(), 2.0);
		Location location = p.getLocation().toVector().add(p.getLocation().getDirection().multiply(1.2)).toLocation(p.getWorld()).clone().add(0, 1.8, 0);
		final Pig pig = (Pig) p.getWorld().spawnEntity(location, EntityType.PIG);
		pig.setVelocity(p.getLocation().getDirection().multiply(1.8));
		Game.playSound(Sound.PIG_DEATH, p.getLocation(), 1f, 0.5f);
		Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), new Runnable(){
			public void run(){
				pigs.add(pig.getUniqueId());
			}
		}, 3l);
	}
	
	public static void checkFirst(){
		for (Pig pig : Bukkit.getWorld("PrisonMap").getEntitiesByClass(Pig.class)){
			if (pigs.contains(pig.getUniqueId())){
				boolean bo = true;
				for (Entity e : pig.getNearbyEntities(1.1, 1, 1.1)){
					if (e instanceof Player){
						bo = false;
						((Player) e).damage(0.0);
						e.setVelocity(pig.getVelocity().setY(0.8));
						ParticleEffect.FIREWORKS_SPARK.display(0.6f, 0.6f, 0.6f, 0.1f, 50, pig.getLocation(), 100);
						ParticleEffect.EXPLOSION_NORMAL.display(0.6f, 0.6f, 0.6f, 0.1f, 50, pig.getLocation(), 100);
						pigs.remove(pig.getUniqueId());
						pig.teleport(pig.getLocation().clone().subtract(0, 500, 0));
					}
				}
				if (bo){
					if (pig.getLocation().clone().subtract(0, 0.5, 0).getBlock().getType() != Material.AIR){
						if (Game.isBreakable(pig.getLocation().clone().subtract(0, 0.5, 0).getBlock().getType())){
						pig.getLocation().clone().subtract(0, 0.5, 0).getBlock().setType(Material.AIR);
						}
						ParticleEffect.FIREWORKS_SPARK.display(0.6f, 0.6f, 0.6f, 0.1f, 50, pig.getLocation(), 100);
						ParticleEffect.EXPLOSION_NORMAL.display(0.6f, 0.6f, 0.6f, 0.1f, 50, pig.getLocation(), 100);
						pigs.remove(pig.getUniqueId());
						pig.teleport(pig.getLocation().subtract(0, 500, 0));
					}
				}
				Location location = pig.getLocation().toVector().add(pig.getLocation().clone().getDirection().multiply(-1.7)).toLocation(pig.getWorld()).clone().add(0, 0.4, 0);
				ParticleEffect.FIREWORKS_SPARK.display(0.01f, 0.01f,01f, 0.01f, 1, location, 100);
			}
		}
	}
	
	public static void attemptSecond(Player p){
		if (cooldown2.containsKey(p.getName())){
			p.sendMessage(ChatColor.RED + "Cooldown: " + ChatColor.YELLOW + cooldown2.get(p.getName()) + ChatColor.RED + ".");
			return;
		}
		cooldown2.put(p.getName(), 8.0);
		final Wolf wolf1 = (Wolf) p.getWorld().spawnEntity(p.getLocation(), EntityType.WOLF);
		final Wolf wolf2 = (Wolf) p.getWorld().spawnEntity(p.getLocation(), EntityType.WOLF);
		final Wolf wolf3 = (Wolf) p.getWorld().spawnEntity(p.getLocation(), EntityType.WOLF);
		final Wolf wolf4 = (Wolf) p.getWorld().spawnEntity(p.getLocation(), EntityType.WOLF);
		wolf1.setAngry(true);
		wolf2.setAngry(true);
		wolf3.setAngry(true);
		wolf4.setAngry(true);
		wolf1.setOwner(p);
		wolf2.setOwner(p);
		wolf3.setOwner(p);
		wolf4.setOwner(p);
		wolf1.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10 * 20, 4));
		wolf2.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10 * 20, 4));
		wolf3.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10 * 20, 4));
		wolf4.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10 * 20, 4));
		for (Entity e : p.getNearbyEntities(20, 20, 20)){
			if (e instanceof Player && Game.playerInGame((Player) e)){
				wolf1.setTarget((LivingEntity) e);
				wolf2.setTarget((LivingEntity) e);
				wolf3.setTarget((LivingEntity) e);
				wolf4.setTarget((LivingEntity) e);
			}
		}
		Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), new Runnable(){
			public void run(){
				ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(Material.REDSTONE_BLOCK, (byte)0), 0.3f, 0.3f, 0.3f, 0.3f, 20, wolf1.getLocation(), 100);
				ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(Material.REDSTONE_BLOCK, (byte)0), 0.3f, 0.3f, 0.3f, 0.3f, 20, wolf2.getLocation(), 100);
				ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(Material.REDSTONE_BLOCK, (byte)0), 0.3f, 0.3f, 0.3f, 0.3f, 20, wolf3.getLocation(), 100);
				ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(Material.REDSTONE_BLOCK, (byte)0), 0.3f, 0.3f, 0.3f, 0.3f, 20, wolf4.getLocation(), 100);
				wolf1.teleport(wolf4.getLocation().clone().subtract(0, 500, 0));
				wolf2.teleport(wolf4.getLocation().clone().subtract(0, 500, 0));
				wolf3.teleport(wolf4.getLocation().clone().subtract(0, 500, 0));
				wolf4.teleport(wolf4.getLocation().clone().subtract(0, 500, 0));
			}
		}, 5 * 20l);
	}
	
	public static void attemptThird(Player p){
		if (cooldown3.containsKey(p.getName())){
			p.sendMessage(ChatColor.RED + "Cooldown: " + ChatColor.YELLOW + cooldown3.get(p.getName()) + ChatColor.RED + ".");
			return;
		}
		cooldown3.put(p.getName(), 7.0);
		final Creeper creep1 = (Creeper) p.getWorld().spawnEntity(p.getLocation().clone().add(0, 2, 0), EntityType.CREEPER);
		final Creeper creep2 = (Creeper) p.getWorld().spawnEntity(p.getLocation().clone().add(0, 2, 0), EntityType.CREEPER);
		final Creeper creep3 = (Creeper) p.getWorld().spawnEntity(p.getLocation().clone().add(0, 2, 0), EntityType.CREEPER);
		final Creeper creep4 = (Creeper) p.getWorld().spawnEntity(p.getLocation().clone().add(0, 2, 0), EntityType.CREEPER);
		final Creeper creep5 = (Creeper) p.getWorld().spawnEntity(p.getLocation().clone().add(0, 2, 0), EntityType.CREEPER);
		final Creeper creep6 = (Creeper) p.getWorld().spawnEntity(p.getLocation().clone().add(0, 2, 0), EntityType.CREEPER);
		double x = (-0.7 + (0.7 - -0.7) * new Random().nextDouble());
		double y = (0.3 + (0.9 - -0.3) * new Random().nextDouble());
		double z = (-0.7 + (0.7 - -0.7) * new Random().nextDouble());
		double x1 = (-0.7 + (0.7 - -0.7) * new Random().nextDouble());
		double y1 = (0.3 + (0.9 - -0.3) * new Random().nextDouble());
		double z1 = (-0.7 + (0.7 - -0.7) * new Random().nextDouble());
		double x2 = (-0.7 + (0.7 - -0.7) * new Random().nextDouble());
		double y2 = (0.3 + (0.9 - -0.3) * new Random().nextDouble());
		double z2 = (-0.7 + (0.7 - -0.7) * new Random().nextDouble());
		double x3 = (-0.7 + (0.7 - -0.7) * new Random().nextDouble());
		double y3 = (0.3 + (0.9 - -0.3) * new Random().nextDouble());
		double z3 = (-0.7 + (0.7 - -0.7) * new Random().nextDouble());
		double x4 = (-0.7 + (0.7 - -0.7) * new Random().nextDouble());
		double y4 = (0.3 + (0.9 - -0.3) * new Random().nextDouble());
		double z4 = (-0.7 + (0.7 - -0.7) * new Random().nextDouble());
		double x5 = (-0.7 + (0.7 - -0.7) * new Random().nextDouble());
		double y5 = (0.3 + (0.9 - -0.3) * new Random().nextDouble());
		double z5 = (-0.7 + (0.7 - -0.7) * new Random().nextDouble());
		creep1.setVelocity(new Vector(x, y, z));
		creep2.setVelocity(new Vector(x1, y1, z1));
		creep3.setVelocity(new Vector(x2, y2, z2));
		creep4.setVelocity(new Vector(x3, y3, z3));
		creep5.setVelocity(new Vector(x4, y4, z4));
		creep6.setVelocity(new Vector(x5, y5, z5));
		creep1.setPowered(true);
		creep2.setPowered(true);
		creep3.setPowered(true);
		creep4.setPowered(true);
		creep5.setPowered(true);
		creep6.setPowered(true);
		creeps.add(creep1.getUniqueId());
		creeps.add(creep2.getUniqueId());
		creeps.add(creep3.getUniqueId());
		creeps.add(creep4.getUniqueId());
		creeps.add(creep5.getUniqueId());
		creeps.add(creep6.getUniqueId());
		Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), new Runnable(){
			public void run(){
				ParticleEffect.EXPLOSION_LARGE.display(0.3f, 1f, 0.3f, 0.3f, 10, creep1.getLocation().clone().add(0, 0.5, 0), 100);
				ParticleEffect.EXPLOSION_LARGE.display(0.3f, 1f, 0.3f, 0.3f, 10, creep2.getLocation().clone().add(0, 0.5, 0), 100);
				ParticleEffect.EXPLOSION_LARGE.display(0.3f, 1f, 0.3f, 0.3f, 10, creep3.getLocation().clone().add(0, 0.5, 0), 100);
				ParticleEffect.EXPLOSION_LARGE.display(0.3f, 1f, 0.3f, 0.3f, 10, creep4.getLocation().clone().add(0, 0.5, 0), 100);
				ParticleEffect.EXPLOSION_LARGE.display(0.3f, 1f, 0.3f, 0.3f, 10, creep5.getLocation().clone().add(0, 0.5, 0), 100);
				ParticleEffect.EXPLOSION_LARGE.display(0.3f, 1f, 0.3f, 0.3f, 10, creep6.getLocation().clone().add(0, 0.5, 0), 100);
				Game.playSound(Sound.EXPLODE, creep1.getLocation(), 1f, 0.2f);
				Game.playSound(Sound.EXPLODE, creep2.getLocation(), 1f, 0.2f);
				Game.playSound(Sound.EXPLODE, creep3.getLocation(), 1f, 0.2f);
				Game.playSound(Sound.EXPLODE, creep4.getLocation(), 1f, 0.2f);
				Game.playSound(Sound.EXPLODE, creep5.getLocation(), 1f, 0.2f);
				Game.playSound(Sound.EXPLODE, creep6.getLocation(), 1f, 0.2f);
				
				if (creep1.getLocation().getBlockY() >= 45){
				creep1.getLocation().clone().subtract(0, 1, 0).getBlock().setType(Material.AIR);
				creep1.getLocation().clone().subtract(1, 1, 0).getBlock().setType(Material.AIR);
				creep1.getLocation().clone().subtract(0, 1, 1).getBlock().setType(Material.AIR);
				creep1.getLocation().clone().subtract(-1, 1, 0).getBlock().setType(Material.AIR);
				creep1.getLocation().clone().subtract(0, 1, -1).getBlock().setType(Material.AIR);
				}
				if (creep2.getLocation().getBlockY() >= 45){
				creep2.getLocation().clone().subtract(0, 1, 0).getBlock().setType(Material.AIR);
				creep2.getLocation().clone().subtract(1, 1, 0).getBlock().setType(Material.AIR);
				creep2.getLocation().clone().subtract(0, 1, 1).getBlock().setType(Material.AIR);
				creep2.getLocation().clone().subtract(-1, 1, 0).getBlock().setType(Material.AIR);
				creep2.getLocation().clone().subtract(0, 1, -1).getBlock().setType(Material.AIR);
				}
				if (creep3.getLocation().getBlockY() >= 45){
				creep3.getLocation().clone().subtract(0, 1, 0).getBlock().setType(Material.AIR);
				creep3.getLocation().clone().subtract(1, 1, 0).getBlock().setType(Material.AIR);
				creep3.getLocation().clone().subtract(0, 1, 1).getBlock().setType(Material.AIR);
				creep3.getLocation().clone().subtract(-1, 1, 0).getBlock().setType(Material.AIR);
				creep3.getLocation().clone().subtract(0, 1, -1).getBlock().setType(Material.AIR);
				}
				if (creep4.getLocation().getBlockY() >= 45){
				creep4.getLocation().clone().subtract(0, 1, 0).getBlock().setType(Material.AIR);
				creep4.getLocation().clone().subtract(1, 1, 0).getBlock().setType(Material.AIR);
				creep4.getLocation().clone().subtract(0, 1, 1).getBlock().setType(Material.AIR);
				creep4.getLocation().clone().subtract(-1, 1, 0).getBlock().setType(Material.AIR);
				creep4.getLocation().clone().subtract(0, 1, -1).getBlock().setType(Material.AIR);
				}
				if (creep5.getLocation().getBlockY() >= 45){
					creep5.getLocation().clone().subtract(0, 1, 0).getBlock().setType(Material.AIR);
					creep5.getLocation().clone().subtract(1, 1, 0).getBlock().setType(Material.AIR);
					creep5.getLocation().clone().subtract(0, 1, 1).getBlock().setType(Material.AIR);
					creep5.getLocation().clone().subtract(-1, 1, 0).getBlock().setType(Material.AIR);
					creep5.getLocation().clone().subtract(0, 1, -1).getBlock().setType(Material.AIR);
					}
				if (creep6.getLocation().getBlockY() >= 45){
					creep6.getLocation().clone().subtract(0, 1, 0).getBlock().setType(Material.AIR);
					creep6.getLocation().clone().subtract(1, 1, 0).getBlock().setType(Material.AIR);
					creep6.getLocation().clone().subtract(0, 1, 1).getBlock().setType(Material.AIR);
					creep6.getLocation().clone().subtract(-1, 1, 0).getBlock().setType(Material.AIR);
					creep6.getLocation().clone().subtract(0, 1, -1).getBlock().setType(Material.AIR);
					}
				creep1.teleport(creep4.getLocation().clone().subtract(0, 500, 0));
				creep2.teleport(creep4.getLocation().clone().subtract(0, 500, 0));
				creep3.teleport(creep4.getLocation().clone().subtract(0, 500, 0));
				creep4.teleport(creep4.getLocation().clone().subtract(0, 500, 0));
				creep5.teleport(creep4.getLocation().clone().subtract(0, 500, 0));
				creep6.teleport(creep4.getLocation().clone().subtract(0, 500, 0));
				creeps.remove(creep1.getUniqueId());
				creeps.remove(creep2.getUniqueId());
				creeps.remove(creep3.getUniqueId());
				creeps.remove(creep4.getUniqueId());
				creeps.remove(creep5.getUniqueId());
				creeps.remove(creep6.getUniqueId());
			}
		}, 3 * 20l);
		
	}
	
	public static void giveItems(Player p){
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "Punch someone with this to knock them");
		lore.add(ChatColor.GRAY + "away. Right-Click to launch a pig that");
		lore.add(ChatColor.GRAY + "can break the ground.");
		ItemStack first = ItemAPI.getItem(Material.STICK, ChatColor.GREEN + "Knockback Stick - Pig Gun", lore, 1);
		p.getInventory().addItem(first);
		lore.clear();
		lore.add(ChatColor.GRAY + "Right-Click to spawn an army of wolf that");
		lore.add(ChatColor.GRAY + "attacks the nearest player.");
		p.getInventory().addItem(ItemAPI.getItem(Material.BLAZE_ROD, ChatColor.GREEN + "Wolf Attack", lore, 1));
		lore.clear();
		lore.add(ChatColor.GRAY + "Right-Click to shoot creepers out of your head, exploding");
		lore.add(ChatColor.GRAY + "blocks.");
		p.getInventory().addItem(ItemAPI.getItem(Material.DIAMOND_HOE, ChatColor.GREEN + "Creeper Mahem", lore, 1));
		p.updateInventory();
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
	
}
