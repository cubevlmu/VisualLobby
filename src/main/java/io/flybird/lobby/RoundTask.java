package io.flybird.lobby;

import io.flybird.VisualLobby;
import io.flybird.platform.io.YAMLUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.scheduler.BukkitRunnable;

public class RoundTask extends BukkitRunnable {

    public RoundTask() {

        YAMLUtils utils = VisualLobby.pluginsConfig;
        var world = utils.getChild("World");

        assert world != null;
        long time = world.getLong("Time");

        for(var w : Bukkit.getWorlds())
        {
            w.setTime(time);
        }
    }

    @Override
    public void run() {
        System.gc();
    }
}
