package me.BenLoe.SuperSpleef.Classes;

import me.BenLoe.SuperSpleef.Files;

import org.bukkit.entity.Player;

public enum KitType {

	SCOUT, BOMBER, BEASTTAMER, TANK; 
	
	public static KitType getKit(Player p){
		if (Files.getDataFile().contains("Players." + p.getName() + ".Kit")){
			String s = Files.getDataFile().getString("Players." + p.getName() + ".Kit");
			for (KitType k : KitType.values()){
				if (s == k.name()){
					return k;
				}
			}
		}
		return SCOUT;
	}
	
	public void setKit(Player p){
		Files.getDataFile().set("Players." + p.getName() + ".Kit", this.name());
		Files.saveDataFile();
	}
	
	public void buyKit(Player p){
		Files.getDataFile().set("Players." + p.getName() + "." + this.name(), true);
		Files.saveDataFile();
	}
	public boolean ownsKit(Player p){
		if (Files.getDataFile().contains("Players." + p.getName() + "." + this.name())){
			return Files.getDataFile().getBoolean("Players." + p.getName() + "." + this.name());
		}
		return false;
	}
}
