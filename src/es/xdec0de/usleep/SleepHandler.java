package es.xdec0de.usleep;

import org.bukkit.Bukkit;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult;
import org.bukkit.event.player.PlayerBedLeaveEvent;

import es.xdec0de.usleep.api.USleepAPI;
import es.xdec0de.usleep.api.events.SleepErrorEvent;
import es.xdec0de.usleep.utils.NotificationHandler;
import es.xdec0de.usleep.utils.files.USPConfig;
import es.xdec0de.usleep.utils.files.USPMessage;
import es.xdec0de.usleep.utils.files.USPSetting;

public class SleepHandler implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBedEnter(PlayerBedEnterEvent e) {
		Player p = e.getPlayer();
		if(e.getBed().getLocation().getWorld().getEnvironment().equals(Environment.NORMAL)) {
			if(e.getBedEnterResult().equals(BedEnterResult.OK)) {
				if(!USleepAPI.hasSleepCooldown(p)) {
					if(!USleepAPI.handleSleep(p)) {
						e.setCancelled(true);
						NotificationHandler.sendSleepMessage(p, USPMessage.NO_PERMS, "%perm%", USPConfig.getString(USPSetting.PERM_PERCENT_SLEEP));
					}
				} else
					NotificationHandler.sendSleepMessage(p, USPMessage.PERCENT_TOO_FAST);
			} else {
				e.setCancelled(true);
				SleepErrorEvent see = new SleepErrorEvent(p, e.getBedEnterResult());
				Bukkit.getPluginManager().callEvent(see);
				if(see.sendsMessage())
					NotificationHandler.sendSleepMessage(p, USPMessage.valueOf(e.getBedEnterResult().name()));
				if(see.playsSound())
					NotificationHandler.playSound(p, USPSetting.SOUND_SLEEP_ERROR);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBedLeave(PlayerBedLeaveEvent e) {
		USleepAPI.handleWakeUp();
	}
}
