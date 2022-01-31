package es.xdec0de.usleep.api;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;

import es.xdec0de.usleep.USleep;
import es.xdec0de.usleep.utils.NotificationHandler;
import es.xdec0de.usleep.utils.USPMessage;
import es.xdec0de.usleep.utils.USPSetting;
import es.xdec0de.usleep.utils.files.USPConfig;
import es.xdec0de.usleep.utils.files.USPMessages;

public class USleepAPI {

	private static int numSleep = 0;
	private static List<UUID> onDelay = new ArrayList<UUID>();

	public static boolean handleSleep(Player player) {
		int cooldown = USPConfig.getInt(USPSetting.PERCENT_SLEEP_COOLDOWN);
		if(cooldown > 0) { 
			UUID uuid = player.getUniqueId();
			onDelay.add(uuid);
			Bukkit.getScheduler().runTaskTimerAsynchronously(USleep.getPlugin(USleep.class), () -> onDelay.remove(uuid), 0L, cooldown * 20L);
		}
		if(USPConfig.getBoolean(USPSetting.INSTANT_SLEEP_ENABLED) && player.hasPermission(USPConfig.getString(USPSetting.PERM_INSTANT_SLEEP))) // instant
			resetDay(player.getWorld(), player);
		else if(USPConfig.getBoolean(USPSetting.PERCENT_SLEEP_ENABLED) && player.hasPermission(USPConfig.getString(USPSetting.PERM_PERCENT_SLEEP))) { // percent
			numSleep++;
			if(getRequiredPlayers() <= numSleep) {
				NotificationHandler.broadcastActionbarSleepMessage(USPMessage.PERCENT_OK, "%required%", Integer.toString(getRequiredPlayers()), "%current%", Integer.toString(numSleep));
				NotificationHandler.broadcastSound(USPSetting.SOUND_SLEEP_OK);
			} else
				resetDay(player.getWorld(), null);
		} else
			return false;
		return true;
	}

	public static boolean hasSleepCooldown(Player player) {
		return onDelay.contains(player.getUniqueId());
	}

	public static void handleWakeUp() {
		if(numSleep > 0) {
			numSleep--;
			NotificationHandler.broadcastActionbarSleepMessage(USPMessage.PERCENT_OK, "%required%", Integer.toString(getRequiredPlayers()), "%current%", Integer.toString(numSleep));
			NotificationHandler.broadcastSound(USPSetting.SOUND_SLEEP_LEAVE);
		}
	}

	private static void resetDay(World world, Player player) {
		numSleep = 0;
		if(USPConfig.getBoolean(USPSetting.NIGHT_SKIP_EFFECT_ENABLED)) {
			doNightSkipEffect(world);
		} else {
			world.setTime(0L);
			world.setThundering(false);
			world.setStorm(false);
		}
		if(player != null) {
			USPMessages.broadcast(USPMessage.INSTANT_OK, "%player%", player.getName());
			NotificationHandler.broadcastSound(USPSetting.SOUND_NEXTDAY_PERCENT);
		} else {
			USPMessages.broadcast(USPMessage.PERCENT_NEXT_DAY);
			NotificationHandler.broadcastSound(USPSetting.SOUND_NEXTDAY_PERCENT);
		}
	}

	public static int getRequiredPlayers() {
		return Math.round(getActivePlayers() * USPConfig.getInt(USPSetting.PERCENT_SLEEP_PERCENT) / 100.0F);
	}

	public static int getActivePlayers() {
		List<Player> list = new LinkedList<Player>();
		boolean ignoreAFK = USPConfig.getBoolean(USPSetting.PERCENT_SLEEP_IGNORE_AFK);
		boolean ignoreVanished = USPConfig.getBoolean(USPSetting.PERCENT_SLEEP_IGNORE_VANISHED);
		if(ignoreAFK || ignoreVanished) {
			if(Bukkit.getPluginManager().getPlugin("Essentials") != null) {
				Essentials ess = (Essentials)Bukkit.getServer().getPluginManager().getPlugin("Essentials");
				for(Player p : Bukkit.getOnlinePlayers().stream().filter(on -> !list.contains(on)).collect(Collectors.toList())) {
					User user = ess.getUser(p);
					if((ignoreAFK && user.isAfk()) || (ignoreVanished && user.isVanished()))
						list.add(p);
				}
			}
			if(ignoreVanished)
				if(Bukkit.getPluginManager().getPlugin("SuperVanish") != null || Bukkit.getPluginManager().getPlugin("PremiumVanish") != null)
					for(Player p : Bukkit.getOnlinePlayers().stream().filter(on -> !list.contains(on)).collect(Collectors.toList()))
						if(isVanished(p))
							list.add(p);
		}
		return Bukkit.getOnlinePlayers().size() - list.size();
	}

	public static boolean isVanished(Player player) {
		for(MetadataValue meta : player.getMetadata("vanished"))
			if(meta.asBoolean())
				return true;
		return false;
	}

	public static void doNightSkipEffect(World world) {
		double increase = USPConfig.getInt(USPSetting.NIGHT_SKIP_EFFECT_INCREMENT);
		int stop = (int) (Math.round(increase) + 1);
		while(world.getTime() <= stop)
			world.setTime(world.getTime() + (int) increase);
	}
}
