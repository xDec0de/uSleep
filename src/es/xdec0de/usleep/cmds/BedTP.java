package es.xdec0de.usleep.cmds;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import es.xdec0de.usleep.utils.Message;
import es.xdec0de.usleep.utils.Setting;
import es.xdec0de.usleep.utils.files.Config;
import es.xdec0de.usleep.utils.files.Messages;

public class BedTP implements CommandExecutor {
	
	public boolean onCommand(CommandSender sndr, Command cmd, String label, String[] args) {
		if(sndr instanceof Player) {
			Player p = (Player)sndr;
			if(args.length == 0) {
				if(p.hasPermission(Config.getString(Setting.BEDTP_PERM))) {
					if(((Player) sndr).getBedLocation() != null) {
						p.teleport(p.getBedSpawnLocation());
					} else {
						Messages.sendMessage(sndr, Message.BEDTP_ERROR);
					}
				} else {
					sndr.sendMessage(Messages.getMessage(Message.NO_PERMS).replaceAll("%perm%", Config.getString(Setting.BEDTP_PERM)));
				}
			} else if(args.length == 1) {
				if(sndr.hasPermission(Config.getString(Setting.BEDTP_OTHER_PERM))) {
					if(getBedLocation(args[0]) != null) {
						((Player) sndr).teleport(getBedLocation(args[0]));
						if(Bukkit.getPlayer(args[0]) != null) {
							sndr.sendMessage(Messages.getMessage(Message.BEDTP_TELEPORT_OTHER).replaceAll("%player%", Bukkit.getPlayer(args[0]).getName()));
						} else {
							sndr.sendMessage(Messages.getMessage(Message.BEDTP_TELEPORT_OTHER).replaceAll("%player%", args[0]));
						}
					} else {
						Messages.sendMessage(sndr, Message.BEDTP_ERROR);
					}
				} else {
					sndr.sendMessage(Messages.getMessage(Message.NO_PERMS).replaceAll("%perm%", Config.getString(Setting.BEDTP_OTHER_PERM)));
				}
			} else {
				Messages.sendMessage(sndr, Message.BEDTP_USAGE);
			}
		} else {
			Messages.sendMessage(sndr, Message.NO_CONSOLE);
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