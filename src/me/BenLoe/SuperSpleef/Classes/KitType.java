package me.BenLoe.SuperSpleef.Classes;

import me.BenLoe.SuperSpleef.Files;

import org.bukkit.entity.Player;

public enum KitType {

	SCOUT, BOMBER, BEASTTAMER, TANK; 
	
	public static KitType getKit(Player p){
		if (Files.getDataFile().contains("Players." + p.getUniqueId() + ".Kit")){
			String s = Files.getDataFile().getString("Players." + p.getUniqueId() + ".Kit");
			for (KitType k : KitType.values()){
				if (s == k.name()){
					return k;
				}
			}
		}
		return SCOUT;
	}
	
	public void setKit(Player p){
		Files.getDataFile().set("Players." + p.getUniqueId() + ".Kit", this.name());
		Files.saveDataFile();
	}
	
	public void buyKit(Player p){
		Files.getDataFile().set("Players." + p.getUniqueId() + "." + this.name(), true);
		Files.saveDataFile();
	}
	public boolean ownsKit(Player p){
		if (Files.getDataFile().contains("Players." + p.getUniqueId() + "." + this.name())){
			return Files.getDataFile().getBoolean("Players." + p.getUniqueId() + "." + this.name());
		}
		return false;
	}
}
