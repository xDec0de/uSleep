package es.xdec0de.usleep.api;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import com.earth2me.essentials.Essentials;
import es.xdec0de.usleep.utils.Message;
import es.xdec0de.usleep.utils.Setting;
import es.xdec0de.usleep.utils.SuperVanish;
import es.xdec0de.usleep.utils.files.Config;
import es.xdec0de.usleep.utils.files.Messages;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class SleepAPI {
	
	public static int NumSleep = 0;	
	
	public int getPlayerCount() {
		List<Player> list = new ArrayList<Player>();
		if(Config.getBoolean(Setting.ESSENTIALS_IGNORE_AFK) && Bukkit.getPluginManager().getPlugin("Essentials") != null) {
			Essentials ess = (Essentials)Bukkit.getServer().getPluginManager().getPlugin("Essentials");
			for(Player p : Bukkit.getOnlinePlayers()) {
				if(!list.contains(p)) {
					if(ess.getUser(p).isAfk()) {
						list.add(p);
					}
				}
	        }
		}
		if(Config.getBoolean(Setting.IGNORE_VANISHED)) {
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
		Bukkit.broadcastMessage(Messages.getMessage(Message.INSTANT_OK).replaceAll("%player%", p.getName()));
		if(Config.getBoolean(Setting.PERCENT_SLEEP_SOUNDS_ENABLED)) {
			for (Player on : Bukkit.getOnlinePlayers()) {
                on.playSound(on.getLocation(), Sound.valueOf(Config.getString(Setting.PERCENT_SLEEP_NEXT_DAY_SOUND)), 1.0F, 1.0F); 
			}
		}
	}
	
	public void broadcastPercentNextDay() {
		Bukkit.broadcastMessage(Messages.getMessage(Message.PERCENT_NEXT_DAY));
		if(Config.getBoolean(Setting.PERCENT_SLEEP_SOUNDS_ENABLED)) {
			for (Player on : Bukkit.getOnlinePlayers()) {
                on.playSound(on.getLocation(), Sound.valueOf(Config.getString(Setting.PERCENT_SLEEP_NEXT_DAY_SOUND)), 1.0F, 1.0F); 
			}
		}
	}
	
	public void broadcastSleep() {
		if(Config.getBoolean(Setting.ACTIONBAR_ENABLED)) {
			for(Player p : Bukkit.getOnlinePlayers()) {
				p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Messages.getMessage(Message.PERCENT_OK).replaceAll("%required%", Integer.toString(Math.round(getPlayerCount() * Config.get().getInt("Events.PercentSleep.Percentage") / 100.0F))).replaceAll("%current%", Integer.toString(NumSleep))));
			}
		} else {
			Bukkit.broadcastMessage(Messages.getMessage(Message.PERCENT_OK).replaceAll("%required%", Integer.toString(Math.round(getPlayerCount() * Config.get().getInt("Events.PercentSleep.Percentage") / 100.0F))).replaceAll("%current%", Integer.toString(NumSleep)));
		}
		if(Config.getBoolean(Setting.PERCENT_SLEEP_SOUNDS_ENABLED)) {
			for(Player p : Bukkit.getOnlinePlayers()) {
				p.playSound(p.getLocation(), Sound.valueOf(Config.getString(Setting.PERCENT_SLEEP_SOUND)), 1.0F, 1.0F); 
			}
		}
	}
	
	public void broadcastWakeUp() {
		if(Config.getBoolean(Setting.ACTIONBAR_ENABLED)) {
			for(Player p : Bukkit.getOnlinePlayers()) {
				p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Messages.getMessage(Message.PERCENT_OK).replaceAll("%required%", Integer.toString(Math.round(getPlayerCount() * Config.get().getInt("Events.PercentSleep.Percentage") / 100.0F))).replaceAll("%current%", Integer.toString(NumSleep))));
			}
		} else {
			Bukkit.broadcastMessage(Messages.getMessage(Message.PERCENT_OK).replaceAll("%required%", Integer.toString(Math.round(getPlayerCount() * Config.get().getInt("Events.PercentSleep.Percentage") / 100.0F))).replaceAll("%current%", Integer.toString(NumSleep)));
		}
		if(Config.getBoolean(Setting.PERCENT_SLEEP_SOUNDS_ENABLED)) {
			for(Player p : Bukkit.getOnlinePlayers()) {
				p.playSound(p.getLocation(), Sound.valueOf(Config.getString(Setting.PERCENT_SLEEP_LEAVE_SOUND)), 1.0F, 1.0F); 
			}
		}
	}
}
