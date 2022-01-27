package es.xdec0de.usleep.bedinteract;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult;
import es.xdec0de.usleep.USleep;
import es.xdec0de.usleep.api.SleepAPI;
import es.xdec0de.usleep.utils.USPMessage;
import es.xdec0de.usleep.utils.USPSetting;
import es.xdec0de.usleep.utils.files.USPConfig;
import es.xdec0de.usleep.utils.files.USPMessages;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Sleep implements Listener {
	
	SleepAPI uSleepAPI = new SleepAPI();
	
	public static List<String> onDelay = new ArrayList<>();
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBedEnter(PlayerBedEnterEvent e) {
		if(e.getBed().getLocation().getWorld().getEnvironment().equals(Environment.NORMAL)) {
			Player p = e.getPlayer();
			if(p.hasPermission(USPConfig.getString(USPSetting.INSTANT_SLEEP_PERM))) {
				SleepAPI.NumSleep = 0;
		        p.getWorld().setTime(0L);
		        p.getWorld().setThundering(false);
		        p.getWorld().setStorm(false);
		        uSleepAPI.broadcastInstantNextDay(p);
			} else if(p.hasPermission(USPConfig.getString(USPSetting.PERCENT_SLEEP_PERM))) {
				if(e.getBedEnterResult().equals(BedEnterResult.OK)) {
					if(!onDelay.contains(p.getName())) {
						SleepAPI.NumSleep++;
						if(SleepAPI.NumSleep != Math.round(uSleepAPI.getPlayerCount() * USPConfig.getInt(USPSetting.PERCENT_SLEEP_PERCENT) / 100.0F)) {
							onDelay.add(p.getName());
							uSleepAPI.broadcastSleep();
						} else {
							SleepAPI.NumSleep = 0;
					        p.getWorld().setTime(0L);
					        p.getWorld().setThundering(false);
					        p.getWorld().setStorm(false);
					        uSleepAPI.broadcastPercentNextDay();
						}
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(USleep.plugin, new Runnable() {
							public void run() {
								onDelay.remove(p.getName());
			                }
						},  (USPConfig.getInt(USPSetting.PERCENT_SLEEP_PREVENT_SPAM_COOLDOWN) * 20));
					} else {
						if(USPConfig.getBoolean(USPSetting.ACTIONBAR_ENABLED)) {
							sendActionBar(p, USPMessages.getMessage(USPMessage.PERCENT_TOO_FAST));
						} else {
							USPMessages.sendMessage(p, USPMessage.PERCENT_TOO_FAST);
						}
						if(USPConfig.getBoolean(USPSetting.PERCENT_SLEEP_SOUNDS_ENABLED)) {
							p.playSound(p.getLocation(), Sound.valueOf(USPConfig.getString(USPSetting.PERCENT_SLEEP_ERROR_SOUND)), 1.0F, 1.0F); 
						}
						e.setCancelled(true);
					}
				} else {
					String result = e.getBedEnterResult().name();
					e.setCancelled(true);
					if(USPConfig.getBoolean(USPSetting.ACTIONBAR_ENABLED)) {
						sendActionBar(p, USPMessages.getMessage(USPMessage.valueOf(result)));
					}
					if(USPConfig.getBoolean(USPSetting.PERCENT_SLEEP_SOUNDS_ENABLED)) {
						p.playSound(p.getLocation(), Sound.valueOf(USPConfig.getString(USPSetting.PERCENT_SLEEP_ERROR_SOUND)), 1.0F, 1.0F); 
					}
				}
			} else {
				p.sendMessage(USPMessages.getMessage(USPMessage.NO_PERMS).replaceAll("%perm%", USPConfig.getString(USPSetting.PERCENT_SLEEP_PERM)));
				if(USPConfig.getBoolean(USPSetting.PERCENT_SLEEP_SOUNDS_ENABLED)) {
					p.playSound(p.getLocation(), Sound.valueOf(USPConfig.getString(USPSetting.PERCENT_SLEEP_ERROR_SOUND)), 1.0F, 1.0F); 
				}
				e.setCancelled(true);
			}
		}
	}
	
	private void sendActionBar(Player p, String msg) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(USleep.plugin, new Runnable() {
		    @Override
		    public void run() {
		    	p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
		    }
		}, 1L);
	}
}
