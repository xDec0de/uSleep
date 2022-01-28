package es.xdec0de.usleep.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import es.xdec0de.usleep.utils.USPMessage;
import es.xdec0de.usleep.utils.USPSetting;
import es.xdec0de.usleep.utils.files.USPConfig;
import es.xdec0de.usleep.utils.files.USPMessages;

public class USleepCMD implements CommandExecutor {

	public boolean onCommand(CommandSender sndr, Command cmd, String label, String[] args) {
		if (args.length == 1) {
			switch(args[0].toLowerCase()) {
			case "reload": case "rl":
				if(sndr.hasPermission(USPConfig.getString(USPSetting.PERM_RELOAD))) {
					USPConfig.reload();
					USPMessages.reload();
					USPMessages.sendMessage(sndr, USPMessage.RELOAD);
				} else
					USPMessages.sendMessage(sndr, USPMessage.NO_PERMS, "%perm%", USPConfig.getString(USPSetting.PERM_RELOAD));
				break;
			default:
				USPMessages.sendMessage(sndr, USPMessage.USLEEP_USAGE);
			}
		} else
			USPMessages.sendMessage(sndr, USPMessage.USLEEP_USAGE);
		return true;
	}
}
