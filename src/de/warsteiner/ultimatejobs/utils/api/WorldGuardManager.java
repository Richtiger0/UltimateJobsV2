package de.warsteiner.ultimatejobs.utils.api;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import de.warsteiner.ultimatejobs.UltimateJobs;

public class WorldGuardManager {

	public static void load(ArrayList<String> flags) {
		System.out.println("§4§lLoading WorldGuard Support...");

		FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();

		for (String b : flags) {
			StateFlag flag = new StateFlag(b, true);

			registry.register((Flag) flag);
		}
	}
	
 

	public static boolean checkFlag(org.bukkit.Location location, String flag, Player p) {
		 
			int priority = -1;
			WorldGuard instance = WorldGuard.getInstance();
			RegionContainer container = instance.getPlatform().getRegionContainer();
			BukkitWorld bukkitWorld = new BukkitWorld(p.getWorld());
			RegionManager regions = container.get((World) bukkitWorld);
			RegionQuery query = container.createQuery();
			Location wLoc = new Location((Extent) bukkitWorld, location.getX(), location.getY(), location.getZ());
			ProtectedRegion selected = null;
			for (ProtectedRegion r : query.getApplicableRegions(wLoc)) {
				if (r.getPriority() > priority) {
					priority = r.getPriority();
					selected = r;
				}
			}
			if (selected == null)
				selected = regions.getRegion("__global__");
			String state = "ALLOW";
			if (selected != null)
				for (Flag<?> a : (Iterable<Flag<?>>) selected.getFlags().keySet()) {
					if (a != null && a.getName().equals(flag)) {
						state = selected.getFlags().get(a).toString();
						break;
					}
				}
	
			if (state.equals("ALLOW")) {
				return true;
			}
	
			return false;
 
	}
}
