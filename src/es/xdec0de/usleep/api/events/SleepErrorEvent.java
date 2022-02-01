package es.xdec0de.usleep.api.events;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult;

import es.xdec0de.usleep.utils.EnumUtils;
import es.xdec0de.usleep.utils.files.USPConfig;
import es.xdec0de.usleep.utils.files.USPMessage;
import es.xdec0de.usleep.utils.files.USPMessages;
import es.xdec0de.usleep.utils.files.USPSetting;

public class SleepErrorEvent extends Event {

	private final Player player;
	private final BedEnterResult result;
	private String message;
	private Sound sound = (Sound) EnumUtils.getEnum(Sound.class, USPConfig.getString(USPSetting.SOUND_SLEEP_ERROR));

	private static final HandlerList HANDLERS = new HandlerList();

	public SleepErrorEvent(Player player, BedEnterResult result) {
		this.player = player;
		this.result = result;
		this.message = USPMessages.getMessage((USPMessage) EnumUtils.ofOther(USPMessage.class, result));
	}

	public Player getPlayer() {
		return player;
	}

	public BedEnterResult getResult() {
		return result;
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
}
