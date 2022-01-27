package es.xdec0de.usleep.api;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;

import es.xdec0de.usleep.USleep;
import es.xdec0de.usleep.utils.SuperVanish;
import es.xdec0de.usleep.utils.USPMessage;
import es.xdec0de.usleep.utils.USPSetting;
import es.xdec0de.usleep.utils.files.USPConfig;
import es.xdec0de.usleep.utils.files.USPMessages;

public class SleepAPI {

	private static int numSleep = 0;
	private static List<UUID> onDelay = new ArrayList<UUID>();

	public static boolean handleSleep(Player player) {
		boolean instant = USPConfig.getBoolean(USPSetting.INSTANT_SLEEP_ENABLED) && player.hasPermission(USPConfig.getString(USPSetting.INSTANT_SLEEP_PERM));
		boolean percent = USPConfig.getBoolean(USPSetting.PERCENT_SLEEP_ENABLED) && player.hasPermission(USPConfig.getString(USPSetting.PERCENT_SLEEP_ENABLED));
		if(USPConfig.getBoolean(USPSetting.PERCENT_SLEEP_PREVENT_SPAM)) { 
			UUID uuid = player.getUniqueId();
			onDelay.add(uuid);
			Bukkit.getScheduler().runTaskTimerAsynchronously(USleep.getInstance(), () -> onDelay.remove(uuid), 0L, USPConfig.getInt(USPSetting.PERCENT_SLEEP_PREVENT_SPAM_COOLDOWN) * 20L);
		}
		if(instant)
			resetDay(player.getWorld(), player);
		if(percent) {
			numSleep++;
			if(getRequiredPlayers() <= numSleep)
				broadcastSleep(false);
			else
				resetDay(player.getWorld(), null);
		} else
			return false;
		return true;
	}

	public static boolean hasSleepCooldown(Player player) {
		return onDelay.contains(player.getUniqueId());
	}

	public static void handleWakeUp() {
		if(numSleep != 0) {
			numSleep--;
			broadcastSleep(true);
		}
	}

	private static void resetDay(World world, Player player) {
		SleepAPI.numSleep = 0;
		world.setTime(0L);
		world.setThundering(false);
		world.setStorm(false);
		if(player != null)
			broadcastInstantNextDay(player);
		else
			broadcastPercentNextDay();
	}

	public static int getRequiredPlayers() {
		return Math.round(getActivePlayers() * USPConfig.getInt(USPSetting.PERCENT_SLEEP_PERCENT) / 100.0F);
	}

	public static int getActivePlayers() {
		List<Player> list = new LinkedList<Player>();
		boolean ignoreAFK = USPConfig.getBoolean(USPSetting.ESSENTIALS_IGNORE_AFK);
		boolean ignoreVanished = USPConfig.getBoolean(USPSetting.IGNORE_VANISHED);
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
						if(SuperVanish.isVanished(p))
							list.add(p);
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
	
	private static void broadcastSleep(boolean leaving) {
		String required = Integer.toString(getRequiredPlayers());
		String current = Integer.toString(numSleep);
		if(USPConfig.getBoolean(USPSetting.ACTIONBAR_ENABLED))
			USPMessages.broadcastActionbar(USPMessage.PERCENT_OK, "%required%", required, "%current%", current);
		else
			USPMessages.broadcast(USPMessage.PERCENT_OK, "%required%", required, "%current%", current);
		if(USPConfig.getBoolean(USPSetting.PERCENT_SLEEP_SOUNDS_ENABLED)) {
			Sound sound = leaving ? Sound.valueOf(USPConfig.getString(USPSetting.PERCENT_SLEEP_LEAVE_SOUND)) : Sound.valueOf(USPConfig.getString(USPSetting.PERCENT_SLEEP_SOUND));
			Bukkit.getOnlinePlayers().forEach(on -> on.playSound(on.getLocation(), sound, 1.0F, 1.0F));
		}
	}
}
