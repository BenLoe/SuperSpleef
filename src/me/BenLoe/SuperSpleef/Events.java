package me.BenLoe.SuperSpleef;

import java.util.List;

import me.BenLoe.SuperSpleef.Classes.BeastTamer;
import me.BenLoe.SuperSpleef.Classes.Bomber;
import me.BenLoe.SuperSpleef.Classes.KitType;
import me.BenLoe.SuperSpleef.Classes.Scout;
import me.BenLoe.SuperSpleef.Classes.Tank;
import me.BenLoe.SuperSpleef.Game.GameState;
import me.BenLoe.SuperSpleef.Menu.KitMenu;
import me.BenLoe.SuperSpleef.Menu.MenuItem;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;

public class Events implements Listener {

	public static Main plugin;
	public Events(Main instance){
		plugin = instance;
	}

	@EventHandler
	public void inventoryChange(InventoryClickEvent event){
		Player p = (Player) event.getWhoClicked();
		if (KitMenu.ininv.contains(p.getName())){
			if (MenuItem.wasAItem(event.getRawSlot())){
				event.setCancelled(true);
				MenuItem.getItemFromSlot(event.getRawSlot()).wasClicked(p);
			}
		}
	}
	
	@EventHandler
	public void closeInventory(InventoryCloseEvent event){
		if (KitMenu.ininv.contains(event.getPlayer().getName())){
			KitMenu.ininv.remove(event.getPlayer().getName());
		}
	}
	
	@EventHandler
	public void npcClick(NPCRightClickEvent event){
		int id = event.getNPC().getId();
		if (id == 151){
			KitType.SCOUT.setKit(event.getClicker());
			event.getClicker().sendMessage(ChatColor.YELLOW + "Super Spleef kit set to: " + ChatColor.GREEN + "Scout");
		}
		if (id == 152){
			KitType.BOMBER.setKit(event.getClicker());
			event.getClicker().sendMessage(ChatColor.YELLOW + "Super Spleef kit set to: " + ChatColor.GREEN + "Bomber");
		}
		if (id == 153){	
			if (KitType.TANK.ownsKit(event.getClicker())){
				KitType.TANK.setKit(event.getClicker());
				event.getClicker().sendMessage(ChatColor.YELLOW + "Super Spleef kit set to: " + ChatColor.AQUA + "Tank");
			}else{
			event.getClicker().sendMessage(ChatColor.RED + "You do not own this kit. Buy it at the kit buyer.");
			}
		}
		if (id == 154){
			if (KitType.BEASTTAMER.ownsKit(event.getClicker())){
				KitType.BEASTTAMER.setKit(event.getClicker());
				event.getClicker().sendMessage(ChatColor.YELLOW + "Super Spleef kit set to: " + ChatColor.AQUA + "BeastTamer");
			}else{
			event.getClicker().sendMessage(ChatColor.RED + "You do not own this kit. Buy it at the kit buyer.");
			}
		}
		if (id == 155){
			KitMenu.open(event.getClicker());
		}
	}
	
	@EventHandler
	public void signCreate(SignChangeEvent event){
		Player p = event.getPlayer();
		if (p.isOp() && event.getLine(0).equalsIgnoreCase("[ss queue]")){
			Game.setLocation("Sign", event.getBlock().getLocation());
			event.setLine(0, "§9§l[Super Spleef]");
			event.setLine(1, "Click to join");
			event.setLine(2, "the queue.");
			event.setLine(3, "§eIn queue: §b0");
			Sign s = (Sign) event.getBlock().getState();
			s.update();
		}
	}
	
	@EventHandler
	public void interact(PlayerInteractEvent event){
		Player p = event.getPlayer();
		KitType kt = KitType.getKit(p);
		if (Game.playerInGame(p) && Game.getGameState() == GameState.INGAME && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)){
			if (p.getInventory().getHeldItemSlot() == 0){
				switch(kt){
				case SCOUT:
					Scout.attemptFirst(p);
					break;
				case BEASTTAMER:
					BeastTamer.attemptFirst(p);
					break;
				case BOMBER:
					Bomber.attemptFirst(p);
					break;
				case TANK:
					Tank.attemptFirst(p);
					break;
				}
			}
			if (p.getInventory().getHeldItemSlot() == 1){
				switch(kt){
				case SCOUT:
					Scout.attemptSecond(p);
					break;
				case BEASTTAMER:
					BeastTamer.attemptSecond(p);
					break;
				case BOMBER:
					Bomber.attemptSecond(p);
					break;
				case TANK:
					Tank.attemptSecond(p);
					break;
				}
			}
			if (p.getInventory().getHeldItemSlot() == 2 && p.getInventory().getItemInHand() != null){
				switch(kt){
				case SCOUT:
					Scout.attemptThird(p);
					break;
				case BEASTTAMER:
					BeastTamer.attemptThird(p);
					break;
				case BOMBER:
					Bomber.attemptThird(p);
					break;
				case TANK:
					Tank.attemptThird(p);
					break;
				}
			}
		}
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Location loc = event.getClickedBlock().getLocation();
			Location loc2 = Game.getLocation("Sign");
			if (loc.getBlockX() == loc2.getBlockX() && loc.getBlockY() == loc2.getBlockY() && loc.getBlockZ() == loc2.getBlockZ()){
				Game.addToQueue(p);
				return;
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@EventHandler
	public void playerMove(PlayerMoveEvent event){
		Player p = event.getPlayer();
		if (Game.playerInGame(p)){
			if (p.getLocation().getBlockY() <= 44){
				if (Game.getGameState() == GameState.INGAME){
				Game.Die(p);
				}else if (Game.getGameState() == GameState.WARMUP){
					p.teleport(Game.getLocation("Spawn1"));
				}
			}
		}
		if (!Game.watching.contains(p.getName())){
			List<Vector> vectors = (List<Vector>) Files.getDataFile().getList("Watching");
			for (Vector v : vectors){
				if (p.getLocation().getBlock().getLocation().getBlockX() == v.getBlockX() && p.getLocation().getBlock().getLocation().getBlockZ() == v.getBlockZ()){
					Game.watching.add(p.getName());
					break;
				}
			}
		}else{
			if (p.getLocation().clone().subtract(0, 1, 0).getBlock().getType() != Material.SMOOTH_BRICK && p.getLocation().clone().subtract(0, 3, 0).getBlock().getType() != Material.SMOOTH_BRICK && p.getLocation().clone().subtract(0, 2, 0).getBlock().getType() != Material.SMOOTH_BRICK){
				Game.watching.remove(p.getName());
				if (Game.inqueue.contains(p.getName())){
					p.sendMessage(Game.tag + ChatColor.YELLOW + "You left the spectating area, so you were removed from the queue.");
					Game.inqueue.remove(p.getName());
				}
			}
		}
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void playerHitPlayer(EntityDamageByEntityEvent event){
		if (event.getEntity() instanceof Player && event.getDamager() instanceof Player){
			Player p = (Player) event.getEntity();
			Player damager = (Player) event.getDamager();
			if (Game.playerInGame(p)){
				if (damager.getInventory().getHeldItemSlot() == 0){
					event.setCancelled(false);
					p.damage(0.0);
					event.setDamage(0.0);
					p.setHealth(20.0);	
				}else{
					event.setCancelled(true);
				}
			}
		}
		if (event.getDamager() instanceof Player){
			if (event.getEntity() instanceof Wolf){
				event.setCancelled(true);
			}
			if (event.getEntity() instanceof Pig && BeastTamer.pigs.contains(event.getEntity().getUniqueId())){
				event.setCancelled(true);
			}
			if (event.getEntity() instanceof Creeper && BeastTamer.creeps.contains(event.getEntity().getUniqueId())){
				event.setCancelled(true);
			}
		}
		if (event.getEntity() instanceof Player){
			if (event.getDamager() instanceof Wolf){
				event.setCancelled(false);
				Player p = (Player) event.getEntity();
				p.damage(0.0);
				event.setDamage(0.0);
				p.setHealth(20.0);
			}
		}
	}
	
	@EventHandler
	public void entityDamge(EntityDamageEvent event){
		if (event.getEntity() instanceof Player){
			Player p = (Player) event.getEntity();
			if (Game.playerInGame(p)){
				if (event.getCause() == DamageCause.LAVA || event.getCause() == DamageCause.FALL){
					event.setCancelled(true);
				}
			}
		}
		if (event.getEntity() instanceof Creeper){
			if (BeastTamer.creeps.contains(event.getEntity().getUniqueId())){
				event.setCancelled(true);
			}
		}
		if (event.getEntity() instanceof Pig){
			if (BeastTamer.pigs.contains(event.getEntity().getUniqueId())){
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void entityTarget(EntityTargetEvent event){
		if (BeastTamer.creeps.contains(event.getEntity().getUniqueId())){
			event.setCancelled(true);
			((Creeper) event.getEntity()).setTarget(null);
		}
	}
	
	@EventHandler
	public void playerLeave(PlayerQuitEvent event){
		Player p = event.getPlayer();
		if (Game.inqueue.contains(p.getName())){
			Game.inqueue.remove(p.getName());
		}
		if (Game.watching.contains(p.getName())){
			Game.watching.remove(p.getName());
		}
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
				Game.sendToAll(Game.tag + ChatColor.RED  + p.getName() + ChatColor.YELLOW  + " left the game.");
			}
		}
	}
	
}
