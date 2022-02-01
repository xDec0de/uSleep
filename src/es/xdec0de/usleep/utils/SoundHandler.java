package es.xdec0de.usleep.utils;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import es.xdec0de.usleep.utils.files.USPSetting;

public class SoundHandler {

	public static void playSound(Player player, Sound sound) {
		if(sound != null)
			player.playSound(player.getLocation(), sound, 1.0F, 1.0F);
	}

	public static void broadcastSound(USPSetting setting) {
		String soundStr = setting.asString();
		if(soundStr != null && !soundStr.isEmpty()) {
			Sound sound = (Sound)EnumUtils.getEnum(Sound.class, soundStr);
			if(sound != null)
				Bukkit.getOnlinePlayers().forEach(on -> on.playSound(on.getLocation(), sound, 1.0F, 1.0F));
		}
	}

	public static void broadcastSound(List<Player> players, USPSetting setting) {
		String soundStr = setting.asString();
		if(soundStr != null && !soundStr.isEmpty()) {
			Sound sound = (Sound)EnumUtils.getEnum(Sound.class, soundStr);
			if(sound != null)
				players.forEach(on -> on.playSound(on.getLocation(), sound, 1.0F, 1.0F));
		}
	}
}
