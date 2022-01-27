package es.xdec0de.usleep.api;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.earth2me.essentials.Essentials;

import es.xdec0de.usleep.utils.SuperVanish;
import es.xdec0de.usleep.utils.USPMessage;
import es.xdec0de.usleep.utils.USPSetting;
import es.xdec0de.usleep.utils.files.USPConfig;
import es.xdec0de.usleep.utils.files.USPMessages;

public class SleepAPI {

	public static int NumSleep = 0;	

	public int getPlayerCount() {
		List<Player> list = new ArrayList<Player>();
		if(USPConfig.getBoolean(USPSetting.ESSENTIALS_IGNORE_AFK) && Bukkit.getPluginManager().getPlugin("Essentials") != null) {
			Essentials ess = (Essentials)Bukkit.getServer().getPluginManager().getPlugin("Essentials");
			for(Player p : Bukkit.getOnlinePlayers().stream().filter(on -> !list.contains(on)).collect(Collectors.toList()))
				if(ess.getUser(p).isAfk())
					list.add(p);
		}
		if(USPConfig.getBoolean(USPSetting.IGNORE_VANISHED)) {
			if(Bukkit.getPluginManager().getPlugin("Essentials") != null) {
				Essentials ess = (Essentials)Bukkit.getServer().getPluginManager().getPlugin("Essentials");
				for(Player p : Bukkit.getOnlinePlayers().stream().filter(on -> !list.contains(on)).collect(Collectors.toList()))
					if(ess.getUser(p).isVanished())
						list.add(p);
			}
			if(Bukkit.getPluginManager().getPlugin("SuperVanish") != null || Bukkit.getPluginManager().getPlugin("PremiumVanish") != null) {
				for(Player p : Bukkit.getOnlinePlayers().stream().filter(on -> !list.contains(on)).collect(Collectors.toList()))
					if(SuperVanish.isVanished(p))
						list.add(p);
			}
		}
		return Bukkit.getOnlinePlayers().size() - list.size();
	}
	
	public void broadcastInstantNextDay(Player p) {
		USPMessages.broadcast(USPMessage.INSTANT_OK, "%player%", p.getName());
		if(USPConfig.getBoolean(USPSetting.PERCENT_SLEEP_SOUNDS_ENABLED))
			Bukkit.getOnlinePlayers().forEach(on -> on.playSound(on.getLocation(), Sound.valueOf(USPConfig.getString(USPSetting.PERCENT_SLEEP_NEXT_DAY_SOUND)), 1.0F, 1.0F));
	}
	
	public void broadcastPercentNextDay() {
		USPMessages.broadcast(USPMessage.PERCENT_NEXT_DAY);
		if(USPConfig.getBoolean(USPSetting.PERCENT_SLEEP_SOUNDS_ENABLED))
			Bukkit.getOnlinePlayers().forEach(on -> on.playSound(on.getLocation(), Sound.valueOf(USPConfig.getString(USPSetting.PERCENT_SLEEP_NEXT_DAY_SOUND)), 1.0F, 1.0F));
	}
	
	public void broadcastSleep() {
		String required = Integer.toString(Math.round(getPlayerCount() * USPConfig.get().getInt("Events.PercentSleep.Percentage") / 100.0F));
		String current = Integer.toString(NumSleep);
		if(USPConfig.getBoolean(USPSetting.ACTIONBAR_ENABLED))
			USPMessages.broadcastActionbar(USPMessage.PERCENT_OK, "%required%", required, "%current%", current);
		else
			USPMessages.broadcast(USPMessage.PERCENT_OK, "%required%", required, "%current%", current);
		if(USPConfig.getBoolean(USPSetting.PERCENT_SLEEP_SOUNDS_ENABLED))
			Bukkit.getOnlinePlayers().forEach(on -> on.playSound(on.getLocation(), Sound.valueOf(USPConfig.getString(USPSetting.PERCENT_SLEEP_SOUND)), 1.0F, 1.0F));
	}
	
	public void broadcastWakeUp() {
		String required = Integer.toString(Math.round(getPlayerCount() * USPConfig.get().getInt("Events.PercentSleep.Percentage") / 100.0F));
		String current = Integer.toString(NumSleep);
		if(USPConfig.getBoolean(USPSetting.ACTIONBAR_ENABLED))
			USPMessages.broadcastActionbar(USPMessage.PERCENT_OK, "%required%", required, "%current%", current);
		else
			USPMessages.broadcast(USPMessage.PERCENT_OK, "%required%", required, "%current%", current);
		if(USPConfig.getBoolean(USPSetting.PERCENT_SLEEP_SOUNDS_ENABLED))
			Bukkit.getOnlinePlayers().forEach(on -> on.playSound(on.getLocation(), Sound.valueOf(USPConfig.getString(USPSetting.PERCENT_SLEEP_LEAVE_SOUND)), 1.0F, 1.0F));
	}
}
