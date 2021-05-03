package de.warsteiner.ultimatejobs.utils.api;

import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

import de.warsteiner.ultimatejobs.UltimateJobs;

public class LevelAPI {
	
	public String getJobNeedExp(String uuid, String job) {
		
		UltimateJobs plugin = UltimateJobs.getPlugin();
		
		YamlConfiguration cfg = plugin.getAPI().getConfigOfJob(job);
 
		int next = plugin.getPlayerAPI().getJobLevel(uuid, job)+1;
		
		String path = "L"+next;
		
		String need = cfg.getString("Levels."+path+".Need");
		
		if(!isMaxLevel(need)) {
			return need;
		} 
		return "MaxLevelReached"; 
	}
	
	public boolean PlayeLevelIsAlreadyMaxed(String uuid, String job) {
		UltimateJobs plugin = UltimateJobs.getPlugin();
		
		YamlConfiguration cfg = plugin.getAPI().getConfigOfJob(job);
 
		int next = plugin.getPlayerAPI().getJobLevel(uuid, job)+1;
		
		String path = "L"+next;
		
		String need = cfg.getString("Levels."+path+".Need");
		
		return isMaxLevel(need);
	}
	
	public boolean isMaxLevel(String need) {
		if(need.equalsIgnoreCase("MAX")) {
			return true;
		}
		return false;
	}

	public String getDisPlayOfLevel(String uuid, String job, int level) {
		
		UltimateJobs plugin = UltimateJobs.getPlugin();
		
		YamlConfiguration cfg = plugin.getAPI().getConfigOfJob(job);
 
		String path = "L"+level;
		
		String d = cfg.getString("Levels."+path+".Display");
	 
		return d; 
	}
	
	public String getLevelRewardRype(String uuid, String job, int level) {
		
		UltimateJobs plugin = UltimateJobs.getPlugin();
		
		YamlConfiguration cfg = plugin.getAPI().getConfigOfJob(job);
 
		String path = "L"+level;
		
		String d = cfg.getString("Levels."+path+".Reward");
	 
		return d; 
	}
	
}
