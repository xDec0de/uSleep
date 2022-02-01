package es.xdec0de.usleep.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;

import es.xdec0de.usleep.api.events.NightSkipEvent;
import es.xdec0de.usleep.utils.ListUtils;
import es.xdec0de.usleep.utils.NotificationHandler;
import es.xdec0de.usleep.utils.files.USPMessage;
import es.xdec0de.usleep.utils.files.USPMessages;
import es.xdec0de.usleep.utils.files.USPSetting;
import es.xdec0de.usleep.utils.files.USPWorlds;

public class USleepAPI {

	private int numSleep = 0;
	private List<UUID> onDelay = new ArrayList<UUID>();
	private List<SleepGroup> sleepGroups = new ArrayList<SleepGroup>();

	USleepAPI() {} // Just to avoid instantiation by other plugins...

	boolean setup() {
		HashMap<String, List<String>> errors = new HashMap<String, List<String>>();
		for(String id : USPWorlds.getGroupIdentifiers()) {
			SleepGroup group = new SleepGroup(id);
			List<String> groupErrors = group.build();
			if(!groupErrors.isEmpty())
				errors.put(id, groupErrors);
			sleepGroups.add(group);
		}
		if(!errors.isEmpty()) {
			USPMessages.log(" ");
			USPMessages.logCol("&cSleep group errors detected at &4worlds.yml&8:");
			for(String id : errors.keySet())
				USPMessages.logCol("  &4- &6"+id+" &chas non-existing worlds&8: &e"+ListUtils.join(errors.get(id).toArray(), "&8, &e")+"&c.");
			return false;
		}
		return true;
	}

	public boolean handleSleep(Player player) {
		int cooldown = USPSetting.PERCENT_SLEEP_COOLDOWN.asInt();
		if(cooldown > 0) { 
			UUID uuid = player.getUniqueId();
			onDelay.add(uuid);
			Bukkit.getScheduler().runTaskTimerAsynchronously(USleep.getPlugin(USleep.class), () -> onDelay.remove(uuid), 0L, cooldown * 20L);
		}
		if(USPSetting.INSTANT_SLEEP_ENABLED.asBoolean() && player.hasPermission(USPSetting.PERM_INSTANT_SLEEP.asString())) // instant
			resetDay(player.getWorld(), player);
		else if(USPSetting.PERCENT_SLEEP_ENABLED.asBoolean() && player.hasPermission(USPSetting.PERM_PERCENT_SLEEP.asString())) { // percent
			numSleep++;
			if(getRequiredPlayers() < numSleep) {
				USPMessage.PERCENT_OK.broadcast("%required%", Integer.toString(getRequiredPlayers()), "%current%", Integer.toString(numSleep));
				NotificationHandler.broadcastSound(USPSetting.SOUND_SLEEP_OK);
			} else
				resetDay(player.getWorld(), null);
		} else
			return false;
		return true;
	}

	public boolean hasSleepCooldown(Player player) {
		return onDelay.contains(player.getUniqueId());
	}

	public void handleWakeUp() {
		if(numSleep > 0) {
			numSleep--;
			USPMessage.PERCENT_OK.broadcast("%required%", Integer.toString(getRequiredPlayers()), "%current%", Integer.toString(numSleep));
			NotificationHandler.broadcastSound(USPSetting.SOUND_SLEEP_LEAVE);
		}
	}

	private void resetDay(World world, Player player) {
		SleepMode mode = player != null ? SleepMode.INSTANT : SleepMode.PERCENT;
		NightSkipEvent nse = new NightSkipEvent(world, mode);
		Bukkit.getPluginManager().callEvent(nse);
		if(!nse.isCancelled()) {
			numSleep = 0;
			if(USPSetting.NIGHT_SKIP_EFFECT_ENABLED.asBoolean())
				doNightSkipEffect(world);
			else {
				world.setTime(0L);
				world.setThundering(false);
				world.setStorm(false);
			}
			if(mode.equals(SleepMode.INSTANT)) {
				USPMessage.INSTANT_OK.broadcast("%player%", player.getName());
				NotificationHandler.broadcastSound(USPSetting.SOUND_NEXTDAY_PERCENT);
			} else {
				USPMessage.PERCENT_NEXT_DAY.broadcast();
				NotificationHandler.broadcastSound(USPSetting.SOUND_NEXTDAY_INSTANT);
			}
		}
	}

	public int getRequiredPlayers() {
		return Math.round(getActivePlayers() * USPSetting.PERCENT_SLEEP_PERCENT.asInt() / 100.0F);
	}

	public int getActivePlayers() {
		List<Player> list = new LinkedList<Player>();
		boolean ignoreAFK = USPSetting.PERCENT_SLEEP_IGNORE_AFK.asBoolean();
		boolean ignoreVanished = USPSetting.PERCENT_SLEEP_IGNORE_VANISHED.asBoolean();
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

	public boolean isVanished(Player player) {
		for(MetadataValue meta : player.getMetadata("vanished"))
			if(meta.asBoolean())
				return true;
		return false;
	}

	public void doNightSkipEffect(World world) {
		Environment env = world.getEnvironment();
		if(env.equals(Environment.NORMAL) || env.equals(Environment.CUSTOM))
			new NightSkipEffectTask(world, USPSetting.NIGHT_SKIP_EFFECT_INCREMENT.asInt()).runTaskTimer(USleep.getPlugin(USleep.class), 0, 1);
	}
}
