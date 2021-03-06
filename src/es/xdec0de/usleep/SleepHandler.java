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
import es.xdec0de.usleep.utils.files.USPMessage;
import es.xdec0de.usleep.utils.files.USPMessages;
import es.xdec0de.usleep.utils.files.USPSetting;

public class SleepHandler implements Listener {

	private final USleepAPI api = USleepAPI.getInstance();

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
						if(cancel = !api.handleSleep(p, false))
							USPMessage.NO_PERMS.send(p, "%perm%", USPSetting.PERM_PERCENT_SLEEP.asString());
						else
							see = new SleepErrorEvent(p, USPMessage.NO_PERMS.getString("%perm%", USPSetting.PERM_PERCENT_SLEEP.asString()), SleepErrorReason.NO_PERMISSIONS, e.getBed());
					} else
						see = new SleepErrorEvent(p, USPMessage.TOO_FAST.getString(), SleepErrorReason.TOO_FAST, e.getBed());
				} else
					see = new SleepErrorEvent(p, USPMessage.ALREADY_SKIPPING.getString(), SleepErrorReason.ALREADY_SKIPPING, e.getBed());
			} else
				see = new SleepErrorEvent(p, USPMessage.valueOf(e.getBedEnterResult().name()).getString(), SleepErrorReason.valueOf(e.getBedEnterResult().name()), e.getBed());
		} else {
			see = new SleepErrorEvent(p, USPMessage.NOT_POSSIBLE_HERE.getString(), SleepErrorReason.NOT_POSSIBLE_HERE, e.getBed());
			cancel = false;
		}
		if(see != null) {
			Bukkit.getPluginManager().callEvent(see);
			if(!(cancel = !see.isCancelled())) {
				if(USPSetting.ACTIONBAR_ENABLED.asBoolean())
					USPMessages.sendActionBar(p, see.getMessage());
				else
					USPMessages.sendMessage(p, see.getMessage());
				p.playSound(p, see.getSound(), 1.0F, 1.0F);
			}
		}
		e.setCancelled(cancel);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBedLeave(PlayerBedLeaveEvent e) {
		api.handleWakeUp(e.getPlayer());
	}
}
