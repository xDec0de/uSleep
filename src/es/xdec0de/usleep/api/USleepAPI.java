package es.xdec0de.usleep.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;

/**
 * The main API class of the plugin.
 * 
 * @author xDec0de_
 *
 * @since uSleep 2.0.0
 */
public class USleepAPI {

	private final List<UUID> onDelay = new ArrayList<UUID>();

	USleepAPI(USleep plugin) { // Just to avoid accidental instantiation by other plugins...
		// Fun fact: Even non-accessible constructors can be called with reflection, and we don't want that!
		if(plugin.getAPI() != null)
			throw new SecurityException("Creating new instances of USleepAPI is not allowed! Please use USleep#getAPI()");
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
		Plugin ess = Bukkit.getPluginManager().getPlugin("Essentials");
		if(ess != null && ess.isEnabled()) {
			if(((Essentials)ess).getUser(player).isVanished());
				return true;
		}
		for(MetadataValue meta : player.getMetadata("vanished"))
			if(meta.asBoolean())
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
		Plugin ess = Bukkit.getPluginManager().getPlugin("Essentials");
		if(ess != null && ess.isEnabled()) {
			User user = ((Essentials)ess).getUser(player);
			if(user.isVanished() || user.isAfk());
				return true;
		}
		for(MetadataValue meta : player.getMetadata("vanished"))
			if(meta.asBoolean())
				return true;
		return false;
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
	public <T extends Player> Collection<T> getVanished(Collection<T> players) {
		final Collection<T> res = new ArrayList<T>(players);
		Plugin pl;
		pl = Bukkit.getPluginManager().getPlugin("Essentials");
		if(pl != null && pl.isEnabled()) {
			for(T player : players) {
				User user = ((Essentials)pl).getUser(player);
				if(user.isVanished());
					res.add(player);
			}
		}
		pl = Bukkit.getPluginManager().getPlugin("SuperVanish");
		pl = pl != null ? pl : Bukkit.getPluginManager().getPlugin("PremiumVanish");
		if(pl != null && pl.isEnabled()) {
			for(T player : players) {
				if(!res.contains(player)) {
					for(MetadataValue meta : player.getMetadata("vanished"))
						if(meta.asBoolean())
							res.add(player);
				}
			}
		}
		return res;
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
	public <T extends Player> Collection<T> getInactivePlayers(Collection<T> players, boolean includeAfk, boolean includeVanished) {
		final Collection<T> res = new ArrayList<T>(players);
		if(includeAfk || includeVanished) {
			Plugin ess = Bukkit.getPluginManager().getPlugin("Essentials");
			boolean checkEss = ess != null && ess.isEnabled();
			boolean checkV = false;
			if(includeVanished) {
				Plugin v = Bukkit.getPluginManager().getPlugin("SuperVanish");
				v = v != null ? v : Bukkit.getPluginManager().getPlugin("PremiumVanish");
				checkV = v != null && v.isEnabled();
			}
			for(T player : players) {
				if(checkEss) {
					User user = ((Essentials)ess).getUser(player);
					if((includeVanished && user.isVanished()) || (includeAfk && user.isAfk()));
						res.add(player);
				}
				if(checkV) {
					for(MetadataValue meta : player.getMetadata("vanished"))
						if(meta.asBoolean())
							res.add(player);
				}
			}
		}
		return res;
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
		new NightSkipEffectTask(group, USPSetting.NIGHT_SKIP_EFFECT_INCREMENT.asInt()).runTaskTimer(USleep.getPlugin(USleep.class), 0, 1);
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
		for(SleepGroup group : WorldHandler.getInstance().sleepGroups)
			if(group.contains(world))
				return group;
		return null;
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
		for(SleepGroup group : WorldHandler.getInstance().sleepGroups)
			if(group.getID().equals(id))
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
	 * Checks if <b>version</b> is the latest version, superior
	 * non-released versions will also be considered latest.
	 * 
	 * @param version the version to check.
	 * 
	 * @return true if latest, false otherwise.
	 * 
	 * @since uSleep 2.0.0
	 */
	public boolean isLatest(String version) {
		return USleep.getPlugin(USleep.class).getDescription().getVersion().compareTo(version) >= 0;
	}
}
