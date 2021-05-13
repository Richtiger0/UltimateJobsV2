package de.warsteiner.ultimatejobs.events.jobs;

import java.util.ArrayList;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import de.warsteiner.ultimatejobs.UltimateJobs;
import de.warsteiner.ultimatejobs.utils.Action;
import de.warsteiner.ultimatejobs.utils.api.JobAPI;

public class ActionBlockPlace implements Listener {

	@EventHandler
	public void onBreak(BlockPlaceEvent event) { 
		UltimateJobs plugin = UltimateJobs.getPlugin();
		JobAPI api = plugin.getAPI();
		Player player = event.getPlayer();
		String UUID = ""+player.getUniqueId(); 
		Block block = event.getBlock();
		
		
		if(event.isCancelled()) {
			return;
		}
		
		String world = player.getLocation().getWorld().getName();
		 
		ArrayList<String> jobs = api.getJobsWithAction(Action.PLACE);
		
		for(String job : jobs) {
		
			if(!api.canWorkInWorld(world, job)) {
				return;
			}
			
			if(plugin.getPlayerAPI().isInJob(UUID, job)) {
			 
				String id = ""+block.getType();
				
				if(api.isSupportedID(job, id)) {
					
					if(api.getReward(job, id)) {
						 
						plugin.getRewardAPI().addRewardAndSendMessage(job, player, id);
						
						plugin.getPlayerAPI().addCount1(UUID, job, 1);
						
					}
					
				}
 				
			}
			
		} 
	}
}
