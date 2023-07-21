package es.xdec0de.usleep.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import com.google.common.base.Enums;

import es.xdec0de.usleep.api.events.NightSkipEvent;
import es.xdec0de.usleep.api.events.SleepHandleEvent;
import me.xdec0de.mcutils.files.PluginFile;
import me.xdec0de.mcutils.general.Replacer;

/**
 * A class representing a group of
 * worlds sharing their sleep count
 * and other sleep-related attributes.
 * 
 * @author xDec0de_
 *
 * @since v2.0.0
 */
public class SleepGroup {

	private final USleepAPI api;

	private final List<World> worlds = new ArrayList<World>();
	private final String id;
	private final int percent;

	private int sleeping = 0;
	boolean isNightSkipping = false;

	SleepGroup(USleepAPI api, String id, List<World> worlds) {
		this.id = id;
		this.worlds = worlds;
		this.api = api;
		// TODO Fix later, this needs a recode for the new worlds.yml file anyways.
		this.percent = id.equals(api.getDefaultSleepGroupID()) ? this.percent = USPSetting.PERCENT_SLEEP_PERCENT.asInt() : USPWorlds.getPercentRequired(id);
	}

	/**
	 * Handles a <b>player</b> sleeping, essentially augmenting
	 * {@link #getPlayersSleeping()} by one on percent sleep
	 * and directly skipping the night on instant sleep.
	 * This <b>DOES NOT</b> force <b>player</b> to enter a bed,
	 * for that you can use {@link HumanEntity#sleep(org.bukkit.Location, boolean)}
	 * which is recommended over this as this method is only
	 * intended for sleep count handling while canceling uSleep's
	 * events to override it's default behavior.
	 * 
	 * @param player the player sleeping, see {@link HumanEntity#isSleeping()},
	 * uSleep doesn't check if the <b>player</b> is already sleeping as
	 * this shouldn't happen in any case with the default plugin behavior.
	 * @param forced whether to bypass permission check or not, this is <b>NOT</b>
	 * able to force sleep if both {@link USPSetting#PERCENT_SLEEP_ENABLED} and
	 * {@link USPSetting#INSTANT_SLEEP_ENABLED} return false.
	 * 
	 * @return true if <b>player</b> is able to sleep, false otherwise.
	 * being "able" to sleep means that <b>player</b> actually has
	 * permissions to sleep, as if percent and instant sleep are disabled
	 * or <b>player</b> lacks permission on both, it won't be able to sleep
	 * and thus, call sleep handling on any {@link SleepGroup}.
	 * 
	 * @since uSleep 2.0.0
	 * 
	 * @see SleepHandleEvent
	 */
	public boolean handleSleep(Player player, boolean forced) {
		final PluginFile cfg = api.getPlugin().getConfig();
		SleepMode mode = null;
		if (cfg.getBoolean("Features.InstantSleep.Enabled") && (forced || player.hasPermission(cfg.getString("Permissions.Sleep.Instant"))))
			mode = SleepMode.INSTANT;
		else if (cfg.getBoolean("Features.PercentSleep.Enabled") && (forced || player.hasPermission(cfg.getString("Permissions.Sleep.Percent"))))
			mode = SleepMode.PERCENT;
		if (mode == null)
			return false; // Either both modes are off or the player doesn't have access to neither of them.
		SleepHandleEvent she = new SleepHandleEvent(player, this, mode);
		Bukkit.getPluginManager().callEvent(she);
		if (she.isCancelled())
			return false; // Sleep was cancelled by other plugin, we just return false here.
		List<Player> players = getPlayers();
		if (mode == SleepMode.INSTANT)
			return resetTime(player, mode);
		// Mode must be SleepMode.PERCENT here, no need to check again.
		api.addToSleepCooldown(player.getUniqueId(), cfg.getInt("Features.PercentSleep.Cooldown", 5));
		sleeping++;
		int required = getRequiredPlayers();
		if (required > sleeping)
			broadcast(players, "PercentSleep.OK", "Sounds.Sleep.Ok", "%required%", required, "%current%", sleeping);
		else
			resetTime(player, mode);
		return true; // Either instant or percent sleep were handled at this point.
	}

	/**
	 * Handles a player waking up, essentially decreasing
	 * {@link #getPlayersSleeping()} by one if it's > 0.
	 * This <b>DOES NOT</b> force any player to leave a bed,
	 * for that you can use {@link HumanEntity#wakeup(boolean)}
	 * which is recommended over this as this method is only
	 * intended for sleep count handling while canceling uSleep's
	 * events to override its default behavior.
	 * 
	 * @since uSleep 2.0.0
	 */
	public void handleWakeUp() {
		if (sleeping <= 0)
			return;
		sleeping--;
		broadcast(getPlayers(), "PercentSleep.Leave", "Sounds.Sleep.Leave", "%required%", getRequiredPlayers(), "%current%", sleeping);
	}

	private boolean resetTime(Player player, SleepMode mode) {
		NightSkipEvent nse = new NightSkipEvent(this, mode, player, api.getPlugin().getConfig().getBoolean("Features.NightSkipEffect.Enabled"));
		Bukkit.getPluginManager().callEvent(nse);
		if (nse.isCancelled())
			return false; // Event was cancelled by another plugin.
		sleeping = 0;
		List<Player> players = getPlayers();
		if (nse.doesSkipEffect())
			api.doNightSkipEffect(this);
		else {
			for(World world : worlds) {
				world.setTime(0L);
				world.setThundering(false);
				world.setStorm(false);
			}
		}
		if (mode.equals(SleepMode.INSTANT))
			broadcast(players, "InstantSleep.OK", "Sounds.NextDay.Instant");
		else
			broadcast(players, "PercentSleep.OK", "Sounds.NextDay.Percent");
		return true;
	}

	/**
	 * Gets the required amount of players to skip a night.
	 * 
	 * @return the required amount of players to skip a night.
	 * 
	 * @since v2.0.0
	 */
	public int getRequiredPlayers() {
		return Math.round((getPlayers().size() - getInactivePlayers().size()) * percent / 100.0F);
	}

	/**
	 * Gets the list of afk or vanished players in {@link #getPlayers()}.
	 * Depending on configuration uSleep can ignore either group
	 * individually, so checking their respective setting is recommended.
	 *
	 * @return a new list of players containing the inactive players.
	 * 
	 * @see USleepAPI#getInactivePlayers(Collection, boolean, boolean)
	 * @see USPSetting#PERCENT_SLEEP_IGNORE_AFK
	 * @see USPSetting#PERCENT_SLEEP_IGNORE_VANISHED
	 * 
	 * @since v2.0.0
	 */
	public Collection<Player> getInactivePlayers() {
		final PluginFile cfg = api.getPlugin().getConfig();
		final String ignoreAfkPath = "Features.PercentSleep.Ignored.AFK";
		final String ignoreVanishPath = "Features.PercentSleep.Ignored.Vanished";
		return api.getInactivePlayers(getPlayers(), !cfg.getBoolean(ignoreAfkPath), !cfg.getBoolean(ignoreVanishPath));
	}

	/**
	 * Gets the list of players currently present on this group.
	 * 
	 * @return the list of players on this group.
	 * 
	 * @since v2.0.0
	 */
	public List<Player> getPlayers() {
		List<Player> players = new ArrayList<Player>();
		for (World world : worlds)
			players.addAll(world.getPlayers());
		return players;
	}

	/**
	 * Gets the ID of the group specified in <b>worlds.yml</b>
	 * 
	 * @return the ID of the group.
	 * 
	 * @since v2.0.0
	 */
	public String getID() {
		return id;
	}

	/**
	 * Gets the list of worlds in this group.
	 * 
	 * @return the list of worlds in this group.
	 * 
	 * @since v2.0.0
	 */
	public List<World> getWorlds() {
		return worlds;
	}

	/**
	 * Checks if a world is present on this group.
	 * 
	 * @param world the world to check.
	 * 
	 * @return true if the world is on the group, false otherwise.
	 * 
	 * @since v2.0.0
	 */
	public boolean contains(World world) {
		return worlds.contains(world);
	}

	/**
	 * Gets the amount of players sleeping in this group.
	 * 
	 * @return the amount of players sleeping in this group.
	 * 
	 * @since v2.0.0
	 */
	public int getPlayersSleeping() {
		return sleeping;
	}

	/**
	 * Whether the group is skipping the night or not.
	 * 
	 * @return whether the group is skipping the night or not.
	 * 
	 * @since v2.0.0
	 */
	public boolean isNightSkipping() {
		return isNightSkipping;
	}

	private void broadcast(List<Player> players, String msgPath, String soundPath, Object... replacements) {
		Sound sound = Enums.getIfPresent(Sound.class, api.getPlugin().getConfig().getString(soundPath, "")).orNull();
		String msg = api.getPlugin().getMessages().getString(msgPath);
		if (sound == null && msg == null)
			return;
		if (replacements != null && replacements.length != 0)
			msg = new Replacer(replacements).replaceAt(msg);
		for (Player target : players) {
			if (sound != null)
				target.playSound(target.getLocation(), sound, 1.0F, 1.0F);
			if (msg != null && !msg.isBlank())
				target.sendMessage(msg);
		}
	}
}
