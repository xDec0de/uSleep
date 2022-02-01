package es.xdec0de.usleep.utils.files;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import es.xdec0de.usleep.USleep;

public class USPConfig {

	private static USleep plugin = USleep.getPlugin(USleep.class);

	public static FileConfiguration cfg;
	public static File file;

	public static boolean setup(boolean isByReload) {
		if (!plugin.getDataFolder().exists())
			plugin.getDataFolder().mkdir(); 
		if (!(file = new File(plugin.getDataFolder(), "config.yml")).exists())
			plugin.saveResource("config.yml", false); 
		reload(true, isByReload);
		return checkStatus();
	}

	private static void reload(boolean update, boolean isByReload) {
		cfg = (FileConfiguration)YamlConfiguration.loadConfiguration(file);
		if(update && FileUtils.updateFile(file, "config.yml", isByReload))
			reload(false, isByReload);
	}

	public static void save() {
		try {
			cfg.save(file);
		} catch (IOException e) {
			USPMessages.logColRep("%prefix% &8[&cWarning&8] &cCould not save &econfig.yml&c.");
		}
	} 

	private static boolean checkStatus() {
		int percent = getInt(USPSetting.PERCENT_SLEEP_PERCENT);
		if(percent > 100 || percent < 1 ) {
			USPMessages.log(" ");
			USPMessages.logCol("&cConfiguration errors detected at &4config.yml&8:");
			USPMessages.logCol("  &4- &cPercent value is invalid, using default &8(&e50&8)");
			cfg.set(USPSetting.PERCENT_SLEEP_PERCENT.getPath(), 50);
			save();
			reload(false, false);
			return false;
		}
		return true;
	}

	public static String getString(USPSetting setting) {
		return cfg.getString(setting.getPath());
	}

	public static List<String> getStringList(USPSetting setting) {
		return cfg.getStringList(setting.getPath());
	}

	public static int getInt(USPSetting setting) {
		return cfg.getInt(setting.getPath());
	}

	public static boolean getBoolean(USPSetting setting) {
		return cfg.getBoolean(setting.getPath());
	}
}
