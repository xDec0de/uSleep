package es.xdec0de.usleep;

import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import es.xdec0de.usleep.bedinteract.Sleep;
import es.xdec0de.usleep.bedinteract.WakeUp;
import es.xdec0de.usleep.cmds.BedTP;
import es.xdec0de.usleep.cmds.USleepCMD;
import es.xdec0de.usleep.utils.Setting;
import es.xdec0de.usleep.utils.UpdateChecker;
import es.xdec0de.usleep.utils.files.Config;
import es.xdec0de.usleep.utils.files.Messages;

public class USPMain  extends JavaPlugin {
	
	public static USPMain plugin;
	public static Plugin instance;
	  
	public void onEnable() {
		plugin = this;
		instance = (Plugin)this;
		setupFiles();
		registerCommands();
		registerEvents();
		Bukkit.getConsoleSender().sendMessage(" ");
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8|------------------------------------------>"));
		Bukkit.getConsoleSender().sendMessage(" ");
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "             &e&luSleep &8- &aEnabled"));
		Bukkit.getConsoleSender().sendMessage(" ");
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &b- &7Author&8: &bxDec0de_"));
		Bukkit.getConsoleSender().sendMessage(" ");
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &b- &7Version: &b"+plugin.getDescription().getVersion()));
		Bukkit.getConsoleSender().sendMessage(" ");
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8|------------------------------------------>"));
		Bukkit.getConsoleSender().sendMessage(" ");
		checkDependencies();
		checkUpdates();
	}
	  
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage(" ");
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8|------------------------------------------>"));
		Bukkit.getConsoleSender().sendMessage(" ");
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "             &e&luSleep &8- &cDisabled"));
		Bukkit.getConsoleSender().sendMessage(" ");
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &b- &7Author&8: &bxDec0de_"));
		Bukkit.getConsoleSender().sendMessage(" ");
	    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &b- &7Version: &b"+plugin.getDescription().getVersion()));
	    Bukkit.getConsoleSender().sendMessage(" ");
	    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8|------------------------------------------>"));
	    Bukkit.getConsoleSender().sendMessage(" ");
	}
	
	private void setupFiles() {
		Config.setup();
		Config.save();
		Config.update();
	    Messages.setup();
	    Messages.save();
	    Messages.update();
	}
	
	private void registerCommands() {
		getCommand("usleep").setExecutor(new USleepCMD());
	    getCommand("bedtp").setExecutor(new BedTP());
	}
	
	private void registerEvents() {
		this.getServer().getPluginManager().registerEvents(new Sleep(), this);
		this.getServer().getPluginManager().registerEvents(new WakeUp(), this);
		this.getServer().getPluginManager().registerEvents(new UpdateChecker(), this);
	}
	
	private void checkDependencies() {
		if(Bukkit.getPluginManager().getPlugin("Essentials") != null) {
	    	Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &e- &bEssentials &7detected (&av" + Bukkit.getPluginManager().getPlugin("Essentials").getDescription().getVersion() + "&7)"));
	    	Bukkit.getConsoleSender().sendMessage(" ");
	    } else {
	    	Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &e- &eEssentials &cnot detected, disabling &6AFK &cand &6Vanish &ccheck. &8(&bEssentials only&8)"));
		    Bukkit.getConsoleSender().sendMessage(" ");
		    Config.get().set("Essentials.IgnoreAFK", Boolean.valueOf(false));
		    Config.get().set("Essentials.IgnoreVanished", Boolean.valueOf(false));
		    Config.save();
		    Config.reload();
	    } 
	    if(Bukkit.getPluginManager().getPlugin("SuperVanish") != null || Bukkit.getPluginManager().getPlugin("PremiumVanish") != null) {
	    	if (Bukkit.getPluginManager().getPlugin("SuperVanish") != null) {
	    		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &e- &bSuperVanish &7detected (&av" + Bukkit.getPluginManager().getPlugin("SuperVanish").getDescription().getVersion() + "&7)"));
	    	} else {
	    		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &e- &bPremiumVanish &7detected (&av" + Bukkit.getPluginManager().getPlugin("PremiumVanish").getDescription().getVersion() + "&7)"));
	    	} 
	    	Bukkit.getConsoleSender().sendMessage(" ");
	    } else {
	    	Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &e- &eSuperVanish &cor &ePremiumVanish &cnot detected, disabling &6Vanish &ccheck. &8(&bSuperVanish only&8)"));
		    Bukkit.getConsoleSender().sendMessage(" ");
		    Config.get().set("SuperVanish.IgnoreVanished", Boolean.valueOf(false));
		    Config.save();
		    Config.reload();
	    }
	}
	
	private void checkUpdates() {
		if(Config.getBoolean(Setting.UPDATER_ENABLED) && Config.getBoolean(Setting.UPDATER_MESSAGE_CONSOLE)) {
			UpdateChecker.getLatestVersion(version -> {
				Bukkit.getConsoleSender().sendMessage(" ");
	            if(isLatest(version)) {
	            	Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8|------------------------------------------>"));
	            	Bukkit.getConsoleSender().sendMessage(" ");
	            	Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&e          uSleep &7update checker"));
	            	Bukkit.getConsoleSender().sendMessage(" ");
	            	Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b- &7You are running the latest version."));
	            	Bukkit.getConsoleSender().sendMessage(" ");
	            	Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8|------------------------------------------>"));
	            } else {
	            	Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8|------------------------------------------>"));
	            	Bukkit.getConsoleSender().sendMessage(" ");
	            	Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&e          uSleep &7update checker"));
	            	Bukkit.getConsoleSender().sendMessage(" ");
	            	Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b- &7A new version is available&8: &6v"+ version));
	            	Bukkit.getConsoleSender().sendMessage(" ");
	            	Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b- &7Currently using&8: &cv"+plugin.getDescription().getVersion()));
	            	Bukkit.getConsoleSender().sendMessage(" ");
	            	Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8|------------------------------------------>"));
	            }
	            Bukkit.getConsoleSender().sendMessage(" ");
	        });
		}
	}

    private boolean isLatest(String update) {
    	String using = plugin.getDescription().getVersion();
        String s1 = normalisedVersion(using);
        String s2 = normalisedVersion(update);
        int cmp = s1.compareTo(s2);
        if(cmp == 0 || cmp > 0) {
        	return true;
        } else {
        	return false;
        }
    }

    private String normalisedVersion(String version) {
        return normalisedVersion(version, ".", 4);
    }

    private String normalisedVersion(String version, String sep, int maxWidth) {
        String[] split = Pattern.compile(sep, Pattern.LITERAL).split(version);
        StringBuilder sb = new StringBuilder();
        for (String s : split) {
            sb.append(String.format("%" + maxWidth + 's', s));
        }
        return sb.toString();
    }
}