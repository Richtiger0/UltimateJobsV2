package de.warsteiner.ultimatejobs.events;
 

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.warsteiner.ultimatejobs.UltimateJobs;
import de.warsteiner.ultimatejobs.utils.api.events.PlayerDataChangeEvent;
 
public class PlayerLevelCheckEvent implements Listener {
	@EventHandler
	public void onChange(PlayerDataChangeEvent event) {
		String uuid = event.getUUID();
		Player player = Bukkit.getPlayer(UUID.fromString(uuid.toString()));
		player.sendMessage("yay");
	}

 
}
