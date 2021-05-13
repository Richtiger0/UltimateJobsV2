package de.warsteiner.ultimatejobs.events.jobs;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Beehive;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import de.warsteiner.ultimatejobs.UltimateJobs;
import de.warsteiner.ultimatejobs.utils.api.JobAPI;
 

public class ActionHoney implements Listener {

	  @EventHandler
	  public void onBreak(PlayerInteractEvent event) {
	 	if (event.isCancelled()) {
		 return;
		 }

		UltimateJobs plugin = UltimateJobs.getPlugin();
		JobAPI api = plugin.getAPI();
		Player player = event.getPlayer();
		String UUID = ""+player.getUniqueId(); 
 
	   Action action = event.getAction();
	   Block clickedBlock = event.getClickedBlock();
 
 
	 if (action == Action.RIGHT_CLICK_BLOCK && clickedBlock != null && (
			clickedBlock.getType().equals(Material.BEEHIVE) || clickedBlock.getType().equals(Material.BEE_NEST))) {
			
		      Material item = player.getItemInHand().getType();
			 
			 if(item != null) {
				if(item == Material.GLASS_BOTTLE) {
					
					 BlockData bdata = clickedBlock.getBlockData();
					  Beehive beehive = (Beehive)bdata; 
				 
					  if( beehive.getHoneyLevel() != beehive.getMaximumHoneyLevel()) {
						  event.setCancelled(true);
						  return;
					  }
					  
						String world = player.getLocation().getWorld().getName();
						 
						ArrayList<String> jobs = api.getJobsWithAction(de.warsteiner.ultimatejobs.utils.Action.HONEY);
						
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
	  }
}
