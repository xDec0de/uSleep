package es.xdec0de.usleep.api.events;

import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult;
import org.bukkit.event.player.PlayerEvent;

import com.google.common.base.Enums;

import es.xdec0de.usleep.api.NightSkipEffectTask;
import es.xdec0de.usleep.api.SleepGroup;

/**
 * Called whenever a player fails to sleep.
 * 
 * @since v2.0.0
 * 
 * @author xDec0de_
 */
public class SleepErrorEvent extends PlayerEvent implements Cancellable {

	private final SleepErrorReason reason;
	private String message;
	private Sound sound = Enums.getIfPresent(Sound.class, USPSetting.SOUND_SLEEP_ERROR.asString()).orNull();
	private boolean cancelled;
	private final Block bed;

	private static final HandlerList HANDLERS = new HandlerList();

	public SleepErrorEvent(Player player, String message, SleepErrorReason reason, Block bed) {
		super(player);
		this.reason = reason;
		this.message = message;
		this.bed = bed;
	}

	/**
	 * Gets the <b>final</b> {@link SleepErrorReason} why the event got called.
	 * 
	 * @return the reason why the event has been called.
	 */
	public SleepErrorReason getError() {
		return reason;
	}

	/**
	 * Gets the message being sent to the player,
	 * null meaning that no message will be sent.
	 * 
	 * @return the message being sent to the player. Can be null.
	 * 
	 * @since v2.0.0
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message being sent to the player. If
	 * <b>message</b> is null, no message will be sent.
	 * 
	 * @param message the sound to play.
	 * 
	 * @since v2.0.0
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Sets the {@link Sound} being played to the player. If
	 * <b>sound</b> is null, no sound will be played.
	 * 
	 * @param sound the sound to play.
	 * 
	 * @since v2.0.0
	 */
	public void setSound(Sound sound) {
		this.sound = sound;
	}

	/**
	 * Gets the {@link Sound} played to the player. Same as getting
	 * {@link USPSetting#SOUND_SLEEP_ERROR} as a sound, null being no sound.
	 * 
	 * @return the sound played to the player. Can be null
	 * 
	 * @since v2.0.0
	 */
	public Sound getSound() {
		return sound;
	}

	/**
	 * Returns the bed {@link Block} involved in this event.
	 * 
	 * @return the bed block involved in this event.
	 * 
	 * @since v2.0.0
	 */
	public Block getBed() {
		return bed;
	}

	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

	/**
	 * Represents the reason why a {@link SleepErrorEvent} gets called.
	 * 
	 * @since v2.0.0
	 * 
	 * @author xDec0de_
	 */
	public enum SleepErrorReason {
		/**
		 * Replacement of {@link BedEnterResult#NOT_POSSIBLE_HERE}
		 * 
		 * @since v2.0.0
		 */
		NOT_POSSIBLE_HERE,
		/**
		 * Replacement of {@link BedEnterResult#NOT_POSSIBLE_NOW}
		 * 
		 * @since v2.0.0
		 */
		NOT_POSSIBLE_NOW,
		/**
		 * Replacement of {@link BedEnterResult#TOO_FAR_AWAY}
		 * 
		 * @since v2.0.0
		 */
		TOO_FAR_AWAY,
		/**
		 * Replacement of {@link BedEnterResult#NOT_SAFE}
		 * 
		 * @since v2.0.0
		 */
		NOT_SAFE,
		/**
		 * Replacement of {@link BedEnterResult#OTHER_PROBLEM}
		 * 
		 * @since v2.0.0
		 */
		OTHER_PROBLEM,
		/**
		 * Entering the bed is prevented due to {@link SleepErrorEvent#getPlayer()}
		 * not having enough permissions to both instant and percent sleep.
		 * 
		 * @since v2.0.0
		 */
		NO_PERMISSIONS,
		/**
		 * Entering the bed is prevented due to {@link SleepErrorEvent#getPlayer()}
		 * sleeping faster than allowed by the sleep cooldown.
		 * 
		 * @since v2.0.0
		 */
		TOO_FAST,
		/**
		 * Entering the bed is prevented due to a {@link NightSkipEffectTask}
		 * being executed on the {@link SleepGroup} the {@link World} of the bed is in.
		 * 
		 * @since v2.0.0
		 */
		ALREADY_SKIPPING;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}
}
