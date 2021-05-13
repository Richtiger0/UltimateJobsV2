package de.warsteiner.ultimatejobs.events.jobs;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack; 

import de.warsteiner.ultimatejobs.UltimateJobs;
import de.warsteiner.ultimatejobs.utils.Action;
import de.warsteiner.ultimatejobs.utils.api.JobAPI;
 
public class ActionMilk implements Listener {
	
	@EventHandler
	public void onIt(PlayerInteractAtEntityEvent event) {
		Entity clicked = event.getRightClicked();
		
		UltimateJobs plugin = UltimateJobs.getPlugin();
		JobAPI api = plugin.getAPI();
		Player player = event.getPlayer();
		String UUID = ""+player.getUniqueId(); 
 
		if (clicked instanceof Cow) {
			/*    */ 
			/*    */       
			 Cow c = (Cow)clicked;
		
			 	if(event.getPlayer().getItemInHand() == null) {
			 		return;
			 	}
			 
			   if (event.isCancelled()) {
				  return; 
			   }
  
 
				  ItemStack item = event.getPlayer().getItemInHand();
  
				  
				 	String world = player.getLocation().getWorld().getName();
					 
					ArrayList<String> jobs = api.getJobsWithAction(Action.MILK);
					
					for(String job : jobs) {
						
						if(!api.canWorkInWorld(world, job)) {
							return;
						}
						
						if(plugin.getPlayerAPI().isInJob(UUID, job)) {
						 
							String id = ""+item.getType();
							
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
