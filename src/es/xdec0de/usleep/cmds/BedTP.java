package es.xdec0de.usleep.cmds;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import es.xdec0de.usleep.USleep;
import es.xdec0de.usleep.api.events.BedTeleportTryEvent;
import me.xdec0de.mcutils.general.MCCommand;

public class BedTP extends MCCommand<USleep> {

	public BedTP(USleep uSleep) {
		super(uSleep, "bedtp");
		setRestrictedSenderClass(ConsoleCommandSender.class);
	}

	@Override
	public boolean onCommand(CommandSender sndr, String[] args) {
		Player sender = (Player)sndr;
		OfflinePlayer target = asOfflinePlayer(0, args, sender);
		final boolean isSelfTp = args.length == 0;
		final String perm = getPlugin().getConfig().getString(isSelfTp ? "perms.bedTP.self" : "perms.bedTP.other");
		if (!sender.hasPermission(perm))
			return getPlugin().getMessages().send(sender, "noPerms", "%perm%", perm);
		if (target == null)
			return getPlugin().getMessages().send(sender, "cmds.bedTP.offline", "%target%", args[0]);
		Location bed = target.getBedSpawnLocation();
		if (bed == null)
			return getPlugin().getMessages().send(sender, "noBed");
		BedTeleportTryEvent bte = new BedTeleportTryEvent(sender, target, bed);
		if (bte.isCancelled())
			return true;
		if (bte.getBedSpawnLocation() == null)
			return getPlugin().getMessages().send(sender, "noBed");
		sender.teleport(bte.getBedSpawnLocation());
		return getPlugin().getMessages().send(sender, isSelfTp ? "cmds.bedTP.tp" : "cmds.bedTP.tpOther", "%target%", target);
	}

	@Override
	public List<String> onTab(CommandSender sender, String[] args) {
		if (args.length == 1 && sender.hasPermission(getPlugin().getConfig().getString("perms.bedTP.other")))
			return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
		return null;
	}
}
