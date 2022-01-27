package es.xdec0de.usleep.utils.files;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import es.xdec0de.usleep.USleep;
import es.xdec0de.usleep.utils.USPSetting;

public class USPConfig {
	
	private static USleep plugin = USleep.getPlugin(USleep.class);
	public static FileConfiguration cfg;
	public static File file;
	
	private final static String path = "config.yml";
	
	  
	public static void setup() {
		if(!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
	    }
	    if(!(file = new File(plugin.getDataFolder(), path)).exists()) {
	    	plugin.saveResource(path, false);
	    }
	    cfg = YamlConfiguration.loadConfiguration(file);
	}
	
	public static void update() {
		try {
			if(new File(plugin.getDataFolder() + "/"+path).exists()) {
				boolean changesMade = false;
	            YamlConfiguration updated = new YamlConfiguration();
	            updated.load(FileUtils.copyInputStreamToFile(plugin.getResource(path)));
	            cfg.load(plugin.getDataFolder() + "/"+path);
	            for(String str : updated.getKeys(true)) {
	            	if(!cfg.getKeys(true).contains(str)) {
	            		cfg.set(str, updated.get(str));
	                    changesMade = true;
	                }
	            }
	            if(changesMade) {
	            	cfg.save(plugin.getDataFolder() + "/"+path);
	            	reload();
	            	Bukkit.getConsoleSender().sendMessage(" ");
	                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&lu&9&lSleep&8: &7Updated "+path+" for &bv"+plugin.getDescription().getVersion()));
	            }
	        }
		} catch (IOException | InvalidConfigurationException e) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&lu&9&lSleep: &8[&cWarning&8] &eCould not update "+path+" file!"));
	    }
    }
	  
	public static FileConfiguration get() {
	    return cfg;
	}
	  
	public static void save() {
	    try {
	      cfg.save(file);
	    } catch (IOException e) {
	      Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&lu&9&lSleep: &8[&cWarning&8] &eCould not save "+path+".yml file!"));
	    } 
	}
	  
	public static void reload() {
	    cfg = (FileConfiguration)YamlConfiguration.loadConfiguration(file);
	}
	public static String getString(USPSetting setting) {
    	return ChatColor.translateAlternateColorCodes('&', get().getString(setting.getPath()));
    }
    
    public static List<String> getStringList(USPSetting setting) {
    	List<String> list = new ArrayList<String>();
    	for(String s : get().getStringList(setting.getPath())) {
    		list.add(ChatColor.translateAlternateColorCodes('&', s));
    	}
    	return list;
    }
    
    public static int getInt(USPSetting setting) {
    	return get().getInt(setting.getPath());
    }
    
    public static long getLong(USPSetting setting) {
    	return get().getLong(setting.getPath());
    }
    
    public static double getDouble(USPSetting setting) {
    	return get().getDouble(setting.getPath());
    }
    
    public static boolean getBoolean(USPSetting setting) {
    	return get().getBoolean(setting.getPath());
    }
}