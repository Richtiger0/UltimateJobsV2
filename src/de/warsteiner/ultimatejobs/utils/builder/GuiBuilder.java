package de.warsteiner.ultimatejobs.utils.builder;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.scheduler.BukkitRunnable;

import de.warsteiner.ultimatejobs.UltimateJobs;
 
public class GuiBuilder {

	public Inventory createGui(Player p, int size, String name) {
		final Inventory inv = Bukkit.createInventory(null, size, name);
		return inv;
	}
	
	public void setJobsItems(InventoryView inventory, Player p) {
		UltimateJobs plugin = UltimateJobs.getPlugin();
		new BukkitRunnable() {
			
			@Override
			public void run() {
				
				List<String> jobs = plugin.getJobsListConfig().getStringList("Jobs");
				
				for(String job : jobs) {
					
					String display = plugin.getAPI().getJobDisplay(job);
					int slot = plugin.getAPI().getSlotOfJob(job);
					double price = plugin.getAPI().getJobPrice(job);
					
				}
				
			}
		}.runTaskAsynchronously(UltimateJobs.getPlugin());
	}
	
}
