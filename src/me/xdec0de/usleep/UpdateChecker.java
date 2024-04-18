package me.xdec0de.usleep;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.codersky.mcutils.files.yaml.PluginFile;
import net.codersky.mcutils.updaters.sources.SpigotUpdaterSource;
import net.codersky.mcutils.updaters.sources.VersionInfo;

public class UpdateChecker implements Listener {

	private final USleep uSleep;

	UpdateChecker(USleep plugin) {
		this.uSleep = plugin;
	}

	void checkUpdates(CommandSender target) {
		Bukkit.getScheduler().runTaskAsynchronously(uSleep, () -> {
			final PluginFile cfg = uSleep.getConfig();
			final String path = target instanceof Player ? "player" : "console";
			if (!cfg.getBoolean("features.updater." + path) || !target.hasPermission(cfg.getString("permissions.updater.notify", "")))
				return;
			final String current = uSleep.getDescription().getVersion();
			final VersionInfo latest = new SpigotUpdaterSource(72205).getLatestVersion();
			if (latest == null)
				uSleep.getMessages().send(target, "updater.error." + path);
			else if (uSleep.getAPI().isNewerVersion(latest.getVersion()))
				uSleep.getMessages().send(target, "updater.available." + path, "%current%", current, "%latest%", latest.getVersion(), "%link%", latest.getVersionUrl());
			else
				uSleep.getMessages().send(target, "updater.latest." + path, "%current%", current);
		});
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		checkUpdates(e.getPlayer());
	}
}
