package es.xdec0de.usleep.utils.files;

import java.util.List;

public enum USPSetting {

	// Features //

	UPDATER_NOTIFY_CONSOLE("Features.Updater.Console"),
	UPDATER_NOTIFY_PLAYERS("Features.Updater.Players"),

	INSTANT_SLEEP_ENABLED("Features.InstantSleep.Enabled"),

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

	/**
	 * Gets the corresponding <b>config.yml</b>'s path of the setting.
	 * 
	 * @return The path to the setting.
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Gets the setting as a string.
	 * 
	 * @return The setting as a string or an empty string if {@link #getPath()} is invalid or the setting is not a String.
	 */
	public String asString() {
		return USPConfig.get().getString(path, "");
	}

	/**
	 * Gets the setting as a string list.
	 * 
	 * @return The setting as a string list or an empty string list if {@link #getPath()} is invalid.
	 */
	public List<String> asStringList() {
		return USPConfig.get().getStringList(path);
	}

	/**
	 * Gets the setting as an integer.
	 * 
	 * @return The setting as an integer or 0 if {@link #getPath()} is invalid or the setting is not a number.
	 */
	public int asInt() {
		return USPConfig.get().getInt(path, 0); // Yeah, I know default is already 0.
	}

	/**
	 * Gets the setting as a boolean.
	 * 
	 * @return The setting as a boolean or <b>false</b> if {@link #getPath()} is invalid or the setting is not a boolean.
	 */
	public boolean asBoolean() {
		return USPConfig.get().getBoolean(path, false); // Same
	}

	/**
	 * Sets {@link #getPath()} to the specified value, without saving, if value is null, the entry will be removed.
	 * 
	 * @param value New value to set the path to.
	 */
	public void set(Object value) {
		USPConfig.get().set(path, value);
	}

	/**
	 * Sets {@link #getPath()} to the specified value, then, saves and reloads <b>config.yml</b>, if value is null, the entry will be removed.
	 * 
	 * @param value New value to set the path to.
	 */
	public void setReload(Object value) {
		USPConfig.get().set(path, value);
		USPConfig.save();
		USPConfig.reload(false, false);
	}
}
