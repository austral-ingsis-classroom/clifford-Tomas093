package edu.austral.ingsis.clifford;

import java.util.ArrayList;

public interface FileSystem {
  String getName();

  Folder getParent();

  default ArrayList<FileSystem> getChildren() {
    return new ArrayList<>();
  }

  default String getPath() {
    StringBuilder path = new StringBuilder();
    FileSystem current = this;
    while (current != null) {
      if (!current.getName().isEmpty()) {
        path.insert(0, "/" + current.getName());
      }
      current = current.getParent();
    }
    if (path.length() == 0) {
      path.insert(0, "/");
    }
    return path.toString();
  }

  void delete();
}
