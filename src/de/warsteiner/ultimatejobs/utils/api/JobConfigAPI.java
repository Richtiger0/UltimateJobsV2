package de.warsteiner.ultimatejobs.utils.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
 
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import de.warsteiner.ultimatejobs.UltimateJobs;
import de.warsteiner.ultimatejobs.utils.Action;
import net.md_5.bungee.api.ChatColor;
 
public class JobConfigAPI {
	
	public int getSlotOfJob(String id) {
		YamlConfiguration config = getConfigOfJob(id);
		
		int price  = config.getInt("Slot");

		return price;
	}
	
	public double getJobPrice(String id) {
		YamlConfiguration config = getConfigOfJob(id);
		
		double price  = config.getDouble("Price");

		return price;
	}
	
	public String getJobMaterial(String id) {
		YamlConfiguration config = getConfigOfJob(id);
 
		String mat = config.getString("Material");
 
		return mat;
	}
	
	public List<String> getLoreOfJob(String id) {
		
		//check if player has job and check if player is in job
		
		List<String> lore = UltimateJobs.getJobsGUIConfig().getStringList("");
 
		return lore;
	}
	
	public List<String> getItemFlags(String id) {
		YamlConfiguration config = getConfigOfJob(id);
 
		List<String> lore = config.getStringList("ItemFlag");
 
		return lore;
	}
	
	public String getJobDisplay(String id) {
		YamlConfiguration config = getConfigOfJob(id);
		
		if( config.getString("Display") == null) {
			 UltimateJobs.getPlugin().getLogger().warning("§cThe option §aCONFIG.GET(DISPLAY) §cdoesnt exist in config of: §a"+id);
				 
		}
		
		String name = config.getString("Display").replaceAll("&", "§");
		
		String finalname =  toHex(name);
		
		return finalname;
	}
	
	private static final Pattern pattern = Pattern.compile("(?<!\\\\)(#[a-fA-F0-9]{6})");

    public static String toHex(String motd) {
        Matcher matcher = pattern.matcher(motd);
        while (matcher.find()) {
            String color = motd.substring(matcher.start(), matcher.end());
            motd = motd.replace(color, "" + ChatColor.of(color));
        }

        return motd;
    }
    
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
