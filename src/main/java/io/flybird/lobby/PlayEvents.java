package io.flybird.lobby;

import io.flybird.VisualLobby;
import io.flybird.platform.io.YAMLUtils;
import io.flybird.platform.logger.Logger;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class PlayEvents implements Listener {

    private final boolean isWetherEnable, DisableDead, DisableHunger, DisableHeart, EnableProtectGameMode;
    private final int[] repownPoint;

    public PlayEvents(){
        Logger logger = Logger.createOwnLogger(this);
        logger.info("Core Event Register");

        YAMLUtils utils = VisualLobby.pluginsConfig;
        var world = utils.getChild("World");

        assert world != null;
        isWetherEnable = world.getBoolean("WeatherChangeDeny");
        var Weather = world.getInt("Weather");

        for(var w : Bukkit.getWorlds()){
            w.setWeatherDuration(Weather);
        }

        var RespownPoint = world.getChild("RespownPoint");
        assert RespownPoint != null;

        repownPoint = new int[]{
                RespownPoint.getInt("x"),
                RespownPoint.getInt("y"),
                RespownPoint.getInt("z")
        };

        var player = world.getChild("Player");
        DisableDead = player.getBoolean("DisableDead");
        DisableHunger = player.getBoolean("DisableHunger");
        DisableHeart = player.getBoolean("DisableHunger");
        EnableProtectGameMode = player.getBoolean("EnableProtectGameMode");
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e){

        if(e.getTo().getY() < -64){
            tpToRes(e.getPlayer());
        }
    }

    private void tpToRes(Player player){
        player.teleport(
                new Location(
                        player.getWorld(),
                        repownPoint[0],
                        repownPoint[1],
                        repownPoint[2]
                )
        );
    }

    @EventHandler
    public void onRepown(PlayerRespawnEvent e){
        e.setRespawnLocation(
                new Location(
                        e.getPlayer().getWorld(),
                        repownPoint[0],
                        repownPoint[1],
                        repownPoint[2]
                )
        );
    }

    @EventHandler
    public void onGameMode(PlayerGameModeChangeEvent e){
        /*if(EnableProtectGameMode)
            if(!e.getPlayer().isOp())
                if(e.getPlayer().getGameMode() != GameMode.ADVENTURE) {
                    e.getPlayer().setGameMode(GameMode.ADVENTURE);
                }else
                    e.setCancelled(true);
            else
                e.setCancelled(true);
        else
            e.setCancelled(true);*/
        //TODO Fixup This Code
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){

        if(EnableProtectGameMode && !e.getPlayer().isOp())
            e.getPlayer().setGameMode(GameMode.ADVENTURE);
        tpToRes(e.getPlayer());
    }

    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    public void onPlace(BlockDispenseEvent e){
        var y = e.getBlock().getY();
        if(y == 255 || y == 0 && e.getBlock().getType().name().endsWith("SHULKER_BOX"))
            e.setCancelled(true);
    }

    @EventHandler
    public void onDeath(EntityDamageEvent event){
        if(DisableHeart && event.getEntity() instanceof Player)
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onWeather(WeatherChangeEvent e){
        if(isWetherEnable)
            e.setCancelled(true);
    }
}
