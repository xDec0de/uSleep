package es.xdec0de.usleep.utils;

public enum Setting {
	
	TITLES_ENABLD("Visuals.Titles"),
	ACTIONBAR_ENABLED("Visuals.UseActionBar"),
	
	PERCENT_SLEEP_ENABLED("Events.PercentSleep.Enabled"),
	PERCENT_SLEEP_PERM("Events.PercentSleep.Permission"),
	PERCENT_SLEEP_PERCENT("Events.PercentSleep.Percentage"),
	PERCENT_SLEEP_PREVENT_SPAM("Events.PercentSleep.PreventSpam"),
	PERCENT_SLEEP_PREVENT_SPAM_COOLDOWN("Events.PercentSleep.PreventSpamCooldown"),
	PERCENT_SLEEP_SOUNDS_ENABLED("Events.PercentSleep.Sounds.Enabled"),
	PERCENT_SLEEP_SOUND("Events.PercentSleep.Sounds.Sleep"),
	PERCENT_SLEEP_ERROR_SOUND("Events.PercentSleep.Sounds.Error"),
	PERCENT_SLEEP_LEAVE_SOUND("Events.PercentSleep.Sounds.Leave"),
	PERCENT_SLEEP_NEXT_DAY_SOUND("Events.PercentSleep.Sounds.NextDay"),
	
	INSTANT_SLEEP_ENABLED("Events.InstantSleep.Enabled"),
	INSTANT_SLEEP_PERM("Events.InstantSleep.Permission"),
	
	RELOAD_PERM("Commands.usleep.ReloadPerm"),
	BEDTP_PERM("Commands.Bed.UseSelfPerm"),
	BEDTP_OTHER_PERM("Commands.Bed.UseOtherPerm"),
	
	ESSENTIALS_IGNORE_AFK("Essentials.IgnoreAFK"),
	IGNORE_VANISHED("IgnoreVanished"),
	
	UPDATER_MESSAGE_PERMISSION("Updater.Message.Permission"),
	UPDATER_MESSAGE_PLAYER("Updater.Message.Players"),
	UPDATER_MESSAGE_CONSOLE("Updater.Message.Console"),
	UPDATER_ENABLED("Updater.Enabled");
	
	private String path;
	
	Setting(String string) {
		this.path = string;
	}
	
	public String getPath() {
		return path;
	}
}
