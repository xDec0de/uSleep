package es.xdec0de.usleep.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

import es.xdec0de.usleep.utils.ListUtils;
import es.xdec0de.usleep.utils.files.USPMessages;
import es.xdec0de.usleep.utils.files.USPSetting;
import es.xdec0de.usleep.utils.files.USPWorlds;

/**
 * The main API class of the plugin.
 * 
 * @author xDec0de_
 *
 * @since v2.0.0
 */
public class USleepAPI {

	private static USleepAPI instance;

	private List<UUID> onDelay = new ArrayList<UUID>();
	private List<SleepGroup> sleepGroups = new ArrayList<SleepGroup>();

	USleepAPI() { // Just to avoid accidental instantiation by other plugins...
		// Fun fact: Even non-accessible constructors can be called with reflection, and we don't want that!
		if(instance != null)
			throw new SecurityException("Creating new instances of USleepAPI is not allowed! Please use USleepAPI#getInstance()");
	}

	/**
	 * Gets the current instance of the API.
	 * 
	 * @return the instance of the API.
	 * 
	 * @since v2.0.0
	 */
	public static USleepAPI getInstance() {
		return instance != null ? instance : (instance = new USleepAPI());
	}

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

	/**
	 * Checks if <b>player</b> is on the sleep cooldown list.
	 * 
	 * @param player the player to check.
	 * @return true if the player is on the list, false otherwise.
	 * 
	 * @since v2.0.0
	 */
	public boolean hasSleepCooldown(Player player) {
		return onDelay.contains(player.getUniqueId());
	}

	/**
	 * Adds <b>player</b> to the sleep cooldown list for
	 * <b>seconds</b> amount of seconds, meaning that
	 * it won't be able to sleep for the specified
	 * amount of seconds. If the player is already
	 * present on the list or <b>seconds</b> is minor
	 * or equal to zero, the player won't be added to the list.
	 * 
	 * @param player the player being added to the cooldown list.
	 * @param seconds the amount of seconds to add the <b>player</b>.
	 * @return true if the player has been added to the sleep cooldown list, false otherwise
	 * 
	 * @since v2.0.0
	 */
	public boolean addToSleepCooldown(UUID player, int seconds) {
		if(seconds > 0 && !onDelay.contains(player)) {
			onDelay.add(player);
			Bukkit.getScheduler().runTaskTimerAsynchronously(USleep.getPlugin(USleep.class), () -> onDelay.remove(player), 0L, seconds * 20L);
			return true;
		}
		return false;
	}

	/**
	 * Handles the sleeping of <b>player</b>, this
	 * <b>DOES NOT</b> force the player to enter a bed,
	 * it only calls {@link SleepGroup#handleSleep(Player)} on
	 * the {@link SleepGroup} the <b>player</b> is in.
	 * 
	 * @param player the player sleeping
	 * @return true if <b>player<b> is able to sleep, false otherwise.
	 * being "able" to sleep means that <b>player</b> actually has
	 * permissions to sleep, as if percent and instant sleep are disabled
	 * or <b>player</b> lacks permission on both, it won't be able to sleep
	 * and thus, call sleep handling on any world.
	 * 
	 * @since v2.0.0
	 */
	public boolean handleSleep(Player player) {
		return getSleepGroup(player.getWorld()).handleSleep(player);
	}

	/**
	 * Handles the waking up of a player, this
	 * <b>DOES NOT</b> force the player to leave the bed,
	 * it only calls {@link SleepGroup#handleWakeUp()} on
	 * the {@link SleepGroup} the <b>player</b> is in.
	 * 
	 * @param player the player waking up.
	 * 
	 * @since v2.0.0
	 */
	public void handleWakeUp(Player player) {
		getSleepGroup(player.getWorld()).handleWakeUp();
	}

	/**
	 * Checks if <b>player</b> is vanished.
	 * 
	 * @param player the player to check.
	 * @return true if <b>player</b> is vanished, false otherwise.
	 * 
	 * @since v2.0.0
	 */
	public boolean isVanished(Player player) {
		for(MetadataValue meta : player.getMetadata("vanished"))
			if(meta.asBoolean())
				return true;
		return false;
	}

	/**
	 * Starts a {@link NightSkipEffectTask} on the
	 * specified {@link SleepGroup}.
	 * 
	 * @param group the group affected by the night skip effect.
	 * 
	 * @since v2.0.0
	 */
	public void doNightSkipEffect(SleepGroup group) {
		new NightSkipEffectTask(group, USPSetting.NIGHT_SKIP_EFFECT_INCREMENT.asInt()).runTaskTimer(USleep.getPlugin(USleep.class), 0, 1);
	}

	/**
	 * 
	 * Sleep groups
	 * 
	 */

	/**
	 * Gets the {@link SleepGroup} a world is in.
	 * Null shouln't be returned as every world has
	 * to be at least on the default sleep group.
	 * 
	 * @param world the world to check.
	 * @return the sleep group a world is in.
	 * 
	 * @since v2.0.0
	 */
	public SleepGroup getSleepGroup(World world) {
		for(SleepGroup group : sleepGroups)
			if(group.contains(world))
				return group;
		return null;
	}

	/**
	 * Checks if <b>version</b> is the latest version, superior
	 * non-released versions will also be considered latest.
	 * 
	 * @param version the version to check.
	 * @return true if latest, false otherwise.
	 * 
	 * @since v2.0.0
	 */
	public boolean isLatest(String version) {
		return USleep.getPlugin(USleep.class).getDescription().getVersion().compareTo(version) >= 0;
	}
}
