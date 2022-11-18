package es.xdec0de.usleep;

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

import com.google.common.base.Enums;

import es.xdec0de.usleep.api.SleepGroup;
import es.xdec0de.usleep.api.USleep;
import es.xdec0de.usleep.api.events.SleepErrorEvent;
import es.xdec0de.usleep.api.events.SleepErrorEvent.SleepErrorReason;

public class SleepHandler implements Listener {

	private final USleep plugin;

	public SleepHandler(USleep plugin) {
		this.plugin = plugin;
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
		SleepGroup group = plugin.getAPI().getSleepGroup(e.getBed().getWorld());
		if (group.isNightSkipping())
			errorReason = SleepErrorReason.ALREADY_SKIPPING;
		else if (plugin.getAPI().hasSleepCooldown(p))
			errorReason = SleepErrorReason.TOO_FAST;
		else if (cancel = !plugin.getAPI().handleSleep(p, false))
			errorReason = SleepErrorReason.NO_PERMISSIONS;
		if (errorReason != null) {
			Sound errorSound = Enums.getIfPresent(Sound.class, plugin.getConfig().getString("Sounds.Sleep.Error")).orNull();
			String errorMsg =  plugin.getMessages().getColoredString(errorReason.getMessagePath());
			SleepErrorEvent see = new SleepErrorEvent(p, errorMsg, errorReason, errorSound, e.getBed());
			Bukkit.getPluginManager().callEvent(see);
			if (!(cancel = !see.isCancelled())) {
				plugin.strings().sendFormattedMessage(p, plugin.strings().applyColor(see.getMessage()));
				p.playSound(p, see.getSound(), 1.0F, 1.0F);
			}
		}
		e.setCancelled(cancel);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBedLeave(PlayerBedLeaveEvent e) {
		plugin.getAPI().handleWakeUp(e.getPlayer());
	}
}
