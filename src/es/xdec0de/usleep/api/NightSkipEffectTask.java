package es.xdec0de.usleep.api;

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
	private final SleepGroup group;

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
		this.group = group;
	}

	@Override
	public void run() {
		group.isNightSkipping = true;
		for(World world : worlds) {
			if(world.getTime() <= increase) {
				cancel();
				group.isNightSkipping = false;
			}
			world.setTime(world.getTime() + increase);
		}
	}
}
