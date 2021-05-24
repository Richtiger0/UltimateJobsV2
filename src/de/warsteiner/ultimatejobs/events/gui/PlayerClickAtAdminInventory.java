package de.warsteiner.ultimatejobs.events.gui;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.warsteiner.ultimatejobs.UltimateJobs;
import de.warsteiner.ultimatejobs.utils.api.other.SetUpManager;

public class PlayerClickAtAdminInventory implements Listener  {
	
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
	if (e.getClickedInventory() == null) {
		return;
	}
	if (e.getCurrentItem() == null) {
		return;
	}

	if (e.getView().getTitle() == null) {
		return;
	}

	if(e.getCurrentItem().getItemMeta() == null) {
		return;
	}

	if(e.getCurrentItem().getItemMeta().getDisplayName() == null) {
		return;
	}
	   
	 Player p = (Player)e.getWhoClicked();

	 UltimateJobs plugin = UltimateJobs.getPlugin();
	 YamlConfiguration ms = plugin.getTranslation();
 
	 String display = e.getCurrentItem().getItemMeta().getDisplayName();
	  
	if (e.getView().getTitle().contains("§bUltimateJobs")) {
		
		e.setCancelled(true);
		
		String[] sp = e.getView().getTitle().split(":");
		
		String s1 = sp[1].replaceAll("§a", " ").replaceAll(" ", "");
		
 
		if (s1.equalsIgnoreCase(plugin.getDescription().getVersion())) {
			if(display.equalsIgnoreCase("§8< §aCreate new Job §8>")) {
			   p.closeInventory();
			   SetUpManager.startStep(p, 1, true);
			} else if(display.equalsIgnoreCase("§8< §fManage existing Jobs §8>")) {
				p.sendMessage("§cThis feature is currently in work!");
			}
		}
		
	}
	}
}
