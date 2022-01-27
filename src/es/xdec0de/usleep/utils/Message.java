package es.xdec0de.usleep.utils;

public enum Message {
	
	UPDATE_AVAILABLE_PLAYER("Events.Updater.Join"),
	
	PERCENT_NEXT_DAY("Events.PercentSleep.NextDay"),
	PERCENT_TOO_FAST("Events.PercentSleep.TooFast"),
	PERCENT_TITLE("Events.PercentSleep.Title"),
	PERCENT_SUBTITLE("Events.PercentSleep.Subtitle"),
	
	INSTANT_OK("Events.InstantSleep.OK"),
	PERCENT_OK("Events.PercentSleep.OK"),
	
	NOT_POSSIBLE_HERE("Events.Sleep.NOT_POSSIBLE_HERE"),
	NOT_POSSIBLE_NOW("Events.Sleep.NOT_POSSIBLE_NOW"),
	NOT_SAFE("Events.Sleep.NOT_SAFE"),
	OTHER_PROBLEM("Events.Sleep.OTHER_PROBLEM"),
	TOO_FAR_AWAY("Events.Sleep.TOO_FAR_AWAY"),
	
	BEDTP_TELEPORT("Commands.BedTP.Teleport"),
	BEDTP_TELEPORT_OTHER("Commands.BedTP.TeleportOther"),
	BEDTP_ERROR("Commands.BedTP.Error"),
	BEDTP_USAGE("Commands.BedTP.Usage"),
	
	USLEEP_USAGE("Commands.usleep.Usage"),
	RELOAD("Commands.usleep.Reload"),
	
	PLAYER_NOT_FOUND("Core.PlayerNotFound"),
	NO_CONSOLE("Core.NoConsole"),
	NO_PERMS("Core.NoPerms"),
	ERROR_PREFIX("Core.ErrorPrefix"),
	PREFIX("Core.Prefix");
	
	private String path;
	
	Message(String string) {
		this.path = string;
	}
	
	public String getPath() {
		return path;
	}
}
