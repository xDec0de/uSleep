package es.xdec0de.usleep.api;

/**
 * Represents the mode used to sleep.
 * 
 * @since uSleep 2.0.0
 * 
 * @author xDec0de_
 */
public enum SleepMode {

	/**
	 * Instant sleep, when used, a {@link SleepGroup} will
	 * skip it's night ignoring the amount of players sleeping.
	 * 
	 * @since uSleep 2.0.0
	 */
	INSTANT,

	/**
	 * Percent sleep, when used, a {@link SleepGroup} will
	 * increment it's player sleeping amount until the required
	 * amount is reached, when reached, it will skip it's night.
	 * 
	 * @since uSleep 2.0.0
	 * 
	 * @see SleepGroup#getPlayersSleeping()
	 * @see SleepGroup#getRequiredPlayers()
	 */
	PERCENT;
}
