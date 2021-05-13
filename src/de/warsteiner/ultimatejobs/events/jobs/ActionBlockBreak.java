package de.warsteiner.ultimatejobs.events.jobs;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import de.warsteiner.ultimatejobs.UltimateJobs;
import de.warsteiner.ultimatejobs.utils.Action;
import de.warsteiner.ultimatejobs.utils.api.JobAPI;
import de.warsteiner.ultimatejobs.utils.api.WorldGuardManager; 

public class ActionBlockBreak implements Listener {

	@EventHandler
	public void onBreak(BlockBreakEvent event) { 
		UltimateJobs plugin = UltimateJobs.getPlugin();
		JobAPI api = plugin.getAPI();
		Player player = event.getPlayer();
		String UUID = ""+player.getUniqueId(); 
		Block block = event.getBlock();
		
		
		if(event.isCancelled()) {
			return;
		}
		
	 	if(block.hasMetadata("placed-by-player")) {
		 	return;
	 	}
	  
	 	if(!api.canWorkInRegion(player, "action-break")) {
	 		return;
	 	}
	 	
	 	String world = player.getLocation().getWorld().getName();
	 
		ArrayList<String> jobs = api.getJobsWithAction(Action.BREAK);
		
		for(String job : jobs) {
			
			if(!api.canWorkInWorld(world, job)) {
				return;
			}
			
			if(plugin.getPlayerAPI().isInJob(UUID, job)) {
			 
				String id = ""+block.getType();
				
				if(api.isSupportedID(job, id)) {
					
					if(api.getReward(job, id)) {
						 
						plugin.getRewardAPI().addRewardAndSendMessage(job, player, id, 1);
						
						plugin.getPlayerAPI().addCount1(UUID, job, 1);
						
					}
					
				}
 				
			}
			
		} 
	}
	
}
