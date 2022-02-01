package es.xdec0de.usleep.api.events;

import org.bukkit.World;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NightSkipEvent extends Event implements Cancellable {
	
	private final World world;
	private boolean cancelled;

	private static final HandlerList HANDLERS = new HandlerList();

	public NightSkipEvent(World world) {
		this.world = world;
	}

	public World getWorld() {
		return world;
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
