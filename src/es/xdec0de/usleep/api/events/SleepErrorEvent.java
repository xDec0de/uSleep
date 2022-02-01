package es.xdec0de.usleep.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult;

import es.xdec0de.usleep.utils.USPSetting;
import es.xdec0de.usleep.utils.files.USPConfig;

public class SleepErrorEvent extends Event {
	
	private final Player player;
	private final BedEnterResult result;
	private boolean sendMessage = true;
	private boolean playSound;

	private static final HandlerList HANDLERS = new HandlerList();

	public SleepErrorEvent(Player player, BedEnterResult result) {
		this.player = player;
		this.result = result;
		String soundStr = USPConfig.getString(USPSetting.SOUND_SLEEP_ERROR);
		this.playSound = soundStr != null && !soundStr.isEmpty();
	}

	public Player getPlayer() {
		return player;
	}

	public BedEnterResult getResult() {
		return result;
	}

	public boolean sendsMessage() {
		return sendMessage;
	}

	public void setSendsMessage(boolean sendMessage) {
		this.sendMessage = sendMessage;
	}

	public void setPlaysSound(boolean playSound) {
		this.playSound = playSound;
	}

	public boolean playsSound() {
		return playSound;
	}

	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
}
