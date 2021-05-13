package de.warsteiner.ultimatejobs.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.warsteiner.ultimatejobs.UltimateJobs;

public class PlayerJobTabComplete implements TabCompleter{

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		
		Player p = (Player) s;
		
		ArrayList<String> l = new ArrayList<String>();
		
		int length = args.length;
		
		UltimateJobs plugin = UltimateJobs.getPlugin();
		
		YamlConfiguration tr = plugin.getTranslation();
		
		YamlConfiguration cmd = plugin.getCommandConfig();
		
		List<String> jobs = plugin.getPlayerAPI().getCurrentJobs(""+p.getUniqueId());
		
		Collection<? extends Player> b = Bukkit.getOnlinePlayers();
		
	//	for(Player pl : b) {
		//	l.add(pl.getName());
		//}
		
		if(length == 1) {
			if(cmd.getBoolean("Command.Help.Use")) {
				if(p.hasPermission(cmd.getString("Command.Help.Permission"))) {
					l.add(cmd.getString("Command.Help.Usage"));
				}
			} 
			if(cmd.getBoolean("Command.Limit.Use")) {
				if(p.hasPermission(cmd.getString("Command.Limit.Permission"))) {
					l.add(cmd.getString("Command.Limit.Usage"));
				}
			} 
			if(cmd.getBoolean("Command.Stats.Use")) {
				if(p.hasPermission(cmd.getString("Command.Stats.Permission"))) {
					l.add(cmd.getString("Command.Stats.Usage"));
				}
			}  
			if(cmd.getBoolean("Command.Browse.Use")) {
				if(p.hasPermission(cmd.getString("Command.Browse.Permission"))) {
					l.add(cmd.getString("Command.Browse.Usage"));
				}
			} 
			if(cmd.getBoolean("Command.Info.Use")) {
				if(p.hasPermission(cmd.getString("Command.Info.Permission"))) {
					l.add(cmd.getString("Command.Info.Usage"));
				}
			} 
			if(cmd.getBoolean("Command.Join.Use")) {
				if(p.hasPermission(cmd.getString("Command.Join.Permission"))) {
					l.add(cmd.getString("Command.Join.Usage"));
				}
			}
			if(cmd.getBoolean("Command.Admin.Use")) {
				if(p.hasPermission(cmd.getString("Command.Admin.Permission"))) {
					l.add(cmd.getString("Command.Admin.Usage"));
				}
			}
			if(jobs.size() != 0) {
				if(cmd.getBoolean("Command.Leave.Use")) {
					if(p.hasPermission(cmd.getString("Command.Leave.Permission"))) {
						l.add(cmd.getString("Command.Leave.Usage"));
					}
				}
				if(cmd.getBoolean("Command.LeaveALL.Use")) {
					if(p.hasPermission(cmd.getString("Command.LeaveALL.Permission"))) {
						l.add(cmd.getString("Command.LeaveALL.Usage"));
					}
				}
			}
		} else if (length == 2) {
			if(cmd.getBoolean("Command.Stats.Use")) {
				if(args[0].equalsIgnoreCase(cmd.getString("Command.Stats.Usage"))) {
					if(p.hasPermission(cmd.getString("Command.Stats.Permission"))) {
						for(Player pl : b) {
							 	l.add(pl.getName());
						}
					}
				}
			} 
			if(cmd.getBoolean("Command.Info.Use")) {
				if(args[0].equalsIgnoreCase(cmd.getString("Command.Info.Usage"))) {
					if(p.hasPermission(cmd.getString("Command.Info.Permission"))) {
						for(String pl : plugin.getAPI().getJobsWithConfigIDS()) {
							l.add(pl);
						}
					}
				}
			} 
			if(cmd.getBoolean("Command.Join.Use")) {
				if(args[0].equalsIgnoreCase(cmd.getString("Command.Join.Usage"))) {
					if(p.hasPermission(cmd.getString("Command.Join.Permission"))) {
						for(String pl : plugin.getPlayerAPI().getOwn(""+p.getUniqueId())) {
							if(!plugin.getPlayerAPI().isInJob(""+p.getUniqueId(), pl)) {
								String id = plugin.getAPI().getConfigIdOfJob(pl);
								l.add(id);
							}
						}
					}
				}
			}
			
			if(cmd.getBoolean("Command.Admin.Use")) {
				if(args[0].equalsIgnoreCase(cmd.getString("Command.Admin.Usage"))) {
					if(p.hasPermission(cmd.getString("Command.Admin.Permission"))) {
						 
						if(cmd.getBoolean("Command.Admin.BlockInfo.Use")) { 
							if(p.hasPermission(cmd.getString("Command.Admin.BlockInfo.Permission"))) {
								l.add(cmd.getString("Command.Admin.BlockInfo.Usage"));
							} 
						}
						
						if(cmd.getBoolean("Command.Admin.Reload.Use")) { 
							if(p.hasPermission(cmd.getString("Command.Admin.Reload.Permission"))) {
								l.add(cmd.getString("Command.Admin.Reload.Usage"));
							} 
						}
						
						if(cmd.getBoolean("Command.Admin.AddJob.Use")) { 
							if(p.hasPermission(cmd.getString("Command.Admin.AddJob.Permission"))) {
								l.add(cmd.getString("Command.Admin.AddJob.Usage"));
							} 
						}
						
						if(cmd.getBoolean("Command.Admin.RemJob.Use")) { 
							if(p.hasPermission(cmd.getString("Command.Admin.RemJob.Permission"))) {
								l.add(cmd.getString("Command.Admin.RemJob.Usage"));
							} 
						}
						
						if(cmd.getBoolean("Command.Admin.SetLevel.Use")) { 
							if(p.hasPermission(cmd.getString("Command.Admin.SetLevel.Permission"))) {
								l.add(cmd.getString("Command.Admin.RemJob.Usage"));
							} 
						}
						
						if(cmd.getBoolean("Command.AddLevel.RemLevel.Use")) { 
							if(p.hasPermission(cmd.getString("Command.Admin.RemLevel.Permission"))) {
								l.add(cmd.getString("Command.Admin.RemLevel.Usage"));
							} 
						}
						
						if(cmd.getBoolean("Command.Admin.RemLevel.Use")) { 
							if(p.hasPermission(cmd.getString("Command.Admin.RemLevel.Permission"))) {
								l.add(cmd.getString("Command.Admin.RemLevel.Usage"));
							} 
						}
						
						if(cmd.getBoolean("Command.Admin.SetExp.Use")) { 
							if(p.hasPermission(cmd.getString("Command.Admin.SetExp.Permission"))) {
								l.add(cmd.getString("Command.Admin.SetExp.Usage"));
							} 
						}
						
						if(cmd.getBoolean("Command.Admin.AddExp.Use")) { 
							if(p.hasPermission(cmd.getString("Command.Admin.AddExp.Permission"))) {
								l.add(cmd.getString("Command.Admin.AddExp.Usage"));
							} 
						}
						if(cmd.getBoolean("Command.Admin.RemExp.Use")) { 
							if(p.hasPermission(cmd.getString("Command.Admin.RemExp.Permission"))) {
								l.add(cmd.getString("Command.Admin.RemExp.Usage"));
							} 
						}
						
						if(cmd.getBoolean("Command.Admin.SetLimit.Use")) { 
							if(p.hasPermission(cmd.getString("Command.Admin.SetLimit.Permission"))) {
								l.add(cmd.getString("Command.Admin.SetLimit.Usage"));
							} 
						}
						
						if(cmd.getBoolean("Command.Admin.AddLimit.Use")) { 
							if(p.hasPermission(cmd.getString("Command.Admin.AddLimit.Permission"))) {
								l.add(cmd.getString("Command.Admin.AddLimit.Usage"));
							} 
						}
						if(cmd.getBoolean("Command.Admin.RemLimit.Use")) { 
							if(p.hasPermission(cmd.getString("Command.Admin.RemLimit.Permission"))) {
								l.add(cmd.getString("Command.Admin.RemLimit.Usage"));
							} 
						}
						
					}
				}
			}
			
			if(jobs.size() != 0) {
				if(cmd.getBoolean("Command.Leave.Use")) {
					if(args[0].equalsIgnoreCase(cmd.getString("Command.Leave.Usage"))) {
						if(p.hasPermission(cmd.getString("Command.Leave.Permission"))) {
							for(String pl : plugin.getPlayerAPI().getCurrentJobs(""+p.getUniqueId())) {
							
									String id = plugin.getAPI().getConfigIdOfJob(pl);
									l.add(id);
								
							}
						}
					}
				}
			}
			
		} else if (length == 3) {
			 
		}else if (length == 4) {
			 
		}
		
		return l;
		
	}
}









