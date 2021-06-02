package de.warsteiner.ultimatejobs.utils.addons.levelsgui;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import de.warsteiner.ultimatejobs.UltimateJobs;

public class LevelsGUI {

	public static Inventory get(Player p, String job) {
		UltimateJobs plugin = UltimateJobs.getPlugin();
		
		YamlConfiguration cfg = plugin.getLevelsGUIConfig();
		
		String disjob = plugin.getAPI().getJobDisplay(job);
		
		String name = plugin.getAPI().toHex(cfg.getString("Name").replaceAll("<job>", disjob).replaceAll("&", "§"));
		
		Inventory inv = plugin.getBuilder().createGui(p, cfg.getInt("Size")*9, name);
		
		Bukkit.getScheduler().runTaskAsynchronously(UltimateJobs.getPlugin(), new Runnable() {

			@Override
			public void run() {
				  for (int i = start; i < end; i++) {
					 
				 }
			}
			
		});
		
		return inv;
	}
	
}
