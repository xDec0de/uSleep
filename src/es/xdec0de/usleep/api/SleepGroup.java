package es.xdec0de.usleep.api;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;

import es.xdec0de.usleep.api.events.NightSkipEvent;
import es.xdec0de.usleep.utils.SoundHandler;
import es.xdec0de.usleep.utils.files.USPMessage;
import es.xdec0de.usleep.utils.files.USPSetting;
import es.xdec0de.usleep.utils.files.USPWorlds;

public class SleepGroup {

	List<World> worlds = new ArrayList<World>();
	private final String id;
	private int sleeping = 0, percent;
	private boolean isNightSkipping = false;

	SleepGroup(String id) {
		this.id = id;
	}

	SleepGroup(List<World> worlds) {
		this("__usleep_def_sleep_group__");
		this.worlds = worlds;
		this.percent = USPSetting.PERCENT_SLEEP_PERCENT.asInt();
	}

	List<String> build() {
		if(!worlds.isEmpty())
			worlds.clear();
		List<String> errors = new LinkedList<String>();
		for(String worldName : USPWorlds.getWorldsInGroup(id)) {
			World world = Bukkit.getWorld(worldName);
			if(world != null)
				worlds.add(Bukkit.getWorld(worldName));
			else
				errors.add(worldName);
		}
		this.percent = USPWorlds.getPercentRequired(id);
		return errors;
	}

	public boolean handleSleep(Player player) {
		List<Player> players = getPlayers();
		USleepAPI api = USleep.getPlugin(USleep.class).getAPI();
		api.addToSleepCooldown(player.getUniqueId(), USPSetting.PERCENT_SLEEP_COOLDOWN.asInt());
		if(USPSetting.INSTANT_SLEEP_ENABLED.asBoolean() && player.hasPermission(USPSetting.PERM_INSTANT_SLEEP.asString())) // instant
			resetTime(player);
		else if(USPSetting.PERCENT_SLEEP_ENABLED.asBoolean() && player.hasPermission(USPSetting.PERM_PERCENT_SLEEP.asString())) { // percent
			sleeping++;
			int required = getRequiredPlayers();
			if(required > sleeping) {
				USPMessage.PERCENT_OK.broadcast(players, "%required%", Integer.toString(required), "%current%", Integer.toString(sleeping));
				SoundHandler.broadcastSound(players, USPSetting.SOUND_SLEEP_OK);
			} else
				resetTime(null);
		} else
			return false;
		return true;
	}

	public void handleWakeUp() {
		if(sleeping > 0) {
			sleeping--;
			List<Player> players = getPlayers();
			USPMessage.PERCENT_OK.broadcast(players, "%required%", Integer.toString(getRequiredPlayers()), "%current%", Integer.toString(sleeping));
			SoundHandler.broadcastSound(players, USPSetting.SOUND_SLEEP_LEAVE);
		}
	}

	private void resetTime(Player player) {
		SleepMode mode = player != null ? SleepMode.INSTANT : SleepMode.PERCENT;
		NightSkipEvent nse = new NightSkipEvent(this, mode);
		Bukkit.getPluginManager().callEvent(nse);
		if(!nse.isCancelled()) {
			this.isNightSkipping = true;
			sleeping = 0;
			boolean skipEffect = USPSetting.NIGHT_SKIP_EFFECT_ENABLED.asBoolean();
			List<Player> players = getPlayers();
			if(skipEffect)
				USleep.getPlugin(USleep.class).getAPI().doNightSkipEffect(this);
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
			this.isNightSkipping = false;
		}
	}

	public int getRequiredPlayers() {
		return Math.round((getPlayers().size() - getInactivePlayers().size()) * percent / 100.0F);
	}

	public List<Player> getInactivePlayers() {
		USleepAPI api = USleep.getPlugin(USleep.class).getAPI();
		List<Player> players = getPlayers();
		List<Player> list = new LinkedList<Player>();
		boolean ignoreAFK = USPSetting.PERCENT_SLEEP_IGNORE_AFK.asBoolean();
		boolean ignoreVanished = USPSetting.PERCENT_SLEEP_IGNORE_VANISHED.asBoolean();
		if(ignoreAFK || ignoreVanished) {
			if(Bukkit.getPluginManager().getPlugin("Essentials") != null) {
				Essentials ess = (Essentials)Bukkit.getServer().getPluginManager().getPlugin("Essentials");
				for(Player p : players.stream().filter(on -> !list.contains(on)).collect(Collectors.toList())) {
					User user = ess.getUser(p);
					if((ignoreAFK && user.isAfk()) || (ignoreVanished && user.isVanished()))
						list.add(p);
				}
			}
			if(ignoreVanished)
				if(Bukkit.getPluginManager().getPlugin("SuperVanish") != null || Bukkit.getPluginManager().getPlugin("PremiumVanish") != null)
					for(Player p : players.stream().filter(on -> !list.contains(on)).collect(Collectors.toList()))
						if(api.isVanished(p))
							list.add(p);
		}
		return list;
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
