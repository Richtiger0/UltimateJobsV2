package de.warsteiner.ultimatejobs.events.gui;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;

import de.warsteiner.ultimatejobs.UltimateJobs;

public class PlayerClickAtMainInventory implements Listener {
	
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
					
		 String name = plugin.getJobsGUIConfig().getString("Name").replaceAll("&", "§");
		 
		 String display = e.getCurrentItem().getItemMeta().getDisplayName();
		 
		if (e.getView().getTitle().equalsIgnoreCase(plugin.getAPI().toHex(name))) {
			e.setCancelled(true);
			 
			plugin.getBuilder().runAction(p, display, plugin.getJobsGUIConfig(), "Custom_Items_Used", "Custom_Items", null);
			
			for(String job : plugin.getJobsListConfig().getStringList("Jobs")) {
				
				String dis = plugin.getAPI().getJobDisplay(job);
				
				if(dis.equalsIgnoreCase(display)) {
				 
						if(!plugin.getPlayerAPI().getOwn(""+p.getUniqueId()).contains(job.toUpperCase())) {
							String mode = plugin.getAPI().getJobPriceMode(job);
							String price = plugin.getAPI().getJobPriceAsPrice(job);
							
							if(plugin.getAPI().canBuyJob(p, mode, price)) {
								plugin.getAPI().buyJobAndRemoveMoney(p, mode, price, job);
								plugin.getPlayerAPI().createJob(""+p.getUniqueId(), job);
								plugin.getBuilder().setCustomItemsInInventory(p.getOpenInventory(), p, plugin.getJobsGUIConfig(), "Custom_Items", "Custom_Items_Used", plugin.getJobsGUIConfig().getStringList("PlaceHolders"));
				 
								
					 
								plugin.getBuilder().setJobsItems(p.getOpenInventory(), p);
								
							} else {
								String message = ms.getString("Translation.Prefix") + ms.getString("Translation.Not_Enough");
								p.sendMessage(plugin.getAPI().toHex(message).replaceAll("&", "§"));
							}
						} else {
							YamlConfiguration cfg = plugin.getJobsGUIConfig();
							
							if(plugin.getPlayerAPI().isInJob(""+p.getUniqueId(), job)) {
								String m = cfg.getString("ActionOnClick").toUpperCase();
								
								if(m.equalsIgnoreCase("LEAVE")) {
									
									plugin.getAPI().leaveJob(p, job);
									
									String message = ms.getString("Translation.Prefix") + ms.getString("Translation.LeftJob").replaceAll("<job>", plugin.getAPI().getJobDisplay(job));
									p.sendMessage(plugin.getAPI().toHex(message).replaceAll("&", "§"));
									plugin.getBuilder().setCustomItemsInInventory(p.getOpenInventory(), p, plugin.getJobsGUIConfig(), "Custom_Items", "Custom_Items_Used", plugin.getJobsGUIConfig().getStringList("PlaceHolders"));
								 
									 
									plugin.getBuilder().setJobsItems(p.getOpenInventory(), p);
									 
								} else 	if(m.equalsIgnoreCase("OPTIONS")) {
									String gui = plugin.getJobsGUIConfig().getString("Options_Name").replaceAll("<job>", plugin.getAPI().getJobDisplay(job));
									p.openInventory(plugin.getBuilder().createGui(p, 9*plugin.getJobsGUIConfig().getInt("Options_Size"), gui));
									plugin.getBuilder().setCustomItemsInInventory(p.getOpenInventory(), p, plugin.getJobsGUIConfig(), "Options_Custom_Items", "Options_Custom_Items_Used", plugin.getJobsGUIConfig().getStringList("Options_PlaceHolders"));
								  
								}
						 
							} else {
								plugin.getAPI().joinJob(p, job);

								plugin.getBuilder().setCustomItemsInInventory(p.getOpenInventory(), p, plugin.getJobsGUIConfig(), "Custom_Items", "Custom_Items_Used", plugin.getJobsGUIConfig().getStringList("PlaceHolders"));
								
								 
								plugin.getBuilder().setJobsItems(p.getOpenInventory(), p);
							}
							
						 
						}
						return;
					 
					
				}
				
			}
			
			
			
		}
	}
	
}
