package es.xdec0de.usleep.utils;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import es.xdec0de.usleep.USleep;
import es.xdec0de.usleep.utils.files.USPConfig;
import es.xdec0de.usleep.utils.files.USPMessage;
import es.xdec0de.usleep.utils.files.USPMessages;
import es.xdec0de.usleep.utils.files.USPSetting;

public class NotificationHandler {

	public static void playSound(Player player, USPSetting setting) {
		String soundStr = USPConfig.getString(setting);
		if(soundStr != null && !soundStr.isEmpty()) {
			Sound sound = (Sound)getEnum(Sound.class, soundStr);
			if(sound != null)
				player.playSound(player.getLocation(), sound, 1.0F, 1.0F);
		}
	}

	public static void broadcastSound(USPSetting setting) {
		String soundStr = USPConfig.getString(setting);
		if(soundStr != null && !soundStr.isEmpty()) {
			Sound sound = (Sound)getEnum(Sound.class, soundStr);
			if(sound != null)
				Bukkit.getOnlinePlayers().forEach(on -> on.playSound(on.getLocation(), sound, 1.0F, 1.0F));
		}
	}

	private static <E extends Enum<E>> Enum<E> getEnum(Class<E> enumClass, String str) {
		if(str == null)
			return null;
		try {
			return Enum.valueOf(enumClass, str);
		} catch (IllegalArgumentException iae) {
			return null;
		}
	}

	/**
	 * Sends an sleep message with colors {@link #applyColor(String)} and the default {@link Replacer}, empty messages will be ignored and the message wont be sent. 
	 * Sleep messages will be sent on actionbar if the setting is enabled on config.yml.
	 * 
	 * @param player The player that will receive the message.
	 * @param msg The message to get.
	 * 
	 * @see #getMessage(USPMessage)
	 */
	public static void sendSleepMessage(Player player, USPMessage msg) {
		if(USPConfig.getBoolean(USPSetting.ACTIONBAR_ENABLED)) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(USleep.getPlugin(USleep.class), new Runnable() {
				@Override
				public void run() {
					USPMessages.sendActionbar(player, msg);
				}
			}, 1L);
		}
		else
			USPMessages.sendMessage(player, msg);
	}

	/**
	 * Sends an sleep message with colors {@link #applyColor(String)} and the default {@link Replacer}, also, 
	 * the specified replacer is added to the default replacer, empty messages will be ignored and the message wont be sent. 
	 * Sleep messages will be sent on actionbar if the setting is enabled on config.yml.
	 * 
	 * @param player The player that will receive the message.
	 * @param msg The message to get.
	 * @param replacer The replacer to apply.
	 * 
	 * @see #getMessage(USPMessage, Replacer)
	 */
	public static void sendSleepMessage(Player player, USPMessage msg, Replacer replacer) {
		if(USPConfig.getBoolean(USPSetting.ACTIONBAR_ENABLED)) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(USleep.getPlugin(USleep.class), new Runnable() {
				@Override
				public void run() {
					USPMessages.sendActionbar(player, msg, replacer);
				}
			}, 1L);
		}
		else
			USPMessages.sendMessage(player, msg, replacer);
	}

	/**
	 * Sends an sleep message with colors {@link #applyColor(String)} and the default {@link Replacer}, also, a new replacer made with 
	 * the specified strings is added to the default replacer, empty messages will be ignored and the message wont be sent. 
	 * Sleep messages will be sent on actionbar if the setting is enabled on config.yml.
	 * 
	 * @param player The player that will receive the message.
	 * @param msg The message to get.
	 * @param replacements The replacements to apply.
	 * 
	 * @see #getMessage(USPMessage, String...)
	 */
	public static void sendSleepMessage(Player player, USPMessage msg, String... replacements) {
		if(USPConfig.getBoolean(USPSetting.ACTIONBAR_ENABLED)) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(USleep.getPlugin(USleep.class), new Runnable() {
				@Override
				public void run() {
					USPMessages.sendActionbar(player, msg, replacements);
				}
			}, 1L);
		}
		else
			USPMessages.sendMessage(player, msg, replacements);
	}

	public static void broadcastActionbarSleepMessage(USPMessage msg) {
		if(USPConfig.getBoolean(USPSetting.ACTIONBAR_ENABLED))
			USPMessages.broadcastActionbar(msg);
		else
			USPMessages.broadcast(msg);
	}

	public static void broadcastActionbarSleepMessage(USPMessage msg, Replacer replacer) {
		if(USPConfig.getBoolean(USPSetting.ACTIONBAR_ENABLED))
			USPMessages.broadcastActionbar(msg, replacer);
		else
			USPMessages.broadcast(msg, replacer);
	}

	public static void broadcastActionbarSleepMessage(USPMessage msg, String... replacers) {
		if(USPConfig.getBoolean(USPSetting.ACTIONBAR_ENABLED))
			USPMessages.broadcastActionbar(msg, replacers);
		else
			USPMessages.broadcast(msg, replacers);
	}
}
