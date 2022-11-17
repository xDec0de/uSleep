package es.xdec0de.usleep.utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import es.xdec0de.usleep.api.USleep;
import me.xdec0de.mcutils.files.PluginFile;

public class UpdateChecker implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		final USleep plugin = JavaPlugin.getPlugin(USleep.class);
		final PluginFile cfg = plugin.getConfig();
		final Player target = e.getPlayer();
		if (cfg.getBoolean("Features.Updater.Players") && target.hasPermission(cfg.getString("Permissions.Updater.Notify"))) {
			final String current = plugin.getDescription().getVersion();
			plugin.getLatestVersion(72205, version -> {
				final String path = plugin.getAPI().isLatest(version) ? "Events.Updater.Latest.Player" : "Events.Updater.Available.Player";
				plugin.getMessages().sendColored(target, path, "%new%", version, "%current%", current);
			});
		}
	}
}
