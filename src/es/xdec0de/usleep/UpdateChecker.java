package es.xdec0de.usleep;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.xdec0de.mcutils.files.yaml.PluginFile;

public class UpdateChecker implements Listener {

	private final USleep uSleep;

	public UpdateChecker(USleep plugin) {
		this.uSleep = plugin;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		final PluginFile cfg = uSleep.getConfig();
		final Player target = e.getPlayer();
		if (!cfg.getBoolean("features.updater.players") || !target.hasPermission(cfg.getString("permissions.updater.notify")))
			return;
		final String latest = uSleep.getLatestVersion(72205);
		if (uSleep.getAPI().isHigher(latest))
			uSleep.getMessages().send(target, "updater.available.player", "%current%", uSleep.getDescription().getVersion(), "%latest%", latest);
		else
			uSleep.getMessages().send(target, "updater.latest.player", "%current%", latest);
	}
}
