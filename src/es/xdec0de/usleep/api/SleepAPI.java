package es.xdec0de.usleep.api;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import com.earth2me.essentials.Essentials;
import es.xdec0de.usleep.utils.USPMessage;
import es.xdec0de.usleep.utils.USPSetting;
import es.xdec0de.usleep.utils.SuperVanish;
import es.xdec0de.usleep.utils.files.USPConfig;
import es.xdec0de.usleep.utils.files.USPMessages;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class SleepAPI {
	
	public static int NumSleep = 0;	
	
	public int getPlayerCount() {
		List<Player> list = new ArrayList<Player>();
		if(USPConfig.getBoolean(USPSetting.ESSENTIALS_IGNORE_AFK) && Bukkit.getPluginManager().getPlugin("Essentials") != null) {
			Essentials ess = (Essentials)Bukkit.getServer().getPluginManager().getPlugin("Essentials");
			for(Player p : Bukkit.getOnlinePlayers()) {
				if(!list.contains(p)) {
					if(ess.getUser(p).isAfk()) {
						list.add(p);
					}
				}
	        }
		}
		if(USPConfig.getBoolean(USPSetting.IGNORE_VANISHED)) {
			if(Bukkit.getPluginManager().getPlugin("Essentials") != null) {
				Essentials ess = (Essentials)Bukkit.getServer().getPluginManager().getPlugin("Essentials");
				for(Player p : Bukkit.getOnlinePlayers()) {
					if(!list.contains(p)) {
						if(ess.getUser(p).isVanished()) {
							list.add(p);
						}
					}
		        }
			}
			if(Bukkit.getPluginManager().getPlugin("SuperVanish") != null || Bukkit.getPluginManager().getPlugin("PremiumVanish") != null) {
				for(Player p : Bukkit.getOnlinePlayers()) {
					if(!list.contains(p)) {
			            if(SuperVanish.isVanished(p)) {
			            	list.add(p);
			            }
					}
		        } 
			}
		}
		return Bukkit.getOnlinePlayers().size() - list.size();
	}
	
	public void broadcastInstantNextDay(Player p) {
		Bukkit.broadcastMessage(USPMessages.getMessage(USPMessage.INSTANT_OK).replaceAll("%player%", p.getName()));
		if(USPConfig.getBoolean(USPSetting.PERCENT_SLEEP_SOUNDS_ENABLED)) {
			for (Player on : Bukkit.getOnlinePlayers()) {
                on.playSound(on.getLocation(), Sound.valueOf(USPConfig.getString(USPSetting.PERCENT_SLEEP_NEXT_DAY_SOUND)), 1.0F, 1.0F); 
			}
		}
	}
	
	public void broadcastPercentNextDay() {
		Bukkit.broadcastMessage(USPMessages.getMessage(USPMessage.PERCENT_NEXT_DAY));
		if(USPConfig.getBoolean(USPSetting.PERCENT_SLEEP_SOUNDS_ENABLED)) {
			for (Player on : Bukkit.getOnlinePlayers()) {
                on.playSound(on.getLocation(), Sound.valueOf(USPConfig.getString(USPSetting.PERCENT_SLEEP_NEXT_DAY_SOUND)), 1.0F, 1.0F); 
			}
		}
	}
	
	public void broadcastSleep() {
		if(USPConfig.getBoolean(USPSetting.ACTIONBAR_ENABLED)) {
			for(Player p : Bukkit.getOnlinePlayers()) {
				p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(USPMessages.getMessage(USPMessage.PERCENT_OK).replaceAll("%required%", Integer.toString(Math.round(getPlayerCount() * USPConfig.get().getInt("Events.PercentSleep.Percentage") / 100.0F))).replaceAll("%current%", Integer.toString(NumSleep))));
			}
		} else {
			Bukkit.broadcastMessage(USPMessages.getMessage(USPMessage.PERCENT_OK).replaceAll("%required%", Integer.toString(Math.round(getPlayerCount() * USPConfig.get().getInt("Events.PercentSleep.Percentage") / 100.0F))).replaceAll("%current%", Integer.toString(NumSleep)));
		}
		if(USPConfig.getBoolean(USPSetting.PERCENT_SLEEP_SOUNDS_ENABLED)) {
			for(Player p : Bukkit.getOnlinePlayers()) {
				p.playSound(p.getLocation(), Sound.valueOf(USPConfig.getString(USPSetting.PERCENT_SLEEP_SOUND)), 1.0F, 1.0F); 
			}
		}
	}
	
	public void broadcastWakeUp() {
		if(USPConfig.getBoolean(USPSetting.ACTIONBAR_ENABLED)) {
			for(Player p : Bukkit.getOnlinePlayers()) {
				p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(USPMessages.getMessage(USPMessage.PERCENT_OK).replaceAll("%required%", Integer.toString(Math.round(getPlayerCount() * USPConfig.get().getInt("Events.PercentSleep.Percentage") / 100.0F))).replaceAll("%current%", Integer.toString(NumSleep))));
			}
		} else {
			Bukkit.broadcastMessage(USPMessages.getMessage(USPMessage.PERCENT_OK).replaceAll("%required%", Integer.toString(Math.round(getPlayerCount() * USPConfig.get().getInt("Events.PercentSleep.Percentage") / 100.0F))).replaceAll("%current%", Integer.toString(NumSleep)));
		}
		if(USPConfig.getBoolean(USPSetting.PERCENT_SLEEP_SOUNDS_ENABLED)) {
			for(Player p : Bukkit.getOnlinePlayers()) {
				p.playSound(p.getLocation(), Sound.valueOf(USPConfig.getString(USPSetting.PERCENT_SLEEP_LEAVE_SOUND)), 1.0F, 1.0F); 
			}
		}
	}
}
