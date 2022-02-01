package es.xdec0de.usleep.utils.files;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import es.xdec0de.usleep.api.USleep;

public class USPConfig {

	private static USleep plugin = USleep.getPlugin(USleep.class);

	private static FileConfiguration cfg;
	private static File file;

	public static boolean setup(boolean isByReload) {
		if (!plugin.getDataFolder().exists())
			plugin.getDataFolder().mkdir(); 
		if (!(file = new File(plugin.getDataFolder(), "config.yml")).exists())
			plugin.saveResource("config.yml", false); 
		reload(true, isByReload);
		return checkStatus();
	}

	static void reload(boolean update, boolean isByReload) {
		cfg = (FileConfiguration)YamlConfiguration.loadConfiguration(file);
		if(update && FileUtils.updateFile(file, "config.yml", isByReload))
			reload(false, isByReload);
	}

	static void save() {
		try {
			cfg.save(file);
		} catch (IOException e) {
			USPMessages.logColRep("%prefix% &8[&cWarning&8] &cCould not save &econfig.yml&c.");
		}
	}

	static FileConfiguration get() {
		return cfg;
	}

	private static boolean checkStatus() {
		int percent = USPSetting.PERCENT_SLEEP_PERCENT.asInt();
		if(percent > 100 || percent < 1 ) {
			USPMessages.log(" ");
			USPMessages.logCol("&cConfiguration errors detected at &4config.yml&8:");
			USPMessages.logCol("  &4- &cPercent value is invalid, using default &8(&e50&8)");
			USPSetting.PERCENT_SLEEP_PERCENT.setReload(50);
			return false;
		}
		return true;
	}
}
