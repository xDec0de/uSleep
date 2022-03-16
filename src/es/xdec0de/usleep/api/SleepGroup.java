package es.xdec0de.usleep.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import es.xdec0de.usleep.api.events.NightSkipEvent;
import es.xdec0de.usleep.api.events.SleepHandleEvent;
import es.xdec0de.usleep.utils.SoundHandler;
import es.xdec0de.usleep.utils.files.USPMessage;
import es.xdec0de.usleep.utils.files.USPSetting;
import es.xdec0de.usleep.utils.files.USPWorlds;

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

	List<World> worlds = new ArrayList<World>();
	private final String id;
	private int sleeping = 0, percent;
	boolean isNightSkipping = false;

	SleepGroup(String id, List<World> worlds) {
		this.id = id;
		this.worlds = worlds;
		if(id.equals("__usleep_def_sleep_group__"))
			this.percent = USPSetting.PERCENT_SLEEP_PERCENT.asInt();
		else
			this.percent = USPWorlds.getPercentRequired(id);
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
	 * @return true if the player was allowed to sleep, false otherwise.
	 * 
	 * @since v2.0.0
	 * 
	 * @see SleepHandleEvent
	 * 
	 */
	public boolean handleSleep(Player player, boolean forced) {
		SleepMode mode = null;
		if(USPSetting.INSTANT_SLEEP_ENABLED.asBoolean() && (forced || player.hasPermission(USPSetting.PERM_INSTANT_SLEEP.asString())))
			mode = SleepMode.INSTANT;
		else if(USPSetting.PERCENT_SLEEP_ENABLED.asBoolean() && (forced || player.hasPermission(USPSetting.PERM_PERCENT_SLEEP.asString())))
			mode = SleepMode.PERCENT;
		SleepHandleEvent she = new SleepHandleEvent(player, this, mode);
		Bukkit.getPluginManager().callEvent(she);
		if(!she.isCancelled()) {
			List<Player> players = getPlayers();
			USleepAPI.getInstance().addToSleepCooldown(player.getUniqueId(), USPSetting.PERCENT_SLEEP_COOLDOWN.asInt());
			if(mode.equals(SleepMode.INSTANT))
				resetTime(player, mode);
			else if(mode.equals(SleepMode.PERCENT)) {
				sleeping++;
				int required = getRequiredPlayers();
				if(required > sleeping) {
					USPMessage.PERCENT_OK.broadcast(players, "%required%", Integer.toString(required), "%current%", Integer.toString(sleeping));
					SoundHandler.broadcastSound(players, USPSetting.SOUND_SLEEP_OK);
				} else
					resetTime(player, mode);
			} else
				return false;
			return true;
		}
		return false;
	}

	/**
	 * Handles a player waking up, essentially decreasing
	 * {@link #getPlayersSleeping()} by one if it's > 0.
	 * This <b>DOES NOT</b> force any player to leave a bed,
	 * for that you can use {@link HumanEntity#wakeup(boolean)}
	 * which is recommended over this as this method is only
	 * intended for sleep count handling while canceling uSleep's
	 * events to override it's default behavior.
	 * 
	 * @since v2.0.0
	 */
	public void handleWakeUp() {
		if(sleeping > 0) {
			sleeping--;
			List<Player> players = getPlayers();
			USPMessage.PERCENT_OK.broadcast(players, "%required%", Integer.toString(getRequiredPlayers()), "%current%", Integer.toString(sleeping));
			SoundHandler.broadcastSound(players, USPSetting.SOUND_SLEEP_LEAVE);
		}
	}

	private void resetTime(Player player, SleepMode mode) {
		NightSkipEvent nse = new NightSkipEvent(this, mode, player, USPSetting.NIGHT_SKIP_EFFECT_ENABLED.asBoolean());
		Bukkit.getPluginManager().callEvent(nse);
		if(!nse.isCancelled()) {
			sleeping = 0;
			List<Player> players = getPlayers();
			if(nse.doesSkipEffect())
				USleepAPI.getInstance().doNightSkipEffect(this);
			else {
				for(World world : worlds) {
					world.setTime(0L);
					world.setThundering(false);
					world.setStorm(false);
				}
			}
			if(mode.equals(SleepMode.INSTANT)) {
				USPMessage.INSTANT_OK.broadcast(players, "%player%", player.getName());
				SoundHandler.broadcastSound(players, USPSetting.SOUND_NEXTDAY_PERCENT);
			} else {
				USPMessage.PERCENT_NEXT_DAY.broadcast(players);
				SoundHandler.broadcastSound(players, USPSetting.SOUND_NEXTDAY_INSTANT);
			}
		}
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
		return USleepAPI.getInstance().getInactivePlayers(getPlayers(), USPSetting.PERCENT_SLEEP_IGNORE_AFK.asBoolean(), USPSetting.PERCENT_SLEEP_IGNORE_VANISHED.asBoolean());
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
		for(World world : worlds)
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
}
