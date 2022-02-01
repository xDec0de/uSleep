package es.xdec0de.usleep.api;

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

	private final World world;
	private final int increase;

	/**
	 * Creates a NightSkippEffect task
	 * 
	 * @param world The world affected by this task.
	 * @param increment The amount of time incremented on each execution.
	 * 
	 * @throws IllegalArgumentException If world is null, increment is <= 0 or if {@link World#getEnvironment()} is equal to {@link Environment#NETHER} or {@link Environment#THE_END}.
	 * 
	 * @since uSleep v2.0.0
	 */
	NightSkipEffectTask(World world, int increment) {
		Validate.notNull(world, "World cannot be null");
		if(increment <= 0)
			throw new IllegalArgumentException("Increment must be higher than zero.");
		Environment env = world.getEnvironment();
		if(env.equals(Environment.NETHER) || env.equals(Environment.THE_END))
			throw new IllegalArgumentException("Worlds of environment "+env.toString()+ " are not supported for NightSkipEffect tasks");
		
		this.world = world;
		this.increase = increment;
	}

	@Override
	public void run() {
		if(world.getTime() <= increase)
			cancel();
		world.setTime(world.getTime() + increase);
	}
}
