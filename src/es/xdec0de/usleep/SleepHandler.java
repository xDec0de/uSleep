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
import es.xdec0de.usleep.api.USleepAPI;
import es.xdec0de.usleep.api.events.SleepErrorEvent;
import es.xdec0de.usleep.api.events.SleepErrorEvent.SleepErrorReason;
import es.xdec0de.usleep.utils.EnumUtils;
import es.xdec0de.usleep.utils.SoundHandler;
import es.xdec0de.usleep.utils.files.USPMessage;
import es.xdec0de.usleep.utils.files.USPMessages;
import es.xdec0de.usleep.utils.files.USPSetting;

public class SleepHandler implements Listener {

	private final USleepAPI api;

	public SleepHandler() {
		this.api = USleepAPI.getInstance();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBedEnter(PlayerBedEnterEvent e) {
		Player p = e.getPlayer();
		Environment env = e.getBed().getWorld().getEnvironment();
		boolean cancel = true;
		SleepErrorEvent see = null;
		if(env.equals(Environment.NORMAL) || env.equals(Environment.CUSTOM)) {
			if(e.getBedEnterResult().equals(BedEnterResult.OK)) {
				SleepGroup group = api.getSleepGroup(e.getBed().getWorld());
				if(!group.isNightSkipping()) {
					if(!api.hasSleepCooldown(p)) {
						if(cancel = !api.handleSleep(p))
							USPMessage.NO_PERMS.send(p, "%perm%", USPSetting.PERM_PERCENT_SLEEP.asString());
						else
							see = new SleepErrorEvent(p, SleepErrorReason.NO_PERMISSIONS);
					} else
						see = new SleepErrorEvent(p, SleepErrorReason.TOO_FAST);
				} else
					see = new SleepErrorEvent(p, SleepErrorReason.ALREADY_SKIPPING);
			} else {
				see = new SleepErrorEvent(p, (SleepErrorReason)EnumUtils.ofOther(SleepErrorReason.class, e.getBedEnterResult()));
				if(USPSetting.ACTIONBAR_ENABLED.asBoolean())
					USPMessages.sendActionBar(p, see.getMessage());
				else
					USPMessages.sendMessage(p, see.getMessage());
				SoundHandler.playSound(p, see.getSound());
			}
		} else {
			see = new SleepErrorEvent(p, SleepErrorReason.NOT_POSSIBLE_HERE);
			cancel = false;
		}
		if(see != null) {
			Bukkit.getPluginManager().callEvent(see);
			if(USPSetting.ACTIONBAR_ENABLED.asBoolean())
				USPMessages.sendActionBar(p, see.getMessage());
			else
				USPMessages.sendMessage(p, see.getMessage());
			SoundHandler.playSound(p, see.getSound());
		}
		e.setCancelled(cancel);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBedLeave(PlayerBedLeaveEvent e) {
		api.handleWakeUp(e.getPlayer());
	}
}
