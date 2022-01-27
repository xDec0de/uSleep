package es.xdec0de.usleep;

import org.bukkit.Sound;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult;
import org.bukkit.event.player.PlayerBedLeaveEvent;

import es.xdec0de.usleep.api.SleepAPI;
import es.xdec0de.usleep.utils.USPMessage;
import es.xdec0de.usleep.utils.USPSetting;
import es.xdec0de.usleep.utils.files.USPConfig;
import es.xdec0de.usleep.utils.files.USPMessages;

public class SleepHandler implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBedEnter(PlayerBedEnterEvent e) {
		Player p = e.getPlayer();
		if(e.getBed().getLocation().getWorld().getEnvironment().equals(Environment.NORMAL)) {
			if(e.getBedEnterResult().equals(BedEnterResult.OK)) {
				if(!SleepAPI.hasSleepCooldown(p)) {
					if(!SleepAPI.handleSleep(p))
						USPMessages.sendSleepMessage(p, USPMessage.NO_PERMS);
				} else
					USPMessages.sendSleepMessage(p, USPMessage.PERCENT_TOO_FAST);
			} else {
				e.setCancelled(true);
				USPMessages.sendSleepMessage(p, USPMessage.valueOf(e.getBedEnterResult().name()));
				if(USPConfig.getBoolean(USPSetting.PERCENT_SLEEP_SOUNDS_ENABLED))
					p.playSound(p.getLocation(), Sound.valueOf(USPConfig.getString(USPSetting.PERCENT_SLEEP_ERROR_SOUND)), 1.0F, 1.0F); 
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBedLeave(PlayerBedLeaveEvent e) {
		SleepAPI.handleWakeUp();
	}
}
