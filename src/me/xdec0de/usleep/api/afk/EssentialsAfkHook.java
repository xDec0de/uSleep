package me.xdec0de.usleep.api.afk;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.earth2me.essentials.Essentials;

import net.codersky.mcutils.java.MCLists;

public class EssentialsAfkHook implements AfkHook {

	private final Plugin essentials = Bukkit.getPluginManager().getPlugin("Essentials");

	@Override
	public boolean isAfk(Player player) {
		if (essentials == null)
			return false;
		return ((Essentials) essentials).getUser(player).isAfk();
	}

	@Override
	public List<Player> getAfk(List<Player> players) {
		if (essentials == null)
			return players;
		final Essentials essentials = (Essentials) this.essentials;
		return MCLists.filter(p -> essentials.getUser(p).isAfk(), players);
	}

}
