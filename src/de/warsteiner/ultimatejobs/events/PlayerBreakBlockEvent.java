package de.warsteiner.ultimatejobs.events;

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

public class PlayerBreakBlockEvent implements Listener {

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
	 
		ArrayList<String> jobs = api.getJobsWithAction(Action.BREAK);
		
		for(String job : jobs) {
			
			if(plugin.getPlayerAPI().isInJob(UUID, job)) {
			 
				String id = ""+block.getType();
				
				if(api.isSupportedID(job, id)) {
					
					if(api.getReward(job, id)) {
						
						plugin.getPlayerAPI().addJobExp(""+player.getUniqueId(), job, 5);
						
						plugin.getRewardAPI().sendRewardMessage(job,player, "1", "2", "3", "4");
						
					}
					
				}
 				
			}
			
		} 
	}
	
}
