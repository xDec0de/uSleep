package es.xdec0de.usleep.utils.files;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

	public static Replacer getDefaultReplacer() {
		return new Replacer("%prefix%", prefix, "%error%", errorPrefix);
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

	static FileConfiguration getFile() {
		return cfg;
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
			Bukkit.getConsoleSender().sendMessage(applyColor(getDefaultReplacer().replaceAt(str)));
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
			Bukkit.getConsoleSender().sendMessage(applyColor(getDefaultReplacer().add(replacements).replaceAt(str)));
		// It's not necessary to null check as applyColor does it.
	}

	/**
	 * Applies color ({@link #applyColor(String)}) and {@link #getDefaultReplacer()} to the specified string and then sends it a player, 
	 * if the string is null or empty, nothing will be done.
	 * 
	 * @param player The player that will receive the message.
	 * @param str The string to send.
	 */
	public static void sendMessage(Player player, String str) {
		if(str != null && !str.isEmpty())
			player.sendMessage(applyColor(getDefaultReplacer().replaceAt(str)));
	}

	/**
	 * Applies color ({@link #applyColor(String)}) and {@link #getDefaultReplacer()} to the specified string and then sends it a player 
	 * as an action bar, if the string is null or empty, nothing will be done.
	 * 
	 * @param player The player that will receive the message.
	 * @param str The string to send.
	 */
	public static void sendActionBar(Player player, String str) {
		if(str != null && !str.isEmpty())
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(applyColor(getDefaultReplacer().replaceAt(str))));
	}
}
