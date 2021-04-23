package de.warsteiner.ultimatejobs.utils.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import de.warsteiner.ultimatejobs.UltimateJobs;
import de.warsteiner.ultimatejobs.utils.Action;
 
public class JobConfigAPI {
	
	public ArrayList<String> getJobsWithAction(Action at) {
		
		ArrayList<String> list = new ArrayList<String>();
 
				
				List<String> jobs = UltimateJobs.getJobsListConfig().getStringList("Jobs");
				
				for(String job : jobs) {
				
					if(getConfigOfJob(job) == null) {
						 UltimateJobs.getPlugin().getLogger().warning("§cThe Config for Job §a"+job+" §cdoesnt exist!");
							 
					}
					
					YamlConfiguration config = getConfigOfJob(job);
			 
					if(config.getString("Action") == null) {
						 UltimateJobs.getPlugin().getLogger().warning("§cThe option §aCONFIG.GET(ACTION) §cdoesnt exist in config of: §a"+job);
						 
					}
				 
					String action = config.getString("Action");
					 
					if(Action.valueOf(action) == null) {
						 UltimateJobs.getPlugin().getLogger().warning("§cThe action §a"+action+" §cdoesnt exist for the Job §a"+job+"§c!");
					 
					}
			 
					if(Action.valueOf(action).equals(at)) {
					 
						list.add(job);
					}
					
				}
 
		return list;
		
	}
	
	public YamlConfiguration getConfigOfJob(String id) {
 
	    File joblist;
	    YamlConfiguration jl = null;
	    
		joblist = new File(UltimateJobs.getPlugin().getDataFolder()+"/jobs/", id.toUpperCase()+".yml");
		 if (!joblist.exists()) {
			    UltimateJobs.getPlugin().getLogger().warning("§cThe requested file §a"+id.toUpperCase()+" §cisnt existing!");
		    	return null;
		    }
		        jl= new YamlConfiguration();
		        try {
		        	jl.load(joblist);
		        } catch (IOException | InvalidConfigurationException e) {
		            e.printStackTrace();
		        }
 
		return jl;
		
	}

}
