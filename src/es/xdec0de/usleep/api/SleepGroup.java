package es.xdec0de.usleep.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import es.xdec0de.usleep.api.events.NightSkipEvent;
import es.xdec0de.usleep.api.events.SleepHandleEvent;
import es.xdec0de.usleep.utils.SoundHandler;
import es.xdec0de.usleep.utils.files.USPMessage;
import es.xdec0de.usleep.utils.files.USPSetting;
import es.xdec0de.usleep.utils.files.USPWorlds;

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

	public void handleWakeUp() {
		if(sleeping > 0) {
			sleeping--;
			List<Player> players = getPlayers();
			USPMessage.PERCENT_OK.broadcast(players, "%required%", Integer.toString(getRequiredPlayers()), "%current%", Integer.toString(sleeping));
			SoundHandler.broadcastSound(players, USPSetting.SOUND_SLEEP_LEAVE);
		}
	}

	private void resetTime(Player player, SleepMode mode) {
		NightSkipEvent nse = new NightSkipEvent(this, mode, player);
		Bukkit.getPluginManager().callEvent(nse);
		if(!nse.isCancelled()) {
			sleeping = 0;
			boolean skipEffect = USPSetting.NIGHT_SKIP_EFFECT_ENABLED.asBoolean();
			List<Player> players = getPlayers();
			if(skipEffect)
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

	public int getRequiredPlayers() {
		return Math.round((getPlayers().size() - getInactivePlayers().size()) * percent / 100.0F);
	}

	public Collection<Player> getInactivePlayers() {
		return USleepAPI.getInstance().getInactivePlayers(getPlayers(), USPSetting.PERCENT_SLEEP_IGNORE_AFK.asBoolean(), USPSetting.PERCENT_SLEEP_IGNORE_VANISHED.asBoolean());
	}

	public List<Player> getPlayers() {
		List<Player> players = new ArrayList<Player>();
		for(World world : worlds)
			players.addAll(world.getPlayers());
		return players;
	}

	public String getID() {
		return id;
	}

	public List<World> getWorlds() {
		return worlds;
	}

	public boolean contains(World world) {
		return worlds.contains(world);
	}

	public int getPlayersSleeping() {
		return sleeping;
	}

	public boolean isNightSkipping() {
		return isNightSkipping;
	}
}
