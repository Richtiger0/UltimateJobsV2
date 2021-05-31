package de.warsteiner.ultimatejobs.events.jobs;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

import de.warsteiner.ultimatejobs.UltimateJobs;
import de.warsteiner.ultimatejobs.utils.Action;
import de.warsteiner.ultimatejobs.utils.api.JobAPI;
import de.warsteiner.ultimatejobs.utils.api.WorldGuardManager;

public class ActionCraft implements Listener {

	@EventHandler
	public void onEvent(CraftItemEvent event) {
		UltimateJobs plugin = UltimateJobs.getPlugin();
		JobAPI api = plugin.getAPI();
		Player player = (Player) event.getWhoClicked();
		String UUID = ""+player.getUniqueId(); 
		
	 	if (event.isCancelled()) {
		 return;
		 }
	 	
	 	if(!api.canWorkInRegion(player, "action-craft")) {
	 		return;
	 	}
	 	 
	 	Material block = event.getInventory().getResult().getType();
	 	int amount = event.getInventory().getResult().getAmount(); 
		
		String world = player.getLocation().getWorld().getName();
		 
		ArrayList<String> jobs = api.getJobsWithAction(Action.CRAFT);
		
		for(String job : jobs) {
		
			if(!api.canWorkInWorld(world, job)) {
				return;
			}
			
			if(plugin.getPlayerAPI().isInJob(UUID, job)) {
			 
				String id = ""+block;
				
				if(api.isSupportedID(job, id)) {
					
					if(api.getReward(job, id)) {
						 
						plugin.getRewardAPI().addRewardAndSendMessage(job, player, id, amount);
						
						plugin.getPlayerAPI().addCount1(UUID, job, amount);
						
					}
					
				}
 				
			}
			
		} 
	}
}
