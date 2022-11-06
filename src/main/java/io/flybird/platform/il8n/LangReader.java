package io.flybird.platform.il8n;

import io.flybird.platform.io.YAMLObject;
import io.flybird.platform.io.YAMLUtils;
import io.flybird.platform.logger.Logger;

import java.io.File;
import java.util.Objects;

public class LangReader {
    private final YAMLObject object;

    public LangReader(String langage){

        File file = new File(
                String.format(
                        "%s/Plugins/VisualLobby/%s.yml",
                        System.getProperty("user.dir"),
                        langage
                )
        );

        if(!file.exists()){
            YAMLUtils.releaseConfigFromAssets("/DefaultLang.yml", file);
        }

        YAMLUtils utils = new YAMLUtils(file);
        Logger logger = Logger.createOwnLogger(this);
        logger.info("Using The Language %s For Platform Laguage", utils.getChild("Info").getString("Name"));
        object = utils.getChild("Language");
    }

    public String getString(String key){
        return object.getString(key);
    }

    public String getStringWithReplace(
            String key,
            String placehelder,
            String replace
    ){
        return Objects.requireNonNullElse(object.getString(key), "NULL")
                .replace(placehelder, replace);
    }
}
