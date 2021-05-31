package de.warsteiner.ultimatejobs.events.jobs;

import java.util.ArrayList;

import org.bukkit.CropState;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.material.Crops;
import org.bukkit.material.MaterialData;

import de.warsteiner.ultimatejobs.UltimateJobs;
import de.warsteiner.ultimatejobs.utils.Action;
import de.warsteiner.ultimatejobs.utils.api.JobAPI;

public class ActionFarm implements Listener {

	@EventHandler
	public void onEvent(BlockBreakEvent event) { 
		UltimateJobs plugin = UltimateJobs.getPlugin();
		JobAPI api = plugin.getAPI();
		Player player = event.getPlayer();
		String UUID = ""+player.getUniqueId(); 
		Block block = event.getBlock();
		
		
		if(event.isCancelled()) {
			return;
		}
		
		if(!api.canWorkInRegion(player, "action-farm")) {
	 		return;
	 	}
		
		if(!isFullyGrownOld(block)) {
			return;
		}
		
		String world = player.getLocation().getWorld().getName();
		 
		ArrayList<String> jobs = api.getJobsWithAction(Action.FARM);
		
		for(String job : jobs) {
		
			if(!api.canWorkInWorld(world, job)) {
				return;
			}
			
			if(plugin.getPlayerAPI().isInJob(UUID, job)) {
			 
				String id = ""+block.getType();
				
				if(api.isSupportedID(job, id)) {
					
					if(api.getReward(job, id)) {
						 
						plugin.getRewardAPI().addRewardAndSendMessage(job, player, id,1);
						
						plugin.getPlayerAPI().addCount1(UUID, job, 1);
						
					}
					
				}
 				
			}
			
		} 
	}

	/*    */   public static boolean isFullyGrownOld(Block block) {
		/* 11 */     MaterialData md = block.getState().getData();
		/*    */     
					if(block.getType() == Material.MELON
							|| block.getType() == Material.PUMPKIN) {
						return true;
					}
		
		/* 13 */     if (md instanceof Crops) {
		/* 14 */       return (((Crops)md).getState() == CropState.RIPE);
		/*    */     }
		/* 16 */     return false;
		/*    */   }
	
}
