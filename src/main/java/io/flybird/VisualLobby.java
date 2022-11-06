package io.flybird;

import io.flybird.lobby.Announcer;
import io.flybird.lobby.PlayEvents;
import io.flybird.lobby.RoundTask;
import io.flybird.nmsutils.ChatBuilder;
import io.flybird.nmsutils.command.CmdHelper;
import io.flybird.platform.il8n.LangReader;
import io.flybird.platform.io.YAMLUtils;
import io.flybird.platform.logger.Logger;
import io.flybird.platform.logger.SharedLoggers;
import io.flybird.platform.models.ModelManger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class VisualLobby extends JavaPlugin {

    private final LangReader reader;
    public static VisualLobby instance;
    public static final YAMLUtils pluginsConfig = new YAMLUtils("config.yml");
    private final Logger logger = Logger.createOwnLogger(this);

    public static VisualLobby getInstance(){
        return instance;
    }

    public LangReader getReader(){
        return reader;
    }

    public VisualLobby(){
        instance = this;
        reader = new LangReader(pluginsConfig.getChild("Common").getString("Language"));
    }

    public void reload(){
        logger.success("Begin Reload Config!");
        pluginsConfig.reload();
        logger.success("Reload Config Done!");
    }

    @Override
    public void onEnable() {
        logger.info("VisualLobby Plugin");
        logger.info("Base On VisualManger Platform");
        logger.info("Develop By CubeVlmu And FlyBirdStudio");
        logger.info("=====================================");
        logger.success("Plugin Begin To Init ->");

        Bukkit.getPluginManager().registerEvents(new PlayEvents(), this);
        Bukkit.getPluginManager().registerEvents(new ChatBuilder(), this);

        new RoundTask().runTaskTimer(
                this,
                15 * 60 * 1000,
                15 * 60 * 1000
        );

        var Announcer = pluginsConfig.getChild("World").getChild("Announcer");

        assert Announcer != null;
        var RoundTitle = Announcer.getChild("RoundTitle");
        assert RoundTitle != null;
        String roundMsg = RoundTitle.getString("Message");

        var announcerCls = new Announcer(roundMsg);
        Bukkit.getPluginManager().registerEvents(announcerCls, this);

        var time = RoundTitle.getInt("Time");
        if(time == 0)
            time = 10;

        if(RoundTitle.getBoolean("Enable")){
            announcerCls.runTaskTimer(
                    this,
                    (long) time * 60 * 1000,
                    (long) time * 60 * 1000
            );
        }

        ModelManger manger = new ModelManger("io.flybird.lobby");
        if(manger.loadModel()) {
            new CmdHelper(manger);
        }

        logger.success("Init Plugin Done~");
    }

    @Override
    public void onDisable() {
        SharedLoggers.unInitLoggers();
    }
}
