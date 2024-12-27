package me.xdec0de.usleep.api.events;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * Called whenever a player tries to teleport
 * to a bed using the <b>/bedtp</b> command.
 * 
 * @since uSleep 2.0.0
 * 
 * @author xDec0de_
 */
public class BedTeleportTryEvent extends PlayerEvent implements Cancellable {

	private final OfflinePlayer target;
	private Location bedSpawn;
	private boolean cancelled = false;

	private static final HandlerList HANDLERS = new HandlerList();

	public BedTeleportTryEvent(Player player, OfflinePlayer target, Location bedSpawn) {
		super(player);
		this.target = target;
		this.bedSpawn = bedSpawn;
	}

	/**
	 * Gets the spawn {@link Location} of the bed the player
	 * will be teleported to.
	 * 
	 * @return the spawn location of the bed the player will
	 * be teleported to. Can be null.
	 * 
	 * @since uSleep 2.0.0
	 */
	@Nullable
	public Location getBedSpawnLocation() {
		return bedSpawn;
	}

	/**
	 * Sets the {@link Location} the player will be
	 * teleported to, if the new {@link Location} is null,
	 * uSleep will send the no bed message to the sender
	 * on /bedtp.
	 * 
	 * @param bedSpawn the new location the player will
	 * be teleported to.
	 * 
	 * @since uSleep 2.0.0
	 */
	@Nonnull
	public BedTeleportTryEvent setBedSpawnLocation(@Nullable Location bedSpawn) {
		this.bedSpawn = bedSpawn;
		return this;
	}

	/**
	 * Gets the owner of the bed. Can be
	 * null if {@link #getPlayer()} specified
	 * a player that never joined the server.
	 * 
	 * @return the owner of the bed.
	 * 
	 * @since uSleep 2.0.0
	 */
	@Nullable
	public OfflinePlayer getTarget() {
		return target;
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
