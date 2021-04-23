package de.warsteiner.ultimatejobs;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import de.warsteiner.ultimatejobs.events.PlayerBreakBlockEvent;
import de.warsteiner.ultimatejobs.utils.api.JobConfigAPI;

 
public class UltimateJobs extends JavaPlugin {
	
	private static UltimateJobs plugin;
	 
	private static YamlConfiguration jl;
	private static JobConfigAPI api;
	
	@Override
	public void onEnable() {
		
		plugin  = this;
		
		//create classes & load
		api = new JobConfigAPI();
 
		//we create the jobs folder
        File file = new File(getPlugin().getDataFolder()+"/jobs/");
        file.mkdir();
        
        File file2 = new File(getPlugin().getDataFolder()+"/data/");
        file2.mkdir();
		
        //create custom configs
	    File joblist;
	 
		joblist = new File(UltimateJobs.getPlugin().getDataFolder(), "Jobs.yml");
		        if (!joblist.exists()) {
		        	joblist.getParentFile().mkdirs();
		            UltimateJobs.getPlugin(). saveResource("Jobs.yml", false);
		         }

		        jl= new YamlConfiguration();
		        try {
		        	jl.load(joblist);
		        } catch (IOException | InvalidConfigurationException e) {
		            e.printStackTrace();
		        }
		    
	    Bukkit.getPluginManager().registerEvents(new PlayerBreakBlockEvent(), this);
		
	}
	
	public static YamlConfiguration getJobsListConfig() {
		return jl;
	}
	
	public static JobConfigAPI getAPI() {
		return api;
	}
	
	public static UltimateJobs getPlugin() {
		return plugin;
	}

}
