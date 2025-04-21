package edu.austral.ingsis.clifford;

public class File implements FileSystem {
  String name;
  Folder parent;

  public File(String name, Folder parent) {
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

  @Override
  public String getPath() {
    return FileSystem.super.getPath();
  }

  @Override
  public void delete() {
    if (parent != null) {
      parent.children.remove(name);
    }
  }
}
