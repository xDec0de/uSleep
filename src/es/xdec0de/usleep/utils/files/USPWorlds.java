package es.xdec0de.usleep.utils.files;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import es.xdec0de.usleep.api.USleep;
import es.xdec0de.usleep.utils.ListUtils;

public class USPWorlds {

	private static USleep plugin = USleep.getPlugin(USleep.class);

	private static FileConfiguration cfg;
	private static File file;

	private static final HashMap<String, List<String>> groups = new HashMap<String, List<String>>();

	public static boolean setup() {
		if (!plugin.getDataFolder().exists())
			plugin.getDataFolder().mkdir(); 
		if (!(file = new File(plugin.getDataFolder(), "worlds.yml")).exists())
			plugin.saveResource("worlds.yml", false); 
		return reload();
	}

	private static boolean reload() {
		cfg = (FileConfiguration)YamlConfiguration.loadConfiguration(file);
		groups.clear();
		for(String groupId : cfg.getConfigurationSection("Groups").getKeys(false)) {
			ArrayList<String> worlds = new ArrayList<String>();
			for(String worldID : cfg.getStringList("Groups."+groupId+".Worlds")) {
				worlds.add(worldID);
			}
			groups.put(groupId, worlds);
		}
		return checkStatus();
	}

	static void save() {
		try {
			cfg.save(file);
		} catch (IOException e) {
			USPMessages.logColRep("%prefix% &8[&cWarning&8] &cCould not save &eworlds.yml&c.");
		}
	}

	static FileConfiguration get() {
		return cfg;
	}

	public static Set<String> getGroupIdentifiers() {
		return groups.keySet();
	}

	public static List<String> getWorldsInGroup(String groupID) {
		return groups.get(groupID);
	}

	public static String getSleepGroupID(World world) {
		for(String groupID : groups.keySet())
			if(groups.get(groupID).contains(world.getName()))
				return groupID;
		return "__usleep_def_sleep_group__";
	}

	public static int getPercentRequired(String groupID) {
		return cfg.getInt("Groups."+groupID+".Percent");
	}

	private static boolean checkStatus() {
		HashMap<String, List<String>> groupErrors = new HashMap<String, List<String>>();
		for(String groupId : groups.keySet()) {
			ArrayList<String> worlds = new ArrayList<String>();
			for(String worldID : groups.get(groupId)) {
				if(Bukkit.getWorld(worldID) == null)
					worlds.add(worldID);
			}
			if(!worlds.isEmpty())
				groupErrors.put(groupId, worlds);
		}
		if(!groupErrors.isEmpty()) {
			USPMessages.logCol("&cSleep group errors detected at &4worlds.yml&8:");
			for(String id : groupErrors.keySet())
				USPMessages.logCol("  &4- &6"+id+" &chas non-existing worlds&8: &e"+ListUtils.join(groupErrors.get(id).toArray(), "&8, &e")+"&c.");
			return false;
		}
		return true;
	}
}
