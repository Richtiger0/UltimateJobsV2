package de.warsteiner.ultimatejobs.events.jobs;

import java.util.ArrayList;

import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import de.warsteiner.ultimatejobs.UltimateJobs;
import de.warsteiner.ultimatejobs.utils.Action;
import de.warsteiner.ultimatejobs.utils.api.JobAPI;

public class ActionKillMob implements Listener {
	
	 @EventHandler
	 public void onKill(EntityDeathEvent event) {
	 
			UltimateJobs plugin = UltimateJobs.getPlugin();
			JobAPI api = plugin.getAPI(); 
			  
			 EntityType ent_type = event.getEntity().getType();
			 
 
    if(event.getEntity().getKiller() != null) {
		 Player killer = event.getEntity().getKiller();
		 String UUID = ""+killer.getUniqueId(); 
		 
 
			String world = killer.getLocation().getWorld().getName();
			 
			ArrayList<String> jobs = api.getJobsWithAction(Action.KILL_MOB);
			
			for(String job : jobs) {
			
				if(!api.canWorkInWorld(world, job)) {
					return;
				}
				
				if(plugin.getPlayerAPI().isInJob(UUID, job)) {
			 
					String id = ""+ent_type;
					
					if(api.isSupportedID(job, id)) {
						
						if(api.getReward(job, id)) {
							 
							plugin.getRewardAPI().addRewardAndSendMessage(job, killer, id);
							
							plugin.getPlayerAPI().addCount1(UUID, job, 1);
							
						}
						
					}
	 				
				}
				
			} 
		 
    }
}

}
