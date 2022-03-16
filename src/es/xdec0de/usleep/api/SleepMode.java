package es.xdec0de.usleep.api;

import es.xdec0de.usleep.utils.files.USPSetting;

/**
 * Represents the mode used to sleep.
 * 
 * @since v2.0.0
 * 
 * @author xDec0de_
 */
public enum SleepMode {
	/**
	 * Instant sleep, when used, a {@link SleepGroup} will
	 * skip it's night ignoring the amount of players sleeping.
	 * 
	 * @see USPSetting#INSTANT_SLEEP_ENABLED
	 */
	INSTANT,
	/**
	 * Percent sleep, when used, a {@link SleepGroup} will
	 * increment it's player sleeping amount until the required
	 * amount is reached, when reached, it will skip it's night.
	 * 
	 * @see USPSetting#PERCENT_SLEEP_ENABLED
	 * @see SleepGroup#getPlayersSleeping()
	 * @see SleepGroup#getRequiredPlayers()
	 */
	PERCENT;
}
