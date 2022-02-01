package es.xdec0de.usleep.api;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;

import es.xdec0de.usleep.utils.files.USPWorlds;

public class SleepGroup {

	List<World> worlds = new ArrayList<World>();
	private final String id;
	private int sleeping = 0;

	SleepGroup(String id) {
		this.id = id;
	}

	SleepGroup(List<World> worlds) {
		this("__usleep_def_sleep_group__");
		this.worlds = worlds;
	}

	List<String> build() {
		List<String> errors = new LinkedList<String>();
		for(String worldName : USPWorlds.getWorldsInGroup(id)) {
			World world = Bukkit.getWorld(worldName);
			if(world != null)
				worlds.add(Bukkit.getWorld(worldName));
			else
				errors.add(worldName);
		}
		return errors;
	}

	public String getID() {
		return id;
	}

	public List<World> getWorlds() {
		return worlds;
	}

	public boolean contains(World world) {
		return worlds.contains(world);
	}

	public int getPlayersSleeping() {
		return sleeping;
	}
}
