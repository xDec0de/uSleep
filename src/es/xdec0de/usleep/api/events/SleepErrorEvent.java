package es.xdec0de.usleep.api.events;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import es.xdec0de.usleep.utils.EnumUtils;
import es.xdec0de.usleep.utils.files.USPSetting;

public class SleepErrorEvent extends PlayerEvent implements Cancellable {

	private final SleepErrorReason reason;
	private String message;
	private Sound sound = (Sound) EnumUtils.getEnum(Sound.class, USPSetting.SOUND_SLEEP_ERROR.asString());
	private boolean cancelled;

	private static final HandlerList HANDLERS = new HandlerList();

	public SleepErrorEvent(Player player, String message, SleepErrorReason reason) {
		super(player);
		this.reason = reason;
		this.message = message;
	}

	public SleepErrorReason getError() {
		return reason;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setSound(Sound sound) {
		this.sound = sound;
	}

	public Sound getSound() {
		return sound;
	}

	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

	public enum SleepErrorReason {
		NOT_POSSIBLE_HERE,
		NOT_POSSIBLE_NOW,
		TOO_FAR_AWAY,
		NOT_SAFE,
		OTHER_PROBLEM,
		NO_PERMISSIONS,
		TOO_FAST,
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
