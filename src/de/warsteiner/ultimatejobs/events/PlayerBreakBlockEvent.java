package de.warsteiner.ultimatejobs.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import de.warsteiner.ultimatejobs.UltimateJobs;
import de.warsteiner.ultimatejobs.utils.Action; 

public class PlayerBreakBlockEvent implements Listener {

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		event.getPlayer().sendMessage("henlo §b"+UltimateJobs.getAPI().getJobsWithAction(Action.BREAK));
		 
	}
	
}
