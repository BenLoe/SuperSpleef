package me.BenLoe.SuperSpleef;



public class Stats {

	private int Wins, GamesPlayed;
	private String p;
	
	public Stats(int shards, int eshards, String p){
		this.Wins = shards;
		this.GamesPlayed = eshards;
		this.p = p;
	}
	
	public static Stats getStats(String p){
		int Wins = 0;
		int GamesPlayed = 0;
		if (Files.getDataFile().contains("Players." + p + ".Wins")){
			Wins = Files.getDataFile().getInt("Players." + p + ".Wins");
		}
		if (Files.getDataFile().contains("Players." + p + ".GamesPlayed")){
			GamesPlayed = Files.getDataFile().getInt("Players." + p + ".GamesPlayed");
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
		Files.getDataFile().set("Players." + p + ".Wins", i);
		Files.saveDataFile();
		return new Stats(i, GamesPlayed, p);
	}
	
	public Stats setEnchantedShards(int i){
		Files.getDataFile().set("Players." + p + ".GamesPlayed", i);
		Files.saveDataFile();
		return new Stats(Wins, i, p);
	}
	
	public Stats addWins(int i){
		Files.getDataFile().set("Players." + p + ".Wins", Wins + i);
		Files.saveDataFile();
		return new Stats(Wins + i, GamesPlayed, p);
	}
	
	public Stats addGamesPlayed(int i){
		Files.getDataFile().set("Players." + p + ".GamesPlayed", GamesPlayed + i);
		Files.saveDataFile();
		return new Stats(Wins, GamesPlayed + i, p);
	}
	
}
