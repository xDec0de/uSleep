package es.xdec0de.usleep.utils;
import java.net.URL;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.IOUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import es.xdec0de.usleep.USPMain;
import es.xdec0de.usleep.utils.files.Config;
import es.xdec0de.usleep.utils.files.Messages;
import net.md_5.bungee.api.ChatColor;

public class UpdateChecker implements Listener {

    private USPMain plugin;
	private int resourceId;
	
	public UpdateChecker(USPMain plugin, int resourceId) {
		this.plugin = plugin;
		this.resourceId = resourceId;
	}
    
    @SuppressWarnings("deprecation")
	public void getLatestVersion(Consumer<String> consumer) {
    	Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
	        try {
	            JSONArray versionsArray = (JSONArray) JSONValue.parseWithException(IOUtils.toString(new URL(String.valueOf("https://api.spiget.org/v2/resources/"+resourceId+"/versions?size=" + Integer.MAX_VALUE + "&spiget__ua=SpigetDocs"))));
	            String version = ((JSONObject) versionsArray.get(versionsArray.size() - 1)).get("name").toString();
	            if(!version.isEmpty()) {
	            	consumer.accept(version);
	            }
	        }
	        catch (Exception e) {
	        	Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&lu&9&lSleep&8: &8[&cWarning&8] &cAn error occurred while checking for updates&8:&6 " + e.getMessage()));
	        }
    	});
    }
    
    @EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if(Config.getBoolean(Setting.UPDATER_ENABLED) && Config.getBoolean(Setting.UPDATER_MESSAGE_PLAYER)) {
			if(e.getPlayer().hasPermission(Config.getString(Setting.UPDATER_MESSAGE_PERMISSION))) {
				getLatestVersion(version -> {
					if(!USPMain.plugin.getDescription().getVersion().equalsIgnoreCase(version)) {
						e.getPlayer().sendMessage(Messages.getMessage(Message.UPDATE_AVAILABLE_PLAYER).replaceAll("%current%", USPMain.plugin.getDescription().getVersion()).replaceAll("%ver%", version));
					}
				});
			}
		}
	}
}