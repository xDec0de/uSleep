package es.xdec0de.usleep.bedinteract;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import es.xdec0de.usleep.api.SleepAPI;

public class WakeUp implements Listener {
	
	SleepAPI uSleepAPI = new SleepAPI();
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBedLeave(PlayerBedLeaveEvent e) {
		if(SleepAPI.numSleep != 0) {
	      SleepAPI.numSleep--;
	      uSleepAPI.broadcastWakeUp();
	    } 
	}
}