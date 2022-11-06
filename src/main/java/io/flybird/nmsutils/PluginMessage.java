package io.flybird.nmsutils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PluginMessage {
  public static void sendLog(Player player, String prefix, String content) {
    player.sendMessage(String.format("|> %s[%s]%s %s", ChatColor.GOLD, prefix, ChatColor.WHITE, content));
  }
  
  public static void sendLog(CommandSender player, String prefix, String content) {
    player.sendMessage(String.format("|> %s[%s]%s %s", ChatColor.GOLD, prefix, ChatColor.WHITE, content));
  }
  
  public static void sendLog(CommandSender player, String prefix, String content, Object... replace) {
    player.sendMessage(String.format("|> %s[%s]%s %s", ChatColor.GOLD, prefix, ChatColor.WHITE,

            String.format(content, new Object[] { replace })));
  }
  
  public static void sendLog(Player player, String prefix, String content, Object... replace) {
    player.sendMessage(String.format("|> %s[%s]%s %s", ChatColor.GOLD, prefix, ChatColor.WHITE,

            String.format(content, new Object[] { replace })));
  }
  
  public static void broadcastLog(String prefix, ChatColor prefixColor, String message, ChatColor messageColor) {
    Bukkit.getServer().broadcastMessage(
        String.format("|> %s[%s] %s%s", prefixColor, prefix, messageColor, message));
  }
  
  public static void broadcastMessage(String prefix, ChatColor prefixColor, String message, ChatColor messageColor) {
    Bukkit.getServer().broadcastMessage(
        String.format("|> %s%s : %s%s", prefixColor, prefix, messageColor, message));
  }
  
  public static void createAnnounceBar(String text, BarColor textColor) {
    Bukkit.createBossBar(text, textColor, BarStyle.SOLID, BarFlag.PLAY_BOSS_MUSIC);
  }
}
