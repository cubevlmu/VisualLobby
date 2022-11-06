package io.flybird.platform.logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Logger extends BasicLogger {

    public static Logger createOwnLogger(String name){
        return new Logger(name);
    }

    public static Logger createOwnLogger(Class name){
        return new Logger(name.getName());
    }

    public static Logger createOwnLogger(Object name){
        return new Logger(name.getClass().getName());
    }

    private final String senderName;

    private Logger(String name) {
        super(Bukkit.getLogger());
        senderName = name;
    }

    private String buildLog(
            Object log,
            ChatColor color
    ){
        return String.format(
                "%s[%s]%s %s",
                color,
                senderName,
                ChatColor.WHITE,
                log
        );
    }

    @Override
    public void info(Object log) {
        getBukkitLogger().info(buildLog(
                log,
                ChatColor.AQUA
        ));
    }

    @Override
    public void info(Object log, Object... replace) {
        info(String.format(
                (String) log,
                replace
        ));
    }

    @Override
    public void success(Object log) {
        getBukkitLogger().info(buildLog(
                log,
                ChatColor.GREEN
        ));
    }

    @Override
    public void success(Object log, Object... replace) {
        success(String.format(
                (String) log,
                replace
        ));
    }

    @Override
    public void error(Object log) {
        getBukkitLogger().info(buildLog(
                log,
                ChatColor.RED
        ));
    }

    @Override
    public void error(Object log, Object... replace) {
        error(String.format(
                (String) log,
                replace
        ));
    }

    @Override
    public void warn(Object log) {
        getBukkitLogger().info(buildLog(
                log,
                ChatColor.YELLOW
        ));
    }

    @Override
    public void warn(Object log, Object... replace) {
        warn(String.format(
                (String) log,
                replace
        ));
    }

    public void assertLog(String cmd, boolean b) {
        if(b)
            warn(cmd);
    }
}
