package io.flybird.lobby;

import io.flybird.VisualLobby;
import io.flybird.nmsutils.PluginMessage;
import io.flybird.nmsutils.command.ICmd;
import io.flybird.nmsutils.command.MinecraftCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

@MinecraftCommand(CmdName = "VisualLobby", Alies = {"vlobby"}, Info = "The Visual Lobby Control Command")
public class VisualLobbyCommand implements ICmd {

    @Override
    public boolean onAction(CommandSender paramCommandSender, String[] paramArrayOfString, String paramString) {
        switch (paramArrayOfString[0]){
            case "reload" -> VisualLobby.getInstance().reload();
            case "update" -> PluginMessage.sendLog(
                    paramCommandSender,
                    "VisualLobby",
                    "Error When Checking Update...");
            case "info" -> PluginMessage.sendLog(
                    paramCommandSender,
                    "VisualLobby",
                    "The VisualPlatform Project \n VisualLobby Plugin \n Version 0.1 \n By CubeVlmu & FlyBirdStudio"
            );
        }
        return false;
    }

    @Override
    public List<String> onTabAction(CommandSender paramCommandSender, String[] paramArrayOfString, String paramString) {
        return List.of(
                new String[]{
                        "reload",
                        "update",
                        "info"
                }
        );
    }

    @Override
    public void onRegistry() {

    }

    @Override
    public void onUnRegistry() {
        System.gc();
    }
}
