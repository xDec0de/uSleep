package es.xdec0de.usleep.utils.files;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import es.xdec0de.usleep.api.USleep;

public class USPWorlds {

	private static USleep plugin = USleep.getPlugin(USleep.class);

	private static FileConfiguration cfg;
	private static File file;

	public static boolean setup() {
		if (!plugin.getDataFolder().exists())
			plugin.getDataFolder().mkdir(); 
		if (!(file = new File(plugin.getDataFolder(), "worlds.yml")).exists())
			plugin.saveResource("worlds.yml", false); 
		reload();
		return true;
	}

	private static void reload() {
		cfg = (FileConfiguration)YamlConfiguration.loadConfiguration(file);
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

	public static Set<String> getGroupIdentifiers() {
		return cfg.getConfigurationSection("Groups").getKeys(false);
	}
	
	public static List<String> getWorldsInGroup(String groupID) {
		return cfg.getStringList("Groups."+groupID+".Worlds");
	}

	public static int getPercentRequired(String groupID) {
		return cfg.getInt("Groups."+groupID+".Percent");
	}
}
