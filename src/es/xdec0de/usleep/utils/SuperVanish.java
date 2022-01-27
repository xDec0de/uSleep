package es.xdec0de.usleep.utils;

import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

public class SuperVanish {

	public static boolean isVanished(Player player) {
		for(MetadataValue meta : player.getMetadata("vanished"))
			if(meta.asBoolean())
				return true;
		return false;
	}
}
