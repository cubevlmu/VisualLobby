package io.flybird.platform.models;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PluginComponent {
  String name();
  
  priority loadPriority();
  
  public enum priority {
    Hardest, Normal, Lowest;
  }
}
