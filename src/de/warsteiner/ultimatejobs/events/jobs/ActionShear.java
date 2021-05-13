package de.warsteiner.ultimatejobs.events.jobs;

import java.util.ArrayList;

import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerShearEntityEvent;

import de.warsteiner.ultimatejobs.UltimateJobs;
import de.warsteiner.ultimatejobs.utils.Action;
import de.warsteiner.ultimatejobs.utils.api.JobAPI;

public class ActionShear implements Listener {
	
	@EventHandler
	 public void playerShearEvent(PlayerShearEntityEvent event) {
		
		UltimateJobs plugin = UltimateJobs.getPlugin();
		JobAPI api = plugin.getAPI();
		Player player = event.getPlayer();
		String UUID = ""+player.getUniqueId(); 
		
	 	if (event.isCancelled()) {
			 return;
			 }
		
		if (event.getEntity() instanceof Sheep) {
	    
			 Sheep sheep = (Sheep)event.getEntity();
			 
			 DyeColor color = sheep.getColor();
			 
				String world = player.getLocation().getWorld().getName();
				 
				ArrayList<String> jobs = api.getJobsWithAction(Action.PLACE);
				
				for(String job : jobs) {
				
					if(!api.canWorkInWorld(world, job)) {
						return;
					}
					
					if(plugin.getPlayerAPI().isInJob(UUID, job)) {
					 
						String id = ""+color;
						
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
