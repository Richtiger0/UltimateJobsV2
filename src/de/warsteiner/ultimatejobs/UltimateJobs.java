package de.warsteiner.ultimatejobs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import de.warsteiner.ultimatejobs.command.PlayerJobCommand;
import de.warsteiner.ultimatejobs.command.PlayerJobTabComplete;
import de.warsteiner.ultimatejobs.events.PlayerBlockPlaceEventAddFlag;
import de.warsteiner.ultimatejobs.events.PlayerExistEvent;
import de.warsteiner.ultimatejobs.events.PlayerLevelCheckEvent;
import de.warsteiner.ultimatejobs.events.gui.PlayerClickAtMainInventory;
import de.warsteiner.ultimatejobs.events.gui.PlayerClickAtOptionsInventory;
import de.warsteiner.ultimatejobs.events.jobs.ActionBlockBreak;
import de.warsteiner.ultimatejobs.events.jobs.ActionBlockPlace;
import de.warsteiner.ultimatejobs.events.jobs.ActionCraft;
import de.warsteiner.ultimatejobs.events.jobs.ActionEat;
import de.warsteiner.ultimatejobs.events.jobs.ActionFish;
import de.warsteiner.ultimatejobs.events.jobs.ActionHoney;
import de.warsteiner.ultimatejobs.events.jobs.ActionKillMob;
import de.warsteiner.ultimatejobs.events.jobs.ActionMilk;
import de.warsteiner.ultimatejobs.events.jobs.ActionShear;
import de.warsteiner.ultimatejobs.utils.Metrics;
import de.warsteiner.ultimatejobs.utils.api.JobAPI;
import de.warsteiner.ultimatejobs.utils.api.LevelAPI;
import de.warsteiner.ultimatejobs.utils.api.PlayerAPI;
import de.warsteiner.ultimatejobs.utils.api.RewardAPI;
import de.warsteiner.ultimatejobs.utils.api.WorldGuardManager;
import de.warsteiner.ultimatejobs.utils.builder.GuiBuilder;
import de.warsteiner.ultimatejobs.utils.data.PlayerJobDataFile;
import net.milkbowl.vault.economy.Economy;

public class UltimateJobs extends JavaPlugin {

	private static UltimateJobs plugin;
	private static Economy econ;
	private static YamlConfiguration jl;
	private static JobAPI api;
	private static GuiBuilder builder;
	private static YamlConfiguration jobsgui;
	private static YamlConfiguration main;
	private static PlayerAPI player;
	private static PlayerJobDataFile data;
	private static YamlConfiguration translation;
	private static LevelAPI lapi;
	private static RewardAPI rewards;
	private static YamlConfiguration cmd;

	@Override
	public void onLoad() {

		ArrayList<String> list = new ArrayList<String>();

		list.add("action-break");
		list.add("action-place");
		list.add("action-killmob");
		list.add("action-fish");
		list.add("action-milk");
		list.add("action-honey");
		list.add("action-eat");
		list.add("action-shear");
		list.add("action-craft");

		if (getServer().getPluginManager().getPlugin("WorldGuard") != null) {
			WorldGuardManager.load(list);
		}

	}

	@Override
	public void onEnable() {

		plugin = this;

		// create classes & load
		api = new JobAPI();
		builder = new GuiBuilder();
		player = new PlayerAPI();
		lapi = new LevelAPI();
		rewards = new RewardAPI();

		setupEconomy();

		// we create the jobs folder
		File file = new File(getPlugin().getDataFolder() + "/jobs/");
		file.mkdir();

		File file2 = new File(getPlugin().getDataFolder() + "/data/");
		file2.mkdir();

		// create custom configs
		load();

		// load data files
		data = new PlayerJobDataFile();
		data.create();

		// job events
		Bukkit.getPluginManager().registerEvents(new ActionBlockBreak(), this);
		Bukkit.getPluginManager().registerEvents(new ActionBlockPlace(), this);
		Bukkit.getPluginManager().registerEvents(new ActionKillMob(), this);
		Bukkit.getPluginManager().registerEvents(new ActionFish(), this);
		Bukkit.getPluginManager().registerEvents(new ActionMilk(), this);
		Bukkit.getPluginManager().registerEvents(new ActionHoney(), this);
		Bukkit.getPluginManager().registerEvents(new ActionEat(), this);
		Bukkit.getPluginManager().registerEvents(new ActionShear(), this);
		Bukkit.getPluginManager().registerEvents(new ActionCraft(), this);

		Bukkit.getPluginManager().registerEvents(new PlayerBlockPlaceEventAddFlag(), this);
		// other events

		Bukkit.getPluginManager().registerEvents(new PlayerExistEvent(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerClickAtMainInventory(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerClickAtOptionsInventory(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerLevelCheckEvent(), this);

		getCommand("jobs").setExecutor(new PlayerJobCommand());

		if (cmd.getBoolean("TabComplete")) {
			getCommand("jobs").setTabCompleter(new PlayerJobTabComplete());
		}

		UltimateJobs.getPlugin().getLogger().info("§a ");
		UltimateJobs.getPlugin().getLogger().info("§8-> §cLoaded UltimateJobs!");
		UltimateJobs.getPlugin().getLogger()
				.info("§7Jobs: " + getJobsListConfig().getStringList("Jobs").size() + " loaded");
		UltimateJobs.getPlugin().getLogger().info("§a ");

		new Metrics(getPlugin(), 8753);
	}
	
	public void load() {
		File joblist;
		File jobgui;
		File mainfile;
		File trans;
		File cmdfile;

		joblist = new File(UltimateJobs.getPlugin().getDataFolder(), "Jobs.yml");
		jobgui = new File(UltimateJobs.getPlugin().getDataFolder(), "JobsGUI.yml");
		mainfile = new File(UltimateJobs.getPlugin().getDataFolder(), "Main.yml");
		trans = new File(UltimateJobs.getPlugin().getDataFolder(), "Translation.yml");
		cmdfile = new File(UltimateJobs.getPlugin().getDataFolder(), "Command.yml");

		if (!trans.exists()) {
			trans.getParentFile().mkdirs();
			UltimateJobs.getPlugin().saveResource("Translation.yml", true);
		}

		translation = new YamlConfiguration();
		try {
			translation.load(trans);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}

		if (!cmdfile.exists()) {
			cmdfile.getParentFile().mkdirs();
			UltimateJobs.getPlugin().saveResource("Command.yml", true);
		}

		cmd = new YamlConfiguration();
		try {
			cmd.load(cmdfile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}

		if (!mainfile.exists()) {
			mainfile.getParentFile().mkdirs();
			UltimateJobs.getPlugin().saveResource("Main.yml", false);
		}

		main = new YamlConfiguration();
		try {
			main.load(mainfile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}

		if (!joblist.exists()) {
			joblist.getParentFile().mkdirs();
			UltimateJobs.getPlugin().saveResource("Jobs.yml", false);
		}

		jl = new YamlConfiguration();
		try {
			jl.load(joblist);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}

		if (!jobgui.exists()) {
			jobgui.getParentFile().mkdirs();
			UltimateJobs.getPlugin().saveResource("JobsGUI.yml", false);
		}

		jobsgui = new YamlConfiguration();
		try {
			jobsgui.load(jobgui);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	private boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager()
				.getRegistration(Economy.class);
		if (economyProvider != null) {
			econ = (Economy) economyProvider.getProvider();
		}

		return (econ != null);
	}

	public static LevelAPI getLevelAPI() {
		return lapi;
	}

	public static YamlConfiguration getCommandConfig() {
		return cmd;
	}

	public static RewardAPI getRewardAPI() {
		return rewards;
	}

	public static Economy getEconomy() {
		return econ;
	}

	public static YamlConfiguration getJobsListConfig() {
		return jl;
	}

	public static JobAPI getAPI() {
		return api;
	}

	public static YamlConfiguration getTranslation() {
		return translation;
	}

	public static PlayerJobDataFile getPlayerDataFile() {
		return data;
	}

	public static PlayerAPI getPlayerAPI() {
		return player;
	}

	public static YamlConfiguration getMainConfig() {
		return main;
	}

	public static GuiBuilder getBuilder() {
		return builder;
	}

	public static YamlConfiguration getJobsGUIConfig() {
		return jobsgui;
	}

	public static UltimateJobs getPlugin() {
		return plugin;
	}

}
