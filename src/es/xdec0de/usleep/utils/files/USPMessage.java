package es.xdec0de.usleep.utils.files;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import es.xdec0de.usleep.USleep;
import es.xdec0de.usleep.utils.Replacer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * Represents an uSleep message.
 * 
 * @since uSleep v2.0.0
 *
 * @author xDec0de_
 */
public enum USPMessage {

	UPDATE_AVAILABLE_PLAYER("Events.Updater.Available.Player", (byte) -1, false),
	UPDATE_AVAILABLE_CONSOLE("Events.Updater.Available.Console", (byte) -1, true),
	UPDATE_LATEST_PLAYER("Events.Updater.Latest.Player", (byte) -1, false),
	UPDATE_LATEST_CONSOLE("Events.Updater.Latest.Console", (byte) -1, true),

	PERCENT_NEXT_DAY("Events.PercentSleep.NextDay", (byte) 0, true),
	PERCENT_TOO_FAST("Events.PercentSleep.TooFast", (byte) 0, false),
	PERCENT_TITLE("Events.PercentSleep.Title", (byte) -1, false),
	PERCENT_SUBTITLE("Events.PercentSleep.Subtitle", (byte) -1, false),

	INSTANT_OK("Events.InstantSleep.OK", (byte) -1, true),
	PERCENT_OK("Events.PercentSleep.OK", (byte) 1, false),

	NOT_POSSIBLE_HERE("Events.Sleep.NOT_POSSIBLE_HERE", (byte) 1, false),
	NOT_POSSIBLE_NOW("Events.Sleep.NOT_POSSIBLE_NOW", (byte) 1, false),
	NOT_SAFE("Events.Sleep.NOT_SAFE", (byte) 1, false),
	OTHER_PROBLEM("Events.Sleep.OTHER_PROBLEM", (byte) 1, false),
	TOO_FAR_AWAY("Events.Sleep.TOO_FAR_AWAY", (byte) 1, false),

	BEDTP_TELEPORT("Commands.BedTP.Teleport", (byte) -1, false),
	BEDTP_TELEPORT_OTHER("Commands.BedTP.TeleportOther", (byte) -1, false),
	BEDTP_ERROR("Commands.BedTP.Error", (byte) -1, false),
	BEDTP_USAGE("Commands.BedTP.Usage", (byte) -1, false),

	USLEEP_USAGE("Commands.usleep.Usage", (byte) -1, false),
	RELOAD("Commands.usleep.Reload", (byte) -1, false),

	PLAYER_NOT_FOUND("Core.PlayerNotFound", (byte) -1, false),
	NO_CONSOLE("Core.NoConsole", (byte) -1, false),
	NO_PERMS("Core.NoPerms", (byte) -1, false),
	ERROR_PREFIX("Core.ErrorPrefix", (byte) -1, false),
	PREFIX("Core.Prefix", (byte) -1, false);

	private final String path;
	private final byte defActionDelay;
	private byte actionDelay;
	private boolean broadcastsConsole;

	USPMessage(String string, byte actionDelay, boolean broadcastsConsole) {
		this.path = string;
		this.defActionDelay = actionDelay;
		this.actionDelay = actionDelay;
		this.broadcastsConsole = broadcastsConsole;
	}

	/**
	 * Gets the corresponding <b>messages.yml</b>'s path of the message.
	 * 
	 * @return The path to the message.
	 * 
	 * @since uSleep v2.0.0
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Returns whether the message can be sent by uSleep (By default) as an action bar or not, regardless of {@link USPSetting#ACTIONBAR_ENABLED}'s value.
	 * 
	 * @return Whether the message can be sent by uSleep as an action bar.
	 * 
	 * @since uSleep v2.0.0
	 */
	public boolean isActionBarCompatible() {
		return actionDelay >= 0;
	}

	/**
	 * Makes any message action bar compatible, this won't override {@link USPSetting#ACTIONBAR_ENABLED}, it only toggles the possibility for the message to be sent as an action bar message.
	 * 
	 * @param compatible Whether the message should be able to be sent as an action bar message or not.
	 */
	public void setActionBarCompatible(boolean compatible) {
		if(compatible) {
			if(defActionDelay != -1)
				this.actionDelay = defActionDelay;
			else
				this.actionDelay = 0;
		} else
			this.actionDelay = -1;
	}

	/**
	 * Returns whether the message will be broadcasted to the console by uSleep (By default) or not.
	 * 
	 * @since uSleep v2.0.0
	 */
	public boolean broadcastsToConsole() {
		return broadcastsConsole;
	}

	/**
	 * Sets whether the message will be also broadcasted to the console when any broadcast method is called.
	 * 
	 * @param broadcastsConsole Whether the message should be also broadcasted to the console when any broadcast method is called.
	 */
	public void setBroadcastsToConsole(boolean broadcastsConsole) {
		this.broadcastsConsole = broadcastsConsole;
	}

	/**
	 * Gets the message as an uncolored String.
	 * 
	 * @return The message as an uncolored String.
	 * 
	 * @since uSleep v2.0.0
	 */
	public String getStringUncolored() {
		return USPMessages.getFile().getString(path);
	}

	/**
	 * Applies {@link USPMessages#applyColor(String)} and {@link USPMessages#getDefaultReplacer()} to the message.
	 * 
	 * @return The message, colored and with the default replacer applied to it.
	 * 
	 * @since uSleep v2.0.0
	 */
	public String getString() {
		String str = getStringUncolored();
		return (str != null && !str.isEmpty()) ? USPMessages.getDefaultReplacer().replaceAt(USPMessages.applyColor(getStringUncolored())) : null;
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
	public String getString(Replacer replacer) {
		String str = getStringUncolored();
		return (str != null && !str.isEmpty()) ? USPMessages.getDefaultReplacer().add(replacer).replaceAt(USPMessages.applyColor(getStringUncolored())) : null;
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
	public String getString(String... replacements) {
		String str = getStringUncolored();
		return (str != null && !str.isEmpty()) ? USPMessages.getDefaultReplacer().add(replacements).replaceAt(USPMessages.applyColor(getStringUncolored())) : null;
	}

	/**
	 * Sends {@link #getString()} to <b>sender</b>
	 * 
	 * @param sender The {@link CommandSender} that will receive the message.
	 * 
	 * @since uSleep v2.0.0
	 */
	public void send(CommandSender sender) {
		String str = getString();
		if(str != null && !str.isEmpty()) {
			if(sender instanceof Player && actionDelay >= 0 && USPSetting.ACTIONBAR_ENABLED.asBoolean()) {
				if(isActionBarCompatible())
					((Player)sender).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(str));
				else
					Bukkit.getScheduler().runTaskLater(USleep.getPlugin(USleep.class), () -> ((Player)sender).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(str)), actionDelay);
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
	public void send(CommandSender sender, Replacer replacer) {
		String str = getString(replacer);
		if(str != null && !str.isEmpty()) {
			if(sender instanceof Player && actionDelay >= 0 && USPSetting.ACTIONBAR_ENABLED.asBoolean()) {
				if(isActionBarCompatible())
					((Player)sender).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(str));
				else
					Bukkit.getScheduler().runTaskLater(USleep.getPlugin(USleep.class), () -> ((Player)sender).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(str)), actionDelay);
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
	public void send(CommandSender sender, String... replacements) {
		String str = getString(replacements);
		if(str != null && !str.isEmpty()) {
			if(sender instanceof Player && actionDelay >= 0 && USPSetting.ACTIONBAR_ENABLED.asBoolean()) {
				if(isActionBarCompatible())
					((Player)sender).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(str));
				else
					Bukkit.getScheduler().runTaskLater(USleep.getPlugin(USleep.class), () -> ((Player)sender).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(str)), actionDelay);
			} else
				sender.sendMessage(str);
		}
	}

	/**
	 * Broadcasts {@link #getString()} to all online players and to the console if {@link #broadcastsToConsole()} returns true.
	 * 
	 * @since uSleep v2.0.0
	 */
	public void broadcast() {
		String str = getString();
		if(str != null && !str.isEmpty()) {
			if(actionDelay >= 0 && USPSetting.ACTIONBAR_ENABLED.asBoolean()) {
				if(isActionBarCompatible())
					Bukkit.getOnlinePlayers().forEach(on -> on.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(str)));
				else
					Bukkit.getScheduler().runTaskLater(USleep.getPlugin(USleep.class), () -> Bukkit.getOnlinePlayers().forEach(on -> on.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(str))), actionDelay);
			} else
				Bukkit.getOnlinePlayers().forEach(on -> on.sendMessage(str));
			if(broadcastsConsole)
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
	public void broadcast(Replacer replacer) {
		String str = getString(replacer);
		if(str != null && !str.isEmpty()) {
			if(actionDelay >= 0 && USPSetting.ACTIONBAR_ENABLED.asBoolean()) {
				if(isActionBarCompatible())
					Bukkit.getOnlinePlayers().forEach(on -> on.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(str)));
				else
					Bukkit.getScheduler().runTaskLater(USleep.getPlugin(USleep.class), () -> Bukkit.getOnlinePlayers().forEach(on -> on.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(str))), actionDelay);
			} else
				Bukkit.getOnlinePlayers().forEach(on -> on.sendMessage(str));
			if(broadcastsConsole)
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
	public void broadcast(String... replacements) {
		String str = getString();
		if(str != null && !str.isEmpty()) {
			if(actionDelay >= 0 && USPSetting.ACTIONBAR_ENABLED.asBoolean()) {
				if(isActionBarCompatible())
					Bukkit.getOnlinePlayers().forEach(on -> on.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(str)));
				else
					Bukkit.getScheduler().runTaskLater(USleep.getPlugin(USleep.class), () -> Bukkit.getOnlinePlayers().forEach(on -> on.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(str))), actionDelay);
			} else
				Bukkit.getOnlinePlayers().forEach(on -> on.sendMessage(str));
			if(broadcastsConsole)
				Bukkit.getConsoleSender().sendMessage(str);
		}
	}
}
