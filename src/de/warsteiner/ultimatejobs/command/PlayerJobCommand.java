package de.warsteiner.ultimatejobs.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import de.warsteiner.ultimatejobs.UltimateJobs;

public class PlayerJobCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		Player p = (Player) s;
		
		int length = args.length;
		
		UltimateJobs plugin = UltimateJobs.getPlugin();
		
		if(length == 0) { 
			String name = plugin.getJobsGUIConfig().getString("Name").replaceAll("&", "§");
			int size = plugin.getJobsGUIConfig().getInt("Size");
			 
			p.openInventory(UltimateJobs.getBuilder().createGui(p, 9*size, plugin.getAPI().toHex(name)));
			plugin.getBuilder().setJobsItems(p.getOpenInventory(), p);
			plugin.getBuilder().setPlaceHolderItems(p.getOpenInventory(), p, plugin.getJobsGUIConfig().getStringList("PlaceHolders"));
			plugin.getBuilder().setCustomItemsInInventory(p.getOpenInventory(), p, plugin.getJobsGUIConfig(), "Custom_Items");
		}
		
		return false;
	
	}

}
