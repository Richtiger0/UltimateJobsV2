package de.warsteiner.ultimatejobs.utils.api;

import java.util.ArrayList;

import de.warsteiner.ultimatejobs.UltimateJobs;
import de.warsteiner.ultimatejobs.utils.data.PlayerJobDataFile;

public class PlayerAPI {
	
	public void createPlayer(String uuid) {
		PlayerJobDataFile cl = UltimateJobs.getPlayerDataFile();
		ArrayList<String> list = new ArrayList<String>();
		
		cl.load();
		
		cl.get().set("Player."+uuid+".CurrentJob", list);
		cl.get().set("Player."+uuid+".OwnsJob", list);
		
		cl.save(); 
	}
	
	public boolean existPlayer(String uuid) {
		PlayerJobDataFile cl = UltimateJobs.getPlayerDataFile();
		cl.load();
		return cl.get().contains("Player."+uuid+".CurrentJob");
	}

}
