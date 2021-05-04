package de.warsteiner.ultimatejobs.command;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import de.warsteiner.ultimatejobs.UltimateJobs;

public class PlayerJobCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		Player p = (Player) s;
		
		int length = args.length;
		
		UltimateJobs plugin = UltimateJobs.getPlugin();
		
		YamlConfiguration tr = plugin.getTranslation();
		
		YamlConfiguration cmd = plugin.getCommandConfig();
		
		if(length == 0) { 
			String name = plugin.getJobsGUIConfig().getString("Name").replaceAll("&", "§");
			int size = plugin.getJobsGUIConfig().getInt("Size");
			 
			p.openInventory(UltimateJobs.getBuilder().createGui(p, 9*size, plugin.getAPI().toHex(name)));
			plugin.getBuilder().setJobsItems(p.getOpenInventory(), p);
			plugin.getBuilder().setPlaceHolderItems(p.getOpenInventory(), p, plugin.getJobsGUIConfig().getStringList("PlaceHolders"));
			plugin.getBuilder().setCustomItemsInInventory(p.getOpenInventory(), p, plugin.getJobsGUIConfig(), "Custom_Items", "Custom_Items_Used");
			return true;
		} else 	if(length == 1
				&& args[0].equalsIgnoreCase(cmd.getString("Command.Help.Usage"))) {
			
			if(!cmd.getBoolean("Command.Help.Use")) {
				p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Help.Disabled").replaceAll("&", "§")));
				return true;
			}
			 
			if(!p.hasPermission(cmd.getString("Command.Help.Permission"))) {
				p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Help.NoPermMessage").replaceAll("&", "§")));
				return true;
			}
			
			sendHelp(p, cmd, 1);
			return true;
		} else 	if(length == 2
				&& args[0].equalsIgnoreCase(cmd.getString("Command.Help.Usage"))
						&& args[1] != null) {
			
			if(!cmd.getBoolean("Command.Help.Use")) {
				p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Help.Disabled").replaceAll("&", "§")));
				return true;
			}
			 
			if(!p.hasPermission(cmd.getString("Command.Help.Permission"))) {
				p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Help.NoPermMessage").replaceAll("&", "§")));
				return true;
			}
			
			String pg = args[1];
			
			if(!isInt(pg)) {
				p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Help.NotNumber").replaceAll("&", "§")));
				return true;
			}
			
			if(!cmd.contains("Command.Help.Page_"+pg)) {
				p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Help.PageNotFound").replaceAll("<page>", pg).replaceAll("&", "§")));
				return true;
			}
			
			sendHelp(p, cmd, Integer.parseInt(pg));
			return true;
		}
		
		return false;
	
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

	public void sendHelp(Player p, YamlConfiguration cmd, int page) {
		List<String> list = cmd.getStringList("Command.Help.Page_"+page);
		
		for(String l : list) {
			p.sendMessage(UltimateJobs.getAPI().toHex(l.replaceAll("&", "§")));
		}
	}
	
}

















