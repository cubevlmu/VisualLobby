package io.flybird.platform.logger;

import java.util.HashMap;

public class SharedLoggers {
    private static final HashMap<String, Logger> maps
            = new HashMap<>();

    static {
        maps.put(
                "Platform",
                Logger.createOwnLogger("Platform")
        );
    }

    public static void unInitLoggers(){
        maps.clear();
    }

    public static Logger getShardLoggers(
          String key
    ){
        return maps.get(key);
    }
}
