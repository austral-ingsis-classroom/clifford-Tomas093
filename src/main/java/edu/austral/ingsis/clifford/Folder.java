package edu.austral.ingsis.clifford;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Folder implements FileSystem {
  String name;
  Folder parent;
  Map<String, FileSystem> children = new LinkedHashMap<>();

  public Folder(String name, Folder parent) {
    this.name = name;
    this.parent = parent;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Folder getParent() {
    return parent;
  }

  public ArrayList<FileSystem> getChildren() {
    return new ArrayList<>(children.values());
  }

  public FileSystem getChild(String name) {
    return children.get(name);
  }

  public void setParent(Folder parent) {
    this.parent = parent;
  }

  public void addChild(FileSystem child) {
    if (child instanceof Folder) {
      ((Folder) child).setParent(this);
    }
    children.put(child.getName(), child);
  }

  @Override
  public void delete() {
    recursiveDelete();
  }

  private void recursiveDelete() {
    for (FileSystem child : children.values()) {
      if (child instanceof Folder) {
        ((Folder) child).recursiveDelete();
      }
      child.delete();
    }
    children.clear();
    if (parent != null) {
      parent.children.remove(name);
    }
  }

  @Override
  public String getPath() {
    return FileSystem.super.getPath();
  }
}
