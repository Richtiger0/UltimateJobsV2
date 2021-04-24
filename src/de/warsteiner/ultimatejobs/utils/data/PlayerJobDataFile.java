package de.warsteiner.ultimatejobs.utils.data;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import de.warsteiner.ultimatejobs.UltimateJobs; 
 
public class PlayerJobDataFile {
	
	public File cfile;
	public FileConfiguration config;
	
	public String location = UltimateJobs.getMainConfig().getString("Save_Files");
	public UltimateJobs plugin = UltimateJobs.getPlugin();
	
	public void create() {
	if (getfile() != null && 
		!this.plugin.getDataFolder().exists())
		this.plugin.getDataFolder().mkdir(); 
		if (!getfile().exists()) {
		try {
		getfile().createNewFile();
		} catch (Exception e) {
			 UltimateJobs.getPlugin().getLogger().warning("§cError by creating the file: §a"+getfile().getName());
	} 
	}
		this.config = (FileConfiguration)YamlConfiguration.loadConfiguration(this.cfile);
	}

	 public File getfile() {
		this.cfile = new File(this.location, "data_jobs.json");
		if (this.cfile != null) {
		return this.cfile;
		}
		return null;
	}
 
	public void load() {
		this.config = (FileConfiguration)YamlConfiguration.loadConfiguration(this.cfile);
	}
 
	public FileConfiguration get() {
		return this.config;
	}

	public void save() {
	 try {
		 this.config.save(getfile());
	 } catch (Exception e) {
		 UltimateJobs.getPlugin().getLogger().warning("§cError by saving the file: §a"+getfile().getName());
	 } 
	}

}
