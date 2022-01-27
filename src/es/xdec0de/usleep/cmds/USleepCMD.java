package es.xdec0de.usleep.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import es.xdec0de.usleep.utils.Message;
import es.xdec0de.usleep.utils.Setting;
import es.xdec0de.usleep.utils.files.Config;
import es.xdec0de.usleep.utils.files.Messages;

public class USleepCMD implements CommandExecutor {
	
	public boolean onCommand(CommandSender sndr, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			Messages.sendMessage(sndr, Message.USLEEP_USAGE);
		} else if (args.length == 1) {
			switch(args[0].toLowerCase()) {
			case "reload": case "rl":
				if(sndr.hasPermission(Config.getString(Setting.RELOAD_PERM))) {
					Config.update();
					Config.reload();
					Messages.update();
		            Messages.reload();
		            Messages.sendMessage(sndr, Message.RELOAD);
		        } else {
		        	sndr.sendMessage(Messages.getMessage(Message.NO_PERMS).replaceAll("%perm%", Config.getString(Setting.RELOAD_PERM)));
		        } 
				break;
			default:
				Messages.sendMessage(sndr, Message.USLEEP_USAGE);
			}
		} else {
			Messages.sendMessage(sndr, Message.USLEEP_USAGE);
		}  
		return true;
	}
}