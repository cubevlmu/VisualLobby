package io.flybird.lobby;

import io.flybird.VisualLobby;
import io.flybird.nmsutils.PluginMessage;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class Announcer extends BukkitRunnable implements Listener {

    private final List<String> title;
    private final String joinTitle;
    private final String roundMsg;

    public Announcer(String message){
        var config = VisualLobby.pluginsConfig;
        var Announcer = config.getChild("World").getChild("Announcer");

        var PlayerJoin = Announcer.getChild("PlayerJoin");
        if(PlayerJoin.getBoolean("Enable"))
            title = PlayerJoin.getStringList("Message");
        else
            title = null;

        var JoinTitle = Announcer.getChild("JoinTitle");
        if(JoinTitle.getBoolean("Enable"))
            joinTitle = JoinTitle.getString("Message");
        else
            joinTitle = null;

        roundMsg = message;
    }

    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    public void onJoin(PlayerJoinEvent e){
        e.setJoinMessage(null);
        if(title != null){
            for(var t : title){
                PluginMessage.sendLog(
                        e.getPlayer(),
                        VisualLobby.getInstance().getReader().getString("Annoncer"),
                        t
                );
            }
        }

        if(joinTitle != null){
            e.getPlayer().sendTitle(
                    joinTitle,
                    "",
                    15,
                    15,
                    15
            );
        }
    }

    @Override
    public void run() {

        PluginMessage.broadcastMessage(
                VisualLobby.getInstance().getReader().getString("Annoncer"),
                ChatColor.GOLD,
                roundMsg,
                ChatColor.WHITE
        );
        System.gc();
    }
}
