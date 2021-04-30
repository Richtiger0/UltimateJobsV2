package de.warsteiner.ultimatejobs;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import de.warsteiner.ultimatejobs.command.PlayerJobCommand; 
import de.warsteiner.ultimatejobs.events.PlayerBreakBlockEvent;
import de.warsteiner.ultimatejobs.events.PlayerExistEvent;
import de.warsteiner.ultimatejobs.events.gui.PlayerClickAtMainInventory;
import de.warsteiner.ultimatejobs.utils.api.JobAPI;
import de.warsteiner.ultimatejobs.utils.api.PlayerAPI;
import de.warsteiner.ultimatejobs.utils.builder.GuiBuilder;
import de.warsteiner.ultimatejobs.utils.data.PlayerJobDataFile;
import net.milkbowl.vault.economy.Economy; 

 
public class UltimateJobs extends JavaPlugin {
	
	private static UltimateJobs plugin;
	private static Economy econ;
	private static YamlConfiguration jl;
	private static JobAPI api;
	private static GuiBuilder builder;
	private static YamlConfiguration jobsgui;
	private static YamlConfiguration main;
	private static PlayerAPI player;
	private static PlayerJobDataFile data;
	private static YamlConfiguration translation;
	
	@Override
	public void onEnable() {
		
		plugin  = this;
		
		//create classes & load
		api = new JobAPI();
		builder = new GuiBuilder();
		player = new PlayerAPI();
		
		setupEconomy();
      
		//we create the jobs folder
        File file = new File(getPlugin().getDataFolder()+"/jobs/");
        file.mkdir();
        
        File file2 = new File(getPlugin().getDataFolder()+"/data/");
        file2.mkdir();
		
        //create custom configs
	    File joblist;
	    File jobgui;
	    File mainfile;
	    File trans;
	 
		joblist = new File(UltimateJobs.getPlugin().getDataFolder(), "Jobs.yml");
		jobgui = new File(UltimateJobs.getPlugin().getDataFolder(), "JobsGUI.yml");
		mainfile = new File(UltimateJobs.getPlugin().getDataFolder(), "Main.yml");
		trans = new File(UltimateJobs.getPlugin().getDataFolder(), "Translation.yml");
		
        if (!trans.exists()) {
        	trans.getParentFile().mkdirs();
            UltimateJobs.getPlugin(). saveResource("Translation.yml", true);
         }

        translation= new YamlConfiguration();
        try {
        	translation.load(trans);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
		
        if (!mainfile.exists()) {
        	mainfile.getParentFile().mkdirs();
            UltimateJobs.getPlugin(). saveResource("Main.yml", false);
         }

        main= new YamlConfiguration();
        try {
        	main.load(mainfile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
		
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
		        
        if (!jobgui.exists()) {
        	jobgui.getParentFile().mkdirs();
            UltimateJobs.getPlugin(). saveResource("JobsGUI.yml", false);
         }

        jobsgui= new YamlConfiguration();
        try {
        	jobsgui.load(jobgui);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
		    
        //load data files
        data = new PlayerJobDataFile();
        data.create();
        
	     Bukkit.getPluginManager().registerEvents(new PlayerBreakBlockEvent(), this);
	     Bukkit.getPluginManager().registerEvents(new PlayerExistEvent(), this);
	     Bukkit.getPluginManager().registerEvents(new PlayerClickAtMainInventory(), this);
	     
	     getCommand("jobs").setExecutor(new PlayerJobCommand());
	      
	    	//  getCommand("jobs").setTabCompleter((TabCompleter)new PlayerJobTabComplete());
	    
		 UltimateJobs.getPlugin().getLogger().info("§a ");
		 UltimateJobs.getPlugin().getLogger().info("§8-> §cLoaded UltimateJobs!");
		 UltimateJobs.getPlugin().getLogger().info("§7Jobs: "+getJobsListConfig().getStringList("Jobs").size()+" loaded");
		 UltimateJobs.getPlugin().getLogger().info("§a ");  
	    
	}
	
	private boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
	if (economyProvider != null) {
		econ = (Economy)economyProvider.getProvider();
	}
		    
		return (econ != null);
	}
	
	public static Economy getEconomy() {
		return econ;
	}
	
	public static YamlConfiguration getJobsListConfig() {
		return jl;
	}
	
	public static JobAPI getAPI() {
		return api;
	}
	
	public static YamlConfiguration getTranslation() {
		return translation;
	}
	 
	public static PlayerJobDataFile getPlayerDataFile() {
		return data;
	}
	
	public static PlayerAPI getPlayerAPI() {
		return player;
	}
	
	public static YamlConfiguration getMainConfig() {
		return main;
	}
	
	public static GuiBuilder getBuilder() {
		return builder;
	}
	
	public static YamlConfiguration getJobsGUIConfig() {
		return jobsgui;
	}
	
	public static UltimateJobs getPlugin() {
		return plugin;
	}

}
