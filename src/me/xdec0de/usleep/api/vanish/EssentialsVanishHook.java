package me.xdec0de.usleep.api.vanish;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.earth2me.essentials.Essentials;

import net.codersky.mcutils.java.MCLists;

public class EssentialsVanishHook implements VanishHook {

	private final Plugin essentials = Bukkit.getPluginManager().getPlugin("Essentials");

	@Override
	public boolean isVanished(Player player) {
		if (essentials == null)
			return false;
		return ((Essentials) essentials).getUser(player).isVanished();
	}

	@Override
	public List<Player> getVanished(List<Player> players) {
		if (essentials == null)
			return players;
		final Essentials essentials = (Essentials) this.essentials;
		return MCLists.filter(p -> essentials.getUser(p).isVanished(), players);
	}

}
