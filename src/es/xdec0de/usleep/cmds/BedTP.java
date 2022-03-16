package es.xdec0de.usleep.cmds;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import es.xdec0de.usleep.api.events.BedTeleportTryEvent;
import es.xdec0de.usleep.utils.files.USPMessage;
import es.xdec0de.usleep.utils.files.USPSetting;

public class BedTP implements CommandExecutor {

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sndr, Command cmd, String label, String[] args) {
		if(sndr instanceof Player) {
			Player p = (Player)sndr;
			if(args.length == 0) {
				if(p.hasPermission(USPSetting.PERM_BEDTP_SELF.asString())) {
					Location bed = p.getBedSpawnLocation();
					BedTeleportTryEvent bte = new BedTeleportTryEvent(p, p, bed);
					Bukkit.getPluginManager().callEvent(bte);
					if(!bte.isCancelled() && (bed = bte.getBedSpawnLocation()) != null) {
						p.teleport(bed);
						USPMessage.BEDTP_TELEPORT.send(p);
					} else
						USPMessage.BEDTP_ERROR.send(p);
				} else
					USPMessage.NO_PERMS.send(p, "%perm%", USPSetting.PERM_BEDTP_SELF.asString());
			} else if(args.length == 1) {
				if(sndr.hasPermission(USPSetting.PERM_BEDTP_OTHER.asString())) {
					OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
					Location bed = target != null ? target.getBedSpawnLocation() : null;
					BedTeleportTryEvent bte = new BedTeleportTryEvent(p, target, bed);
					Bukkit.getPluginManager().callEvent(bte);
					if(!bte.isCancelled() && (bed = bte.getBedSpawnLocation()) != null) {
						p.teleport(bed);
						USPMessage.BEDTP_TELEPORT_OTHER.send(p, "%player%", args[0]);
					} else
						USPMessage.BEDTP_ERROR.send(p);
				} else
					USPMessage.NO_PERMS.send(p, "%perm%", USPSetting.PERM_BEDTP_SELF.asString());
			} else
				USPMessage.BEDTP_USAGE.send(p);
		} else
			USPMessage.NO_CONSOLE.send(sndr);
		return true;
	}
}
