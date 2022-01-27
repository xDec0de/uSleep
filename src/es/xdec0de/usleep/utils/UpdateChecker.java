package es.xdec0de.usleep.utils;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import es.xdec0de.usleep.USPMain;
import es.xdec0de.usleep.utils.files.Config;
import es.xdec0de.usleep.utils.files.Messages;
import net.md_5.bungee.api.ChatColor;

public class UpdateChecker implements Listener {

	private final static int resourceId = 72205;

	public static void getLatestVersion(Consumer<String> consumer) {
		Bukkit.getScheduler().runTaskAsynchronously(USPMain.instance, () -> {
			try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId).openStream(); Scanner scanner = new Scanner(inputStream)) {
				if (scanner.hasNext())
					consumer.accept(scanner.next());
			} catch (IOException ex) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&lu&9&lSleep&8: &8[&cWarning&8] &cAn error occurred while checking for updates&8:&6 " + ex.getMessage()));
			}
		});
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if(Config.getBoolean(Setting.UPDATER_ENABLED) && Config.getBoolean(Setting.UPDATER_MESSAGE_PLAYER)) {
			if(e.getPlayer().hasPermission(Config.getString(Setting.UPDATER_MESSAGE_PERMISSION))) {
				getLatestVersion(version -> {
					if(!USPMain.plugin.getDescription().getVersion().equalsIgnoreCase(version)) {
						e.getPlayer().sendMessage(Messages.getMessage(Message.UPDATE_AVAILABLE_PLAYER).replaceAll("%current%", USPMain.plugin.getDescription().getVersion()).replaceAll("%ver%", version));
					}
				});
			}
		}
	}
}