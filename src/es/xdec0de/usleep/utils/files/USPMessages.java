package es.xdec0de.usleep.utils.files;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import es.xdec0de.usleep.USleep;
import es.xdec0de.usleep.utils.Replacer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class USPMessages {

	private static USleep plugin = USleep.getPlugin(USleep.class);

	private static FileConfiguration cfg;
	private static File file;

	private final static Pattern HEX_PATTERN = Pattern.compile("#([A-Fa-f0-9]{6})");

	static String prefix = "&7&l<&b&lu&9&lSleep&7&l>&7";
	static String errorPrefix = "&8&l[&4&l!&8&l]&c";

	public static void setup(boolean isByReload) {
		if (!plugin.getDataFolder().exists())
			plugin.getDataFolder().mkdir(); 
		if (!(file = new File(plugin.getDataFolder(), "messages.yml")).exists())
			plugin.saveResource("messages.yml", false); 
		reload(true, isByReload);
	}

	private static void reload(boolean update, boolean isByReload) {
		cfg = (FileConfiguration)YamlConfiguration.loadConfiguration(file);
		if(update && FileUtils.updateFile(file, "messages.yml", isByReload))
			reload(false, isByReload);
		prefix = cfg.getString(USPMessage.PREFIX.getPath());
		errorPrefix = cfg.getString(USPMessage.ERROR_PREFIX.getPath());
	}

	/**
	 * Applies color to the specified string, with hexadecimal color codes support.
	 * 
	 * @param string The string to color
	 * 
	 * @return The string, colored
	 */
	public static String applyColor(String string) {
		if(string == null)
			return "null";
		char COLOR_CHAR = '\u00A7';
		Matcher matcher = HEX_PATTERN.matcher(string);
		StringBuffer buffer = new StringBuffer(string.length() + 4 * 8);
		while (matcher.find()) {
			String group = matcher.group(1);
			matcher.appendReplacement(buffer, COLOR_CHAR + "x"
					+ COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
					+ COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
					+ COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5));
		}
		return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
	}

	// Loggers //

	/**
	 * Sends the specified string to the console, if the string is null, "null" will be sent, if the string is empty, 
	 * nothing will be done.
	 * 
	 * @param str The string to send
	 */
	public static void log(String str) {
		if(str == null)
			str = "null";
		if(!str.isEmpty())
			Bukkit.getConsoleSender().sendMessage(str);
	}

	/**
	 * Applies color ({@link #applyColor(String)}) to the specified string and then sends it to the console, if the string is null, "null" will be sent, 
	 * if the string is empty, nothing will be done.
	 * 
	 * @param str The string to send
	 */
	public static void logCol(String str) {
		if(!str.isEmpty())
			Bukkit.getConsoleSender().sendMessage(applyColor(str));
		// It's not necessary to null check as applyColor does it.
	}

	/**
	 * Applies color ({@link #applyColor(String)}) and the default {@link Replacer} to the specified string and then sends it to the console, 
	 * if the string is null, "null" will be sent, if the string is empty, nothing will be done.
	 * 
	 * @param str The string to send
	 */
	public static void logColRep(String str) {
		if(!str.isEmpty())
			Bukkit.getConsoleSender().sendMessage(applyColor(new Replacer("%prefix%", prefix, "%error%", errorPrefix).replaceAt(str)));
		// It's not necessary to null check as applyColor does it.
	}

	/**
	 * Applies color ({@link #applyColor(String)}) and replacements ({@link Replacer}) to the specified string and then sends it to the console, 
	 * if the string is null, "null" will be sent, if the string is empty, nothing will be done.
	 * 
	 * @param str The string to send ({@link #applyColor(String)})
	 * @replacements The replacements to apply to the string ({@link Replacer})
	 */
	public static void logCol(String str, String... replacements) {
		if(!str.isEmpty())
			Bukkit.getConsoleSender().sendMessage(applyColor(new Replacer("%prefix%", prefix, "%error%", errorPrefix).add(replacements).replaceAt(str)));
		// It's not necessary to null check as applyColor does it.
	}

	// Broadcast //

	/**
	 * Broadcasts a message to all players online and the console, uses {@link #getMessage(USPMessage)}
	 * 
	 * @param msg The message to be broadcasted.
	 */
	public static void broadcast(USPMessage msg) {
		String str = getMessage(msg);
		Bukkit.getOnlinePlayers().forEach(on -> on.sendMessage(str));
		Bukkit.getConsoleSender().sendMessage(str);
	}

	/**
	 * Broadcasts a message to all players online and the console, uses {@link #getMessage(USPMessage, Replacer)}
	 * 
	 * @param msg The message to be broadcasted.
	 * @param replacer The replacer to apply.
	 */
	public static void broadcast(USPMessage msg, Replacer replacer) {
		String str = getMessage(msg, replacer);
		Bukkit.getOnlinePlayers().forEach(on -> on.sendMessage(str));
		Bukkit.getConsoleSender().sendMessage(str);
	}

	/**
	 * Broadcasts a message to all players online and the console, uses {@link #getMessage(USPMessage, String...)}
	 * 
	 * @param msg The message to be broadcasted.
	 * @param replacements The replacements to apply.
	 */
	public static void broadcast(USPMessage msg, String... replacements) {
		String str = getMessage(msg, replacements);
		Bukkit.getOnlinePlayers().forEach(on -> on.sendMessage(str));
		Bukkit.getConsoleSender().sendMessage(str);
	}

	/**
	 * Broadcasts a message to all players online, uses {@link #getMessage(USPMessage)}
	 * 
	 * @param msg The message to be broadcasted.
	 */
	public static void broadcastActionbar(USPMessage msg) {
		String str = getMessage(msg);
		Bukkit.getOnlinePlayers().forEach(on -> on.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(str)));
	}

	/**
	 * Broadcasts a message to all players online, uses {@link #getMessage(USPMessage, Replacer)}
	 * 
	 * @param msg The message to be broadcasted.
	 * @param replacer The replacer to apply.
	 */
	public static void broadcastActionbar(USPMessage msg, Replacer replacer) {
		String str = getMessage(msg, replacer);
		Bukkit.getOnlinePlayers().forEach(on -> on.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(str)));
	}

	/**
	 * Broadcasts a message as an actionbar to all players online, uses {@link #getMessage(USPMessage, String...)}
	 * 
	 * @param msg The message to be broadcasted.
	 * @param replacements The replacements to apply.
	 */
	public static void broadcastActionbar(USPMessage msg, String... replacements) {
		String str = getMessage(msg, replacements);
		Bukkit.getOnlinePlayers().forEach(on -> on.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(str)));
	}

	// Message getters //

	/**
	 * Gets a message with colors {@link #applyColor(String)} and the default {@link Replacer}.
	 * 
	 * @param msg The message to get.
	 * 
	 * @return The message as a string, with colors and the default replacer applied to it.
	 */
	public static String getMessage(USPMessage msg) {
		return applyColor(new Replacer("%prefix%", prefix, "%error%", errorPrefix).replaceAt(cfg.getString(msg.getPath())));
	}

	/**
	 * Gets a message with colors {@link #applyColor(String)} and the default {@link Replacer}, also, the specified replacer is added to the default replacer.
	 * 
	 * @param msg The message to get.
	 * @param replacer The replacer to apply.
	 * 
	 * @return The message as a string, with colors and replacer applied to it.
	 */
	public static String getMessage(USPMessage msg, Replacer replacer) {
		return applyColor(new Replacer("%prefix%", prefix, "%error%", errorPrefix).add(replacer).replaceAt(cfg.getString(msg.getPath())));
	}

	/**
	 * Gets a message with colors {@link #applyColor(String)} and the default {@link Replacer}, also, a new replacer made with the 
	 * specified strings is added to the default replacer.
	 * 
	 * @param msg The message to get.
	 * @param replacements The replacements to apply.
	 * 
	 * @return The message as a string, with colors and replacer applied to it.
	 */
	public static String getMessage(USPMessage msg, String... replacements) {
		return applyColor(new Replacer("%prefix%", prefix, "%error%", errorPrefix).add(replacements).replaceAt(cfg.getString(msg.getPath())));
	}

	// Message senders //

	/**
	 * Sends a message with colors {@link #applyColor(String)} and the default {@link Replacer}, empty messages will be ignored and the message wont be sent.
	 * 
	 * @param sender The sender that will receive the message.
	 * @param msg The message to get.
	 * 
	 * @see #getMessage(USPMessage)
	 */
	public static void sendMessage(CommandSender sender, USPMessage msg) {
		String send = getMessage(msg);
		if(!send.isEmpty())
			sender.sendMessage(send);
	}

	/**
	 * Sends a message with colors {@link #applyColor(String)} and the default {@link Replacer}, also, 
	 * the specified replacer is added to the default replacer, empty messages will be ignored and the message wont be sent.
	 * 
	 * @param sender The sender that will receive the message.
	 * @param msg The message to get.
	 * @param replacer The replacer to apply.
	 * 
	 * @see #getMessage(USPMessage, Replacer)
	 */
	public static void sendMessage(CommandSender sender, USPMessage msg, Replacer replacer) {
		String send = getMessage(msg, replacer);
		if(!send.isEmpty())
			sender.sendMessage(send);
	}

	/**
	 * Sends a message with colors {@link #applyColor(String)} and the default {@link Replacer}, also, a new replacer made with 
	 * the specified strings is added to the default replacer, empty messages will be ignored and the message wont be sent.
	 * 
	 * @param sender The sender that will receive the message.
	 * @param msg The message to get.
	 * @param replacements The replacements to apply.
	 * 
	 * @see #getMessage(USPMessage, String...)
	 */
	public static void sendMessage(CommandSender sender, USPMessage msg, String... replacements) {
		String send = getMessage(msg, replacements);
		if(!send.isEmpty())
			sender.sendMessage(send);
	}

	/**
	 * Sends a message with colors {@link #applyColor(String)} and the default {@link Replacer}, empty messages will be ignored and the message wont be sent.
	 * 
	 * @param player The player that will receive the message.
	 * @param msg The message to get.
	 * 
	 * @see #getMessage(USPMessage)
	 */
	public static void sendMessage(Player player, USPMessage msg) {
		String send = getMessage(msg);
		if(!send.isEmpty())
			player.sendMessage(send);
	}

	/**
	 * Sends a message with colors {@link #applyColor(String)} and the default {@link Replacer}, also, 
	 * the specified replacer is added to the default replacer, empty messages will be ignored and the message wont be sent.
	 * 
	 * @param player The player that will receive the message.
	 * @param msg The message to get.
	 * @param replacer The replacer to apply.
	 * 
	 * @see #getMessage(USPMessage, Replacer)
	 */
	public static void sendMessage(Player player, USPMessage msg, Replacer replacer) {
		String send = getMessage(msg, replacer);
		if(!send.isEmpty())
			player.sendMessage(send);
	}

	/**
	 * Sends a message with colors {@link #applyColor(String)} and the default {@link Replacer}, also, a new replacer made with 
	 * the specified strings is added to the default replacer, empty messages will be ignored and the message wont be sent.
	 * 
	 * @param player The player that will receive the message.
	 * @param msg The message to get.
	 * @param replacements The replacements to apply.
	 * 
	 * @see #getMessage(USPMessage, String...)
	 */
	public static void sendMessage(Player player, USPMessage msg, String... replacements) {
		String send = getMessage(msg, replacements);
		if(!send.isEmpty())
			player.sendMessage(send);
	}

	// Actionbar senders //

	/**
	 * Sends an actionbar with colors {@link #applyColor(String)} and the default {@link Replacer}, empty messages will be ignored and the message wont be sent.
	 * 
	 * @param player The player that will receive the message.
	 * @param msg The message to get.
	 * 
	 * @see #getMessage(USPMessage)
	 */
	public static void sendActionbar(Player player, USPMessage msg) {
		String send = getMessage(msg);
		if(!send.isEmpty())
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(send));
	}

	/**
	 * Sends an actionbar with colors {@link #applyColor(String)} and the default {@link Replacer}, also, 
	 * the specified replacer is added to the default replacer, empty messages will be ignored and the message wont be sent.
	 * 
	 * @param player The player that will receive the message.
	 * @param msg The message to get.
	 * @param replacer The replacer to apply.
	 * 
	 * @see #getMessage(USPMessage, Replacer)
	 */
	public static void sendActionbar(Player player, USPMessage msg, Replacer replacer) {
		String send = getMessage(msg, replacer);
		if(!send.isEmpty())
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(send));
	}

	/**
	 * Sends an actionbar with colors {@link #applyColor(String)} and the default {@link Replacer}, also, a new replacer made with 
	 * the specified strings is added to the default replacer, empty messages will be ignored and the message wont be sent.
	 * 
	 * @param player The player that will receive the message.
	 * @param msg The message to get.
	 * @param replacements The replacements to apply.
	 * 
	 * @see #getMessage(USPMessage, String...)
	 */
	public static void sendActionbar(Player player, USPMessage msg, String... replacements) {
		String send = getMessage(msg, replacements);
		if(!send.isEmpty())
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(send));
	}
}
