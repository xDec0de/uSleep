package me.xdec0de.usleep;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

import com.google.common.base.Enums;

import me.xdec0de.mcutils.files.yaml.PluginFile;
import me.xdec0de.mcutils.java.strings.MCStrings;
import me.xdec0de.usleep.api.SleepGroup;
import me.xdec0de.usleep.api.events.SleepErrorEvent;
import me.xdec0de.usleep.api.events.SleepErrorEvent.SleepErrorReason;

public class SleepHandler implements Listener {

	private final USleep uSleep;

	public SleepHandler(USleep plugin) {
		this.uSleep = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBedEnter(PlayerBedEnterEvent e) {
		boolean cancel = true;
		Player p = e.getPlayer();
		SleepErrorReason errorReason = null;
		Environment env = e.getBed().getWorld().getEnvironment();
		if (!env.equals(Environment.NORMAL) && !env.equals(Environment.CUSTOM)) {
			errorReason = SleepErrorReason.NOT_POSSIBLE_HERE;
			cancel = false;
		} else if (!e.getBedEnterResult().equals(BedEnterResult.OK))
			errorReason = SleepErrorReason.valueOf(e.getBedEnterResult().name());
		SleepGroup group = uSleep.getAPI().getSleepGroup(e.getBed().getWorld());
		if (group.isNightSkipping())
			errorReason = SleepErrorReason.ALREADY_SKIPPING;
		else if (uSleep.getAPI().hasSleepCooldown(p))
			errorReason = SleepErrorReason.TOO_FAST;
		else if (cancel = !uSleep.getAPI().handleSleep(p, false))
			errorReason = SleepErrorReason.NO_PERMISSIONS;
		if (errorReason != null) {
			Sound errorSound = Enums.getIfPresent(Sound.class, uSleep.getConfig().getString("Sounds.Sleep.Error")).orNull();
			String errorMsg =  uSleep.getMessages().getString(errorReason.getMessagePath());
			SleepErrorEvent see = new SleepErrorEvent(p, errorMsg, errorReason, errorSound, e.getBed());
			Bukkit.getPluginManager().callEvent(see);
			if (!(cancel = !see.isCancelled())) {
				MCStrings.sendFormattedMessage(p, MCStrings.applyColor(see.getMessage()));
				p.playSound(p, see.getSound(), 1.0F, 1.0F);
			}
		}
		e.setCancelled(cancel);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBedLeave(PlayerBedLeaveEvent e) {
		uSleep.getAPI().handleWakeUp(e.getPlayer());
	}

	/*
	 * World load & unload for uSleepAPI
	 */

	@EventHandler
	public void onWorldLoad(WorldLoadEvent e) {
		// Find world name in worlds.yml instead of reloading all sleep groups.
		final PluginFile worlds = uSleep.getWorlds();
		for (String groupId : worlds.getConfigurationSection("groups").getKeys(false)) {
			if (!worlds.getStringList("groups." + groupId + ".worlds").contains(e.getWorld().getName()))
				continue;
			final SleepGroup group = uSleep.getAPI().getSleepGroup(groupId);
			if (group == null)
				continue;
			group.addWorld(e.getWorld());
			return;
		}
		uSleep.getAPI().getDefaultSleepGroup().addWorld(e.getWorld());
	}

	@EventHandler
	public void onWorldUnload(WorldUnloadEvent e) {
		final SleepGroup group = uSleep.getAPI().getSleepGroup(e.getWorld());
		if (group != null) // Shouldn't happen, but just in case.
			group.removeWorld(e.getWorld());
	}
}
