package de.warsteiner.ultimatejobs.utils.addons.levelsgui;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.warsteiner.ultimatejobs.UltimateJobs; 

public class LevelsGUI {

	public static Inventory get(Player p, String job, int page) {
		UltimateJobs plugin = UltimateJobs.getPlugin();
		
		YamlConfiguration cfg = plugin.getLevelsGUIConfig();
		
		String disjob = plugin.getAPI().getJobDisplay(job);
		
		String name = plugin.getAPI().toHex(cfg.getString("Name").replaceAll("<page>", ""+page).replaceAll("<job>", disjob).replaceAll("&", "§"));
		
		Inventory inv = plugin.getBuilder().createGui(p, cfg.getInt("Size")*9, name);
		
		List<String> slots = cfg.getStringList("LevelSlots."+page);
		
		int start = 0;
		int end = slots.size();
		
		Bukkit.getScheduler().runTaskAsynchronously(UltimateJobs.getPlugin(), new Runnable() {

			@Override
			public void run() {
				  for (int i = start; i < end; i++) {
					 ItemStack item = new ItemStack(Material.BLACK_WOOL, 1);
				 
					 int real = i + 1;
					 
					 ItemMeta meta = item.getItemMeta();
					 
					 meta.setDisplayName("§bLevel : "+real);
					 
					 item.setItemMeta(meta);
					 
					 inv.setItem(Integer.valueOf(slots.get(i)), item);
					 
				  }
			}
			
		});
		
		return inv;
	}
	
}
