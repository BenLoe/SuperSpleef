package me.BenLoe.SuperSpleef;

import java.util.List;

import org.bukkit.entity.Player;



public class Stats {

	private int Wins, GamesPlayed;
	private Player p;
	
	public Stats(int shards, int eshards, Player p){
		this.Wins = shards;
		this.GamesPlayed = eshards;
		this.p = p;
	}
	
	public static Stats getStats(Player p){
		int Wins = 0;
		int GamesPlayed = 0;
		if (Files.getDataFile().contains("Players." + p.getUniqueId() + ".Wins")){
			Wins = Files.getDataFile().getInt("Players." + p.getUniqueId() + ".Wins");
		}
		if (Files.getDataFile().contains("Players." + p.getUniqueId() + ".GamesPlayed")){
			GamesPlayed = Files.getDataFile().getInt("Players." + p.getUniqueId() + ".GamesPlayed");
		}
		return new Stats(Wins, GamesPlayed, p);
	}
	
	public int getWins(){
		return this.Wins;
	}
	
	public int getGamesPlayed(){
		return this.GamesPlayed;
	}
	
	public Stats setWins(int i){
		Files.getDataFile().set("Players." + p.getUniqueId() + ".Wins", i);
		Files.saveDataFile();
		return new Stats(i, GamesPlayed, p);
	}
	
	public Stats setEnchantedShards(int i){
		Files.getDataFile().set("Players." + p.getUniqueId() + ".GamesPlayed", i);
		Files.saveDataFile();
		return new Stats(Wins, i, p);
	}
	
	public Stats addWins(int i){
		Files.getDataFile().set("Players." + p.getUniqueId() + ".Wins", Wins + i);
		Files.saveDataFile();
		List<String> moneys = Files.getDataFile().getStringList("PlayersList");
		if (!moneys.contains(p.getUniqueId().toString())){
			moneys.add(p.getUniqueId().toString());
			Files.getDataFile().set("PlayersList", moneys);
			Files.saveDataFile();
		}
		return new Stats(Wins + i, GamesPlayed, p);
	}
	
	public Stats addGamesPlayed(int i){
		Files.getDataFile().set("Players." + p.getUniqueId() + ".GamesPlayed", GamesPlayed + i);
		Files.saveDataFile();
		List<String> moneys = Files.getDataFile().getStringList("PlayersList");
		if (!moneys.contains(p.getUniqueId().toString())){
			moneys.add(p.getUniqueId().toString());
			Files.getDataFile().set("PlayersList", moneys);
			Files.saveDataFile();
		}
		return new Stats(Wins, GamesPlayed + i, p);
	}
	
}
