package io.flybird.nmsutils;

import io.flybird.VisualLobby;
import io.flybird.platform.logger.Logger;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;

public class ChatBuilder implements Listener {

    private final Logger logger = Logger.createOwnLogger(this);
    private final String format;
    private final ChatColor adminColor;
    private final List<String> dirtyLang;

    public ChatBuilder(){
        var chat = VisualLobby.pluginsConfig.getChild("World").getChild("Chat");
        format = chat.getString("Format");

        if(chat.getBoolean("EnableChatScan")){
            dirtyLang = chat.getStringList("DirtyMessage");
        }else
            dirtyLang = null;

        adminColor = ChatColor.getByChar(chat.getString("AdminColor"));
    }

    @EventHandler(
            priority = EventPriority.HIGH
    )
    public void onChatAsync(AsyncPlayerChatEvent e){
        if(dirtyLang != null){
            for(var d : dirtyLang)
                if(e.getMessage().contains(d)) {
                    PluginMessage.sendLog(
                            e.getPlayer(),
                            VisualLobby.getInstance().getReader().getString("DirtyMsgWarnTitle"),
                            VisualLobby.getInstance().getReader().getString("DirtyMsgWarnContent")
                                    .replace("{#word}", d)
                    );
                    e.setCancelled(true);
                    break;
                }
        }
    }

    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    public void onChat(AsyncPlayerChatEvent e){
        e.setFormat("|> %2$s");
        e.setMessage(buildChat(
                e.getPlayer(),
                e.getMessage(),
                format
        ));
    }

    public String buildChat(
            Player e,
            String message,
            String format
    ){
       /* if(e.isOp()){
            return format.replace("{#name}", String.format(
                            "%s%s%s",
                            adminColor,
                            e.getName(),
                            ChatColor.WHITE
                    ))
                    .replace("{#message}", message)
                    .replace("{#displyname}", e.getDisplayName());
        }*/

        return format.replace("{#name}", e.getName())
                .replace("{#message}", message)
                .replace("{#displyname}", e.getDisplayName());
    }
}
