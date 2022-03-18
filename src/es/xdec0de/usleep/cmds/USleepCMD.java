package es.xdec0de.usleep.cmds;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import es.xdec0de.usleep.utils.files.USPConfig;
import es.xdec0de.usleep.utils.files.USPMessage;
import es.xdec0de.usleep.utils.files.USPMessages;
import es.xdec0de.usleep.utils.files.USPSetting;
import es.xdec0de.usleep.utils.files.USPWorlds;

public class USleepCMD implements TabExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 1) {
			switch(args[0].toLowerCase()) {
			case "reload": case "rl":
				if(sender.hasPermission(USPSetting.PERM_RELOAD.asString())) {
					USPConfig.setup(true);
					USPMessages.setup(true);
					USPWorlds.setup();
					USPMessage.RELOAD.send(sender);
				} else
					USPMessage.NO_PERMS.send(sender, "%perm%", USPSetting.PERM_RELOAD.asString());
				break;
			default:
				USPMessage.USLEEP_USAGE.send(sender);
				break;
			}
		} else
			USPMessage.USLEEP_USAGE.send(sender);
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		return args.length == 1 && sender.hasPermission(USPSetting.PERM_RELOAD.asString()) ? Arrays.asList("reload") : Arrays.asList("");
	}
}
