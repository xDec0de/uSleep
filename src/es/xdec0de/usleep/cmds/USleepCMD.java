package es.xdec0de.usleep.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import es.xdec0de.usleep.utils.files.USPConfig;
import es.xdec0de.usleep.utils.files.USPMessage;
import es.xdec0de.usleep.utils.files.USPMessages;
import es.xdec0de.usleep.utils.files.USPSetting;

public class USleepCMD implements CommandExecutor {

	public boolean onCommand(CommandSender sndr, Command cmd, String label, String[] args) {
		if (args.length == 1) {
			switch(args[0].toLowerCase()) {
			case "reload": case "rl":
				if(sndr.hasPermission(USPConfig.getString(USPSetting.PERM_RELOAD))) {
					USPConfig.setup(true);
					USPMessages.setup(true);
					USPMessage.RELOAD.send(sndr);
				} else
					USPMessage.NO_PERMS.send(sndr, "%perm%", USPConfig.getString(USPSetting.PERM_RELOAD));
				break;
			default:
				USPMessage.USLEEP_USAGE.send(sndr);
			}
		} else
			USPMessage.USLEEP_USAGE.send(sndr);
		return true;
	}
}
