package io.flybird.nmsutils.command;

import java.util.List;
import org.bukkit.command.CommandSender;

public interface ICmd {
  boolean onAction(CommandSender paramCommandSender, String[] paramArrayOfString, String paramString);
  
  List<String> onTabAction(CommandSender paramCommandSender, String[] paramArrayOfString, String paramString);
  
  void onRegistry();
  
  void onUnRegistry();
}
