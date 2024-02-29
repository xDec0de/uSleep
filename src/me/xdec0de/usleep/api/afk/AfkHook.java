package me.xdec0de.usleep.api.afk;

import java.util.List;

import org.bukkit.entity.Player;

public interface AfkHook {

	public boolean isAfk(Player player);

	public List<Player> getAfk(List<Player> players);

}
