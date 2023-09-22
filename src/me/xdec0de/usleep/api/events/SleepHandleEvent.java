package me.xdec0de.usleep.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import me.xdec0de.usleep.api.SleepGroup;
import me.xdec0de.usleep.api.SleepMode;

/**
 * Called whenever a {@link SleepGroup} handles sleeping.
 * 
 * @since v2.0.0
 * 
 * @author xDec0de_
 */
public class SleepHandleEvent extends PlayerEvent implements Cancellable {

	private final SleepGroup group;
	private final SleepMode mode;
	private boolean cancelled = false;

	private static final HandlerList HANDLERS = new HandlerList();

	public SleepHandleEvent(Player player, SleepGroup group, SleepMode mode) {
		super(player);
		this.group = group;
		this.mode = mode;
	}

	/**
	 * Gets the {@link SleepGroup} that is
	 * handling this sleep event.
	 * 
	 * @return the SleepGroup handling this
	 * sleep event.
	 * 
	 * @see SleepGroup#getPlayersSleeping()
	 * @see SleepGroup#getRequiredPlayers()
	 * 
	 * @since v2.0.0
	 */
	public SleepGroup getGroup() {
		return group;
	}

	/**
	 * Gets the {@link SleepMode} used to
	 * handle this sleep event.
	 * 
	 * @return the SleepMode used to handle this
	 * sleep event.
	 * 
	 * @since v2.0.0
	 */
	public SleepMode getMode() {
		return mode;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
}
