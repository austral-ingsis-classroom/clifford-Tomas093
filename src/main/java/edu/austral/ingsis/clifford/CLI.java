package edu.austral.ingsis.clifford;

import java.util.Scanner;

public class CLI {

  private Folder currentFolder;

  public CLI(Folder root) {
    this.currentFolder = root;
  }

  public void start() {
    Scanner scanner = new Scanner(System.in);

    while (true) {
      System.out.print(currentFolder.getPath() + "> ");
      String input = scanner.nextLine().trim();

      if (input.isEmpty()) {
        continue;
      }

      String[] tokens = input.split(" ", 2);
      String command = tokens[0];
      String arguments = tokens.length > 1 ? tokens[1] : "";

      switch (command) {
        case "mkdir":
          new Mkdir().execute(currentFolder, arguments);
          break;
        case "touch":
          new Touch().execute(currentFolder, arguments);
          break;
        case "ls":
          if (arguments.startsWith("--ord=")) {
            String order = arguments.substring(6);
            new Ls().execute(currentFolder, order);
          } else {
            new Ls().execute(currentFolder, "");
          }
          break;
        case "cd":
          new Cd().execute(currentFolder, arguments);
          break;
        case "rm":
          new Rm().execute(currentFolder, arguments);
          break;
        case "pwd":
          System.out.println(currentFolder.getPath());
          break;
        case "exit":
          System.out.println("Exiting...");
          return;
        default:
          System.out.println("Unknown command: " + command);
      }
    }
  }

  public void setCurrentFolder(Folder folder) {
    this.currentFolder = folder;
  }

  public Folder getCurrentFolder() {
    return currentFolder;
  }

  public static void main(String[] args) {
    Folder root = new Folder("", null);
    CLI cli = new CLI(root);
    CLIHolder.setInstance(cli);
    cli.start();
  }
}
