package io.flybird.platform.io;

import io.flybird.platform.logger.Logger;
import io.flybird.platform.logger.SharedLoggers;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class YAMLUtils {
    private final File yamlFile;
    private final YamlConfiguration configuration;

    public YAMLUtils(String fileName){
        Logger logger = SharedLoggers.getShardLoggers("Platform");
        yamlFile = new File(
                System.getProperty("user.dir"),
                String.format(
                        "/Plugins/VisualLobby/%s",
                        fileName
                )
        );
        if(yamlFile.isDirectory())
            throw new IllegalArgumentException("Error File Path");

        if(
                Objects.equals(fileName, "config.yml") &&
                        !yamlFile.exists()
        ){
            try {
                var stream = this.getClass().getResourceAsStream("/config.yml");
                assert stream != null;
                byte[] result = new byte[stream.available()];
                var l = stream.read(result);
                logger.info(l);

                var read = new String(result);

                var dir = new File(System.getProperty("user.dir"), "/Plugins/VisualLobby");
                if (!dir.exists()) {
                    dir.mkdir();
                }

                FileWriter writer = new FileWriter(yamlFile, false);
                writer.write(read);
                writer.flush();
                writer.close();
                logger.success("Init Config Done!");
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        configuration = YamlConfiguration.loadConfiguration(yamlFile);
    }

    public boolean reload(){
        try {
            configuration.load(yamlFile);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean releaseConfigFromAssets(
            String name,
            File file
    ){
        try {
            var stream = YAMLUtils.class.getResourceAsStream(name);
            assert stream != null;
            byte[] result = new byte[stream.available()];
            var l = stream.read(result);

            var read = new String(result);

            var dir = new File(System.getProperty("user.dir"), "/Plugins/VisualLobby");
            if (!dir.exists()) {
                dir.mkdir();
            }

            FileWriter writer = new FileWriter(file, false);
            writer.write(read);
            writer.flush();
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public YAMLUtils(File file){
        yamlFile = file;
        if(yamlFile.isDirectory())
            throw new IllegalArgumentException("Error File Path");

        configuration = YamlConfiguration.loadConfiguration(yamlFile);

    }

    public YAMLUtils(String fileName, String path){
        yamlFile = new File(
                path,
                fileName
        );
        if(yamlFile.isDirectory())
            throw new IllegalArgumentException("Error File Path");

        configuration = YamlConfiguration.loadConfiguration(yamlFile);

    }

    public YAMLObject getChild(String key){
        return new YAMLObject(configuration.getConfigurationSection(key));
    }

    public void save(){

        try {
            configuration.save(yamlFile);
        }catch (Exception e){
            throw new RuntimeException("Save Config Error");
        }
    }
}
