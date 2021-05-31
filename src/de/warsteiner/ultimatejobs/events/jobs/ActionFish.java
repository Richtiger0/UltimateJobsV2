package de.warsteiner.ultimatejobs.events.jobs;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import de.warsteiner.ultimatejobs.UltimateJobs;
import de.warsteiner.ultimatejobs.utils.Action;
import de.warsteiner.ultimatejobs.utils.api.JobAPI;
import de.warsteiner.ultimatejobs.utils.api.WorldGuardManager;

public class ActionFish implements Listener {
	
	@EventHandler
	public void onEvent(PlayerFishEvent event) {
		
		UltimateJobs plugin = UltimateJobs.getPlugin();
		JobAPI api = plugin.getAPI();
		Player player = event.getPlayer();
		String UUID = ""+player.getUniqueId(); 
		
		if (event.isCancelled()) {
		 return;
		 }
		
		if(!api.canWorkInRegion(player, "action-fish")) {
	 		return;
	 	}
 
	if (event.getCaught() != null) {
	 
		String id = event.getCaught().getName().toUpperCase().replaceAll(" ", "_");
	
	 	String world = player.getLocation().getWorld().getName();
		 
		ArrayList<String> jobs = api.getJobsWithAction(Action.FISH);
		
		for(String job : jobs) {
			
			if(!api.canWorkInWorld(world, job)) {
				return;
			}
			
			if(plugin.getPlayerAPI().isInJob(UUID, job)) {
			 
				if(api.isSupportedID(job, id)) {
					
					if(api.getReward(job, id)) {
						 
						plugin.getRewardAPI().addRewardAndSendMessage(job, player, id,1);
						
						plugin.getPlayerAPI().addCount1(UUID, job, 1);
						
					}
					
				}
 				
			}
			
		} 
		
		}
	}

}
