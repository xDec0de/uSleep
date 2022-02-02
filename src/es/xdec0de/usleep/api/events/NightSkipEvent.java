package es.xdec0de.usleep.api.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import es.xdec0de.usleep.api.SleepGroup;
import es.xdec0de.usleep.api.SleepMode;

public class NightSkipEvent extends Event implements Cancellable {

	private final SleepGroup group;
	private final SleepMode mode;
	private boolean cancelled;

	private static final HandlerList HANDLERS = new HandlerList();

	public NightSkipEvent(SleepGroup group, SleepMode mode) {
		this.group = group;
		this.mode = mode;
	}

	public SleepGroup getGroup() {
		return group;
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
