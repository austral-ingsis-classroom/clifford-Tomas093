package edu.austral.ingsis.clifford;

public class Rm implements Command {

  @Override
  public void execute(Folder fileSystem, String argument) {
    if (argument == null) {
      System.out.println("Error: file cant be null");
      return;
    }
    String[] args = argument.split(" ");
    int fileSystemindex = args.length - 1;
    if (!(args[fileSystemindex].contains("."))) {
      if (args.length == 2 && args[0].equals("--recursive")) {
        deleteDir(fileSystem, args[fileSystemindex]);
      } else {
        System.out.println("cannot remove " + "'" + args[0] + "'" + ", is a directory");
      }
    } else if (args.length == 1) {
      deleteFile(fileSystem, args[0]);
    } else {
      System.out.println("Invalid arguments");
    }
  }

  private void deleteFile(Folder fileSystem, String arg) {
    FileSystem fileToDelete = fileSystem.getChild(arg);
    if (fileToDelete != null) {
      if (fileToDelete instanceof File) {
        fileToDelete.delete();
        System.out.println(arg + " removed");
      } else {
        System.out.println("Invalid file name");
      }
    } else {
      System.out.println("File not found");
    }
  }

  private void deleteDir(Folder fileSystem, String name) {
    FileSystem fileToDelete = fileSystem.getChild(name);
    if (fileToDelete != null) {
      if (fileToDelete instanceof Folder) {
        fileToDelete.delete();
        System.out.println(name + " removed");
      } else {
        System.out.println("Invalid directory name");
      }
    } else {
      System.out.println("Directory not found");
    }
  }
}
