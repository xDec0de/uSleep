package me.xdec0de.usleep.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import me.xdec0de.usleep.api.NightSkipEffectTask;
import me.xdec0de.usleep.api.SleepGroup;
import me.xdec0de.usleep.api.SleepMode;

/**
 * Called whenever a {@link SleepGroup} skips a night.
 * 
 * @since v2.0.0
 * 
 * @author xDec0de_
 */
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

	/**
	 * Gets the {@link SleepGroup} skipping the night
	 * 
	 * @return the SleepGroup skipping the night.
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

	/**
	 * Sets whether a {@link NightSkipEffectTask}
	 * should be applied to {@link #getGroup()} or not.
	 * 
	 * @param skipEffect the new state of the effect.
	 * 
	 * @since v2.0.0
	 */
	public void setDoSkipEffect(boolean skipEffect) {
		this.skipEffect = skipEffect;
	}

	/**
	 * Returns the {@link NightSkipEffectTask} toggle
	 * state on this event.
	 * 
	 * @return the status of the NightSkipEffect on this event.
	 * 
	 * @since v2.0.0
	 */
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
