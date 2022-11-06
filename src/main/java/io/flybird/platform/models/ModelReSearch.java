package io.flybird.platform.models;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ModelReSearch {
  private final ClassLoader loader;
  
  private final String PackageName;
  
  public boolean SearchChildPackage = true;
  
  public ModelReSearch(String PackageName, ClassLoader classLoader) {
    this.PackageName = PackageName.replace(".", "/");
    this.loader = classLoader;
  }
  
  public List<String> GetClasses() throws Exception {
    List<String> AllClasses = null;
    URL url = this.loader.getResource(this.PackageName);
    if (url != null) {
      String PackageType = url.getProtocol();
      if (PackageType.equals("file")) {
        AllClasses = SearchClassesFromFiles(url.getPath());
      } else if (PackageType.equals("jar")) {
        AllClasses = SearchClassesFromJar(url.getPath());
      } else {
        throw new Exception("Unknown File Type Of Assembly");
      } 
    } else {
      throw new NullPointerException("Empty Url Of Assembly");
    } 
    return AllClasses;
  }
  
  private List<String> SearchClassesFromJar(String PathOfJar) {
    List<String> AllClasses = new ArrayList<>();
    String[] jarInfo = PathOfJar.split("!");
    String jarFilePath = jarInfo[0].substring(jarInfo[0].indexOf("/"));
    String packagePath = jarInfo[1].substring(1);
    try {
      JarFile jarFile = new JarFile(jarFilePath);
      Enumeration<JarEntry> entry = jarFile.entries();
      while (entry.hasMoreElements()) {
        JarEntry jarEntry = entry.nextElement();
        String entryName = jarEntry.getName();
        if (entryName.endsWith(".class")) {
          String myPackagePath, substring = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
          if (this.SearchChildPackage) {
            if (entryName.startsWith(packagePath))
              AllClasses.add(substring); 
            continue;
          } 
          int index = entryName.lastIndexOf("/");
          if (index != -1) {
            myPackagePath = entryName.substring(0, index);
          } else {
            myPackagePath = entryName;
          } 
          if (myPackagePath.equals(packagePath))
            AllClasses.add(substring); 
        } 
      } 
    } catch (Exception e) {
      e.printStackTrace();
    } 
    return AllClasses;
  }
  
  private List<String> SearchClassesFromFiles(String Path) {
    List<String> AllClasses = new ArrayList<>();
    File[] childFiles = (new File(Path)).listFiles();
    for (File childFile : childFiles) {
      if (childFile.isDirectory()) {
        if (this.SearchChildPackage)
          AllClasses.addAll(SearchClassesFromFiles(childFile.getPath())); 
      } else {
        String childFilePath = childFile.getPath();
        if (childFilePath.endsWith(".class")) {
          childFilePath = childFilePath.substring(childFilePath.indexOf("\\classes") + 9, childFilePath
              .lastIndexOf("."));
          childFilePath = childFilePath.replace("\\", ".");
          AllClasses.add(childFilePath);
        } 
      } 
    } 
    return AllClasses;
  }
}
