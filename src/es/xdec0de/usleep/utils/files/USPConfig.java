package es.xdec0de.usleep.utils.files;

import java.io.File;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import es.xdec0de.usleep.USleep;
import es.xdec0de.usleep.utils.USPSetting;

public class USPConfig {

	private static USleep plugin = USleep.getPlugin(USleep.class);

	public static FileConfiguration cfg;
	public static File file;

	public static void setup(boolean isByReload) {
		if (!plugin.getDataFolder().exists())
			plugin.getDataFolder().mkdir(); 
		if (!(file = new File(plugin.getDataFolder(), "config.yml")).exists())
			plugin.saveResource("config.yml", false); 
		reload(true, isByReload);
	}

	private static void reload(boolean update, boolean isByReload) {
		cfg = (FileConfiguration)YamlConfiguration.loadConfiguration(file);
		if(update && FileUtils.updateFile(file, "config.yml", isByReload))
			reload(false, isByReload);
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
