package me.xdec0de.usleep.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;

import me.xdec0de.usleep.USleep;
import me.xdec0de.usleep.api.vanish.EssentialsVanishHook;
import me.xdec0de.usleep.api.vanish.MetadataVanishHook;
import me.xdec0de.usleep.api.vanish.VanishHook;
import net.codersky.mcutils.files.yaml.PluginFile;
import net.codersky.mcutils.java.MCLists;

/**
 * The main API class of the plugin.
 * 
 * @author xDec0de_
 *
 * @since uSleep 2.0.0
 */
public class USleepAPI {

	private final USleep uSleep;
	private final List<UUID> onDelay = new ArrayList<UUID>();
	final ArrayList<SleepGroup> sleepGroups = new ArrayList<SleepGroup>();

	private List<VanishHook> vanishHooks = new ArrayList<>();

	public USleepAPI(USleep plugin) {
		if (plugin.getAPI() != null)
			throw new SecurityException("Creating new instances of USleepAPI is not allowed! Please use USleep#getAPI()");
		this.uSleep = plugin;
	}

	void addHooks() {
		// Vanish
		vanishHooks.add(new EssentialsVanishHook());
		vanishHooks.add(new MetadataVanishHook());
		// AFK
	}

	/**
	 * Provides access back to {@link USleep}
	 * 
	 * @return an instance of {@link USleep}.
	 * 
	 * @since uSleep 2.0.0
	 */
	@Nonnull
	public USleep getPlugin() {
		return this.uSleep;
	}

	public USleepAPI reloadSleepGroups() {
		sleepGroups.clear();
		final PluginFile worlds = uSleep.getWorlds();
		final int defPercent = worlds.getInt("defaultPercent", 50);
		SleepGroup defGroup = new SleepGroup(this, getDefaultSleepGroupID(), defPercent).setWorlds(Bukkit.getWorlds());
		for (String groupId : worlds.getConfigurationSection("groups").getKeys(false)) {
			final SleepGroup group = new SleepGroup(this, groupId, worlds.getInt("groups." + groupId + ".percent", defPercent));
			for (String worldName : worlds.getStringList("groups." + groupId + ".worlds")) {
				final World world = Bukkit.getWorld(worldName);
				if (world != null) {
					defGroup.removeWorld(world);
					group.addWorld(world);
				} else
					uSleep.logCol("&8[&4USleep&8] &cIgnoring non-existing world \"&6"+ worldName + "&c\" on group &e" + group);
			}
			sleepGroups.add(group);
		}
		sleepGroups.add(defGroup);
		return this;
	}

	/**
	 * Checks if <b>player</b> is on the sleep cooldown list.
	 * 
	 * @param player the player to check.
	 * 
	 * @return true if the player is on the list, false otherwise.
	 * 
	 * @since uSleep 2.0.0
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
	 * 
	 * @return true if the player has been added to the sleep cooldown list, false otherwise
	 * 
	 * @since uSleep 2.0.0
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
	 * @param forced whether to force sleeping or not, this is <b>NOT</b> able
	 * to force sleep if both {@link USPSetting#PERCENT_SLEEP_ENABLED} and
	 * {@link USPSetting#INSTANT_SLEEP_ENABLED} return false.
	 * 
	 * @return true if <b>player</b> is able to sleep, false otherwise.
	 * being "able" to sleep means that <b>player</b> actually has
	 * permissions to sleep, as if percent and instant sleep are disabled
	 * or <b>player</b> lacks permission on both, it won't be able to sleep
	 * and thus, call sleep handling on any world.
	 * 
	 * @since uSleep 2.0.0
	 */
	public boolean handleSleep(Player player, boolean forced) {
		return getSleepGroup(player.getWorld()).handleSleep(player, forced);
	}

	/**
	 * Handles the waking up of a player, this
	 * <b>DOES NOT</b> force the player to leave the bed,
	 * it only calls {@link SleepGroup#handleWakeUp()} on
	 * the {@link SleepGroup} the <b>player</b> is in.
	 * 
	 * @param player the player waking up.
	 * 
	 * @since uSleep 2.0.0
	 */
	public void handleWakeUp(Player player) {
		getSleepGroup(player.getWorld()).handleWakeUp();
	}

	/**
	 * Checks if <b>player</b> is vanished.
	 * 
	 * @param player the player to check.
	 * 
	 * @return true if <b>player</b> is vanished, false otherwise.
	 * 
	 * @since uSleep 2.0.0
	 */
	public boolean isVanished(Player player) {
		for (VanishHook hook : vanishHooks)
			if (hook.isVanished(player))
				return true;
		return false;
	}

	/**
	 * Checks if <b>player</b> is afk.
	 * 
	 * @param player the player to check.
	 * 
	 * @return true if <b>player</b> is afk, false otherwise.
	 * 
	 * @since uSleep 2.0.0
	 */
	public boolean isAfk(Player player) {
		Plugin ess = Bukkit.getPluginManager().getPlugin("Essentials");
			if(ess != null && ess.isEnabled())
				return (((Essentials)Bukkit.getServer().getPluginManager().getPlugin("Essentials")).getUser(player).isVanished());
		return false;
	}

	/**
	 * Checks if <b>player</b> is vanished or afk.
	 * 
	 * @param player the player to check.
	 * 
	 * @return true if <b>player</b> is vanished or afk, false otherwise.
	 * 
	 * @since uSleep 2.0.0
	 */
	public boolean isInactive(Player player) {
		return isVanished(player) || isAfk(player);
	}

	/**
	 * Gets the list of afk players in <b>players</b>.
	 * 
	 * @param players the list of players to check.
	 * 
	 * @return a new list of players containing the afk players.
	 * 
	 * @since uSleep 2.0.0
	 */
	public <T extends Player> Collection<T> getAfk(Collection<T> players) {
		final Collection<T> res = new ArrayList<T>(players);
		Plugin pl = Bukkit.getPluginManager().getPlugin("Essentials");
		if(pl != null && pl.isEnabled()) {
			for(T player : players) {
				User user = ((Essentials)pl).getUser(player);
				if(user.isAfk());
					res.add(player);
			}
		}
		return res;
	}

	/**
	 * Gets the list of vanished players in <b>players</b>.
	 * 
	 * @param players the list of players to check.
	 * 
	 * @return a new list of players containing the vanished players.
	 * 
	 * @since uSleep 2.0.0
	 */
	public List<Player> getVanished(List<Player> players) {
		final List<Player> remaining = players;
		for (VanishHook hook : vanishHooks)
			remaining.removeAll(hook.getVanished(remaining));
		return remaining;
	}

	/**
	 * Gets the list of afk or vanished players in <b>players</b>.
	 * This method is designed for efficiency to use instead of calling
	 * both {@link #getVanished(Collection)} and {@link #getAfk(Collection)},
	 * as this method will only loop once through the players, it's also useful
	 * for variable afk and vanish including.
	 * 
	 * @param players the list of players to check.
	 * @param includeAfk whether to include afk players to the list or not.
	 * @param includeVanished whether to include vanished players to the list or not.
	 *
	 * @return a new list of players containing the afk or vanished players.
	 * 
	 * @since uSleep 2.0.0
	 * 
	 * @see {@link USPSetting#PERCENT_SLEEP_IGNORE_AFK}
	 * @see {@link USPSetting#PERCENT_SLEEP_IGNORE_VANISHED}
	 */
	public List<Player> getInactivePlayers(List<Player> players, boolean includeAfk, boolean includeVanished) {
		final List<Player> remaining = new ArrayList<>();
		if (includeVanished)
			remaining.addAll(getVanished(players));
		if (includeAfk)
			remaining.addAll(getAfk(players));
		return MCLists.removeDuplicates(remaining);
	}

	/**
	 * Starts a {@link NightSkipEffectTask} on the
	 * specified {@link SleepGroup}.
	 * 
	 * @param group the group affected by the night skip effect.
	 * 
	 * @since uSleep 2.0.0
	 */
	public void doNightSkipEffect(SleepGroup group) {
		new NightSkipEffectTask(group, uSleep.getConfig().getInt("Features.NightSkipEffect.Increment")).runTaskTimer(uSleep, 0, 1);
	}

	/**
	 * 
	 * Sleep groups
	 * 
	 */

	/**
	 * Gets the {@link SleepGroup} a world is in.
	 * Null shouldn't be returned as every world has
	 * to be at least on the default sleep group.
	 * 
	 * @param world the world to check.
	 * 
	 * @return the sleep group a world is in.
	 * 
	 * @since uSleep 2.0.0
	 */
	public SleepGroup getSleepGroup(World world) {
		for (SleepGroup group : sleepGroups)
			if (group.contains(world))
				return group;
		return null;
	}

	/**
	 * Shortcut to get the default {@link SleepGroup},
	 * insead of using {@link #getSleepGroup(String)} with
	 * {@link #getDefaultSleepGroupID()}, which may result
	 * in a long line of code.
	 * 
	 * @return The default {@link SleepGroup}
	 * 
	 * @since uSleep 2.0.0
	 */
	public SleepGroup getDefaultSleepGroup() {
		return getSleepGroup(getDefaultSleepGroupID());
	}

	/**
	 * Gets a {@link SleepGroup} by name.
	 * 
	 * @param id the SleepGroup name to get.
	 * 
	 * @return the SleepGroup with the specified <b>id</b>. Can be null.
	 * 
	 * @since uSleep 2.0.0
	 */
	public SleepGroup getSleepGroup(String id) {
		for (SleepGroup group : sleepGroups)
			if (group.getID().equals(id))
				return group;
		return null;
	}

	/**
	 * Gets the default SleepGroup ID, which is "__usleep_def_sleep_group__".
	 * 
	 * @return "__usleep_def_sleep_group__"
	 * 
	 * @since uSleep 2.0.0
	 */
	public String getDefaultSleepGroupID() {
		return "__usleep_def_sleep_group__";
	}

	/**
	 * Checks if <b>version</b> is a newer version than the
	 * currently installed version of uSleep.
	 * 
	 * @param version the version to check.
	 * 
	 * @return {@code true} if <b>version</b> is a newer version than the
	 * currently installed version of uSleep, {@code false} otherwise.
	 * 
	 * @throws NullPointerException if <b>version</b> is {@code null}.
	 * 
	 * @since uSleep 2.0.0
	 */
	public boolean isNewerVersion(@Nonnull String version) {
		return uSleep.getDescription().getVersion().compareTo(version) >= 0;
	}
}
