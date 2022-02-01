package es.xdec0de.usleep.utils.files;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import es.xdec0de.usleep.USleep;
import es.xdec0de.usleep.utils.Replacer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

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
	private final byte actionDelay;
	private final boolean broadcastsConsole;

	USPMessage(String string, byte actionDelay, boolean broadcastsConsole) {
		this.path = string;
		this.actionDelay = actionDelay;
		this.broadcastsConsole = broadcastsConsole;
	}
	
	public String getPath() {
		return path;
	}

	public String getStringUncolored() {
		return USPMessages.getFile().getString(path);
	}

	public String getString() {
		String str = getStringUncolored();
		return (str != null && !str.isEmpty()) ? USPMessages.getDefaultReplacer().replaceAt(USPMessages.applyColor(getStringUncolored())) : null;
	}

	public String getString(Replacer replacer) {
		String str = getStringUncolored();
		return (str != null && !str.isEmpty()) ? USPMessages.getDefaultReplacer().add(replacer).replaceAt(USPMessages.applyColor(getStringUncolored())) : null;
	}

	public String getString(String... replacements) {
		String str = getStringUncolored();
		return (str != null && !str.isEmpty()) ? USPMessages.getDefaultReplacer().add(replacements).replaceAt(USPMessages.applyColor(getStringUncolored())) : null;
	}

	public void send(CommandSender sender) {
		String str = getString();
		if(str != null && !str.isEmpty()) {
			if(sender instanceof Player && actionDelay >= 0 && USPSetting.ACTIONBAR_ENABLED.asBoolean()) {
				if(actionDelay == 0)
					((Player)sender).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(str));
				else
					Bukkit.getScheduler().runTaskLater(USleep.getPlugin(USleep.class), () -> ((Player)sender).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(str)), actionDelay);
			} else
				sender.sendMessage(str);
		}
	}

	public void send(CommandSender sender, Replacer rep) {
		String str = getString(rep);
		if(str != null && !str.isEmpty()) {
			if(sender instanceof Player && actionDelay >= 0 && USPSetting.ACTIONBAR_ENABLED.asBoolean()) {
				if(actionDelay == 0)
					((Player)sender).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(str));
				else
					Bukkit.getScheduler().runTaskLater(USleep.getPlugin(USleep.class), () -> ((Player)sender).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(str)), actionDelay);
			} else
				sender.sendMessage(str);
		}
	}

	public void send(CommandSender sender, String... replacements) {
		String str = getString(replacements);
		if(str != null && !str.isEmpty()) {
			if(sender instanceof Player && actionDelay >= 0 && USPSetting.ACTIONBAR_ENABLED.asBoolean()) {
				if(actionDelay == 0)
					((Player)sender).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(str));
				else
					Bukkit.getScheduler().runTaskLater(USleep.getPlugin(USleep.class), () -> ((Player)sender).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(str)), actionDelay);
			} else
				sender.sendMessage(str);
		}
	}

	public void broadcast() {
		String str = getString();
		if(str != null && !str.isEmpty()) {
			if(actionDelay >= 0 && USPSetting.ACTIONBAR_ENABLED.asBoolean()) {
				if(actionDelay == 0)
					Bukkit.getOnlinePlayers().forEach(on -> on.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(str)));
				else
					Bukkit.getScheduler().runTaskLater(USleep.getPlugin(USleep.class), () -> Bukkit.getOnlinePlayers().forEach(on -> on.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(str))), actionDelay);
			} else
				Bukkit.getOnlinePlayers().forEach(on -> on.sendMessage(str));
			if(broadcastsConsole)
				Bukkit.getConsoleSender().sendMessage(str);
		}
	}

	public void broadcast(Replacer rep) {
		String str = getString(rep);
		if(str != null && !str.isEmpty()) {
			if(actionDelay >= 0 && USPSetting.ACTIONBAR_ENABLED.asBoolean()) {
				if(actionDelay == 0)
					Bukkit.getOnlinePlayers().forEach(on -> on.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(str)));
				else
					Bukkit.getScheduler().runTaskLater(USleep.getPlugin(USleep.class), () -> Bukkit.getOnlinePlayers().forEach(on -> on.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(str))), actionDelay);
			} else
				Bukkit.getOnlinePlayers().forEach(on -> on.sendMessage(str));
			if(broadcastsConsole)
				Bukkit.getConsoleSender().sendMessage(str);
		}
	}

	public void broadcast(String... replacements) {
		String str = getString();
		if(str != null && !str.isEmpty()) {
			if(actionDelay >= 0 && USPSetting.ACTIONBAR_ENABLED.asBoolean()) {
				if(actionDelay == 0)
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
