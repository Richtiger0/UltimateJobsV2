package de.warsteiner.ultimatejobs.utils.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
 
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.warsteiner.ultimatejobs.UltimateJobs;
import de.warsteiner.ultimatejobs.utils.Action;
import net.md_5.bungee.api.ChatColor;
 
public class JobAPI {
	
	public boolean canBuyJob(Player p, String mode, String much) {
		UltimateJobs plugin = UltimateJobs.getPlugin();
		if(mode.toLowerCase().equalsIgnoreCase("VAULT")) {
			double money = Double.parseDouble(much);
			if(plugin.getEconomy().getBalance(p) >= money)  {
				return true;
			}
		} else 	if(mode.toLowerCase().equalsIgnoreCase("VANILLA")) {
			int price = Integer.parseInt(much);
			if(p.getLevel() >= price) {
				return true;
			}
		}
		return false;
	}
	
	public void joinJob(Player p, String job) {
		
		UltimateJobs plugin = UltimateJobs.getPlugin();
		YamlConfiguration ms = plugin.getTranslation();
		 
		if(canHaveJob(p)) {
			plugin.getPlayerAPI().addCurrentJobs(""+p.getUniqueId(), job);
			String message = ms.getString("Translation.Prefix") + ms.getString("Translation.Joined").replaceAll("<job>", plugin.getAPI().getJobDisplay(job));
			p.sendMessage(plugin.getAPI().toHex(message).replaceAll("&", "§"));
		} else {
			String message = ms.getString("Translation.Prefix") + ms.getString("Translation.TooManyJobs");
			p.sendMessage(plugin.getAPI().toHex(message).replaceAll("&", "§"));
		}
		
	}
	
	public void leaveJob(Player p, String job) {
		UltimateJobs plugin = UltimateJobs.getPlugin();
		plugin.getPlayerAPI().remCurrentJobs(""+p.getUniqueId(), job);
	}
	
	public boolean canHaveJob(Player p) {
		UltimateJobs plugin = UltimateJobs.getPlugin();
		
		List<String> jobs = plugin.getPlayerAPI().getCurrentJobs(""+p.getUniqueId());
	
		int max = plugin.getPlayerAPI().getMaxJobs(""+p.getUniqueId())-1;
 
			if(jobs.size() <= max) {
				return true;
			}
		 
		return false;
		
	}

	public void buyJobAndRemoveMoney(Player p, String mode, String much, String job) {
		UltimateJobs plugin = UltimateJobs.getPlugin();
		
		plugin.getPlayerAPI().addOwnJob(""+p.getUniqueId(), job);
		
		if(mode.toLowerCase().equalsIgnoreCase("VAULT")) {
			double money = Double.parseDouble(much);
			 plugin.getEconomy().withdrawPlayer(p, money);
		} else 	if(mode.toLowerCase().equalsIgnoreCase("VANILLA")) {
			int price = Integer.parseInt(much);
			p.setLevel(p.getLevel()-price);
		}
	}
	
	public String getJobInDisplay(String uuid) {
		UltimateJobs plugin = UltimateJobs.getPlugin();
		if(plugin.getPlayerAPI().getCurrentJobs(uuid).size() == 0) {
			return toHex(plugin.getTranslation().getString("Translation.GUI_None_Jobs"));
		}
		return ""+plugin.getPlayerAPI().getCurrentJobs(uuid).size();
	}
	
	public int getSlotOfJob(String id) {
		YamlConfiguration config = getConfigOfJob(id);
		
		int price  = config.getInt("Slot");

		return price;
	}
	
	public String getJobPriceMode(String id) {
		YamlConfiguration config = getConfigOfJob(id);
 
		String mode  = config.getString("Price.Mode");
 
		return mode;
	}
	
	public String getJobPriceAsPrice(String id) {
		YamlConfiguration config = getConfigOfJob(id);
 
		String mode  = config.getString("Price.Price");
 
		return mode;
	}
	
	public String getJobPrice(String id) {
		YamlConfiguration config = getConfigOfJob(id);
		UltimateJobs plugin = UltimateJobs.getPlugin();
		
		String mode  = config.getString("Price.Mode");

		String r = null;
		
		String much = config.getString("Price.Price");
		
		if(mode.toUpperCase().equalsIgnoreCase("VAULT")) {
			r = ""+much+" "+toHex(plugin.getTranslation().getString("Translation.GUI_Money_Translation"));
		} else if(mode.toUpperCase().equalsIgnoreCase("VANILLA")) {
			r = ""+much+" "+toHex(plugin.getTranslation().getString("Translation.GUI_Vanilla_Translation"));
		}
		 
		return r;
	}
	
	public String getJobMaterial(String id) {
		YamlConfiguration config = getConfigOfJob(id);
 
		String mat = config.getString("Material");
 
		return mat;
	} 
	public List<String> getLoreOfJob(String uuid, String id) {
		
		UltimateJobs plugin = UltimateJobs.getPlugin();
		
		//check if player has job and check if player is in job
		
		List<String> lore =  null;
 
		if(plugin.getPlayerAPI().isInJob(uuid, id)) {
			lore = UltimateJobs.getJobsGUIConfig().getStringList("Lore_InJob");
		} else if(plugin.getPlayerAPI().ownJob(uuid, id)) {
			lore = UltimateJobs.getJobsGUIConfig().getStringList("Lore_Bought");
		} else {
			lore = UltimateJobs.getJobsGUIConfig().getStringList("Lore_Need");
		}
		
		
		return lore;
	}
	
	public List<String> getItemFlags(String id) {
		YamlConfiguration config = getConfigOfJob(id);
 
		List<String> lore = config.getStringList("ItemFlag");
 
		return lore;
	}
	
	public String getJobDisplay(String id) {
		YamlConfiguration config = getConfigOfJob(id);
		
		if( config.getString("Display") == null) {
			 UltimateJobs.getPlugin().getLogger().warning("§cThe option §aCONFIG.GET(DISPLAY) §cdoesnt exist in config of: §a"+id);
				 
		}
		
		String name = config.getString("Display").replaceAll("&", "§");
		
		String finalname =  toHex(name);
		
		return finalname;
	}
	
	private static final Pattern pattern = Pattern.compile("(?<!\\\\)(#[a-fA-F0-9]{6})");

    public static String toHex(String motd) {
        Matcher matcher = pattern.matcher(motd);
        while (matcher.find()) {
            String color = motd.substring(matcher.start(), matcher.end());
            motd = motd.replace(color, "" + ChatColor.of(color));
        }

        return motd;
    }
    
	public ArrayList<String> getJobsWithAction(Action at) {
		
		ArrayList<String> list = new ArrayList<String>();
 
				
				List<String> jobs = UltimateJobs.getJobsListConfig().getStringList("Jobs");
				
				for(String job : jobs) {
				
					if(getConfigOfJob(job) == null) {
						 UltimateJobs.getPlugin().getLogger().warning("§cThe Config for Job §a"+job+" §cdoesnt exist!");
							 
					}
					
					YamlConfiguration config = getConfigOfJob(job);
			 
					if(config.getString("Action") == null) {
						 UltimateJobs.getPlugin().getLogger().warning("§cThe option §aCONFIG.GET(ACTION) §cdoesnt exist in config of: §a"+job);
						 
					}
				 
					String action = config.getString("Action");
					 
					if(Action.valueOf(action) == null) {
						 UltimateJobs.getPlugin().getLogger().warning("§cThe action §a"+action+" §cdoesnt exist for the Job §a"+job+"§c!");
					 
					}
			 
					if(Action.valueOf(action).equals(at)) {
					 
						list.add(job);
					}
					
				}
 
		return list;
		
	}
	
	public YamlConfiguration getConfigOfJob(String id) {
 
	    File joblist;
	    YamlConfiguration jl = null;
	    
		joblist = new File(UltimateJobs.getPlugin().getDataFolder()+"/jobs/", id.toUpperCase()+".yml");
		 if (!joblist.exists()) {
			    UltimateJobs.getPlugin().getLogger().warning("§cThe requested file §a"+id.toUpperCase()+" §cisnt existing!");
		    	return null;
		    }
		        jl= new YamlConfiguration();
		        try {
		        	jl.load(joblist);
		        } catch (IOException | InvalidConfigurationException e) {
		            e.printStackTrace();
		        }
 
		return jl;
		
	}

}
