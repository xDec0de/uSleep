package es.xdec0de.usleep.api.events;

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
 * @since v2.0.0
 * 
 * @author xDec0de_
 */
public class BedTeleportTryEvent extends PlayerEvent implements Cancellable {

	private final OfflinePlayer target;
	private Location bedSpawn;
	private boolean cancelled = false;
	private final BedTeleportResult result;

	private static final HandlerList HANDLERS = new HandlerList();

	public BedTeleportTryEvent(Player player, OfflinePlayer target, Location bedSpawn) {
		super(player);
		this.target = target;
		this.bedSpawn = bedSpawn;
		this.result = bedSpawn != null ? BedTeleportResult.SUCCESS : BedTeleportResult.NULL_LOCATION;
	}

	/**
	 * Gets the spawn {@link Location} of the bed the player
	 * will be teleported to.
	 * 
	 * @return the spawn location of the bed the player will
	 * be teleported to. Can be null.
	 * 
	 * @since v2.0.0
	 */
	public Location getBedSpawnLocation() {
		return bedSpawn;
	}

	/**
	 * Sets the {@link Location} the player will be
	 * teleported to, setting this to null will have the
	 * exact same effect as canceling the event but it isn't
	 * <b>intended</b> to be used that way.
	 * 
	 * @param bedSpawn the new location the player will
	 * be teleported to.
	 * 
	 * @since v2.0.0
	 */
	public void setBedSpawnLocation(Location bedSpawn) {
		this.bedSpawn = bedSpawn;
	}

	/**
	 * Gets the {@link BedTeleportResult} of the teleport.
	 * 
	 * @return the result of the teleport.
	 * 
	 * @since v2.0.0
	 */
	public BedTeleportResult getResult() {
		return result;
	}

	/**
	 * Gets the owner of the bed. Can be
	 * null if {@link #getPlayer()} specified
	 * a player that never joined the server.
	 * 
	 * @return the owner of the bed.
	 */
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

	/**
	 * Represents the result of a {@link BedTeleportTryEvent}
	 * at the time of it's execution, as setting
	 * {@link BedTeleportTryEvent#getBedSpawnLocation()} to
	 * other value might change the real result but won't update
	 * {@link BedTeleportTryEvent#getResult()}'s value.
	 * This is made on purpose to keep track of the original result of the event
	 * as the actual final result can be obtained by null checking the bed location.
	 * 
	 * @author xDec0de_
	 *
	 * @since v2.0.0
	 */
	public enum BedTeleportResult {
		/**
		 * The player has successfully teleported to the bed
		 * or at least it was intended to do so at the execution
		 * of the event, as setting {@link BedTeleportTryEvent#getBedSpawnLocation()}
		 * to null will cause the teleport to fail but won't update the result value.
		 * This is made on purpose to keep track of the original result of the event
		 * as the actual final result can be obtained by null checking the bed location.
		 */
		SUCCESS,
		/**
		 * The player has failed to teleport to the bed as it's location was null
		 * or at least it was intended to do so at the execution of the event, as setting
		 * {@link BedTeleportTryEvent#getBedSpawnLocation()} a valid location will
		 * cause the teleport to execute correctly but won't update the result value.
		 * This is made on purpose to keep track of the original result of the event
		 * as the actual final result can be obtained by null checking the bed location.
		 */
		NULL_LOCATION;
	}
}
