package de.warsteiner.ultimatejobs.utils.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;

import de.warsteiner.ultimatejobs.UltimateJobs;
import de.warsteiner.ultimatejobs.utils.api.events.PlayerDataChangeEvent;
import de.warsteiner.ultimatejobs.utils.data.PlayerJobDataFile;

public class PlayerAPI {
	
	public void createPlayer(String uuid) {
		PlayerJobDataFile cl = UltimateJobs.getPlayerDataFile();
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<String> list2 = new ArrayList<String>();
 
		cl.load();
		
		cl.get().set("Player."+uuid+".CurrentJob", list);
		cl.get().set("Player."+uuid+".OwnsJob", list2);
		cl.get().set("Player."+uuid+".MaxJobs", UltimateJobs.getMainConfig().getInt("MaxJobsDefault"));
		
		cl.save(); 
	}
 
	
	public void UpdateFetcher(String uuid, String name) {
		PlayerJobDataFile cl = UltimateJobs.getPlayerDataFile();
		 
		cl.load();
		cl.get().set("UUIDFetcher."+uuid+".Name", name);
		cl.get().set("UUIDFetcher."+name.toUpperCase()+".UUID", ""+uuid);
		cl.save(); 
	}
	
	public String getNameByUUID(String uuid) {
		PlayerJobDataFile cl = UltimateJobs.getPlayerDataFile();
		 
		cl.load();
		return cl.get().getString("UUIDFetcher."+uuid+".Name");
	}
	
	public String getUUIDByName(String name) {
		PlayerJobDataFile cl = UltimateJobs.getPlayerDataFile();
		 
		cl.load();
		return cl.get().getString("UUIDFetcher."+name.toUpperCase()+".UUID");
	}
	
	public void createJob(String uuid, String job) {
		PlayerJobDataFile cl = UltimateJobs.getPlayerDataFile();
 
		cl.load();
 
		cl.get().set("Job."+uuid+".ID."+job.toUpperCase()+".Level", 1);
		cl.get().set("Job."+uuid+".ID."+job.toUpperCase()+".Exp", 0);
		cl.get().set("Job."+uuid+".ID."+job.toUpperCase()+".Points", 0);
		cl.get().set("Job."+uuid+".ID."+job.toUpperCase()+".Count1", 0);
		cl.get().set("Job."+uuid+".ID."+job.toUpperCase()+".Count2", 0);
		cl.get().set("Job."+uuid+".ID."+job.toUpperCase()+".Count3", 0);
		cl.get().set("Job."+uuid+".ID."+job.toUpperCase()+".Count4", 0);
		cl.get().set("Job."+uuid+".ID."+job.toUpperCase()+".Count5", 0);
		cl.save(); 
	}
	
	public int getCount1(String uuid, String job) {
		PlayerJobDataFile cl = UltimateJobs.getPlayerDataFile(); 
		cl.load(); 
		return cl.get().getInt("Job."+uuid+".ID."+job.toUpperCase()+".Count1");
	}
	
	public void setCount1(String uuid, String job, int d) {
		PlayerJobDataFile cl = UltimateJobs.getPlayerDataFile();
		cl.load();
		cl.get().set("Job."+uuid+".ID."+job.toUpperCase()+".Count1", d); 
		cl.save();  
	}
	
	public void addCount1(String uuid, String job, int m) {
		
		int old = getCount1(uuid, job);
	
		setCount1(uuid, job, old+m);
		
	}
	
	public double getJobExp(String uuid, String job) {
		PlayerJobDataFile cl = UltimateJobs.getPlayerDataFile(); 
		cl.load(); 
		return cl.get().getDouble("Job."+uuid+".ID."+job.toUpperCase()+".Exp");
	}
	
	public void setJobExp(String uuid, String job, double d) {
		PlayerJobDataFile cl = UltimateJobs.getPlayerDataFile();
		cl.load();
		cl.get().set("Job."+uuid+".ID."+job.toUpperCase()+".Exp", d); 
		cl.save(); 
		new PlayerDataChangeEvent(uuid, job.toUpperCase());
	}
	
	public void addJobExp(String uuid, String job, double exp) {
		PlayerJobDataFile cl = UltimateJobs.getPlayerDataFile();
		cl.load();
		
		double old = getJobExp(uuid, job.toUpperCase());
		
		setJobExp(uuid, job.toUpperCase(), old + exp);
	}
	
	public void remJobExp(String uuid, String job, int exp) {
		PlayerJobDataFile cl = UltimateJobs.getPlayerDataFile();
		cl.load();
		
		double old = getJobExp(uuid, job.toUpperCase());
		
		setJobExp(uuid, job.toUpperCase(), old - exp);
	}
	
	public int getJobLevel(String uuid, String job) {
		PlayerJobDataFile cl = UltimateJobs.getPlayerDataFile(); 
		cl.load(); 
		return cl.get().getInt("Job."+uuid+".ID."+job.toUpperCase()+".Level");
	}
	
	public void setJobLevel(String uuid, String job, int level) {
		PlayerJobDataFile cl = UltimateJobs.getPlayerDataFile();
		cl.load();
		cl.get().set("Job."+uuid+".ID."+job.toUpperCase()+".Level", level); 
		cl.save(); 
		new PlayerDataChangeEvent(uuid, job.toUpperCase());
	}
	
	public void addJobLevel(String uuid, String job, int exp) {
		PlayerJobDataFile cl = UltimateJobs.getPlayerDataFile();
		cl.load();
		
		int old = getJobLevel(uuid, job.toUpperCase());
		
		setJobLevel(uuid, job.toUpperCase(), old + exp);
	}
	
	public void remJobLevel(String uuid, String job, int exp) {
		PlayerJobDataFile cl = UltimateJobs.getPlayerDataFile();
		cl.load();
		
		int old = getJobLevel(uuid, job.toUpperCase());
		
		setJobLevel(uuid, job.toUpperCase(), old - exp);
	}
	
	public boolean isInJob(String uuid, String id) {
		PlayerJobDataFile cl = UltimateJobs.getPlayerDataFile();
		cl.load();
		return cl.get().getStringList("Player."+uuid+".CurrentJob").contains(id.toUpperCase()); 
	}
	
	public void addOwnJob(String uuid, String job) {
		PlayerJobDataFile cl = UltimateJobs.getPlayerDataFile();
		cl.load();
		List<String> b = cl.get().getStringList("Player."+uuid+".OwnsJob");
		b.add(job.toUpperCase());
		cl.get().set("Player."+uuid+".OwnsJob", b);
		cl.save();
	}
	
	public void remOwnJob(String uuid, String job) {
		PlayerJobDataFile cl = UltimateJobs.getPlayerDataFile();
		cl.load();
		List<String> b = cl.get().getStringList("Player."+uuid+".OwnsJob");
		b.remove(job.toUpperCase());
		cl.get().set("Player."+uuid+".OwnsJob", b);
		cl.save();
	}
	
	public void addCurrentJobs(String uuid, String job) {
		PlayerJobDataFile cl = UltimateJobs.getPlayerDataFile();
		cl.load();
		List<String> b = cl.get().getStringList("Player."+uuid+".CurrentJob");
		b.add(job.toUpperCase());
		cl.get().set("Player."+uuid+".CurrentJob", b);
		cl.save();
	}
	
	public void remCurrentJobs(String uuid, String job) {
		PlayerJobDataFile cl = UltimateJobs.getPlayerDataFile();
		cl.load();
		List<String> b = cl.get().getStringList("Player."+uuid+".CurrentJob");
		b.remove(job.toUpperCase());
		cl.get().set("Player."+uuid+".CurrentJob", b);
		cl.save();
	}
	
	public void setCurrentJobsToNull(String uuid) {
		PlayerJobDataFile cl = UltimateJobs.getPlayerDataFile();
		cl.load();

		cl.get().set("Player."+uuid+".CurrentJob", null);
		cl.save();
	}
	
	public List<String> getCurrentJobs(String uuid) {
		PlayerJobDataFile cl = UltimateJobs.getPlayerDataFile();
		cl.load();
		return cl.get().getStringList("Player."+uuid+".CurrentJob"); 
	}
	
	public int getMaxJobs(String uuid) {
		PlayerJobDataFile cl = UltimateJobs.getPlayerDataFile();
		cl.load();
		return cl.get().getInt("Player."+uuid+".MaxJobs"); 
	}
	
 
	public void setMaxJobs(String uuid, int d) {
		PlayerJobDataFile cl = UltimateJobs.getPlayerDataFile();
		cl.load();
		cl.get().set("Player."+uuid+".MaxJobs", d); 
		cl.save();  
	}
	
	public void addMaxJobs(String uuid, int m) {
		
		int old = getMaxJobs(uuid);
	
		setMaxJobs(uuid, old+m);
		
	}
	
	public void remMaxJobs(String uuid, int m) {
		
		int old = getMaxJobs(uuid);
	
		setMaxJobs(uuid, old-m);
		
	}
	
	public List<String> getOwn(String uuid) {
		PlayerJobDataFile cl = UltimateJobs.getPlayerDataFile();
		cl.load();
		return cl.get().getStringList("Player."+uuid+".OwnsJob"); 
	}
	
	public boolean ownJob(String uuid, String id) {
		PlayerJobDataFile cl = UltimateJobs.getPlayerDataFile();
		cl.load();
		return cl.get().getStringList("Player."+uuid+".OwnsJob").contains(id.toUpperCase()); 
	}
	
	public boolean existPlayer(String uuid) {
		PlayerJobDataFile cl = UltimateJobs.getPlayerDataFile();
		cl.load();
		return cl.get().contains("Player."+uuid+".CurrentJob");
	}

}
