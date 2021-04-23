package de.warsteiner.ultimatejobs;

import org.bukkit.plugin.java.JavaPlugin;

public class UltimateJobs extends JavaPlugin {
	
	private static UltimateJobs plugin;
	
	@Override
	public void onEnable() {
		
		plugin  = this;
		
	}
	
	public static UltimateJobs getPlugin() {
		return plugin;
	}

}
