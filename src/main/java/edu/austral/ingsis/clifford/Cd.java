package edu.austral.ingsis.clifford;

public class Cd implements Command {

  @Override
  public void execute(Folder fileSystem, String argument) {
    CLI cli = CLIHolder.getInstance(); // Obtén la instancia del CLI

    if (argument.equals("..")) {
      if (fileSystem.getParent() != null) {
        cli.setCurrentFolder(fileSystem.getParent());
        System.out.println("Moved to: " + fileSystem.getParent().getPath());
      } else {
        System.out.println("Moved to: /");
      }
    } else if (argument.startsWith("/")) {
      Folder root = getRoot(fileSystem);
      Folder target = navigateToPath(root, argument.substring(1));
      if (target != null) {
        cli.setCurrentFolder(target);
        System.out.println("Moved to: " + target.getPath());
      } else {
        System.out.println("Path not found");
      }
    } else if (argument.equals(".")) {
      return;
    } else {
      Folder target = navigateToPath(fileSystem, argument);
      if (target != null) {
        cli.setCurrentFolder(target);
        System.out.println("Moved to: " + target.getPath());
      } else {
        System.out.println("Path not found");
      }
    }
  }

  private Folder getRoot(Folder folder) {
    while (folder.getParent() != null) {
      folder = folder.getParent();
    }
    return folder;
  }

  private Folder navigateToPath(Folder start, String path) {
    String[] components = path.split("/");
    Folder current = start;

    for (String component : components) {
      if (component.isEmpty()) continue;
      FileSystem child = current.getChild(component);
      if (child instanceof Folder) {
        current = (Folder) child;
      } else {
        return null;
      }
    }
    return current;
  }
}
