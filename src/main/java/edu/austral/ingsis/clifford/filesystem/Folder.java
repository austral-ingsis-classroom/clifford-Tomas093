package edu.austral.ingsis.clifford.filesystem;

public sealed interface Folder extends FileSystem permits Directory {

  @Override
  default FileSystemType getType() {
    return FileSystemType.FOLDER;
  }
}
