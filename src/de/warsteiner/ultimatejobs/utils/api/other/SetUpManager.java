package de.warsteiner.ultimatejobs.utils.api.other;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.warsteiner.ultimatejobs.UltimateJobs;

public class SetUpManager {
	
	public static HashMap<Player, String> players = new HashMap<Player, String>();
	public static HashMap<Player, Integer> in = new HashMap<Player, Integer>();
	
	public static void clear(Player p) {
		for (int i = 0; i < 60; i++) {
			p.sendMessage("§7");
		}
	}
	
	public static void startStep(Player p, int step, boolean clear) {
		UltimateJobs plugin = UltimateJobs.getPlugin();
		YamlConfiguration tr = plugin.getTranslation();
	   in.put(p, step);
	   if(clear) {
	   clear(p);
	   }
	   if(step == 1) {
		  String m = tr.getString("Translation.Prefix")
				  + " §7Please Enter a Name for the Job§8: §7Use §ccancel §7to cancel";
		  p.sendMessage(plugin.getAPI().toHex(m).replaceAll("&", "§"));  
	   } else  if(step == 2) {
			  String m = tr.getString("Translation.Prefix")
					  + " §7Please Enter a Job Action§8: §7Use §ccancel §7to cancel";
			  p.sendMessage(plugin.getAPI().toHex(m).replaceAll("&", "§"));  
		   } else  if(step == 3) {
				  String m = tr.getString("Translation.Prefix")
						  + " §7Please Enter a Job Display Name§8: §7Use §ccancel §7to cancel";
				  p.sendMessage(plugin.getAPI().toHex(m).replaceAll("&", "§"));  
			   } else  if(step == 4) {
					  String m = tr.getString("Translation.Prefix")
							  + " §7Please Enter a Material§8: §7Use §ccancel §7to cancel";
					  p.sendMessage(plugin.getAPI().toHex(m).replaceAll("&", "§"));  
				   } else  if(step == 5) {
						  String m = tr.getString("Translation.Prefix")
								  + " §7Please Enter a Slot in the JobGUI§8: §7Use §ccancel §7to cancel";
						  p.sendMessage(plugin.getAPI().toHex(m).replaceAll("&", "§"));  
					   }
	}

	public static void cancel(Player p, boolean clear) {
		in.remove(p);
		players.remove(p);
		UltimateJobs plugin = UltimateJobs.getPlugin();
		YamlConfiguration tr = plugin.getTranslation();
 if(clear) {
	   clear(p);
 
		  String m = tr.getString("Translation.Prefix")
				  + " §7You have canceled the job creation!";
		  p.sendMessage(plugin.getAPI().toHex(m).replaceAll("&", "§"));  
	}
	}
	
}
