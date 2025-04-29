package edu.austral.ingsis.clifford.filesystem;

public sealed interface FileSystem permits Directory, File, Folder {

  FileSystemType getType();
  String name();

}
