package de.warsteiner.ultimatejobs.events;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import de.warsteiner.ultimatejobs.UltimateJobs;
import de.warsteiner.ultimatejobs.command.PlayerJobCommand;
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
					SetUpManager.players.put(p, ""+job);
					
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
				} else {
					
					String value = mg.toUpperCase();
					
					String old = SetUpManager.players.get(p);
					
					SetUpManager.players.remove(p);
					SetUpManager.players.put(p, old+":"+value);
					
					String m = tr.getString("Translation.Prefix")
							  + " §7Job Action§8: §a"+value;
					  p.sendMessage(plugin.getAPI().toHex(m).replaceAll("&", "§"));   
					
					SetUpManager.startStep(p, 3, false);
					return;
				}
				
			} else if(step == 3) {
				
				 
					String value = mg.toUpperCase();
					
					String old = SetUpManager.players.get(p);
					
					SetUpManager.players.remove(p);
					SetUpManager.players.put(p, old+":"+value);
					
					String m = tr.getString("Translation.Prefix")
							  + " §7Job Display§8: §a"+value;
					  p.sendMessage(plugin.getAPI().toHex(m).replaceAll("&", "§"));   
					
					SetUpManager.startStep(p, 4, false);
					return;
				 
				
			}  else if(step == 4) {
				
				 
				String value = mg;
				
				String old = SetUpManager.players.get(p);
				
				SetUpManager.players.remove(p);
				SetUpManager.players.put(p, old+":"+value);
				
				String m = tr.getString("Translation.Prefix")
						  + " §7Job Material§8: §a"+value;
				  p.sendMessage(plugin.getAPI().toHex(m).replaceAll("&", "§"));   
				
				SetUpManager.startStep(p, 5, false);
				return;
			 
			
		}  else if(step == 5) {
			
			 
			String value = mg;
			
			if(!isInt(value)) {
				String m = tr.getString("Translation.Prefix")
						  + " §cThe Value must be a Int!";
				  p.sendMessage(plugin.getAPI().toHex(m).replaceAll("&", "§"));   
				return;
			} else {
				String old = SetUpManager.players.get(p);
				
				SetUpManager.players.remove(p);
				SetUpManager.players.put(p, old+":"+value);
				
				String m = tr.getString("Translation.Prefix")
						  + " §7Job Slot§8: §a"+value;
				  p.sendMessage(plugin.getAPI().toHex(m).replaceAll("&", "§"));   
				
				SetUpManager.startStep(p, 6, false);
				return;
			}
		 
		 
	} else if(step == 6) {
		
		 
		String value = mg;
		
		if(!isInt(value)) {
			String m = tr.getString("Translation.Prefix")
					  + " §cThe Value must be a Int!";
			  p.sendMessage(plugin.getAPI().toHex(m).replaceAll("&", "§"));   
			return;
		} else {
			String old = SetUpManager.players.get(p);
			
			SetUpManager.players.remove(p);
			SetUpManager.players.put(p, old+":"+value);
			
			String m = tr.getString("Translation.Prefix")
					  + " §7Job Price§8: §a"+value;
			  p.sendMessage(plugin.getAPI().toHex(m).replaceAll("&", "§"));   
			
			SetUpManager.startStep(p, 7, false);
			return;
		} 
	 
	
 	} else if(step == 7) {
	
	 
	String value = mg;
	
 
		String old = SetUpManager.players.get(p);
		
		SetUpManager.players.remove(p);
		SetUpManager.players.put(p, old+":"+value);
		
		String m = tr.getString("Translation.Prefix")
				  + " §7Stats Name§8: §a"+value;
		  p.sendMessage(plugin.getAPI().toHex(m).replaceAll("&", "§"));   
		
		SetUpManager.startStep(p, 8, false);
		return;
	 
 

} else if(step == 8) {
	
	 
	String value = mg;
	
 
		String old = SetUpManager.players.get(p);
		
		SetUpManager.players.remove(p);
		SetUpManager.players.put(p, old+":"+value);
		
		String m = tr.getString("Translation.Prefix")
				  + " §7Stats ID of Count 1§8: §a"+value;
		  p.sendMessage(plugin.getAPI().toHex(m).replaceAll("&", "§"));   
		
		SetUpManager.startStep(p, 9, false);
		return;
	 
 

} else if(step == 9) {
	
	  
	String value = mg.toUpperCase();
	
		if(value.equalsIgnoreCase("FINISH")) {
			String old = SetUpManager.players.get(p);
		 
		  	SetUpManager.createJob(old);
			 
			SetUpManager.cancel(p, false);
			
			
			String m = tr.getString("Translation.Prefix")
					  + " §7Job created! Now manage this job in the Admin-GUI or open the config file! §cPlease note§8: §7You need to add first the IDS and the Levels of the Job. Without these there will be an issue!";
			  p.sendMessage(plugin.getAPI().toHex(m).replaceAll("&", "§"));   
		}
		
		return;
	 
 

}
			 
		    
		}
		
	}
	
	
	public boolean isInt(String m) {
		boolean b;
		try {
			 Integer.parseInt(m);
			 b = true;
			} catch (NumberFormatException ex){
			   b = false;
			}
		return b;
	}

}















