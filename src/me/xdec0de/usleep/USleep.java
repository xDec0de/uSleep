package me.xdec0de.usleep;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import me.xdec0de.usleep.api.USleepAPI;
import me.xdec0de.usleep.cmds.BedTP;
import me.xdec0de.usleep.cmds.USleepCMD;
import net.codersky.mcutils.MCPlugin;
import net.codersky.mcutils.files.yaml.MessagesFile;
import net.codersky.mcutils.files.yaml.PluginFile;

public class USleep extends MCPlugin {

	private PluginFile worlds;

	private final USleepAPI api = new USleepAPI(this);

	@Override
	public void onEnable() {
		registerFile("config.yml", PluginFile.class);
		registerFile("messages.yml", MessagesFile.class);
		this.worlds = registerFile("worlds", PluginFile.class);
		registerCommands(new USleepCMD(this), new BedTP(this));
		final UpdateChecker updateChecker = new UpdateChecker(this);
		registerEvents(new SleepHandler(this), updateChecker);
		log(" ");
		logCol("&8|------------------------------------------>");
		log(" ");
		logCol("             &e&luSleep &8- &aEnabled");
		log(" ");
		logCol("  &b- &7Author&8: &bxDec0de_");
		log(" ");
		logCol("  &b- &7Version: &b"+getDescription().getVersion());
		log(" ");
		logCol("&8|------------------------------------------>");
		log(" ");
		checkDependencies();
		updateChecker.checkUpdates(Bukkit.getConsoleSender());
		checkFileStatus();
	}

	@Override
	public void onDisable() {
		log(" ");
		logCol("&8|------------------------------------------>");
		log(" ");
		logCol("             &e&luSleep &8- &cDisabled");
		log(" ");
		logCol("  &b- &7Author&8: &bxDec0de_");
		log(" ");
		logCol("  &b- &7Version: &b"+getDescription().getVersion());
		log(" ");
		logCol("&8|------------------------------------------>");
		log(" ");
	}

	private void checkFileStatus() {
		int percent = getConfig().getInt("Features.PercentSleep.Percentage");
		if(percent > 100 || percent < 1 ) {
			logCol(" ", "&cConfiguration errors detected at &4config.yml&8:",
				"  &4- &cPercent value is invalid, using default &8(&e50&8)");
			getConfig().set("Features.PercentSleep.Percentage", 50);
			getConfig().save();
		}
	}

	private void checkDependencies() {
		Plugin ess = Bukkit.getPluginManager().getPlugin("Essentials");
		Plugin vanish = Bukkit.getPluginManager().getPlugin("SuperVanish");
		if(vanish == null)
			vanish = Bukkit.getPluginManager().getPlugin("PremiumVanish");
		if(ess != null) {
			if(vanish == null) {
				vanish = ess;
				logCol("  &e- &bEssentials &7detected &8(&av" + ess.getDescription().getVersion() + "&8) &8[&dVanish &7and &dAFK&8]");
			} else
				logCol("  &e- &bEssentials &7detected &8(&av" + ess.getDescription().getVersion() + "&8) &8[&dAFK&8]");
		} else
			logCol("  &6- &cNo &eAFK &cplugin detected &8- &cAFK support disabled", " ");
		if(vanish != null)
			logCol("  &e- &b"+vanish.getName()+" &7detected &8(&av"+vanish.getDescription().getVersion()+"&8) &8[&dVanish&8]", " ");
		else
			logCol("  &6- &cNo &evanish &cplugin detected &8- &cVanish support disabled", " ");
	}

	/**
	 * Provides access to the {@link USleepAPI}.
	 * 
	 * @return An instance of {@link USleepAPI}.
	 * 
	 * @since uSleep 2.0.0
	 */
	@Nonnull
	public USleepAPI getAPI() {
		return api;
	}

	public PluginFile getWorlds() {
		return worlds;
	}
}
