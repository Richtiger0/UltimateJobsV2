package de.warsteiner.ultimatejobs.utils.builder;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import de.warsteiner.ultimatejobs.UltimateJobs;
import de.warsteiner.ultimatejobs.command.PlayerJobCommand; 
 
public class GuiBuilder {

	public Inventory createGui(Player p, int size, String name) {
		final Inventory inv = Bukkit.createInventory(null, size, UltimateJobs.getAPI().toHex(name).replaceAll("&", "§"));
		return inv;
	}
 
	public void runAction(Player p, String display, YamlConfiguration cfg, String path, String path2, String job) {
			List<String> custom_items = cfg.getStringList(path);
			UltimateJobs plugin = UltimateJobs.getPlugin();
			YamlConfiguration ms = plugin.getTranslation();
			for(String b : custom_items) {
				
				String dis = cfg.getString(path2+"."+b+".Display").replaceAll("<name>", p.getName()).replaceAll("&", "§");
				 
				if(display.equalsIgnoreCase(dis)) {
					
					String action = cfg.getString(path2+"."+b+".Action");
					String mode = cfg.getString("Jobs_UpdateMode").toUpperCase();
					
					if(action.equalsIgnoreCase("CLOSE")) {
						p.closeInventory();
							
						return;
					} else if(action.equalsIgnoreCase("LEAVEALL")) {
					 
							plugin.getPlayerAPI().setCurrentJobsToNull(""+p.getUniqueId());
							
							String message = ms.getString("Translation.Prefix") + ms.getString("Translation.LeftALL");
							p.sendMessage(plugin.getAPI().toHex(message).replaceAll("&", "§"));
							
							if(mode.equalsIgnoreCase("CLOSE")) {
								p.closeInventory();
							} else 	if(mode.equalsIgnoreCase("REOPEN")) {
								plugin.getBuilder().setCustomItemsInInventory(p.getOpenInventory(), p, plugin.getJobsGUIConfig(), "Custom_Items", "Custom_Items_Used", plugin.getJobsGUIConfig().getStringList("PlaceHolders"));
								plugin.getBuilder().setJobsItems(p.getOpenInventory(), p);
							}
							
						 return;
					}  else if(action.equalsIgnoreCase("COMMAND")) {
						p.closeInventory();
						
						String command =cfg.getString(path2+"."+b+".Command").replaceAll("<id>", plugin.getAPI().getConfigIdOfJob(job));
				 
						p.performCommand(command);
						
						return;
					}  else if(action.equalsIgnoreCase("NOTHING")) {
						return;
					} else if(action.equalsIgnoreCase("MAIN")) {
						String name = plugin.getJobsGUIConfig().getString("Name").replaceAll("&", "§");
						int size = plugin.getJobsGUIConfig().getInt("Size");
						 
						p.openInventory(UltimateJobs.getBuilder().createGui(p, 9*size, plugin.getAPI().toHex(name)));
					 
						plugin.getBuilder().setCustomItemsInInventory(p.getOpenInventory(), p, plugin.getJobsGUIConfig(), "Custom_Items", "Custom_Items_Used", plugin.getJobsGUIConfig().getStringList("PlaceHolders"));
						plugin.getBuilder().setJobsItems(p.getOpenInventory(), p);
						return;
					} else if(action.equalsIgnoreCase("LEAVE")) {
						
						plugin.getAPI().leaveJob(p, job);
						
						String name = plugin.getJobsGUIConfig().getString("Name").replaceAll("&", "§");
						int size = plugin.getJobsGUIConfig().getInt("Size");
						 
						p.openInventory(UltimateJobs.getBuilder().createGui(p, 9*size, plugin.getAPI().toHex(name)));
						if(mode.equalsIgnoreCase("CLOSE")) {
							p.closeInventory();
						} else 	if(mode.equalsIgnoreCase("REOPEN")) {
							plugin.getBuilder().setCustomItemsInInventory(p.getOpenInventory(), p, plugin.getJobsGUIConfig(), "Custom_Items", "Custom_Items_Used", plugin.getJobsGUIConfig().getStringList("PlaceHolders"));
							plugin.getBuilder().setJobsItems(p.getOpenInventory(), p);
						}
						return;
					}
					
				} 
			}
	}
  
	public void setAdminItems(InventoryView inventory, Player p) {
		
		ArrayList<String> list = new ArrayList<String>();
		
		list.add("BLACK_STAINED_GLASS_PANE:0:&7");
		list.add("BLACK_STAINED_GLASS_PANE:1:&7");
		list.add("BLACK_STAINED_GLASS_PANE:2:&7");
		list.add("BLACK_STAINED_GLASS_PANE:3:&7");
		list.add("BLACK_STAINED_GLASS_PANE:4:&7");
		list.add("BLACK_STAINED_GLASS_PANE:5:&7");
		list.add("BLACK_STAINED_GLASS_PANE:6:&7");
		list.add("BLACK_STAINED_GLASS_PANE:7:&7");
		list.add("BLACK_STAINED_GLASS_PANE:8:&7");
		list.add("BLACK_STAINED_GLASS_PANE:9:&7");
		list.add("BLACK_STAINED_GLASS_PANE:17:&7");
		list.add("BLACK_STAINED_GLASS_PANE:18:&7");
		list.add("BLACK_STAINED_GLASS_PANE:26:&7");
		list.add("BLACK_STAINED_GLASS_PANE:27:&7");
		list.add("BLACK_STAINED_GLASS_PANE:35:&7");
		list.add("BLACK_STAINED_GLASS_PANE:36:&7");
		list.add("BLACK_STAINED_GLASS_PANE:37:&7");
		list.add("BLACK_STAINED_GLASS_PANE:38:&7");
		list.add("BLACK_STAINED_GLASS_PANE:39:&7");
		list.add("BLACK_STAINED_GLASS_PANE:40:&7");
		list.add("BLACK_STAINED_GLASS_PANE:41:&7");
		list.add("BLACK_STAINED_GLASS_PANE:42:&7");
		list.add("BLACK_STAINED_GLASS_PANE:43:&7");
		list.add("BLACK_STAINED_GLASS_PANE:44:&7");
	 
		setPlaceHolderItems(inventory, p, list);
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				
				new BukkitRunnable() {

					@Override
					public void run() {
						if(inventory.getItem(21) == null) {
							ItemStack item = createItemStack(p, "GREEN_DYE");
							ItemMeta meta = item.getItemMeta();
							meta.setDisplayName("§8< §aCreate new Job §8>");
						 
							item.setItemMeta(meta);
					  
							inventory.setItem(21, item);
					 } 
					 if(inventory.getItem(23) == null) {
							ItemStack item = createItemStack(p, "BOOK");
							ItemMeta meta = item.getItemMeta();
							meta.setDisplayName("§8< §fManage existing Jobs §8>");
						 
							item.setItemMeta(meta);
					  
							inventory.setItem(23, item);
					 }
					}
					
				}.runTaskLater(UltimateJobs.getPlugin(), 2);
			     
			}
		}.runTaskAsynchronously(UltimateJobs.getPlugin()); 
	}
	
	public void setPlaceHolderItems(InventoryView inventory, Player p, List<String> list) {
		 
		new BukkitRunnable() {
			
			@Override
			public void run() {
			       for (String pl : list) {
					    String[] t = pl.split(":");
					    
					   	String mat = t[0];
				       	int slot = Integer.valueOf(t[1]).intValue();
					   	String display = t[2];
				 
						ItemStack item = createItemStack(p, mat);
						ItemMeta meta = item.getItemMeta();
						meta.setDisplayName(display.replaceAll("&", "§"));
						item.setItemMeta(meta);
				 
						inventory.setItem(slot, null);
						inventory.setItem(slot, item);
					 
					
				}
				
			}
		}.runTaskAsynchronously(UltimateJobs.getPlugin()); 
	}
	
	public void setCustomItemsInInventory(InventoryView inventory, Player p, YamlConfiguration config, String path2, String path3, List<String> list) {
		UltimateJobs plugin = UltimateJobs.getPlugin();
		setPlaceHolderItems(inventory, p, list);
		new BukkitRunnable() {
			
			@Override
			public void run() {
				 
				new BukkitRunnable() {
					
					@Override
					public void run() {
		  
						List<String> used = config.getStringList(path3);
						 
						for(String i : used) {
							String path = path2+"."+i+".";
							
							String display =  plugin.getAPI().toHex(config.getString(path+"Display"));
							String icon =  config.getString(path+"Icon");
							boolean show =  config.getBoolean(path+"Show_Only_If_InJob");
							boolean enchanted =  config.getBoolean(path+"Enchanted");
							List<String> flags =  config.getStringList(path+"ItemFlags");
							int slot =  config.getInt(path+"Slot");
							List<String> lore =  config.getStringList(path+"Lore");
							ArrayList<String> list = new ArrayList<String>();
							
							ItemStack item = createItemStack(p, icon);
							ItemMeta meta = item.getItemMeta();
							meta.setDisplayName(display.replaceAll("&", "§"));
							
							if(enchanted) {
								meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
							}
						 
							for(String f : flags) {
								meta.addItemFlags(ItemFlag.valueOf(f));
							}
				 
							for(String l : lore) {
								list.add(plugin.getAPI().toHex(l).replaceAll("<jobs>", plugin.getAPI().getJobInDisplay(""+p.getUniqueId())).replaceAll("&", "§"));
							}
							meta.setLore(list);
							item.setItemMeta(meta);
					 
							boolean b = true;
							
							if(show) {
								 if(plugin.getPlayerAPI().getCurrentJobs(""+p.getUniqueId()).size() == 0) {
									 b = false;
								 }
							}
							 

							if(b) {
								inventory.setItem(slot, null);
								inventory.setItem(slot, item);
							}
							 
						}
						
					}
				}.runTaskAsynchronously(plugin); 
			}
		}.runTaskLater(plugin, 2);
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
					String price = plugin.getAPI().getJobPrice(job);
					String mat = plugin.getAPI().getJobMaterial(job);
					List<String> lore = plugin.getAPI().getLoreOfJob(""+p.getUniqueId(), job);
					List<String> itemflags = plugin.getAPI().getItemFlags(job);
		 
					ArrayList<String> list = new ArrayList<String>();
					
					ItemStack item = createItemStack(p, mat);
					
					ItemMeta meta = item.getItemMeta();
					
					for(String i : itemflags) {
						meta.addItemFlags(ItemFlag.valueOf(i));
					}
					
					if(plugin.getPlayerAPI().isInJob(""+p.getUniqueId(), job)) {
						meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
					}
					
					meta.setDisplayName(plugin.getAPI().toHex(display));
					 
					int level = plugin.getPlayerAPI().getJobLevel(""+p.getUniqueId(), job);
					double exp = plugin.getPlayerAPI().getJobExp(""+p.getUniqueId(), job);
					String formatexp = plugin.getAPI().FormatAsExp(exp);
			 
					boolean ismax = plugin.getLevelAPI().PlayeLevelIsAlreadyMaxed(""+p.getUniqueId(), job);
					
					String expasstring = null;
					String needasstring = null;
					
					if(ismax) {
						expasstring = "0";
						needasstring = plugin.getAPI().toHex(plugin.getTranslation().getString("Levels.MaxedInGui"));
					 } else {
						 expasstring = ""+exp;
							needasstring =""+plugin.getLevelAPI().getJobNeedExp(""+p.getUniqueId(), job);
					 }
					
					String levelname = plugin.getLevelAPI().getDisPlayOfLevel(""+p.getUniqueId(), job, level);
					
					for(String i : lore) {
						list.add(plugin.getAPI().toHex(i).replaceAll("<level_as_name>", levelname).replaceAll("<need>", needasstring).replaceAll("<exp>", expasstring).replaceAll("<level_as_int>", ""+level).replaceAll("<price>", ""+price).replaceAll("&", "§"));
					}
					
					meta.setLore(list);
				 
					item.setItemMeta(meta);
					inventory.setItem(slot, null);
					inventory.setItem(slot, item);
					
				}
				
			}
		}.runTaskAsynchronously(UltimateJobs.getPlugin());
	}
	
	public ItemStack createItemStack(Player p, String item) {
		
		ItemStack i;
		
		if(Material.getMaterial(item) == null) {
			i = generateSkull(item.replaceAll("<skull>", p.getName()));
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
