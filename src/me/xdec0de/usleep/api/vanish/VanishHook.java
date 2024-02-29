package me.xdec0de.usleep.api.vanish;

import java.util.List;

import org.bukkit.entity.Player;

public interface VanishHook {

	public boolean isVanished(Player player);

	public List<Player> getVanished(List<Player> players);

}
