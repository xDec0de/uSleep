package es.xdec0de.usleep.api.events;

import org.bukkit.World;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import es.xdec0de.usleep.api.SleepMode;

public class NightSkipEvent extends Event implements Cancellable {
	
	private final World world;
	private final SleepMode mode;
	private boolean cancelled;

	private static final HandlerList HANDLERS = new HandlerList();

	public NightSkipEvent(World world, SleepMode mode) {
		this.world = world;
		this.mode = mode;
	}

	public World getWorld() {
		return world;
	}

	public SleepMode getMode() {
		return mode;
	}

	public HandlerList getHandlers() {
		return HANDLERS;
	}
	
	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}
