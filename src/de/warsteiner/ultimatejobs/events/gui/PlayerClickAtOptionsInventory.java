package de.warsteiner.ultimatejobs.events.gui;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import de.warsteiner.ultimatejobs.UltimateJobs;

public class PlayerClickAtOptionsInventory implements Listener {
	
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
	if (e.getClickedInventory() == null) {
		return;
	}
	if (e.getCurrentItem() == null) {
		return;
	}

	if (e.getView().getTitle() == null) {
		return;
	}

	if(e.getCurrentItem().getItemMeta() == null) {
		return;
	}

	if(e.getCurrentItem().getItemMeta().getDisplayName() == null) {
		return;
	}
	   
	 Player p = (Player)e.getWhoClicked();

	 UltimateJobs plugin = UltimateJobs.getPlugin();
	 YamlConfiguration ms = plugin.getTranslation();
				
	 
	 
	 String display = e.getCurrentItem().getItemMeta().getDisplayName();
	 
		for(String job : plugin.getJobsListConfig().getStringList("Jobs")) {
			String dis = plugin.getAPI().getJobDisplay(job);
			
			 String name = plugin.getJobsGUIConfig().getString("Options_Name").replaceAll("<job>", dis).replaceAll("&", "§");
			if (e.getView().getTitle().equalsIgnoreCase(plugin.getAPI().toHex(name))) {
			   e.setCancelled(true);
			 
			   
			   plugin.getBuilder().runAction(p, display, plugin.getJobsGUIConfig(), "Options_Custom_Items_Used", "Options_Custom_Items", job);
			   
			   return;
			   
			}
		}
 
			
		}
	 
	}
 
 