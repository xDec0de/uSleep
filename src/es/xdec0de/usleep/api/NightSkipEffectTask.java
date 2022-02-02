package es.xdec0de.usleep.api;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.Validate;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * A class used to apply the NightSkipEffect as a task to a world
 * 
 * @author xDec0de_
 * 
 * @since uSleep v2.0.0
 */
public class NightSkipEffectTask extends BukkitRunnable {

	private final List<World> worlds;
	private final int increase;

	/**
	 * Creates a NightSkippEffect task
	 * 
	 * @param group The sleep group affected by this task.
	 * @param increment The amount of time incremented on each execution.
	 * 
	 * @throws IllegalArgumentException If group is null or increment is <= 0.
	 * 
	 * @since uSleep v2.0.0
	 */
	NightSkipEffectTask(SleepGroup group, int increment) {
		Validate.notNull(group, "World cannot be null");
		if(increment <= 0)
			throw new IllegalArgumentException("Increment must be higher than zero.");
		this.increase = increment;
		this.worlds = group.getWorlds().stream().filter(w -> w.getEnvironment().equals(Environment.NORMAL) || w.getEnvironment().equals(Environment.CUSTOM)).collect(Collectors.toList());
	}

	NightSkipEffectTask(World world, int increment) {
		Validate.notNull(world, "World cannot be null");
		if(increment <= 0)
			throw new IllegalArgumentException("Increment must be higher than zero.");
		this.increase = increment;
		this.worlds = new ArrayList<World>();
		this.worlds.add(world);
	}

	@Override
	public void run() {
		for(World world : worlds) {
			if(world.getTime() <= increase)
				cancel();
			world.setTime(world.getTime() + increase);
		}
	}
}
