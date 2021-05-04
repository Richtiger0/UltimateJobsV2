package de.warsteiner.ultimatejobs.events;
 

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.warsteiner.ultimatejobs.UltimateJobs;
import de.warsteiner.ultimatejobs.utils.api.LevelAPI;
import de.warsteiner.ultimatejobs.utils.api.PlayerAPI;
import de.warsteiner.ultimatejobs.utils.api.events.PlayerDataChangeEvent;
import net.milkbowl.vault.economy.Economy;
 
public class PlayerLevelCheckEvent implements Listener {
	@EventHandler
	public void onChange(PlayerDataChangeEvent event) {
		String uuid = event.getUUID();
		Player player = Bukkit.getPlayer(UUID.fromString(uuid.toString()));
		String job = event.getJob();
	    check(player, job);
	}
	
	 public  void check(Player p, String job) {
 
		 UltimateJobs plugin = UltimateJobs.getPlugin();
		 LevelAPI levels = plugin.getLevelAPI();
		 PlayerAPI data = plugin.getPlayerAPI();
		 YamlConfiguration tr = plugin.getTranslation(); 
		 
		boolean ismax = plugin.getLevelAPI().PlayeLevelIsAlreadyMaxed(""+p.getUniqueId(), job);
		
		if(!ismax) {
			double need = Double.valueOf(plugin.getLevelAPI().getJobNeedExp(""+p.getUniqueId(), job));
 
			double exp = plugin.getPlayerAPI().getJobExp(""+p.getUniqueId(), job);
			
			if(exp == need || exp >= need) {
			 
				  
					String mode = tr.getString("Levels.LevelUpMode").toUpperCase();
				  
					String rmode = levels.getJobLevelUpRewardType(""+p.getUniqueId(), job).toUpperCase();
					
					if(rmode.equalsIgnoreCase("COMMAND_MAP")) {
						
						List<String> list = levels.getRewardList(""+p.getUniqueId(), job);
						
						for(String cmd : list) {
							
							ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
					    
							 Bukkit.dispatchCommand((CommandSender)console, cmd.replaceAll("<name>", p.getName()));
							
						}
						
					}
					
					data.setJobExp(""+p.getUniqueId(), job,0);
					data.addJobLevel(""+p.getUniqueId(), job, 1);
					
					String jobname = plugin.getAPI().getJobDisplay(job);
					String jobneed = plugin.getLevelAPI().getJobNeedExp(""+p.getUniqueId(), job);
			 
					int joblevel = plugin.getPlayerAPI().getJobLevel(""+p.getUniqueId(), job);
					String levelname = plugin.getLevelAPI().getDisPlayOfLevel(""+p.getUniqueId(), job, joblevel);
				  
				 if(mode.equalsIgnoreCase("MESSAGE")) {
					 String message = plugin.getAPI().toHex(tr.getString("Levels.Message").replaceAll("<need>", jobneed).replaceAll("<job>", jobname).replaceAll("<level_as_name>", levelname)	.replace("<level_as_int>", ""+joblevel).replaceAll("&", "§"));
					 p.sendMessage(message);
				 } else if(mode.equalsIgnoreCase("TITLE")) {
					 String message = plugin.getAPI().toHex(tr.getString("Levels.Title.First").replaceAll("<need>", jobneed).replaceAll("<job>", jobname).replaceAll("<level_as_name>", levelname)	.replace("<level_as_int>", ""+joblevel).replaceAll("&", "§"));
					 String message2 = plugin.getAPI().toHex(tr.getString("Levels.Title.Second").replaceAll("<need>", jobneed).replaceAll("<job>", jobname).replaceAll("<level_as_name>", levelname)	.replace("<level_as_int>", ""+joblevel).replaceAll("&", "§"));
					 p.sendTitle(message, message2);
				 }
				
			}
			
		}
		
	 }

 
}
