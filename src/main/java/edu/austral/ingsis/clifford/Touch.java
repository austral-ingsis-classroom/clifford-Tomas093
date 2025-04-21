package edu.austral.ingsis.clifford;

public class Touch implements Command, CreatorCommand {

  @Override
  public void execute(Folder fileSystem, String name) {
    if (isValidName(name)) {
      File newfile = createFile(name, fileSystem);
      fileSystem.addChild(newfile);
      System.out.println(name + " file created");
    } else {
      System.out.println("Invalid file name");
    }
  }

  private File createFile(String name, Folder parent) {
    return new File(name, parent);
  }
}
