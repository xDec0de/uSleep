package es.xdec0de.usleep.cmds;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import es.xdec0de.usleep.utils.USPMessage;
import es.xdec0de.usleep.utils.USPSetting;
import es.xdec0de.usleep.utils.files.USPConfig;
import es.xdec0de.usleep.utils.files.USPMessages;

public class BedTP implements CommandExecutor {

	public boolean onCommand(CommandSender sndr, Command cmd, String label, String[] args) {
		if(sndr instanceof Player) {
			Player p = (Player)sndr;
			if(args.length == 0) {
				if(p.hasPermission(USPConfig.getString(USPSetting.BEDTP_PERM))) {
					Location bed = p.getBedSpawnLocation();
					if(bed != null)
						p.teleport(bed);
					else
						USPMessages.sendMessage(sndr, USPMessage.BEDTP_ERROR);
				} else
					USPMessages.sendMessage(sndr, USPMessage.NO_PERMS, "%perm%", USPConfig.getString(USPSetting.BEDTP_PERM));
			} else if(args.length == 1) {
				if(sndr.hasPermission(USPConfig.getString(USPSetting.BEDTP_OTHER_PERM))) {
					Location bed = getBedLocation(args[0]);
					if(bed != null) {
						p.teleport(bed);
						USPMessages.sendMessage(sndr, USPMessage.BEDTP_TELEPORT_OTHER, "%player%", args[0]);
					} else
						USPMessages.sendMessage(sndr, USPMessage.BEDTP_ERROR);
				} else
					USPMessages.getMessage(USPMessage.NO_PERMS, "%perm%", USPConfig.getString(USPSetting.BEDTP_OTHER_PERM));
			} else
				USPMessages.sendMessage(sndr, USPMessage.BEDTP_USAGE);
		} else
			USPMessages.sendMessage(sndr, USPMessage.NO_CONSOLE);
		return true;
	}

	@SuppressWarnings("deprecation")
	private Location getBedLocation(String player) {
		Player target = Bukkit.getPlayerExact(player);
		if(target != null)
			return target.getBedSpawnLocation();
		OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(player);
		if(offlineTarget != null)
			return offlineTarget.getBedSpawnLocation();
		return null;
	}
}
