package de.warsteiner.ultimatejobs.utils.api;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.warsteiner.ultimatejobs.UltimateJobs;
import de.warsteiner.ultimatejobs.utils.api.other.ActionBar;
import de.warsteiner.ultimatejobs.utils.api.other.BossBarHandler;

public class RewardAPI {
	
	public void addRewardAndSendMessage(String job, Player player, String id, int amount) {
		
		UltimateJobs plugin = UltimateJobs.getPlugin();
		
		double money = plugin.getAPI().getRewardOfID(job, id) * amount;
		int vanilla = plugin.getAPI().getVanillaOfID(job, id);
		double exp = plugin.getAPI().getExpofID(job, id);
		
		plugin.getEconomy().depositPlayer(player, Double.valueOf(money));
		
		player.giveExp(Integer.valueOf(vanilla));
		
		if(!plugin.getLevelAPI().PlayeLevelIsAlreadyMaxed(""+player.getUniqueId(), job)) {
		plugin.getPlayerAPI().addJobExp(""+player.getUniqueId(), job, Double.valueOf(exp));
		}
		
		sendRewardMessage(job, player, ""+money, ""+vanilla, ""+exp);
		
	}
	
	public void sendRewardMessage(String job, Player player, String money, String vanilla, String exp) {
		
		UltimateJobs plugin = UltimateJobs.getPlugin();
		
		YamlConfiguration tr = plugin.getTranslation();
		
		String mode = tr.getString("Jobs.RewardMode").toUpperCase();
 
		String jobname = plugin.getAPI().getJobDisplay(job);
		String jobneed = plugin.getLevelAPI().getJobNeedExp(""+player.getUniqueId(), job);
		String jobexp = plugin.getAPI().FormatAsExp(plugin.getPlayerAPI().getJobExp(""+player.getUniqueId(), job));
		int joblevel = plugin.getPlayerAPI().getJobLevel(""+player.getUniqueId(), job);
		String levelname = plugin.getLevelAPI().getDisPlayOfLevel(""+player.getUniqueId(), job, joblevel);
		
		if(mode.equalsIgnoreCase("BOSSBAR")) {
 
			String message = tr.getString("Jobs.BossBar.Message")
				.replaceAll("<level_as_name>", levelname)	.replace("<level_as_int>", ""+joblevel)		.replace("<need>", jobneed)	.replace("<ep>", ""+jobexp)	.replace("<van>", vanilla).replace("<exp>", exp)	.replace("<money>", money)	.replace("<job>", jobname).replaceAll("&", "§");
			int ticks = tr.getInt("Jobs.BossBar.Ticks");
			String color = tr.getString("Jobs.BossBar.Color");
			String style = tr.getString("Jobs.BossBar.Style");
			
	 
			BossBarHandler.sendBar(job, player, BarColor.valueOf(color), BarStyle.valueOf(style), ticks, plugin.getAPI().toHex(message), 0.5);
			
		} else 	if(mode.equalsIgnoreCase("ACTIONBAR")) {
			String message = tr.getString("Jobs.ActionBar.Message")
					.replaceAll("<level_as_name>", levelname)	.replace("<level_as_int>", ""+joblevel)		.replace("<need>", jobneed)	.replace("<ep>", ""+jobexp)	.replace("<van>", vanilla).replace("<exp>", exp)	.replace("<money>", money)	.replace("<job>", jobname).replaceAll("&", "§");
				 ActionBar.sendActionbar(player, message);
		}
		
	}

}
