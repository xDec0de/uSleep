package es.xdec0de.usleep.api;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;

import es.xdec0de.usleep.SleepHandler;
import es.xdec0de.usleep.cmds.BedTP;
import es.xdec0de.usleep.cmds.USleepCMD;
import es.xdec0de.usleep.utils.UpdateChecker;
import me.xdec0de.mcutils.MCPlugin;
import me.xdec0de.mcutils.files.MessagesFile;
import me.xdec0de.mcutils.files.PluginFile;

public class USleep extends MCPlugin {

	private PluginFile cfg, worlds;
	private MessagesFile msg;

	private final USleepAPI api = new USleepAPI(this);

	@Override
	public void onEnable() {
		this.cfg = registerFile("config", PluginFile.class);
		this.msg = registerFile("messages", MessagesFile.class);
		this.worlds = registerFile("messages", PluginFile.class);
		registerCommands(new USleepCMD(this), new BedTP(this));
		registerEvents(new SleepHandler(this), WorldHandler.getInstance(), new UpdateChecker(this));
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
		checkUpdates();
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
		int percent = cfg.getInt("Features.PercentSleep.Percentage");
		if(percent > 100 || percent < 1 ) {
			logCol(" ", "&cConfiguration errors detected at &4config.yml&8:",
				"  &4- &cPercent value is invalid, using default &8(&e50&8)");
			cfg.set("Features.PercentSleep.Percentage", 50);
			cfg.save();
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

	private void checkUpdates() {
		if (!cfg.getBoolean("features.updater.console"))
			return;
		final ConsoleCommandSender console = Bukkit.getConsoleSender();
		final String latest = getLatestVersion(72205);
		if (api.isHigher(latest))
			msg.send(console, "updater.available.player", "%current%", getDescription().getVersion(), "%latest%", latest);
		else
			msg.send(console, "updater.latest.player", "%current%", latest);
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

	@Override
	public PluginFile getConfig() {
		return cfg;
	}

	public MessagesFile getMessages() {
		return msg;
	}
}
