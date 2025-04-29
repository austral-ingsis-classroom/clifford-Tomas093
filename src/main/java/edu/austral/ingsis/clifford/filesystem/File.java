package edu.austral.ingsis.clifford.filesystem;

public record File(String name) implements FileSystem {


  @Override
  public FileSystemType getType() {
    return FileSystemType.FILE;
  }
}
