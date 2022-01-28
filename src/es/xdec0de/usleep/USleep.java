package es.xdec0de.usleep;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import es.xdec0de.usleep.cmds.BedTP;
import es.xdec0de.usleep.cmds.USleepCMD;
import es.xdec0de.usleep.utils.USPSetting;
import es.xdec0de.usleep.utils.UpdateChecker;
import es.xdec0de.usleep.utils.files.USPConfig;
import es.xdec0de.usleep.utils.files.USPMessages;

public class USleep  extends JavaPlugin {

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
		USPConfig.setup(false);
		USPMessages.setup(false);
		getCommand("usleep").setExecutor(new USleepCMD());
		getCommand("bedtp").setExecutor(new BedTP());
		getServer().getPluginManager().registerEvents(new SleepHandler(), this);
		getServer().getPluginManager().registerEvents(new UpdateChecker(), this);
	}

	private void checkDependencies() {
		Plugin ess = Bukkit.getPluginManager().getPlugin("Essentials");
		Plugin vanish = Bukkit.getPluginManager().getPlugin("SuperVanish");
		if(vanish == null)
			vanish = Bukkit.getPluginManager().getPlugin("PremiumVanish");
		if(ess != null) {
			USPMessages.logCol("  &e- &bEssentials &7detected &8(&av" + ess.getDescription().getVersion() + "&8) &8[&dVanish &7and &dAFK&8]");
			if(vanish == null)
				vanish = ess;
		} else
			USPMessages.logCol("  &6- &cNo &eAFK &cplugin detected &8- &cAFK support disabled");
		USPMessages.log(" ");
		if(vanish != null)
			USPMessages.logCol("  &e- &b"+vanish.getName()+" &7detected &8(&av"+vanish.getDescription().getVersion()+"&8) &8[&dVanish&8]");
		else
			USPMessages.logCol("  &6- &cNo &evanish &cplugin detected &8- &cVanish support disabled");
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
}
