package es.xdec0de.usleep.api;

import java.util.ArrayList;
import java.util.HashMap;
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

import es.xdec0de.usleep.utils.ListUtils;
import es.xdec0de.usleep.utils.files.USPMessages;
import es.xdec0de.usleep.utils.files.USPSetting;
import es.xdec0de.usleep.utils.files.USPWorlds;

public class USleepAPI {

	private List<UUID> onDelay = new ArrayList<UUID>();
	private List<SleepGroup> sleepGroups = new ArrayList<SleepGroup>();

	USleepAPI() {} // Just to avoid instantiation by other plugins...

	boolean setup() {
		HashMap<String, List<String>> errors = new HashMap<String, List<String>>();
		List<World> worlds = new ArrayList<World>(Bukkit.getWorlds());
		for(String id : USPWorlds.getGroupIdentifiers()) {
			SleepGroup group = new SleepGroup(id);
			List<String> groupErrors = group.build();
			if(!groupErrors.isEmpty())
				errors.put(id, groupErrors);
			group.worlds.forEach(world -> worlds.remove(world));
			sleepGroups.add(group);
		}
		if(!worlds.isEmpty())
			sleepGroups.add(new SleepGroup(worlds));
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
		return getSleepGroup(player.getWorld()).handleSleep(player);
	}

	public boolean hasSleepCooldown(Player player) {
		return onDelay.contains(player.getUniqueId());
	}

	public boolean addToSleepCooldown(UUID player, int seconds) {
		if(seconds > 0 && !onDelay.contains(player)) {
			onDelay.add(player);
			Bukkit.getScheduler().runTaskTimerAsynchronously(USleep.getPlugin(USleep.class), () -> onDelay.remove(player), 0L, seconds * 20L);
			return true;
		}
		return false;
	}

	public void handleWakeUp(Player player) {
		getSleepGroup(player.getWorld()).handleWakeUp();
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

	public void doNightSkipEffect(SleepGroup group) {
		new NightSkipEffectTask(group, USPSetting.NIGHT_SKIP_EFFECT_INCREMENT.asInt()).runTaskTimer(USleep.getPlugin(USleep.class), 0, 1);
	}

	/**
	 * 
	 * Sleep groups
	 * 
	 */

	public SleepGroup getSleepGroup(World world) {
		for(SleepGroup group : sleepGroups)
			if(group.contains(world))
				return group;
		return null;
	}
}
