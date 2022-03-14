package es.xdec0de.usleep.api.events;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import es.xdec0de.usleep.utils.EnumUtils;
import es.xdec0de.usleep.utils.files.USPMessage;
import es.xdec0de.usleep.utils.files.USPSetting;

public class SleepErrorEvent extends PlayerEvent {

	private final SleepErrorReason reason;
	private String message;
	private Sound sound = (Sound) EnumUtils.getEnum(Sound.class, USPSetting.SOUND_SLEEP_ERROR.asString());

	private static final HandlerList HANDLERS = new HandlerList();

	public SleepErrorEvent(Player player, SleepErrorReason result) {
		super(player);
		this.reason = result;
		USPMessage msg = ((USPMessage) EnumUtils.ofOther(USPMessage.class, result));
		this.message = msg != null ? msg.getString() : null;
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
}
