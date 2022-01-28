package es.xdec0de.usleep;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import es.xdec0de.usleep.cmds.BedTP;
import es.xdec0de.usleep.cmds.USleepCMD;
import es.xdec0de.usleep.utils.USPSetting;
import es.xdec0de.usleep.utils.UpdateChecker;
import es.xdec0de.usleep.utils.files.USPConfig;
import es.xdec0de.usleep.utils.files.USPMessages;

public class USleep  extends JavaPlugin {

	private static USleep instance;

	public void onEnable() {
		executeEnable();
		USPMessages.log(" ");
		USPMessages.logCol("&8|------------------------------------------>");
		USPMessages.log(" ");
		USPMessages.logCol("             &e&luSleep &8- &aEnabled");
		USPMessages.log(" ");
		USPMessages.logCol("  &b- &7Author&8: &bxDec0de_");
		USPMessages.log(" ");
		USPMessages.logCol("  &b- &7Version: &b"+getDescription().getVersion());
		USPMessages.log(" ");
		USPMessages.logCol("&8|------------------------------------------>");
		USPMessages.log(" ");
		checkDependencies();
		checkUpdates();
	}

	public void onDisable() {
		USPMessages.log(" ");
		USPMessages.logCol("&8|------------------------------------------>");
		USPMessages.log(" ");
		USPMessages.logCol("             &e&luSleep &8- &cDisabled");
		USPMessages.log(" ");
		USPMessages.logCol("  &b- &7Author&8: &bxDec0de_");
		USPMessages.log(" ");
		USPMessages.logCol("  &b- &7Version: &b"+getDescription().getVersion());
		USPMessages.log(" ");
		USPMessages.logCol("&8|------------------------------------------>");
		USPMessages.log(" ");
	}

	private void executeEnable() {
		instance = this;
		USPConfig.setup();
		USPConfig.save(); // TODO What?
		USPMessages.setup();
		getCommand("usleep").setExecutor(new USleepCMD());
		getCommand("bedtp").setExecutor(new BedTP());
		getServer().getPluginManager().registerEvents(new SleepHandler(), this);
		getServer().getPluginManager().registerEvents(new UpdateChecker(), this);
	}

	private void checkDependencies() {
		if(Bukkit.getPluginManager().getPlugin("Essentials") != null)
			USPMessages.logCol("  &e- &bEssentials &7detected (&av" + Bukkit.getPluginManager().getPlugin("Essentials").getDescription().getVersion() + "&7)");
		else {
			USPMessages.logCol("  &e- &eEssentials &cnot detected, disabling &6AFK &cand &6Vanish &ccheck. &8(&bEssentials only&8)");
			USPConfig.get().set("Essentials.IgnoreAFK", false);
			USPConfig.get().set("Essentials.IgnoreVanished", false);
			USPConfig.save();
			USPConfig.reload();
		}
		USPMessages.log(" ");
		if(Bukkit.getPluginManager().getPlugin("SuperVanish") != null || Bukkit.getPluginManager().getPlugin("PremiumVanish") != null) {
			if (Bukkit.getPluginManager().getPlugin("SuperVanish") != null)
				USPMessages.logCol("  &e- &bSuperVanish &7detected (&av" + Bukkit.getPluginManager().getPlugin("SuperVanish").getDescription().getVersion() + "&7)");
			else
				USPMessages.logCol("  &e- &bPremiumVanish &7detected (&av" + Bukkit.getPluginManager().getPlugin("PremiumVanish").getDescription().getVersion() + "&7)");
		} else {
			USPMessages.logCol("  &e- &eSuperVanish &cor &ePremiumVanish &cnot detected, disabling &6Vanish &ccheck. &8(&bSuperVanish only&8)");
			USPConfig.get().set("SuperVanish.IgnoreVanished", false);
			USPConfig.save();
			USPConfig.reload();
		}
		USPMessages.log(" ");
	}

	private void checkUpdates() {
		if(USPConfig.getBoolean(USPSetting.UPDATER_NOTIFY_CONSOLE)) {
			UpdateChecker.getLatestVersion(version -> {
				USPMessages.log(" ");
				if(isLatest(version)) {
					USPMessages.logCol("&8|------------------------------------------>");
					USPMessages.log(" ");
					USPMessages.logCol("&e          uSleep &7update checker");
					USPMessages.log(" ");
					USPMessages.logCol("&b- &7You are running the latest version.");
					USPMessages.log(" ");
					USPMessages.logCol("&8|------------------------------------------>");
				} else {
					USPMessages.logCol("&8|------------------------------------------>");
					USPMessages.log(" ");
					USPMessages.logCol("&e          uSleep &7update checker");
					USPMessages.log(" ");
					USPMessages.logCol("&b- &7A new version is available&8: &6v"+ version);
					USPMessages.log(" ");
					USPMessages.logCol("&b- &7Currently using&8: &cv"+getDescription().getVersion());
					USPMessages.log(" ");
					USPMessages.logCol("&8|------------------------------------------>");
				}
				USPMessages.log(" ");
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
