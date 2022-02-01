package es.xdec0de.usleep.utils.files;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import es.xdec0de.usleep.api.USleep;
import es.xdec0de.usleep.utils.Replacer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * Interface for uSleep messages, so new messages can be added by other plugins!
 * 
 * @since uSleep v2.0.0
 * 
 * @author xDec0de_
 */
public interface USleepMessage {

	/**
	 * Makes any message action bar compatible, this won't override {@link USPSetting#ACTIONBAR_ENABLED}, it only toggles the possibility for the message to be sent as an action bar message.
	 * 
	 * @param compatible Whether the message should be able to be sent as an action bar message or not.
	 */
	public String getPath();

	/**
	 * Returns whether the message will be broadcasted to the console by uSleep (By default) or not.
	 * 
	 * @since uSleep v2.0.0
	 */
	public boolean isActionBarCompatible();

	/**
	 * Sets whether the message will be also broadcasted to the console when any broadcast method is called.
	 * 
	 * @param broadcastsConsole Whether the message should be also broadcasted to the console when any broadcast method is called.
	 */
	public boolean broadcastsToConsole();

	/**
	 * Gets the message as an uncolored String.
	 * 
	 * @return The message as an uncolored String.
	 * 
	 * @since uSleep v2.0.0
	 */
	default public String getStringUncolored() {
		return USPMessages.getFile().getString(getPath());
	}

	/**
	 * Applies {@link USPMessages#applyColor(String)} and {@link USPMessages#getDefaultReplacer()} to the message.
	 * 
	 * @return The message, colored and with the default replacer applied to it.
	 * 
	 * @since uSleep v2.0.0
	 */
	default String getString() {
		String str = getStringUncolored();
		return (str != null && !str.isEmpty()) ? USPMessages.getDefaultReplacer().replaceAt(USPMessages.applyColor(str)) : null;
	}

	/**
	 * Applies {@link USPMessages#applyColor(String)} and {@link USPMessages#getDefaultReplacer()} with <b>replacer</b> added to it to the message.
	 * 
	 * @param replacer The replacer to add.
	 * 
	 * @return The message, colored and with the default replacer and the specified replacer applied to it.
	 * 
	 * @since uSleep v2.0.0
	 */
	default public String getString(Replacer replacer) {
		String str = getStringUncolored();
		return (str != null && !str.isEmpty()) ? USPMessages.getDefaultReplacer().add(replacer).replaceAt(USPMessages.applyColor(str)) : null;
	}

	/**
	 * Applies {@link USPMessages#applyColor(String)} and {@link USPMessages#getDefaultReplacer()} with <b>replacements</b> added to it to the message.
	 * 
	 * @param replacements The replacements to add.
	 * 
	 * @return The message, colored and with the default replacer and the specified replacer applied to it.
	 * 
	 * @since uSleep v2.0.0
	 */
	default public String getString(String... replacements) {
		String str = getStringUncolored();
		return (str != null && !str.isEmpty()) ? USPMessages.getDefaultReplacer().add(replacements).replaceAt(USPMessages.applyColor(str)) : null;
	}

	/**
	 * Sends {@link #getString()} to <b>sender</b>
	 * 
	 * @param sender The {@link CommandSender} that will receive the message.
	 * 
	 * @since uSleep v2.0.0
	 */
	default public void send(CommandSender sender) {
		String str = getString();
		if(str != null && !str.isEmpty()) {
			if(sender instanceof Player && isActionBarCompatible() && USPSetting.ACTIONBAR_ENABLED.asBoolean()) {
				if(isActionBarCompatible())
					((Player)sender).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(str));
				else
					Bukkit.getScheduler().runTaskLater(USleep.getPlugin(USleep.class), () -> ((Player)sender).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(str)), 1L);
			} else
				sender.sendMessage(str);
		}
	}

	/**
	 * Sends {@link #getString(Replacer)} to <b>sender</b>
	 * 
	 * @param sender The {@link CommandSender} that will receive the message.
	 * @param replacer The replacer to apply.
	 * 
	 * @since uSleep v2.0.0
	 */
	default public void send(CommandSender sender, Replacer replacer) {
		String str = getString(replacer);
		if(str != null && !str.isEmpty()) {
			if(sender instanceof Player && isActionBarCompatible() && USPSetting.ACTIONBAR_ENABLED.asBoolean()) {
				if(isActionBarCompatible())
					((Player)sender).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(str));
				else
					Bukkit.getScheduler().runTaskLater(USleep.getPlugin(USleep.class), () -> ((Player)sender).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(str)), 1L);
			} else
				sender.sendMessage(str);
		}
	}

	/**
	 * Sends {@link #getString(String...)} to <b>sender</b>
	 * 
	 * @param sender The {@link CommandSender} that will receive the message.
	 * @param replacements The replacements to apply.
	 * 
	 * @since uSleep v2.0.0
	 */
	default public void send(CommandSender sender, String... replacements) {
		String str = getString(replacements);
		if(str != null && !str.isEmpty()) {
			if(sender instanceof Player && isActionBarCompatible() && USPSetting.ACTIONBAR_ENABLED.asBoolean()) {
				if(isActionBarCompatible())
					((Player)sender).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(str));
				else
					Bukkit.getScheduler().runTaskLater(USleep.getPlugin(USleep.class), () -> ((Player)sender).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(str)), 1L);
			} else
				sender.sendMessage(str);
		}
	}

	/**
	 * Broadcasts {@link #getString()} to all online players and to the console if {@link #broadcastsToConsole()} returns true.
	 * 
	 * @since uSleep v2.0.0
	 */
	default public void broadcast() {
		String str = getString();
		if(str != null && !str.isEmpty()) {
			if(isActionBarCompatible() && USPSetting.ACTIONBAR_ENABLED.asBoolean())
				Bukkit.getOnlinePlayers().forEach(on -> on.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(str)));
			else
				Bukkit.getOnlinePlayers().forEach(on -> on.sendMessage(str));
			if(broadcastsToConsole())
				Bukkit.getConsoleSender().sendMessage(str);
		}
	}

	/**
	 * Broadcasts {@link #getString()} to all online players and to the console if {@link #broadcastsToConsole()} returns true.
	 * 
	 * @param replacer The replacer to apply.
	 * 
	 * @since uSleep v2.0.0
	 */
	default public void broadcast(Replacer replacer) {
		String str = getString(replacer);
		if(str != null && !str.isEmpty()) {
			if(isActionBarCompatible() && USPSetting.ACTIONBAR_ENABLED.asBoolean())
				Bukkit.getOnlinePlayers().forEach(on -> on.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(str)));
			else
				Bukkit.getOnlinePlayers().forEach(on -> on.sendMessage(str));
			if(broadcastsToConsole())
				Bukkit.getConsoleSender().sendMessage(str);
		}
	}

	/**
	 * Broadcasts {@link #getString()} to all online players and to the console if {@link #broadcastsToConsole()} returns true.
	 * 
	 * @param replacements The replacements to apply.
	 * 
	 * @since uSleep v2.0.0
	 */
	default public void broadcast(String... replacements) {
		String str = getString();
		if(str != null && !str.isEmpty()) {
			if(isActionBarCompatible() && USPSetting.ACTIONBAR_ENABLED.asBoolean())
				Bukkit.getOnlinePlayers().forEach(on -> on.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(str)));
			else
				Bukkit.getOnlinePlayers().forEach(on -> on.sendMessage(str));
			if(broadcastsToConsole())
				Bukkit.getConsoleSender().sendMessage(str);
		}
	}

	/**
	 * Broadcasts {@link #getString()} to the specified players and to the console if {@link #broadcastsToConsole()} returns true.
	 * 
	 * @param players The players that will receive the message.
	 * 
	 * @since uSleep v2.0.0
	 */
	default public void broadcast(List<Player> players) {
		String str = getString();
		if(str != null && !str.isEmpty()) {
			if(isActionBarCompatible() && USPSetting.ACTIONBAR_ENABLED.asBoolean())
				players.forEach(on -> on.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(str)));
			else
				players.forEach(on -> on.sendMessage(str));
			if(broadcastsToConsole())
				Bukkit.getConsoleSender().sendMessage(str);
		}
	}

	/**
	 * Broadcasts {@link #getString()} to the specified players and to the console if {@link #broadcastsToConsole()} returns true.
	 * 
	 * @param players The players that will receive the message.
	 * @param replacer The replacer to apply.
	 * 
	 * @since uSleep v2.0.0
	 */
	default public void broadcast(List<Player> players, Replacer replacer) {
		String str = getString(replacer);
		if(str != null && !str.isEmpty()) {
			if(isActionBarCompatible() && USPSetting.ACTIONBAR_ENABLED.asBoolean())
				players.forEach(on -> on.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(str)));
			else
				players.forEach(on -> on.sendMessage(str));
			if(broadcastsToConsole())
				Bukkit.getConsoleSender().sendMessage(str);
		}
	}

	/**
	 * Broadcasts {@link #getString()} to the specified players and to the console if {@link #broadcastsToConsole()} returns true.
	 * 
	 * @param players The players that will receive the message.
	 * @param replacements The replacements to apply.
	 * 
	 * @since uSleep v2.0.0
	 */
	default public void broadcast(List<Player> players, String... replacements) {
		String str = getString();
		if(str != null && !str.isEmpty()) {
			if(isActionBarCompatible() && USPSetting.ACTIONBAR_ENABLED.asBoolean())
				players.forEach(on -> on.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(str)));
			else
				players.forEach(on -> on.sendMessage(str));
			if(broadcastsToConsole())
				Bukkit.getConsoleSender().sendMessage(str);
		}
	}
}
