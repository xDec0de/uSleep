package es.xdec0de.usleep.utils;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import es.xdec0de.usleep.utils.files.USPConfig;
import es.xdec0de.usleep.utils.files.USPSetting;

public class NotificationHandler {

	public static void playSound(Player player, Sound sound) {
		if(sound != null)
			player.playSound(player.getLocation(), sound, 1.0F, 1.0F);
	}

	public static void broadcastSound(USPSetting setting) {
		String soundStr = USPConfig.getString(setting);
		if(soundStr != null && !soundStr.isEmpty()) {
			Sound sound = (Sound)EnumUtils.getEnum(Sound.class, soundStr);
			if(sound != null)
				Bukkit.getOnlinePlayers().forEach(on -> on.playSound(on.getLocation(), sound, 1.0F, 1.0F));
		}
	}
}
