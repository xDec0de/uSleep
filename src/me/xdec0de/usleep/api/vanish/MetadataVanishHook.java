package me.xdec0de.usleep.api.vanish;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.PluginManager;

import net.codersky.mcutils.java.MCLists;

public class MetadataVanishHook implements VanishHook {

	private final boolean enabled;

	public MetadataVanishHook() {
		final PluginManager manager = Bukkit.getPluginManager();
		enabled = manager.getPlugin("SuperVanish") != null || manager.getPlugin("PremiumVanish") != null;
	}

	private boolean check(Player player) {
		for (MetadataValue meta : player.getMetadata("vanished"))
			if (meta.asBoolean())
				return true;
		return false;
	}

	@Override
	public boolean isVanished(Player player) {
		return enabled ? check(player) : false;
	}

	@Override
	public List<Player> getVanished(List<Player> players) {
		return enabled ? MCLists.filter(p -> check(p), players) : players;
	}
}
