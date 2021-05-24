package de.warsteiner.ultimatejobs.events;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import de.warsteiner.ultimatejobs.UltimateJobs;
import de.warsteiner.ultimatejobs.utils.Action;
import de.warsteiner.ultimatejobs.utils.api.other.SetUpManager;

public class PlayerSetupJob implements Listener {
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		UltimateJobs plugin = UltimateJobs.getPlugin();
		YamlConfiguration tr = plugin.getTranslation();
		
		Player p = event.getPlayer();
		String mg = event.getMessage();
		
		if(SetUpManager.in.containsKey(p)) {
			
			event.setCancelled(true);
			
			int step = SetUpManager.in.get(p);
			
			String gb = SetUpManager.players.get(p);
			
			if(mg.toUpperCase().equalsIgnoreCase("CANCEL")) {
				SetUpManager.cancel(p, true);
				return;
			}
			
			if(mg.length() <= 0) {
				String m = tr.getString("Translation.Prefix")
						  + " §cPlease enter a valid Value!";
				  p.sendMessage(plugin.getAPI().toHex(m).replaceAll("&", "§"));   
				return;
			}
			
			if(step == 1) {
				
				String job = mg.toUpperCase();
				 
				File jobfile = new File(UltimateJobs.getPlugin().getDataFolder()+"/jobs/", job+".yml");
				
				if(jobfile.exists()) {
					String m = tr.getString("Translation.Prefix")
							  + " §7That Job already exist!";
					  p.sendMessage(plugin.getAPI().toHex(m).replaceAll("&", "§"));   
					  return;
				} else {
					SetUpManager.players.put(p, job+":");
					
					String m = tr.getString("Translation.Prefix")
							  + " §7Job Name§8: §a"+job;
					  p.sendMessage(plugin.getAPI().toHex(m).replaceAll("&", "§"));   
					
					SetUpManager.startStep(p, 2, false);
					return;
				} 
			} else if(step == 2) {
				
				String action = mg.toUpperCase();
				
				if(!plugin.actions.contains(action)) {
					String m = tr.getString("Translation.Prefix")
							  + " §cThat action doesnt action!";
					  p.sendMessage(plugin.getAPI().toHex(m).replaceAll("&", "§"));   
					return;
				}
				
			}
			
			Bukkit.broadcastMessage(""+gb);
			
		}
		
	}

}















