package es.xdec0de.usleep.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import es.xdec0de.usleep.api.SleepGroup;
import es.xdec0de.usleep.api.SleepMode;

public class NightSkipEvent extends PlayerEvent implements Cancellable {

	private final SleepGroup group;
	private final SleepMode mode;
	private boolean cancelled, skipEffect;

	private static final HandlerList HANDLERS = new HandlerList();

	public NightSkipEvent(SleepGroup group, SleepMode mode, Player player, boolean skipEffect) {
		super(player);
		this.group = group;
		this.mode = mode;
		this.skipEffect = skipEffect;
	}

	public SleepGroup getGroup() {
		return group;
	}

	public SleepMode getMode() {
		return mode;
	}

	public void setDoSkipEffect(boolean skipEffect) {
		this.skipEffect = skipEffect;
	}

	public boolean doesSkipEffect() {
		return skipEffect;
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
