package es.xdec0de.usleep.utils.files;

import org.bukkit.Bukkit;

import es.xdec0de.usleep.api.USleep;
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
public enum USPMessage implements USleepMessage {

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

	public String getPath() {
		return path;
	}

	public boolean isActionBarCompatible() {
		return actionDelay >= 0;
	}

	public void setActionBarCompatible(boolean compatible) {
		if(compatible) {
			if(defActionDelay != -1)
				this.actionDelay = defActionDelay;
			else
				this.actionDelay = 0;
		} else
			this.actionDelay = -1;
	}

	public boolean broadcastsToConsole() {
		return broadcastsConsole;
	}

	public void setBroadcastsToConsole(boolean broadcastsConsole) {
		this.broadcastsConsole = broadcastsConsole;
	}

	@Override
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

	@Override
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

	@Override
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
