package es.xdec0de.usleep.utils.files;

public enum USPSetting implements USleepSetting {

	// Features //

	UPDATER_NOTIFY_CONSOLE("Features.Updater.Console"),
	UPDATER_NOTIFY_PLAYERS("Features.Updater.Players"),

	/**
	 * Used {@link #asBoolean()}, gets if instant sleep is enabled.
	 */
	INSTANT_SLEEP_ENABLED("Features.InstantSleep.Enabled"),

	/**
	 * Used {@link #asBoolean()}, gets if percent sleep is enabled.
	 */
	PERCENT_SLEEP_ENABLED("Features.PercentSleep.Enabled"),
	PERCENT_SLEEP_PERCENT("Features.PercentSleep.Percentage"),
	PERCENT_SLEEP_IGNORE_AFK("Features.PercentSleep.Ignored.AFK"),
	PERCENT_SLEEP_IGNORE_VANISHED("Features.PercentSleep.Ignored.Vanished"),
	PERCENT_SLEEP_COOLDOWN("Features.PercentSleep.Cooldown"),

	ACTIONBAR_ENABLED("Features.Actionbar"),

	NIGHT_SKIP_EFFECT_ENABLED("Features.NightSkipEffect.Enabled"),
	NIGHT_SKIP_EFFECT_INCREMENT("Features.NightSkipEffect.Increment"),

	// Permissions

	PERM_RELOAD("Permissions.Reload"),

	PERM_UPDATER_NOTIFY("Permissions.Updater.Notify"),
	PERM_UPDATER_CHECK("Permissions.Updater.Check"),

	PERM_BEDTP_SELF("Permissions.BedTP.Self"),
	PERM_BEDTP_OTHER("Permissions.BedTP.Other"),

	PERM_INSTANT_SLEEP("Permissions.Sleep.Instant"),
	PERM_PERCENT_SLEEP("Permissions.Sleep.Percent"),
	
	// Sounds //

	SOUND_SLEEP_OK("Sounds.Sleep.Ok"),
	SOUND_SLEEP_ERROR("Sounds.Sleep.Error"),
	SOUND_SLEEP_LEAVE("Sounds.Sleep.Leave"),

	SOUND_NEXTDAY_PERCENT("Sounds.NextDay.Percent"),
	SOUND_NEXTDAY_INSTANT("Sounds.NextDay.Instant");

	private final String path;

	USPSetting(String string) {
		this.path = string;
	}

	public String getPath() {
		return path;
	}
}
