package es.xdec0de.usleep.cmds;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;

import es.xdec0de.usleep.USleep;
import me.xdec0de.mcutils.general.MCCommand;

public class USleepCMD extends MCCommand<USleep> {

	public USleepCMD(USleep plugin) {
		super(plugin, "usleep", "us");
	}

	@Override
	public boolean onCommand(CommandSender sender, String[] args) {
		final String reloadPerm = getPlugin().getConfig().getString("perms.reload");
		if (!sender.hasPermission(reloadPerm))
			return getPlugin().getMessages().send(sender, "noPerms", "%perm%", reloadPerm);
		if (args.length != 1)
			return getPlugin().getMessages().send(sender, "cmds.usleep.usage");
		return switch (args[0].toLowerCase()) {
		case "reload", "rl" -> reload(sender);
		default -> getPlugin().getMessages().send(sender, "cmds.usleep.usage");
		};
	}

	private boolean reload(CommandSender sender) {
		getPlugin().reload();
		return getPlugin().getMessages().send(sender, "cmds.usleep.reload");
	}

	@Override
	public List<String> onTab(CommandSender sender, String[] args) {
		final String reloadPerm = getPlugin().getConfig().getString("Permissions.Reload");
		return args.length == 1 && sender.hasPermission(reloadPerm) ? Arrays.asList("reload") : Arrays.asList("");
	}
}
