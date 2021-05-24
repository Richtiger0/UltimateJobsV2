package de.warsteiner.ultimatejobs.utils.api.other;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
	
	public static void createJob(String id) {
		
		String[] s = id.split(":");
	
		String job = s[0];
		String action = s[1];
		String display = s[2];
		String material = s[3];
		String slot = s[4];
		String price = s[5];
		String stats_count_name_1 = s[6];
		String stats_count_id_1 = s[7]; 
		
		File jobfile = new File(UltimateJobs.getPlugin().getDataFolder()+"/jobs/", job+".yml");
		
		if(!jobfile.exists()) {
			try {
				jobfile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(jobfile);
		
		ArrayList<String> list1 = new ArrayList<String>();
		ArrayList<String> list2 = new ArrayList<String>();
		
		list2.add("world");
		
		list1.add("HIDE_ATTRIBUTES");
		list1.add("HIDE_ENCHANTS");
 
		cfg.set("Action", ""+action);
		cfg.set("Display", ""+display);
		cfg.set("ID", ""+job);
		cfg.set("Material", ""+material);
		cfg.set("Slot", Integer.valueOf(slot));
		cfg.set("Price.Mode", "VAULT");
		cfg.set("Price.Price", ""+price);
		cfg.set("ItemFlag", list1);
		cfg.set("Worlds", list2);
		cfg.set("Translation.Stats_Part_1", ""+stats_count_name_1);
		cfg.set("Translation.Stats_Part_1_ID", ""+stats_count_id_1);
 
		try {
			cfg.save(jobfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		File jbfile = new File(UltimateJobs.getPlugin().getDataFolder(), "Jobs.yml");
		
		YamlConfiguration jbconfig = YamlConfiguration.loadConfiguration(jbfile);
		List<String> jobs = jbconfig.getStringList("Jobs");
		jobs.add(job);
		jbconfig.set("Jobs", jobs);
		try {
			jbconfig.save(jbfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		UltimateJobs.getPlugin().load();
		
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
					   } else  if(step == 6) {
							  String m = tr.getString("Translation.Prefix")
									  + " §7Please Enter a Price for the Job§8: §7Use §ccancel §7to cancel";
							  p.sendMessage(plugin.getAPI().toHex(m).replaceAll("&", "§"));  
						   } else  if(step == 7) {
								  String m = tr.getString("Translation.Prefix")
										  + " §7Please Enter a Name for the default Stats Name of Count 1 §8- §7Example: §bDestroyed Blocks §8: §7Use §ccancel §7to cancel";
								  p.sendMessage(plugin.getAPI().toHex(m).replaceAll("&", "§"));  
							   } else  if(step == 8) {
									  String m = tr.getString("Translation.Prefix")
											  + " §7Please Enter a ID for the default Stats Count 1 §8- §7Example: §bBlocks §8: §7Use §ccancel §7to cancel";
									  p.sendMessage(plugin.getAPI().toHex(m).replaceAll("&", "§"));  
								   } else  if(step == 9) {
										  String m = tr.getString("Translation.Prefix")
												  + " §aJob Creation done! §7Now use §a`finish` §7to create the Job!";
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
