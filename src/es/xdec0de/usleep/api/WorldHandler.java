package es.xdec0de.usleep.api;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

import es.xdec0de.usleep.utils.files.USPWorlds;

class WorldHandler implements Listener {

	private static WorldHandler instance;

	/*
	 *  I decided to go with an ArrayList instead of a HashMap id/SleepGroup
	 *  because, being realistic, it's rare for a server to use more than
	 *  3 sleep groups (If any), so looping through them isn't really an issue and
	 *  I prefer saving up some ram even if it's only a few bytes of strings.
	 */
	final ArrayList<SleepGroup> sleepGroups = new ArrayList<SleepGroup>();

	private WorldHandler() {
		if(instance != null)
			throw new SecurityException("Creating new instances of WorldHandler is not allowed!");
		for(String groupID : USPWorlds.getGroupIdentifiers()) {
			ArrayList<World> worlds = new ArrayList<World>();
			for(String worldName : USPWorlds.getWorldsInGroup(groupID)) {
				World world = Bukkit.getWorld(worldName);
				if(world != null)
					worlds.add(world);
			}
			sleepGroups.add(new SleepGroup(groupID, worlds));
		}
	}

	static WorldHandler getInstance() {
		return instance != null ? instance : (instance = new WorldHandler());
	}

	@EventHandler
	public void onLoad(WorldLoadEvent e) {
		String id = USPWorlds.getSleepGroupID(e.getWorld());
		SleepGroup group = USleepAPI.getInstance().getSleepGroup(id);
		if(group != null)
			group.worlds.add(e.getWorld());
		else
			sleepGroups.add(group = new SleepGroup(id, Arrays.asList(e.getWorld())));
	}

	@EventHandler
	public void onUnload(WorldUnloadEvent e) {
		USleepAPI.getInstance().getSleepGroup(e.getWorld()).worlds.remove(e.getWorld());
	}
}
