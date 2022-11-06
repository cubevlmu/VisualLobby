package io.flybird.nmsutils.command;

import io.flybird.VisualLobby;
import io.flybird.platform.logger.Logger;
import io.flybird.platform.logger.SharedLoggers;
import io.flybird.platform.models.ModelManger;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class CmdHelper implements CommandExecutor, TabExecutor {
  private final Logger logger = SharedLoggers.getShardLoggers("Platform");
  
  private final HashMap<String, ICmd> allCommands = new HashMap<>();
  
  private final CommandMap commandMap = GetCmdMap();
  
  private final boolean toggle = true;
  
  public CmdHelper(ModelManger ml) {
    try {
      this.logger.assertLog("Begin To Register Command", true);
      RegistryCmd(SyncCmdClass(ml));
    } catch (Exception e) {
      this.logger.error("Error When Registering Commands : %s", e.getLocalizedMessage());
    } 
  }
  
  public HashMap<MinecraftCommand, Class<?>> SyncCmdClass(ModelManger ml) {
    HashMap<MinecraftCommand, Class<?>> AllCommands = new HashMap<>();
    HashMap<Annotation[], Class> classes = ml.getAllClass();
    for (Annotation[] annotations : classes.keySet()) {
      if (annotations[0] instanceof MinecraftCommand)
        AllCommands.put((MinecraftCommand)annotations[0], classes.get(annotations)); 
    } 
    return AllCommands;
  }
  
  public void RegistryCmd(HashMap<MinecraftCommand, Class<?>> AllCommands) throws Exception {
    Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
    constructor.setAccessible(true);
    for (MinecraftCommand cmd : AllCommands.keySet()) {
      PluginCommand command = constructor.newInstance(cmd.CmdName(), VisualLobby.instance);
      command.setLabel(cmd.CmdName());
      command.setPermission("default");
      command.setAliases(Arrays.asList(cmd.Alies()));
      command.setDescription(cmd.Info());
      command.setExecutor(this);
      command.setTabCompleter(this);
      try {
        ICmd instance = ((Class<ICmd>)AllCommands.get(cmd)).getDeclaredConstructor(new Class[0]).newInstance();
        this.allCommands.put(cmd
            .CmdName(), instance);
        for (String item : cmd.Alies())
          this.allCommands.put(item, instance); 
        this.logger.info("Register Command -> %s", cmd.CmdName());
        instance.onRegistry();
      } catch (Exception e) {
        this.logger.error("Registry Command Error Reason %s", e.toString());
      } 
      Objects.requireNonNull(this.commandMap).register("Command You input is Error", command);
    } 
    System.gc();
  }
  
  public void UnRegistryCmd() {
    for (String cmd : this.allCommands.keySet()) {
      this.allCommands.get(cmd).onUnRegistry();
      assert this.commandMap != null;
      Objects.requireNonNull(this.commandMap.getCommand(cmd)).unregister(this.commandMap);
    } 
    assert this.commandMap != null;
    this.commandMap.clearCommands();
    System.gc();
  }
  
  private CommandMap GetCmdMap() {
    try {
      Class<?> c = Bukkit.getServer().getClass();
      for (Method method : c.getDeclaredMethods()) {
        if (method.getName().equals("getCommandMap"))
          return (CommandMap)method.invoke(Bukkit.getServer(), new Object[0]); 
      } 
    } catch (Exception e) {
      this.logger.error("Error When Get CmdMap Func %s", e.getMessage());
    } 
    return null;
  }
  
  public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
    try {
      return this.allCommands.get(s).onAction(commandSender, strings, s);
    } catch (Exception e) {
      e.printStackTrace();
      this.logger.error("Error When execute Command, Reason %s Source %s", e.getLocalizedMessage(), e.getStackTrace());
      return false;
    } 
  }
  
  public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
    try {
      return this.allCommands.get(s).onTabAction(commandSender, strings, s);
    } catch (Exception e) {
      return null;
    } 
  }
}
