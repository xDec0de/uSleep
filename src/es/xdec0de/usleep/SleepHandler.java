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

import es.xdec0de.usleep.api.SleepGroup;
import es.xdec0de.usleep.api.USleep;
import es.xdec0de.usleep.api.USleepAPI;
import es.xdec0de.usleep.api.events.SleepErrorEvent;
import es.xdec0de.usleep.utils.SoundHandler;
import es.xdec0de.usleep.utils.files.USPMessage;
import es.xdec0de.usleep.utils.files.USPMessages;
import es.xdec0de.usleep.utils.files.USPSetting;

public class SleepHandler implements Listener {

	private USleepAPI api;

	public SleepHandler() {
		this.api = USleep.getPlugin(USleep.class).getAPI();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBedEnter(PlayerBedEnterEvent e) {
		Player p = e.getPlayer();
		Environment env = e.getBed().getWorld().getEnvironment();
		boolean cancel = true;
		if(env.equals(Environment.NORMAL) || env.equals(Environment.CUSTOM)) {
			if(e.getBedEnterResult().equals(BedEnterResult.OK)) {
				SleepGroup group = api.getSleepGroup(e.getBed().getWorld());
				if(!group.isNightSkipping()) {
					if(!api.hasSleepCooldown(p)) {
						if(cancel = !api.handleSleep(p))
							USPMessage.NO_PERMS.send(p, "%perm%", USPSetting.PERM_PERCENT_SLEEP.asString());
					} else
						USPMessage.TOO_FAST.send(p);
				} else
					USPMessage.ALREADY_SKIPPING.send(p);
			} else {
				SleepErrorEvent see = new SleepErrorEvent(p, e.getBedEnterResult());
				Bukkit.getPluginManager().callEvent(see);
				if(USPSetting.ACTIONBAR_ENABLED.asBoolean())
					USPMessages.sendActionBar(p, see.getMessage());
				else
					USPMessages.sendMessage(p, see.getMessage());
				SoundHandler.playSound(p, see.getSound());
			}
		} else
			cancel = false;
		e.setCancelled(cancel);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBedLeave(PlayerBedLeaveEvent e) {
		api.handleWakeUp(e.getPlayer());
	}
}
