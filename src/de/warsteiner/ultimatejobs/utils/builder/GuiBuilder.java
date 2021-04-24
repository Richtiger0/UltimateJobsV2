package de.warsteiner.ultimatejobs.utils.builder;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import de.warsteiner.ultimatejobs.UltimateJobs;
 
public class GuiBuilder {

	public Inventory createGui(Player p, int size, String name) {
		final Inventory inv = Bukkit.createInventory(null, size, name);
		return inv;
	}
	
	public void setJobsItems(InventoryView inventory, Player p) {
		UltimateJobs plugin = UltimateJobs.getPlugin();
		new BukkitRunnable() {
			
			@Override
			public void run() {
				
				List<String> jobs = plugin.getJobsListConfig().getStringList("Jobs");
				
				for(String job : jobs) {
					
					String display = plugin.getAPI().getJobDisplay(job);
					int slot = plugin.getAPI().getSlotOfJob(job);
					double price = plugin.getAPI().getJobPrice(job);
					String mat = plugin.getAPI().getJobMaterial(job);
					List<String> lore = plugin.getAPI().getLoreOfJob(job);
					List<String> itemflags = plugin.getAPI().getItemFlags(job);
					
					ArrayList<String> list = new ArrayList<String>();
					
					ItemStack item = createItemStack(mat);
					
					ItemMeta meta = item.getItemMeta();
					
					for(String i : itemflags) {
						meta.addItemFlags(ItemFlag.valueOf(i));
					}
					
					meta.setDisplayName(plugin.getAPI().toHex(display));
					 
					for(String i : lore) {
						list.add(plugin.getAPI().toHex(i).replaceAll("&", "§"));
					}
					
					meta.setLore(list);
				 
					item.setItemMeta(meta);
					
					inventory.setItem(slot, item);
					
				}
				
			}
		}.runTaskAsynchronously(UltimateJobs.getPlugin());
	}
	
	public ItemStack createItemStack(String item) {
		
		ItemStack i;
		
		if(Material.getMaterial(item) == null) {
			i = generateSkull(item);
		} else {
			i = new ItemStack(Material.valueOf(item),1);
		}
		return i;
	}
	
	public static ItemStack generateSkull(String owner) {
		 ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
		 SkullMeta skullMeta = (SkullMeta)itemStack.getItemMeta();
		skullMeta.setOwner(owner);
		itemStack.setItemMeta((ItemMeta)skullMeta);
		return itemStack;
	}
	
}
