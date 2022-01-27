package es.xdec0de.usleep.utils.files;
import java.io.File;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import es.xdec0de.usleep.USPMain;
import es.xdec0de.usleep.utils.Message;

public class Messages {
	
	private static USPMain plugin = USPMain.getPlugin(USPMain.class);
	public static FileConfiguration cfg;
	public static File file;
	  
	private final static String path = "messages.yml";
	
	  
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
	
	public static void sendMessage(CommandSender sndr, Message msg) {
		sndr.sendMessage(ChatColor.translateAlternateColorCodes('&', get().getString(msg.getPath()).replaceAll("%prefix%", get().getString(Message.PREFIX.getPath())).replaceAll("%error%", get().getString(Message.ERROR_PREFIX.getPath()))));
	}
	
	public static void sendMessage(Player p, Message msg) {
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', get().getString(msg.getPath()).replaceAll("%prefix%", get().getString(Message.PREFIX.getPath())).replaceAll("%error%", get().getString(Message.ERROR_PREFIX.getPath()))));
	}
	
	public static String getMessage(Message msg) {
		return ChatColor.translateAlternateColorCodes('&', get().getString(msg.getPath()).replaceAll("%prefix%", get().getString(Message.PREFIX.getPath())).replaceAll("%error%", get().getString(Message.ERROR_PREFIX.getPath())));
	}
}