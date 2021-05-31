package de.warsteiner.ultimatejobs.utils;
 
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.warsteiner.ultimatejobs.UltimateJobs;

public class PlaceHolder extends PlaceholderExpansion
{
public String getIdentifier() {
 return "Jobs";
 }

 public String getPlugin() {
return "UltimateJobs";
 }
 
 public String getAuthor() {
 return "Warsteiner37";
}
 
public String getVersion() {
return "v2.0";
}

public String onPlaceholderRequest(Player player, String identifier) {
	UltimateJobs plugin = UltimateJobs.getPlugin();
	YamlConfiguration tr = plugin.getTranslation();
if (player == null || identifier == null) {
  return "PlayerIsNull";
 }
String uuid = ""+player.getUniqueId();
if (identifier.startsWith("current"))
{
	
		String[] split = identifier.split("_");
		
		int want = Integer.parseInt(split[1]);
		
		String job = null;
		
		if(plugin.getPlayerAPI().getCurrentJobs(uuid).size() <= want) {
			job = tr.getString("PlaceHolder.No_Job").replaceAll("&", "§");
		} else {
			
			String name = plugin.getAPI().getJobDisplay(plugin.getPlayerAPI().getCurrentJobs(uuid).get(want));
			
			job = name;
			
		}
		
      return job;
} 
 
if (identifier.startsWith("levelasint"))
{
	
		String[] split = identifier.split("_");
		
		int want = Integer.parseInt(split[1]);
		
		String job = null;
		
		if(plugin.getPlayerAPI().getCurrentJobs(uuid).size() <= want) {
			job = tr.getString("PlaceHolder.No_Level").replaceAll("&", "§");
		} else {
			
			int level = plugin.getPlayerAPI().getJobLevel(uuid, plugin.getPlayerAPI().getCurrentJobs(uuid).get(want));
			
			job = ""+level;
			
		}
		
      return job;
} 
if (identifier.startsWith("levelasname"))
{
	
		String[] split = identifier.split("_");
		
		int want = Integer.parseInt(split[1]);
		
		String job = null;
		
		if(plugin.getPlayerAPI().getCurrentJobs(uuid).size() <= want) {
			job = tr.getString("PlaceHolder.No_Level").replaceAll("&", "§");
		} else {
			
			int level = plugin.getPlayerAPI().getJobLevel(uuid, plugin.getPlayerAPI().getCurrentJobs(uuid).get(want));
			
			job = ""+plugin.getLevelAPI().getDisPlayOfLevel(uuid, plugin.getPlayerAPI().getCurrentJobs(uuid).get(want), level);
			
		}
		
      return job;
} 
if (identifier.startsWith("exp"))
{
	
		String[] split = identifier.split("_");
		
		int want = Integer.parseInt(split[1]);
		
		String job = null;
		
		if(plugin.getPlayerAPI().getCurrentJobs(uuid).size() <= want) {
			job = tr.getString("PlaceHolder.No_Exp").replaceAll("&", "§");
		} else {
			
			if(!plugin.getLevelAPI().PlayeLevelIsAlreadyMaxed(uuid, plugin.getPlayerAPI().getCurrentJobs(uuid).get(want))) {
				double ex = plugin.getPlayerAPI().getJobExp(uuid, plugin.getPlayerAPI().getCurrentJobs(uuid).get(want));
				
				job = ""+plugin.getAPI().FormatAsExp(ex);
			} else {
				job = "0";
			}
			
		}
		
      return job;
} 

    return identifier;
 }
 }


 











