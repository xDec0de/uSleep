package es.xdec0de.usleep.cmds;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
					if(((Player) sndr).getBedLocation() != null) {
						p.teleport(p.getBedSpawnLocation());
					} else {
						USPMessages.sendMessage(sndr, USPMessage.BEDTP_ERROR);
					}
				} else {
					sndr.sendMessage(USPMessages.getMessage(USPMessage.NO_PERMS).replaceAll("%perm%", USPConfig.getString(USPSetting.BEDTP_PERM)));
				}
			} else if(args.length == 1) {
				if(sndr.hasPermission(USPConfig.getString(USPSetting.BEDTP_OTHER_PERM))) {
					if(getBedLocation(args[0]) != null) {
						((Player) sndr).teleport(getBedLocation(args[0]));
						if(Bukkit.getPlayer(args[0]) != null) {
							sndr.sendMessage(USPMessages.getMessage(USPMessage.BEDTP_TELEPORT_OTHER).replaceAll("%player%", Bukkit.getPlayer(args[0]).getName()));
						} else {
							sndr.sendMessage(USPMessages.getMessage(USPMessage.BEDTP_TELEPORT_OTHER).replaceAll("%player%", args[0]));
						}
					} else {
						USPMessages.sendMessage(sndr, USPMessage.BEDTP_ERROR);
					}
				} else {
					sndr.sendMessage(USPMessages.getMessage(USPMessage.NO_PERMS).replaceAll("%perm%", USPConfig.getString(USPSetting.BEDTP_OTHER_PERM)));
				}
			} else {
				USPMessages.sendMessage(sndr, USPMessage.BEDTP_USAGE);
			}
		} else {
			USPMessages.sendMessage(sndr, USPMessage.NO_CONSOLE);
		}
		return true;
	}
	
	@SuppressWarnings("deprecation")
	private Location getBedLocation(String player) {
		if(Bukkit.getPlayer(player) != null) {
			return Bukkit.getPlayer(player).getBedSpawnLocation();
		} else if(Bukkit.getOfflinePlayer(player) != null) {
			return Bukkit.getOfflinePlayer(player).getBedSpawnLocation();
		}
		return null;
	}
}