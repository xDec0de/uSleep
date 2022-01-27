package es.xdec0de.usleep.api;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.earth2me.essentials.Essentials;

import es.xdec0de.usleep.utils.SuperVanish;
import es.xdec0de.usleep.utils.USPMessage;
import es.xdec0de.usleep.utils.USPSetting;
import es.xdec0de.usleep.utils.files.USPConfig;
import es.xdec0de.usleep.utils.files.USPMessages;

public class SleepAPI {

	private static int numSleep = 0;	

	public static boolean handleSleep(Player player) {
		boolean instant = USPConfig.getBoolean(USPSetting.INSTANT_SLEEP_ENABLED) && player.hasPermission(USPConfig.getString(USPSetting.INSTANT_SLEEP_PERM));
		boolean percent = USPConfig.getBoolean(USPSetting.PERCENT_SLEEP_ENABLED) && player.hasPermission(USPConfig.getString(USPSetting.PERCENT_SLEEP_ENABLED));
		if(instant) {
			resetDay(player.getWorld());
			broadcastInstantNextDay(player);
		} if(percent) {
			numSleep++;
			if(getRequiredPlayers() <= numSleep) {
				resetDay(player.getWorld());
				broadcastSleep();
			} else
				broadcastPercentNextDay();
		} else
			return false;
		return true;
	}

	public static void handleWakeUp() {
		if(numSleep != 0) {
			numSleep--;
			broadcastWakeUp();
		}
	}

	private static void resetDay(World world) {
		SleepAPI.numSleep = 0;
		world.setTime(0L);
		world.setThundering(false);
		world.setStorm(false);
	}

	private static int getRequiredPlayers() {
		return Math.round(getPlayerCount() * USPConfig.getInt(USPSetting.PERCENT_SLEEP_PERCENT) / 100.0F);
	}

	private static int getPlayerCount() {
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

	private static void broadcastInstantNextDay(Player p) {
		USPMessages.broadcast(USPMessage.INSTANT_OK, "%player%", p.getName());
		if(USPConfig.getBoolean(USPSetting.PERCENT_SLEEP_SOUNDS_ENABLED))
			Bukkit.getOnlinePlayers().forEach(on -> on.playSound(on.getLocation(), Sound.valueOf(USPConfig.getString(USPSetting.PERCENT_SLEEP_NEXT_DAY_SOUND)), 1.0F, 1.0F));
	}
	
	private static void broadcastPercentNextDay() {
		USPMessages.broadcast(USPMessage.PERCENT_NEXT_DAY);
		if(USPConfig.getBoolean(USPSetting.PERCENT_SLEEP_SOUNDS_ENABLED))
			Bukkit.getOnlinePlayers().forEach(on -> on.playSound(on.getLocation(), Sound.valueOf(USPConfig.getString(USPSetting.PERCENT_SLEEP_NEXT_DAY_SOUND)), 1.0F, 1.0F));
	}
	
	private static void broadcastSleep() {
		String required = Integer.toString(getRequiredPlayers());
		String current = Integer.toString(numSleep);
		if(USPConfig.getBoolean(USPSetting.ACTIONBAR_ENABLED))
			USPMessages.broadcastActionbar(USPMessage.PERCENT_OK, "%required%", required, "%current%", current);
		else
			USPMessages.broadcast(USPMessage.PERCENT_OK, "%required%", required, "%current%", current);
		if(USPConfig.getBoolean(USPSetting.PERCENT_SLEEP_SOUNDS_ENABLED))
			Bukkit.getOnlinePlayers().forEach(on -> on.playSound(on.getLocation(), Sound.valueOf(USPConfig.getString(USPSetting.PERCENT_SLEEP_SOUND)), 1.0F, 1.0F));
	}
	
	private static void broadcastWakeUp() {
		String required = Integer.toString(getRequiredPlayers());
		String current = Integer.toString(numSleep);
		if(USPConfig.getBoolean(USPSetting.ACTIONBAR_ENABLED))
			USPMessages.broadcastActionbar(USPMessage.PERCENT_OK, "%required%", required, "%current%", current);
		else
			USPMessages.broadcast(USPMessage.PERCENT_OK, "%required%", required, "%current%", current);
		if(USPConfig.getBoolean(USPSetting.PERCENT_SLEEP_SOUNDS_ENABLED))
			Bukkit.getOnlinePlayers().forEach(on -> on.playSound(on.getLocation(), Sound.valueOf(USPConfig.getString(USPSetting.PERCENT_SLEEP_LEAVE_SOUND)), 1.0F, 1.0F));
	}
}
