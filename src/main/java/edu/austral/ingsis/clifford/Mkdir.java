package edu.austral.ingsis.clifford;

public class Mkdir implements Command, CreatorCommand {

  @Override
  public void execute(Folder fileSystem, String name) {
    if (isValidName(name)) {
      Folder newFolder = createFolder(name, fileSystem);
      if (fileSystem != null) {
        fileSystem.addChild(newFolder);
      }
      System.out.println(name + " directory  created");
    } else {
      System.out.println("Invalid directory name");
    }
  }

  private Folder createFolder(String name, Folder parent) {
    return new Folder(name, parent);
  }
}
