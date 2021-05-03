package de.warsteiner.ultimatejobs.utils.api;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.warsteiner.ultimatejobs.UltimateJobs;
import de.warsteiner.ultimatejobs.utils.api.other.BossBarHandler;

public class RewardAPI {
	
	public void sendRewardMessage(String job, Player player, String money, String vanilla, String exp, String points) {
		
		UltimateJobs plugin = UltimateJobs.getPlugin();
		
		YamlConfiguration tr = plugin.getTranslation();
		
		String mode = tr.getString("Jobs.RewardMode").toUpperCase();
 
		if(mode.equalsIgnoreCase("BOSSBAR")) {
			
			String message = tr.getString("Jobs.BossBar.Message");
			int ticks = tr.getInt("Jobs.BossBar.Ticks");
			String color = tr.getString("Jobs.BossBar.Color");
			String style = tr.getString("Jobs.BossBar.Style");
			
	 
			BossBarHandler.sendBar(job, player, BarColor.valueOf(color), BarStyle.valueOf(style), ticks, plugin.getAPI().toHex(message), 0.5);
			
		} else 	if(mode.equalsIgnoreCase("ACTIONBAR")) {
			String message = tr.getString("Jobs.ActionBar.Message");
		}
		
	}

}
