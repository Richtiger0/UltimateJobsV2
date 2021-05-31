package de.warsteiner.ultimatejobs.events.jobs;

import java.util.ArrayList;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.ItemStack;

import de.warsteiner.ultimatejobs.UltimateJobs;
import de.warsteiner.ultimatejobs.utils.Action;
import de.warsteiner.ultimatejobs.utils.api.JobAPI;

public class ActionEnchant implements Listener {

	@EventHandler
	public void onEvent(EnchantItemEvent  event) {
		UltimateJobs plugin = UltimateJobs.getPlugin();
		JobAPI api = plugin.getAPI();
		Player player = event.getEnchanter();
		String UUID = ""+player.getUniqueId(); 
		ItemStack block = event.getItem();
		
		
		if(event.isCancelled()) {
			return;
		}
		 
	 	if(!api.canWorkInRegion(player, "action-enchant")) {
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
