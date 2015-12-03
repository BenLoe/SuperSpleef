package me.BenLoe.SuperSpleef;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.Prison.Main.Traits.SpeedTrait;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import me.BenLoe.SuperSpleef.Classes.Bomber;
import me.BenLoe.SuperSpleef.Classes.Scout;
import me.BenLoe.SuperSpleef.Game.GameState;

public class GameManager {

	public static int time = 0;
	public static List<Block> regenerate = new ArrayList<Block>();
	public static int regeni = 20;
	
	public static void setNewRegen(){
		if (regeni == 20){ regeni = 30;return;}
		if (regeni == 30){ regeni = 40;return;}
		if (regeni == 40){ regeni = 20;return;}
	}
	
	@SuppressWarnings("deprecation")
	public static void manage(){
		GameState gs = Game.getGameState();
		int inQueue = Game.inqueue.size();
		if (gs == GameState.WAITING){
			if (inQueue >= 2){
				Game.gs = GameState.COUNTDOWN;
				time = 15;
				Game.sendToAllInQueue(Game.tag + "§eGame starting in §b15 §eseconds.");
			}
		}
		if (gs == GameState.COUNTDOWN){
			if (inQueue < 2){
				Game.gs = GameState.WAITING;
				Game.sendToAllInQueue(Game.tag + "§eNot enough players to start, waiting for more players.");
			}else{
				int newtime = time - 1;
				if (newtime == 0){
					Game.startGame();
					time = 10;
					Game.gs = GameState.WARMUP;
				}else{
					if (newtime == 5 || newtime == 10){
						Game.sendToAllInQueue(Game.tag + "§eStarting in §b"+ newtime + " §eseconds.");
						if (newtime == 5){
							Game.soundToAllInQueue(Sound.CLICK);
						}
					}
					if (newtime == 2 || newtime == 3 || newtime == 4){
						Game.sendToAllInQueue(Game.tag + "§eStarting in §c" + newtime + " §eseconds."	);
						Game.soundToAllInQueue(Sound.CLICK);
					}
					if (newtime == 1){
						Game.sendToAllInQueue(Game.tag + "§eStarting in §c1 §esecond.");
						Game.soundToAllInQueue(Sound.CLICK);
					}
					time = newtime;
				}
			}
		}
		if (gs == GameState.WARMUP){
			int newtime = time - 1;
			if (newtime == 0){
				Game.gs = GameState.INGAME;
				Game.trueStartGame();
				time = 300;
				Game.sendToAll(Game.tag + "§aGood Luck!");
			}else{
				if (newtime == 5 || newtime == 4 || newtime == 3 || newtime == 2 || newtime == 1){
					Game.sendToAll(Game.tag + "§eReceiving kit items in §b" + newtime + " §eseconds.");
				}
				time = newtime;
			}
		}
		if (gs == GameState.INGAME){
			int newtime = time - 1;
				if (Game.ingame.size() == 1){
					time = 10;
					Game.gs = GameState.WIN;
					Game.Win(Bukkit.getPlayer(Game.ingame.get(0)));		
				}else
				if (Game.ingame.size() == 0){
					Game.gs = GameState.WIN;
					time = 10;
					Game.Win(null);
				}else{
					time = newtime;
				}
				
		}
		if (gs == GameState.WIN){
			int newtime = time - 1;
			if (newtime == 0){
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
					SpeedTrait.setCorrectSpeed(p);
					Game.watching.add(p.getName());
				}
				Game.ingame.clear();
				Bomber.tnts.clear();
				Bomber.ultimate.clear();
				Scout.potionItems.clear();
				Scout.potionItems2.clear();
				Game.invs.clear();
				Game.armors.clear();
				Game.gs = GameState.WAITING;
			}else{
				if (newtime == 9 || newtime == 5){
					Firework(Game.getLocation("Spawn1"), Color.AQUA, 0);
					Firework(Game.getLocation("Spawn2"), Color.YELLOW, 0);
					Firework(Game.getLocation("Spawn3"), Color.AQUA, 0);
					Firework(Game.getLocation("Spawn4"), Color.YELLOW, 0);
					Firework(Game.getLocation("Spawn5"), Color.AQUA, 0);
					Firework(Game.getLocation("Spawn6"), Color.YELLOW, 0);
					Firework(Game.getLocation("Spawn7"), Color.AQUA, 0);
					Firework(Game.getLocation("Spawn8"), Color.YELLOW, 0);
					if (!Game.ingame.isEmpty()){
					Firework(Bukkit.getPlayer(Game.ingame.get(0)).getLocation().add(0, 2, 0), Color.TEAL, -1);
					}
				}
				if (newtime == 7 || newtime == 3){
					Firework(Game.getLocation("Spawn1"), Color.BLUE, 0);
					Firework(Game.getLocation("Spawn2"), Color.FUCHSIA, 0);
					Firework(Game.getLocation("Spawn3"), Color.BLUE, 0);
					Firework(Game.getLocation("Spawn4"), Color.FUCHSIA, 0);
					Firework(Game.getLocation("Spawn5"), Color.BLUE, 0);
					Firework(Game.getLocation("Spawn6"), Color.FUCHSIA, 0);
					Firework(Game.getLocation("Spawn7"), Color.BLUE, 0);
					Firework(Game.getLocation("Spawn8"), Color.FUCHSIA, 0);
					if (!Game.ingame.isEmpty()){
					Firework(Bukkit.getPlayer(Game.ingame.get(0)).getLocation().add(0, 2, 0), Color.PURPLE, -1);
					}
				}
				if (newtime == 9){
					setNewRegen();
					Location point1 = Game.getLocation("Point1");
					Location point2 = Game.getLocation("Point2");
					int x1 = point2.getBlockX();
					int y1 = point2.getBlockY();
					int z1 = point2.getBlockZ();
					//MAXIMUM
					int x2 = point1.getBlockX();
					int y2 = point1.getBlockY();
					int z2 = point1.getBlockZ();
					for (int x = x1; x <= x2; x++) {
					    for (int y = y1; y <= y2; y++) {
					        for (int z = z1; z <= z2; z++) {
					           Location end = point1.getWorld().getBlockAt(x, y, z).getLocation();
					           regenerate.add(end.getBlock());
					        }
					    }
					}
				}else if (newtime != 1){
					if (newtime == 2){
						for (int i = 0; i < regenerate.size(); i++){
							Block b = regenerate.get(i);
							Block ref = b.getLocation().clone().subtract(0, regeni, 0).getBlock();
							if (b.getType() != ref.getType() || b.getData() != ref.getData()){
							b.setType(ref.getType());
							b.setData(ref.getData());
							}
						}
						regenerate.clear();
					}else{
						int i = Math.round(regenerate.size() / 5);
						for (int i1 = 0; i1 < i; i1++){
							Random r = new Random();
							Block b = regenerate.get(r.nextInt(regenerate.size()));
							Block ref = b.getLocation().clone().subtract(0, regeni, 0).getBlock();
							if (b.getType() != ref.getType() || b.getData() != ref.getData()){
							b.setType(ref.getType());
							b.setData(ref.getData());
							}
							regenerate.remove(b);
						}
					}
				}
				time = newtime;
			}
		}
		Location loc = Game.getLocation("Sign");
		Sign s = (Sign) loc.getBlock().getState();
		s.setLine(3, "§eIn queue: §b" + inQueue);
		s.update();
	}
	
	public static void Firework(Location loc, Color c, int power){
		Firework fw = (Firework) loc.getWorld().spawn(loc, Firework.class);
		FireworkEffect effect = FireworkEffect.builder().trail(true).flicker(false).withColor(c).with(Type.BALL).build();
		FireworkMeta fwm = fw.getFireworkMeta();
		fwm.clearEffects();
		fwm.addEffect(effect);
		Field f1;
		try {
			f1 = fwm.getClass().getDeclaredField("power");
			f1.setAccessible(true);
			try {
				f1.set(fwm, power);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		fw.setFireworkMeta(fwm);
		}
}
