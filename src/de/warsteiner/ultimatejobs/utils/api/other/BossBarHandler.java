package de.warsteiner.ultimatejobs.utils.api.other;

 import de.warsteiner.ultimatejobs.UltimateJobs;
 import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
 import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
 
public class BossBarHandler
{
private static UltimateJobs plugin = UltimateJobs.getPlugin();

 

public static void sendBar(String job, Player player, BarColor color, BarStyle style, int time, String message, double progress) {
	 double use;
	if(!UltimateJobs.getLevelAPI().PlayeLevelIsAlreadyMaxed(""+player.getUniqueId(), job)) {
		 double jobexp = UltimateJobs.getPlayerAPI().getJobExp(""+player.getUniqueId(), job);
		 double jobneed = Double.parseDouble(UltimateJobs.getLevelAPI().getJobNeedExp(""+player.getUniqueId(), job)) / 100;
				 
		 double p = jobexp / jobneed;
		 
		 double max = 1.0 / 100;
		 
		 
		 double one = max * p ;
		 
		 
		 
		 if(one >= 1.0) {
			 use = 1;
		 } else {
			 use = one;
		 }
	} else {
		use = 1.0;
	}
 
 BossBar bar = Bukkit.createBossBar(message, color, style, new org.bukkit.boss.BarFlag[0]);
 bar.setProgress(use);
 bar.addPlayer(player);
 
plugin.getServer().getScheduler().runTaskLater((Plugin)plugin, bar::removeAll, (time));
}

}
 