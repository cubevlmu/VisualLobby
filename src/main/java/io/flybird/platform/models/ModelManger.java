package io.flybird.platform.models;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;

import io.flybird.platform.logger.Logger;
public class ModelManger {
  private final Logger logger = Logger.createOwnLogger(this);
  
  private final HashMap<Annotation[], Class> allClass = (HashMap)new HashMap<>();
  
  private final HashMap<Class, PluginComponent.priority> tasks = new HashMap<>();
  
  private final HashMap<String, Object> instanceList = new HashMap<>();
  
  private final String packageName;
  
  public HashMap<Annotation[], Class> getAllClass() {
    return this.allClass;
  }
  
  public ModelManger(String packageName) {
    this.packageName = packageName;
  }
  
  public void reloadModels() {
    killAllInstances();
    loadPluginsModels();
  }
  
  public boolean loadModel() {
    ModelReSearch search = new ModelReSearch(this.packageName, getClass().getClassLoader());
    try {
      List<String> allClasses = search.GetClasses();
      for (String item : allClasses) {
        Class<?> clazz = Class.forName(item);
        if ((clazz.getAnnotations()).length != 0)
          this.allClass.put(clazz.getAnnotations(), clazz); 
      } 
      System.gc();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    } 
  }
  
  public void loadPluginsModels() {
    try {
      this.tasks.clear();
      for (Annotation[] key : this.allClass.keySet()) {
        Annotation annotation = key[0];
        if (annotation instanceof PluginComponent) {
          PluginComponent component = (PluginComponent)annotation;
          PluginComponent.priority p = component.loadPriority();
          if (p == null)
            p = PluginComponent.priority.Normal; 
          this.tasks.put(this.allClass.get(key), p);
        } 
      } 
      System.gc();
      Thread thread = new Thread(() -> {
            for (Class clazz : this.tasks.keySet()) {
              PluginComponent component = (PluginComponent)clazz.getAnnotations()[0];
              try {
                Object instance = clazz.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                this.instanceList.put(component.name(), instance);
              } catch (Exception e) {
                e.printStackTrace();
              } 
            } 
            System.gc();
          });
      thread.start();
      this.tasks.clear();
      System.gc();
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public Object getInstance(String name) {
    if (!this.instanceList.containsKey(name))
      return null; 
    return this.instanceList.get(name);
  }
  
  public void killAllInstances() {
    try {
      this.instanceList.clear();
      System.gc();
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
}
