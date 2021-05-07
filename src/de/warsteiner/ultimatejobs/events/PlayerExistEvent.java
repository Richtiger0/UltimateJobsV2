package de.warsteiner.ultimatejobs.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import de.warsteiner.ultimatejobs.UltimateJobs;

public class PlayerExistEvent implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		UltimateJobs plugin = UltimateJobs.getPlugin();
		String uuid = ""+player.getUniqueId();
		if(!plugin.getPlayerAPI().existPlayer(uuid)) {
			plugin.getPlayerAPI().createPlayer(uuid);
		}
		plugin.getPlayerAPI().UpdateFetcher(uuid, player.getName());
	}

}
