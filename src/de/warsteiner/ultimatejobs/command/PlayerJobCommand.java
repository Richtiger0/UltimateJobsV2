package de.warsteiner.ultimatejobs.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.util.BlockIterator;

import de.warsteiner.ultimatejobs.UltimateJobs;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class PlayerJobCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		Player p = (Player) s;
		
		int length = args.length;
		
		UltimateJobs plugin = UltimateJobs.getPlugin();
		
		YamlConfiguration tr = plugin.getTranslation();
		
		YamlConfiguration cmd = plugin.getCommandConfig();
		
		String prefix = plugin.getAPI().toHex(tr.getString("Translation.Prefix").replaceAll("&", "§"));
		
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
				p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Help.Disabled").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				return true;
			}
			 
			if(!p.hasPermission(cmd.getString("Command.Help.Permission"))) {
				p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Help.NoPermMessage").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				return true;
			}
			
			sendHelp(p, cmd, 1);
			return true;
		}else 	if(length == 1
				&& args[0].equalsIgnoreCase(cmd.getString("Command.Limit.Usage"))) {
			
			if(!cmd.getBoolean("Command.Limit.Use")) {
				p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Limit.Disabled").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				return true;
			}
			 
			if(!p.hasPermission(cmd.getString("Command.Limit.Permission"))) {
				p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Limit.NoPermMessage").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				return true;
			}
			
			List<String> jobs = plugin.getPlayerAPI().getCurrentJobs(""+p.getUniqueId());
			int max = plugin.getPlayerAPI().getMaxJobs(""+p.getUniqueId());
	 
			p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Limit.Message").replaceAll("<prefix>", prefix).replaceAll("<count>", ""+max).replaceAll("<current>", ""+jobs.size()).replaceAll("&", "§")));
 
			return true;
		}  else 	if(length == 1
				&& args[0].equalsIgnoreCase(cmd.getString("Command.Browse.Usage"))) {
			
			if(!cmd.getBoolean("Command.Browse.Use")) {
				p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Browse.Disabled").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				return true;
			}
			 
			if(!p.hasPermission(cmd.getString("Command.Browse.Permission"))) {
				p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Browse.NoPermMessage").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				return true;
			}
			
			List<String> jobs = plugin.getJobsListConfig().getStringList("Jobs");
			
			p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Browse.Message").replaceAll("<prefix>", prefix).replaceAll("<count>", ""+jobs.size()).replaceAll("&", "§")));
			String format = plugin.getAPI().toHex(cmd.getString("Command.Browse.Format").replaceAll("<prefix>", prefix).replaceAll("&", "§"));
 
			for(String job : jobs) {
				
				String state = null;
				
				if(plugin.getPlayerAPI().isInJob(""+p.getUniqueId(), job)) {
					state = plugin.getAPI().toHex(cmd.getString("Command.Browse.States.In").replaceAll("<prefix>", prefix).replaceAll("&", "§"));
				} else 	if(plugin.getPlayerAPI().ownJob(""+p.getUniqueId(), job)) {
					state = plugin.getAPI().toHex(cmd.getString("Command.Browse.States.Own").replaceAll("<prefix>", prefix).replaceAll("&", "§"));
				} else {
					state = plugin.getAPI().toHex(cmd.getString("Command.Browse.States.Price").replaceAll("<prefix>", prefix).replaceAll("<price>", plugin.getAPI().getJobPrice(job)).replaceAll("&", "§"));
				}
				
				String name = plugin.getAPI().getJobDisplay(job);
				
				p.sendMessage(format.replaceAll("<state>", state).replaceAll("<prefix>", prefix).replaceAll("<job>", name).replaceAll("&", "§"));
			}
			
			return true;
		} else 	if(length == 1
				&& args[0].equalsIgnoreCase(cmd.getString("Command.LeaveALL.Usage"))) {
			
			if(!cmd.getBoolean("Command.LeaveALL.Use")) {
				p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.LeaveALL.Disabled").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				return true;
			}
			 
			if(!p.hasPermission(cmd.getString("Command.LeaveALL.Permission"))) {
				p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.LeaveALL.NoPermMessage").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				return true;
			}
			
			List<String> jobs = plugin.getPlayerAPI().getCurrentJobs(""+p.getUniqueId());
			
			if(jobs.size() == 0) {
				p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.LeaveALL.NotInAny").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				return true;
			}
			
			plugin.getPlayerAPI().setCurrentJobsToNull(""+p.getUniqueId());
			p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.LeaveALL.Left").replaceAll("<prefix>", prefix).replaceAll("<count>", ""+jobs.size()).replaceAll("&", "§")));
 
			return true;
		}  else 	if(length == 2
				&& args[0].equalsIgnoreCase(cmd.getString("Command.Info.Usage"))
						&& args[1] != null) {
			
			if(!cmd.getBoolean("Command.Info.Use")) {
				p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Info.Disabled").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				return true;
			}
			 
			if(!p.hasPermission(cmd.getString("Command.Info.Permission"))) {
				p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Info.NoPermMessage").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				return true;
			}
			
			String job = args[1];
			
			if(!plugin.getAPI().existJobWithConfigID(job)) {
				p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Info.NoJob").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				return true;
			}
			
			List<String> ids = plugin.getAPI().getIDList(job);
			 
			p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Info.Message").replaceAll("<prefix>", prefix).replaceAll("<job>", plugin.getAPI().getJobDisplay(job)).replaceAll("&", "§")));
			String format = plugin.getAPI().toHex(cmd.getString("Command.Info.Format").replaceAll("<prefix>", prefix).replaceAll("&", "§"));
			for(String b : ids) {
				
				String block = plugin.getAPI().getNameOfBlock(job, b);
				double rw = plugin.getAPI().getRewardOfID(job, b);
				//"&8- &7<block> &7equals to &b<dollar> Dollars"
				p.sendMessage(format.replaceAll("<dollar>", ""+rw).replaceAll("<block>", block).replaceAll("<prefix>", prefix).replaceAll("&", "§"));
			}
			
			return true;
		} else 	if(length == 2
				&& args[0].equalsIgnoreCase(cmd.getString("Command.Help.Usage"))
				&& args[1] != null) {
	
	if(!cmd.getBoolean("Command.Help.Use")) {
		p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Help.Disabled").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
		return true;
	}
	 
	if(!p.hasPermission(cmd.getString("Command.Help.Permission"))) {
		p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Help.NoPermMessage").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
		return true;
	}
	
	String pg = args[1];
	
	if(!isInt(pg)) {
		p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Help.NotNumber").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
		return true;
	}
	
	if(!cmd.contains("Command.Help.Page_"+pg)) {
		p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Help.PageNotFound").replaceAll("<prefix>", prefix).replaceAll("<page>", pg).replaceAll("&", "§")));
		return true;
	}
	
	sendHelp(p, cmd, Integer.parseInt(pg));
	return true;
} else 	if(length == 2
		&& args[0].equalsIgnoreCase(cmd.getString("Command.Join.Usage"))
		&& args[1] != null) {

			if(!cmd.getBoolean("Command.Join.Use")) {
			p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Join.Disabled").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
			return true;
			}
			
			if(!p.hasPermission(cmd.getString("Command.Join.Permission"))) {
			p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Join.NoPermMessage").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
			return true;
			}
			
			String job = args[1];
 
			if(!plugin.getAPI().existJobWithConfigID(job)) {
				p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Join.NoJob").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				return true;
			}
			
			if(!plugin.getPlayerAPI().ownJob(""+p.getUniqueId(), job)) {
				p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Join.Dont_Own").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				return true;
			}
			
			if(plugin.getPlayerAPI().isInJob(""+p.getUniqueId(), job)) {
				p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Join.Already").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				return true;
			}
			
			List<String> jobs = plugin.getPlayerAPI().getCurrentJobs(""+p.getUniqueId());
			
			int max = plugin.getPlayerAPI().getMaxJobs(""+p.getUniqueId())-1;
	 
				if(jobs.size() <= max) {
					plugin.getPlayerAPI().addCurrentJobs(""+p.getUniqueId(), job.toUpperCase());
					p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Join.Joined").replaceAll("<prefix>", prefix).replaceAll("<job>", plugin.getAPI().getJobDisplay(job)).replaceAll("&", "§")));
					return true;
				} else {
					p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Join.Max").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
					return true;
				}
			  
			} else 	if(length == 2
					&& args[0].equalsIgnoreCase(cmd.getString("Command.Leave.Usage"))
					&& args[1] != null) {

						if(!cmd.getBoolean("Command.Leave.Use")) {
						p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Leave.Disabled").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
						return true;
						}
						
						if(!p.hasPermission(cmd.getString("Command.Leave.Permission"))) {
						p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Leave.NoPermMessage").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
						return true;
						}
						
						String job = args[1];
			 
						if(!plugin.getAPI().existJobWithConfigID(job)) {
							p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Leave.NoJob").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
							return true;
						}
	 
						if(!plugin.getPlayerAPI().isInJob(""+p.getUniqueId(), job)) {
							p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Leave.Not_InJob").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
							return true;
						}
						
	 
								plugin.getPlayerAPI().remCurrentJobs(""+p.getUniqueId(), job.toUpperCase());
								p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Leave.Left").replaceAll("<prefix>", prefix).replaceAll("<job>", plugin.getAPI().getJobDisplay(job)).replaceAll("&", "§")));
								return true;
						 
						   
						} else 	if(length == 2
								&& args[0].equalsIgnoreCase(cmd.getString("Command.Stats.Usage"))
								&& args[1] != null) {

									if(!cmd.getBoolean("Command.Stats.Use")) {
									p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Stats.Disabled").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
									return true;
									}
									
									if(!p.hasPermission(cmd.getString("Command.Stats.Permission"))) {
									p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Stats.NoPermMessage").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
									return true;
									}
									
									String mode = cmd.getString("Command.Stats.Mode").toUpperCase();
									
									String pl = args[1];
									
									//DeosntExist
									
									if(plugin.getPlayerAPI().getUUIDByName(pl) == null) {
										p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Stats.DeosntExist").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
										return true;
									}
						 
									String uuid = plugin.getPlayerAPI().getUUIDByName(pl);
									
									List<String> jobs = null;
									
									if(mode.equalsIgnoreCase("CURRENT")) {
										jobs = plugin.getPlayerAPI().getCurrentJobs(uuid);
									} else {
										jobs =plugin.getPlayerAPI().getOwn(uuid);
									}
									
									//    Message: "&8-> &7Player Stats&8: &c<name> &8(&bHover&8)"
								    
								  //  Format: "&8| <job> &8- <level_as_name> &8/ <level_as_int>"
									
									List<String> message_1 = cmd.getStringList("Command.Stats.Message_1");
									List<String> message_2 = cmd.getStringList("Command.Stats.Message_2");
									
									 
									
								 
									String format = plugin.getAPI().toHex(cmd.getString("Command.Stats.Format").replaceAll("<prefix>", prefix).replaceAll("&", "§"));;
									 
									if(jobs.size() != 0) {
										for(String b : message_1) {
											p.sendMessage(plugin.getAPI().toHex(b.replaceAll("<name>", pl).replaceAll("<prefix>", prefix).replaceAll("&", "§")));
										}
										for(String j : jobs) {
											
											YamlConfiguration cfg = plugin.getAPI().getConfigOfJob(j);
											
											int level = plugin.getPlayerAPI().getJobLevel(""+p.getUniqueId(), j);
											String levelname = plugin.getLevelAPI().getDisPlayOfLevel(""+p.getUniqueId(), j, level); 
											
											double exp = plugin.getPlayerAPI().getJobExp(""+p.getUniqueId(), j);
											String formatexp = plugin.getAPI().FormatAsExp(exp);
											
											boolean ismax = plugin.getLevelAPI().PlayeLevelIsAlreadyMaxed(""+p.getUniqueId(), j);
											
											String need = null;
											
											if(ismax) {
												need = plugin.getAPI().toHex(plugin.getTranslation().getString("Levels.MaxedInGui"));
											} else {
												need = plugin.getLevelAPI().getJobNeedExp(""+p.getUniqueId(), j);
											}
											
											String count1 = cfg.getString("Translation.Stats_Part_1");
											String cc1 = cfg.getString("Translation.Stats_Part_1_ID");
											
											String c1 = ""+plugin.getPlayerAPI().getCount1(""+p.getUniqueId(), j);
											
											String l = plugin.getAPI().toHex(cmd.getString("Command.Stats.List")
												.replaceAll("<id_1>", cc1)	.replaceAll("<c1>", c1).replaceAll("<st1>", count1).replaceAll("<exp>", ""+exp).replaceAll("<need>", need)	.replaceAll("<job>", plugin.getAPI().getJobDisplay(j))	.replaceAll("<level_as_string>", levelname).replaceAll("<level_as_int>", ""+level).replaceAll("<prefix>", prefix).replaceAll("&", "§"));;  
											
										  
											TextComponent message = new TextComponent(plugin.getAPI().toHex(format
											 
													.replaceAll("<level_as_int>", ""+level).replaceAll("<level_as_name>", levelname).replaceAll("<job>", plugin.getAPI().getJobDisplay(j)).replaceAll("&","§")));
											 
									         message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(l)));
		 
									        p.spigot().sendMessage(message);
										} 
										for(String b : message_2) {
											p.sendMessage(plugin.getAPI().toHex(b.replaceAll("<name>", pl).replaceAll("<prefix>", prefix).replaceAll("&", "§")));
										}
									} else {
										p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Stats.NoJobsOfPlayer").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
										return true;
									}
									
							    	 
							        
											return true;
									 
									  
									} else 	if(length == 2
											&& args[0].equalsIgnoreCase(cmd.getString("Command.Admin.Usage"))
											&& args[1].equalsIgnoreCase(cmd.getString("Command.Admin.BlockInfo.Usage"))) {

												if(!cmd.getBoolean("Command.Admin.Use")) {
												p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.Disabled").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
												return true;
												}
												
												if(!p.hasPermission(cmd.getString("Command.Admin.Permission"))) {
												p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.NoPermMessage").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
												return true;
												}
												
												if(!cmd.getBoolean("Command.Admin.BlockInfo.Use")) {
													p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.BlockInfo.Disabled").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
													return true;
													}
													
													if(!p.hasPermission(cmd.getString("Command.Admin.BlockInfo.Permission"))) {
													p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.BlockInfo.NoPermMessage").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
													return true;
													}
												
													Block block = getTargetBlock(p, null, 15, false);
													
													if(block.getType() == Material.AIR) {
														p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.BlockInfo.Air").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
														return true;
													}
													
													List<String> mlist = cmd.getStringList("Command.Admin.BlockInfo.Message");
									 
													for(String m : mlist) {
														p.sendMessage(plugin.getAPI().toHex(m.replaceAll("<block>", ""+block.getType()).replaceAll("&", "§")));
													}
													
									} else 	if(length == 4
											&& args[0].equalsIgnoreCase(cmd.getString("Command.Admin.Usage"))
											&& args[1].equalsIgnoreCase(cmd.getString("Command.Admin.AddJob.Usage")
													)) {

												if(!cmd.getBoolean("Command.Admin.Use")) {
												p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.Disabled").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
												return true;
												}
												
												if(!p.hasPermission(cmd.getString("Command.Admin.Permission"))) {
												p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.NoPermMessage").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
												return true;
												}
												
												if(!cmd.getBoolean("Command.Admin.AddJob.Use")) {
													p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.AddJob.Disabled").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
													return true;
													}
													
													if(!p.hasPermission(cmd.getString("Command.Admin.AddJob.Permission"))) {
													p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.AddJob.NoPermMessage").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
													return true;
													}
													
													String pl = args[2];
													
				                                    if(plugin.getPlayerAPI().getUUIDByName(pl) == null) {
				                                        p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.AddJob.DeosntExist").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				                                        return true;
				                                    }
				                         
				                                    String uuid = plugin.getPlayerAPI().getUUIDByName(pl);
				                                    
				                                    String job = args[3];
				                                    
				                                    if(!plugin.getAPI().existJobWithConfigID(job)) {
				            							p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.AddJob.NoJob").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				            							return true;
				            						}
				                                    
				                                    if(plugin.getPlayerAPI().ownJob(""+p.getUniqueId(), job.toUpperCase())) {
				            							p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.AddJob.Already").replaceAll("<job>", job).replaceAll("<name>", pl).replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				            							return true;
				            						}
				                                    
				                                    plugin.getPlayerAPI().addOwnJob(""+p.getUniqueId(), job.toUpperCase());
				                            		p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.AddJob.Added").replaceAll("<job>", job).replaceAll("<name>", pl).replaceAll("<prefix>", prefix).replaceAll("&", "§")));
			            							return true;
							 
													
									} else 	if(length == 4
											&& args[0].equalsIgnoreCase(cmd.getString("Command.Admin.Usage"))
											&& args[1].equalsIgnoreCase(cmd.getString("Command.Admin.RemJob.Usage")
													)) {

												if(!cmd.getBoolean("Command.Admin.Use")) {
												p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.Disabled").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
												return true;
												}
												
												if(!p.hasPermission(cmd.getString("Command.Admin.Permission"))) {
												p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.NoPermMessage").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
												return true;
												}
												
												if(!cmd.getBoolean("Command.Admin.RemJob.Use")) {
													p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.RemJob.Disabled").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
													return true;
													}
													
													if(!p.hasPermission(cmd.getString("Command.Admin.RemJob.Permission"))) {
													p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.RemJob.NoPermMessage").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
													return true;
													}
													
													String pl = args[2];
													
				                                    if(plugin.getPlayerAPI().getUUIDByName(pl) == null) {
				                                        p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.RemJob.DeosntExist").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				                                        return true;
				                                    }
				                         
				                                    String uuid = plugin.getPlayerAPI().getUUIDByName(pl);
				                                    
				                                    String job = args[3];
				                                    
				                                    if(!plugin.getAPI().existJobWithConfigID(job)) {
				            							p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.RemJob.NoJob").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				            							return true;
				            						}
				                                    
				                                    if(!plugin.getPlayerAPI().ownJob(""+p.getUniqueId(), job.toUpperCase())) {
				            							p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.RemJob.DontOwn").replaceAll("<job>", job).replaceAll("<name>", pl).replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				            							return true;
				            						}
				                                    
				                                    plugin.getPlayerAPI().remOwnJob(""+p.getUniqueId(), job.toUpperCase());
				                            		p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.RemJob.Removed").replaceAll("<job>", job).replaceAll("<name>", pl).replaceAll("<prefix>", prefix).replaceAll("&", "§")));
			            							return true;
							 
													
									}else 	if(length == 4
											&& args[0].equalsIgnoreCase(cmd.getString("Command.Admin.Usage"))
											&& args[1].equalsIgnoreCase(cmd.getString("Command.Admin.SetLimit.Usage")
													)) {

												if(!cmd.getBoolean("Command.Admin.Use")) {
												p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.Disabled").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
												return true;
												}
												
												if(!p.hasPermission(cmd.getString("Command.Admin.Permission"))) {
												p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.NoPermMessage").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
												return true;
												}
												
												if(!cmd.getBoolean("Command.Admin.SetLimit.Use")) {
													p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.SetLimit.Disabled").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
													return true;
													}
													
													if(!p.hasPermission(cmd.getString("Command.Admin.SetLimit.Permission"))) {
													p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.SetLimit.NoPermMessage").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
													return true;
													}
													
													String pl = args[2];
													
				                                    if(plugin.getPlayerAPI().getUUIDByName(pl) == null) {
				                                        p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.SetLimit.DeosntExist").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				                                        return true;
				                                    }
				                         
				                                    String uuid = plugin.getPlayerAPI().getUUIDByName(pl);
				                                    
				                                    String limit = args[3];
				                                    
				                                    if(!isInt(limit)) {
				            							p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.RemJob.NotInt").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				            							return true;
				            						}
				                                
				                                    Integer i = Integer.valueOf(limit);
				                                     
				                                    plugin.getPlayerAPI().setMaxJobs(""+p.getUniqueId(), i);
				                                    
				                            		p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.SetLimit.Set").replaceAll("<l>", ""+i).replaceAll("<name>", pl).replaceAll("<prefix>", prefix).replaceAll("&", "§")));
			            							return true;
							 
													
									} else 	if(length == 4
											&& args[0].equalsIgnoreCase(cmd.getString("Command.Admin.Usage"))
											&& args[1].equalsIgnoreCase(cmd.getString("Command.Admin.AddLimit.Usage")
													)) {

												if(!cmd.getBoolean("Command.Admin.Use")) {
												p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.Disabled").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
												return true;
												}
												
												if(!p.hasPermission(cmd.getString("Command.Admin.Permission"))) {
												p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.NoPermMessage").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
												return true;
												}
												
												if(!cmd.getBoolean("Command.Admin.AddLimit.Use")) {
													p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.AddLimit.Disabled").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
													return true;
													}
													
													if(!p.hasPermission(cmd.getString("Command.Admin.AddLimit.Permission"))) {
													p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.AddLimit.NoPermMessage").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
													return true;
													}
													
													String pl = args[2];
													
				                                    if(plugin.getPlayerAPI().getUUIDByName(pl) == null) {
				                                        p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.AddLimit.DeosntExist").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				                                        return true;
				                                    }
				                         
				                                    String uuid = plugin.getPlayerAPI().getUUIDByName(pl);
				                                    
				                                    String limit = args[3];
				                                    
				                                    if(!isInt(limit)) {
				            							p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.AddLimit.NotInt").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				            							return true;
				            						}
				                                
				                                    Integer i = Integer.valueOf(limit);
				                                     
				                                    plugin.getPlayerAPI().addMaxJobs(""+p.getUniqueId(), i);
				                                    
				                            		p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.AddLimit.Added").replaceAll("<l>", ""+i).replaceAll("<name>", pl).replaceAll("<prefix>", prefix).replaceAll("&", "§")));
			            							return true;
							 
													
									}else 	if(length == 4
											&& args[0].equalsIgnoreCase(cmd.getString("Command.Admin.Usage"))
											&& args[1].equalsIgnoreCase(cmd.getString("Command.Admin.RemLimit.Usage")
													)) {

												if(!cmd.getBoolean("Command.Admin.Use")) {
												p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.Disabled").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
												return true;
												}
												
												if(!p.hasPermission(cmd.getString("Command.Admin.Permission"))) {
												p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.NoPermMessage").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
												return true;
												}
												
												if(!cmd.getBoolean("Command.Admin.RemLimit.Use")) {
													p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.RemLimit.Disabled").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
													return true;
													}
													
													if(!p.hasPermission(cmd.getString("Command.Admin.RemLimit.Permission"))) {
													p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.RemLimit.NoPermMessage").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
													return true;
													}
													
													String pl = args[2];
													
				                                    if(plugin.getPlayerAPI().getUUIDByName(pl) == null) {
				                                        p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.RemLimit.DeosntExist").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				                                        return true;
				                                    }
				                         
				                                    String uuid = plugin.getPlayerAPI().getUUIDByName(pl);
				                                    
				                                    String limit = args[3];
				                                    
				                                    if(!isInt(limit)) {
				            							p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.RemLimit.NotInt").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				            							return true;
				            						}
				                                
				                                    Integer i = Integer.valueOf(limit);
				                                     
				                                    plugin.getPlayerAPI().remMaxJobs(""+p.getUniqueId(), i);
				                                    
				                            		p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.RemLimit.Removed").replaceAll("<l>", ""+i).replaceAll("<name>", pl).replaceAll("<prefix>", prefix).replaceAll("&", "§")));
			            							return true;
							 
													
									} else 	if(length == 5
											&& args[0].equalsIgnoreCase(cmd.getString("Command.Admin.Usage"))
											&& args[1].equalsIgnoreCase(cmd.getString("Command.Admin.SetLevel.Usage")
													)) {

												if(!cmd.getBoolean("Command.Admin.Use")) {
												p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.Disabled").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
												return true;
												}
												
												if(!p.hasPermission(cmd.getString("Command.Admin.Permission"))) {
												p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.NoPermMessage").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
												return true;
												}
												
												if(!cmd.getBoolean("Command.Admin.SetLevel.Use")) {
													p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.SetLevel.Disabled").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
													return true;
													}
													
													if(!p.hasPermission(cmd.getString("Command.Admin.SetLevel.Permission"))) {
													p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.SetLevel.NoPermMessage").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
													return true;
													}
													
													String pl = args[2];
													
				                                    if(plugin.getPlayerAPI().getUUIDByName(pl) == null) {
				                                        p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.SetLevel.DeosntExist").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				                                        return true;
				                                    }
				                         
				                                    String uuid = plugin.getPlayerAPI().getUUIDByName(pl);
				                                    
				                                    String job = args[3];
				                                    
				                                    if(!plugin.getAPI().existJobWithConfigID(job)) {
				            							p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.SetLevel.NoJob").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				            							return true;
				            						}
				                                    
				                                    if(!plugin.getPlayerAPI().ownJob(""+p.getUniqueId(), job.toUpperCase())) {
				            							p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.SetLevel.DontOwn").replaceAll("<job>", job).replaceAll("<name>", pl).replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				            							return true;
				            						}
				                                    
				                                    String level = args[4];
				                                    
				                                    if(!isInt(level)) {
				                                    	p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.SetLevel.NotInt").replaceAll("<job>", job).replaceAll("<name>", pl).replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				            							return true;
				                                    }
				                                    
				                                    int level_int = Integer.valueOf(level);
				                                
				                                    plugin.getPlayerAPI().setJobLevel(""+p.getUniqueId(), job.toUpperCase(), level_int);
				                            		p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.SetLevel.Set").replaceAll("<l>", ""+level_int).replaceAll("<job>", job).replaceAll("<name>", pl).replaceAll("<prefix>", prefix).replaceAll("&", "§")));
			            							return true;
							 
													
									} else 	if(length == 5
											&& args[0].equalsIgnoreCase(cmd.getString("Command.Admin.Usage"))
											&& args[1].equalsIgnoreCase(cmd.getString("Command.Admin.RemLevel.Usage")
													)) {

												if(!cmd.getBoolean("Command.Admin.Use")) {
												p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.Disabled").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
												return true;
												}
												
												if(!p.hasPermission(cmd.getString("Command.Admin.Permission"))) {
												p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.NoPermMessage").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
												return true;
												}
												
												if(!cmd.getBoolean("Command.Admin.RemLevel.Use")) {
													p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.RemLevel.Disabled").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
													return true;
													}
													
													if(!p.hasPermission(cmd.getString("Command.Admin.RemLevel.Permission"))) {
													p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.RemLevel.NoPermMessage").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
													return true;
													}
													
													String pl = args[2];
													
				                                    if(plugin.getPlayerAPI().getUUIDByName(pl) == null) {
				                                        p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.RemLevel.DeosntExist").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				                                        return true;
				                                    }
				                         
				                                    String uuid = plugin.getPlayerAPI().getUUIDByName(pl);
				                                    
				                                    String job = args[3];
				                                    
				                                    if(!plugin.getAPI().existJobWithConfigID(job)) {
				            							p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.RemLevel.NoJob").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				            							return true;
				            						}
				                                    
				                                    if(!plugin.getPlayerAPI().ownJob(""+p.getUniqueId(), job.toUpperCase())) {
				            							p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.RemLevel.DontOwn").replaceAll("<job>", job).replaceAll("<name>", pl).replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				            							return true;
				            						}
				                                    
				                                    String level = args[4];
				                                    
				                                    if(!isInt(level)) {
				                                    	p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.RemLevel.NotInt").replaceAll("<job>", job).replaceAll("<name>", pl).replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				            							return true;
				                                    }
				                                    
				                                    int level_int = Integer.valueOf(level);
				                                
				                                    plugin.getPlayerAPI().remJobLevel(""+p.getUniqueId(), job.toUpperCase(), level_int);
				                            		p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.RemLevel.Removed").replaceAll("<l>", ""+level_int).replaceAll("<job>", job).replaceAll("<name>", pl).replaceAll("<prefix>", prefix).replaceAll("&", "§")));
			            							return true;
							 
													
									} else 	if(length == 5
											&& args[0].equalsIgnoreCase(cmd.getString("Command.Admin.Usage"))
											&& args[1].equalsIgnoreCase(cmd.getString("Command.Admin.AddLevel.Usage")
													)) {

												if(!cmd.getBoolean("Command.Admin.Use")) {
												p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.Disabled").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
												return true;
												}
												
												if(!p.hasPermission(cmd.getString("Command.Admin.Permission"))) {
												p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.NoPermMessage").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
												return true;
												}
												
												if(!cmd.getBoolean("Command.Admin.RemLevel.Use")) {
													p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.AddLevel.Disabled").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
													return true;
													}
													
													if(!p.hasPermission(cmd.getString("Command.Admin.AddLevel.Permission"))) {
													p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.AddLevel.NoPermMessage").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
													return true;
													}
													
													String pl = args[2];
													
				                                    if(plugin.getPlayerAPI().getUUIDByName(pl) == null) {
				                                        p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.AddLevel.DeosntExist").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				                                        return true;
				                                    }
				                         
				                                    String uuid = plugin.getPlayerAPI().getUUIDByName(pl);
				                                    
				                                    String job = args[3];
				                                    
				                                    if(!plugin.getAPI().existJobWithConfigID(job)) {
				            							p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.AddLevel.NoJob").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				            							return true;
				            						}
				                                    
				                                    if(!plugin.getPlayerAPI().ownJob(""+p.getUniqueId(), job.toUpperCase())) {
				            							p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.AddLevel.DontOwn").replaceAll("<job>", job).replaceAll("<name>", pl).replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				            							return true;
				            						}
				                                    
				                                    String level = args[4];
				                                    
				                                    if(!isInt(level)) {
				                                    	p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.AddLevel.NotInt").replaceAll("<job>", job).replaceAll("<name>", pl).replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				            							return true;
				                                    }
				                                    
				                                    int level_int = Integer.valueOf(level);
				                                
				                                    plugin.getPlayerAPI().addJobLevel(""+p.getUniqueId(), job.toUpperCase(), level_int);
				                            		p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.AddLevel.Added").replaceAll("<l>", ""+level_int).replaceAll("<job>", job).replaceAll("<name>", pl).replaceAll("<prefix>", prefix).replaceAll("&", "§")));
			            							return true;
							 
													
									} else 	if(length == 5
											&& args[0].equalsIgnoreCase(cmd.getString("Command.Admin.Usage"))
											&& args[1].equalsIgnoreCase(cmd.getString("Command.Admin.SetExp.Usage")
													)) {

												if(!cmd.getBoolean("Command.Admin.Use")) {
												p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.Disabled").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
												return true;
												}
												
												if(!p.hasPermission(cmd.getString("Command.Admin.Permission"))) {
												p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.NoPermMessage").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
												return true;
												}
												
												if(!cmd.getBoolean("Command.Admin.SetExp.Use")) {
													p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.SetExp.Disabled").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
													return true;
													}
													
													if(!p.hasPermission(cmd.getString("Command.Admin.SetExp.Permission"))) {
													p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.SetExp.NoPermMessage").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
													return true;
													}
													
													String pl = args[2];
													
				                                    if(plugin.getPlayerAPI().getUUIDByName(pl) == null) {
				                                        p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.SetExp.DeosntExist").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				                                        return true;
				                                    }
				                         
				                                    String uuid = plugin.getPlayerAPI().getUUIDByName(pl);
				                                    
				                                    String job = args[3];
				                                    
				                                    if(!plugin.getAPI().existJobWithConfigID(job)) {
				            							p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.SetExp.NoJob").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				            							return true;
				            						}
				                                    
				                                    if(!plugin.getPlayerAPI().ownJob(""+p.getUniqueId(), job.toUpperCase())) {
				            							p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.SetExp.DontOwn").replaceAll("<job>", job).replaceAll("<name>", pl).replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				            							return true;
				            						}
				                                    
				                                    String level = args[4];
				                                    
				                                    if(!isInt(level)) {
				                                    	p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.SetExp.NotInt").replaceAll("<job>", job).replaceAll("<name>", pl).replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				            							return true;
				                                    }
				                                    
				                                    int level_int = Integer.valueOf(level);
				                                
				                                    plugin.getPlayerAPI().setJobExp(""+p.getUniqueId(), job.toUpperCase(), level_int);
				                            		p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.SetExp.Set").replaceAll("<l>", ""+level_int).replaceAll("<job>", job).replaceAll("<name>", pl).replaceAll("<prefix>", prefix).replaceAll("&", "§")));
			            							return true;
							 
													
									} else 	if(length == 5
											&& args[0].equalsIgnoreCase(cmd.getString("Command.Admin.Usage"))
											&& args[1].equalsIgnoreCase(cmd.getString("Command.Admin.RemExp.Usage")
													)) {

												if(!cmd.getBoolean("Command.Admin.Use")) {
												p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.Disabled").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
												return true;
												}
												
												if(!p.hasPermission(cmd.getString("Command.Admin.Permission"))) {
												p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.NoPermMessage").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
												return true;
												}
												
												if(!cmd.getBoolean("Command.Admin.RemExp.Use")) {
													p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.RemExp.Disabled").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
													return true;
													}
													
													if(!p.hasPermission(cmd.getString("Command.Admin.RemExp.Permission"))) {
													p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.RemExp.NoPermMessage").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
													return true;
													}
													
													String pl = args[2];
													
				                                    if(plugin.getPlayerAPI().getUUIDByName(pl) == null) {
				                                        p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.RemExp.DeosntExist").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				                                        return true;
				                                    }
				                         
				                                    String uuid = plugin.getPlayerAPI().getUUIDByName(pl);
				                                    
				                                    String job = args[3];
				                                    
				                                    if(!plugin.getAPI().existJobWithConfigID(job)) {
				            							p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.RemExp.NoJob").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				            							return true;
				            						}
				                                    
				                                    if(!plugin.getPlayerAPI().ownJob(""+p.getUniqueId(), job.toUpperCase())) {
				            							p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.RemExp.DontOwn").replaceAll("<job>", job).replaceAll("<name>", pl).replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				            							return true;
				            						}
				                                    
				                                    String level = args[4];
				                                    
				                                    if(!isInt(level)) {
				                                    	p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.RemExp.NotInt").replaceAll("<job>", job).replaceAll("<name>", pl).replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				            							return true;
				                                    }
				                                    
				                                    int level_int = Integer.valueOf(level);
				                                
				                                    plugin.getPlayerAPI().remJobExp(""+p.getUniqueId(), job.toUpperCase(), level_int);
				                            		p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.RemExp.Removed").replaceAll("<l>", ""+level_int).replaceAll("<job>", job).replaceAll("<name>", pl).replaceAll("<prefix>", prefix).replaceAll("&", "§")));
			            							return true;
							 
													
									} else 	if(length == 5
											&& args[0].equalsIgnoreCase(cmd.getString("Command.Admin.Usage"))
											&& args[1].equalsIgnoreCase(cmd.getString("Command.Admin.AddExp.Usage")
													)) {

												if(!cmd.getBoolean("Command.Admin.Use")) {
												p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.Disabled").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
												return true;
												}
												
												if(!p.hasPermission(cmd.getString("Command.Admin.Permission"))) {
												p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.NoPermMessage").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
												return true;
												}
												
												if(!cmd.getBoolean("Command.Admin.AddExp.Use")) {
													p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.AddExp.Disabled").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
													return true;
													}
													
													if(!p.hasPermission(cmd.getString("Command.Admin.AddExp.Permission"))) {
													p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.AddExp.NoPermMessage").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
													return true;
													}
													
													String pl = args[2];
													
				                                    if(plugin.getPlayerAPI().getUUIDByName(pl) == null) {
				                                        p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.AddExp.DeosntExist").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				                                        return true;
				                                    }
				                         
				                                    String uuid = plugin.getPlayerAPI().getUUIDByName(pl);
				                                    
				                                    String job = args[3];
				                                    
				                                    if(!plugin.getAPI().existJobWithConfigID(job)) {
				            							p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.AddExp.NoJob").replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				            							return true;
				            						}
				                                    
				                                    if(!plugin.getPlayerAPI().ownJob(""+p.getUniqueId(), job.toUpperCase())) {
				            							p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.AddExp.DontOwn").replaceAll("<job>", job).replaceAll("<name>", pl).replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				            							return true;
				            						}
				                                    
				                                    String level = args[4];
				                                    
				                                    if(!isInt(level)) {
				                                    	p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.AddExp.NotInt").replaceAll("<job>", job).replaceAll("<name>", pl).replaceAll("<prefix>", prefix).replaceAll("&", "§")));
				            							return true;
				                                    }
				                                    
				                                    int level_int = Integer.valueOf(level);
				                                
				                                    plugin.getPlayerAPI().addJobExp(""+p.getUniqueId(), job.toUpperCase(), level_int);
				                            		p.sendMessage(plugin.getAPI().toHex(cmd.getString("Command.Admin.AddExp.Added").replaceAll("<l>", ""+level_int).replaceAll("<job>", job).replaceAll("<name>", pl).replaceAll("<prefix>", prefix).replaceAll("&", "§")));
			            							return true;
							 
													
									}
		
		return false;
	
	}
	
    public static Block getTargetBlock(Player player, Material lookingFor, int distance, boolean ignoreNoneSolids) {
	if (distance > 15 * 16)
	    distance = 15 * 16;
	if (distance < 1)
	    distance = 1;

	List<Block> blocks = new ArrayList<>();

	try {
	    Block bl = player.getTargetBlock(null, distance);
	    if (bl.getType() != Material.AIR) {
		return bl;
	    }
	} catch (Throwable e) {
	}

	Iterator<Block> itr = new BlockIterator(player, distance);
	while (itr.hasNext()) {
	    Block block = itr.next();
	    blocks.add(block);
	    if (distance != 0 && blocks.size() > distance) {
		blocks.remove(0);
	    }
	    Material material = block.getType();

	    if (ignoreNoneSolids && !material.isSolid())
		continue;

	    if (lookingFor == null) {
		if (material == material.AIR) {
		    break;
		}
	    } else {
		if (lookingFor == material) {
		    return block;
		}
	    }
	}
	return !blocks.isEmpty() ? blocks.get(blocks.size() - 1) : null;
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

















