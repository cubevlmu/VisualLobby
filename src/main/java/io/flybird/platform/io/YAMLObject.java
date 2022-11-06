package io.flybird.platform.io;

import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Objects;

public record YAMLObject(
        ConfigurationSection section
) {

    public YAMLObject getChild(String key){
        var queueValue = section.getConfigurationSection(key);

        if(queueValue == null)
            return null;
        else
            return new YAMLObject(queueValue);
    }

    public String getString(String key){

        var queueValue = section.getString(key);
        return Objects.requireNonNullElse(queueValue, "WHITE");
    }

    public int getInt(String key){

        var queueValue = section.getInt(key);
        return Objects.requireNonNullElse(queueValue, -1);
    }

    public long getLong(String key){

        var queueValue = section.getLong(key);
        return Objects.requireNonNullElse(queueValue, null);
    }

    public boolean getBoolean(String key){

        var queueValue = section.getBoolean(key);
        return Objects.requireNonNullElse(queueValue, false);
    }

    public List<String> getStringList(String key){

        var queueValue = section.getStringList(key);
        return Objects.requireNonNullElse(queueValue, List.of());
    }

}
