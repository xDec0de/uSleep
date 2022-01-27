package es.xdec0de.usleep;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import es.xdec0de.usleep.bedinteract.Sleep;
import es.xdec0de.usleep.bedinteract.WakeUp;
import es.xdec0de.usleep.cmds.BedTP;
import es.xdec0de.usleep.cmds.USleepCMD;
import es.xdec0de.usleep.utils.USPSetting;
import es.xdec0de.usleep.utils.UpdateChecker;
import es.xdec0de.usleep.utils.files.USPConfig;
import es.xdec0de.usleep.utils.files.USPMessages;

public class USleep  extends JavaPlugin {

	private static USleep instance;

	public void onEnable() {
		instance = this;
		setupFiles();
		registerCommands();
		registerEvents();
		Bukkit.getConsoleSender().sendMessage(" ");
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8|------------------------------------------>"));
		Bukkit.getConsoleSender().sendMessage(" ");
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "             &e&luSleep &8- &aEnabled"));
		Bukkit.getConsoleSender().sendMessage(" ");
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &b- &7Author&8: &bxDec0de_"));
		Bukkit.getConsoleSender().sendMessage(" ");
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &b- &7Version: &b"+getDescription().getVersion()));
		Bukkit.getConsoleSender().sendMessage(" ");
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8|------------------------------------------>"));
		Bukkit.getConsoleSender().sendMessage(" ");
		checkDependencies();
		checkUpdates();
	}

	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage(" ");
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8|------------------------------------------>"));
		Bukkit.getConsoleSender().sendMessage(" ");
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "             &e&luSleep &8- &cDisabled"));
		Bukkit.getConsoleSender().sendMessage(" ");
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &b- &7Author&8: &bxDec0de_"));
		Bukkit.getConsoleSender().sendMessage(" ");
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &b- &7Version: &b"+getDescription().getVersion()));
		Bukkit.getConsoleSender().sendMessage(" ");
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8|------------------------------------------>"));
		Bukkit.getConsoleSender().sendMessage(" ");
	}

	private void setupFiles() {
		USPConfig.setup();
		USPConfig.save(); // TODO What?
		USPMessages.setup();
		USPMessages.save(); // TODO Why?
	}

	private void registerCommands() {
		getCommand("usleep").setExecutor(new USleepCMD());
		getCommand("bedtp").setExecutor(new BedTP());
	}

	private void registerEvents() {
		this.getServer().getPluginManager().registerEvents(new Sleep(), this);
		this.getServer().getPluginManager().registerEvents(new WakeUp(), this);
		this.getServer().getPluginManager().registerEvents(new UpdateChecker(), this);
	}

	private void checkDependencies() {
		if(Bukkit.getPluginManager().getPlugin("Essentials") != null) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &e- &bEssentials &7detected (&av" + Bukkit.getPluginManager().getPlugin("Essentials").getDescription().getVersion() + "&7)"));
			Bukkit.getConsoleSender().sendMessage(" ");
		} else {
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &e- &eEssentials &cnot detected, disabling &6AFK &cand &6Vanish &ccheck. &8(&bEssentials only&8)"));
			Bukkit.getConsoleSender().sendMessage(" ");
			USPConfig.get().set("Essentials.IgnoreAFK", Boolean.valueOf(false));
			USPConfig.get().set("Essentials.IgnoreVanished", Boolean.valueOf(false));
			USPConfig.save();
			USPConfig.reload();
		}
		if(Bukkit.getPluginManager().getPlugin("SuperVanish") != null || Bukkit.getPluginManager().getPlugin("PremiumVanish") != null) {
			if (Bukkit.getPluginManager().getPlugin("SuperVanish") != null) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &e- &bSuperVanish &7detected (&av" + Bukkit.getPluginManager().getPlugin("SuperVanish").getDescription().getVersion() + "&7)"));
			} else {
				Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &e- &bPremiumVanish &7detected (&av" + Bukkit.getPluginManager().getPlugin("PremiumVanish").getDescription().getVersion() + "&7)"));
			}
			Bukkit.getConsoleSender().sendMessage(" ");
		} else {
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &e- &eSuperVanish &cor &ePremiumVanish &cnot detected, disabling &6Vanish &ccheck. &8(&bSuperVanish only&8)"));
			Bukkit.getConsoleSender().sendMessage(" ");
			USPConfig.get().set("SuperVanish.IgnoreVanished", Boolean.valueOf(false));
			USPConfig.save();
			USPConfig.reload();
		}
	}

	private void checkUpdates() {
		if(USPConfig.getBoolean(USPSetting.UPDATER_ENABLED) && USPConfig.getBoolean(USPSetting.UPDATER_MESSAGE_CONSOLE)) {
			UpdateChecker.getLatestVersion(version -> {
				Bukkit.getConsoleSender().sendMessage(" ");
				if(isLatest(version)) {
					Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8|------------------------------------------>"));
					Bukkit.getConsoleSender().sendMessage(" ");
					Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&e          uSleep &7update checker"));
					Bukkit.getConsoleSender().sendMessage(" ");
					Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b- &7You are running the latest version."));
					Bukkit.getConsoleSender().sendMessage(" ");
					Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8|------------------------------------------>"));
				} else {
					Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8|------------------------------------------>"));
					Bukkit.getConsoleSender().sendMessage(" ");
					Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&e          uSleep &7update checker"));
					Bukkit.getConsoleSender().sendMessage(" ");
					Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b- &7A new version is available&8: &6v"+ version));
					Bukkit.getConsoleSender().sendMessage(" ");
					Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b- &7Currently using&8: &cv"+getDescription().getVersion()));
					Bukkit.getConsoleSender().sendMessage(" ");
					Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8|------------------------------------------>"));
				}
				Bukkit.getConsoleSender().sendMessage(" ");
			});
		}
	}

	private boolean isLatest(String update) {
		return getDescription().getVersion().compareTo(update) >= 0;
	}

	public static USleep getInstance() {
		return instance;
	}
}
