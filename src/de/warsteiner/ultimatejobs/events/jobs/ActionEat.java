package de.warsteiner.ultimatejobs.events.jobs;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import de.warsteiner.ultimatejobs.UltimateJobs;
import de.warsteiner.ultimatejobs.utils.Action;
import de.warsteiner.ultimatejobs.utils.api.JobAPI;

public class ActionEat implements Listener {

		@EventHandler
	     public void onFoodChange(FoodLevelChangeEvent event){
		 
		if(event.isCancelled()) {
			 return;
			 }
		if(event.getItem() == null) {
			return;
		}
	    if(event.getEntity() instanceof Player){
			UltimateJobs plugin = UltimateJobs.getPlugin();
			JobAPI api = plugin.getAPI();
			Player player = (Player) event.getEntity();
			String UUID = ""+player.getUniqueId(); 
	 
		   
		    Material item = event.getItem().getType();
		    
			String world = player.getLocation().getWorld().getName();
			 
			ArrayList<String> jobs = api.getJobsWithAction(Action.EAT);
			
			for(String job : jobs) {
			
				if(!api.canWorkInWorld(world, job)) {
					return;
				}
				
				if(plugin.getPlayerAPI().isInJob(UUID, job)) {
				 
					String id = ""+item;
					
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
