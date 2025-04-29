package edu.austral.ingsis.clifford.commands.executer;

import edu.austral.ingsis.clifford.filesystem.FileSystem;
import edu.austral.ingsis.clifford.filesystem.Directory;
import edu.austral.ingsis.clifford.result.Result;
import edu.austral.ingsis.clifford.tree.structure.NonBinaryTree;
import edu.austral.ingsis.clifford.tree.structure.Tree;
import edu.austral.ingsis.clifford.tree.structure.TreeNode;

import java.util.Scanner;

public class Cli {
  private Tree<FileSystem> tree;
  private TreeNode<FileSystem> currentNode;
  private final Scanner scanner = new Scanner(System.in);
  private boolean running = true;

  public Cli() {
    // Initialize with root folder
    Directory rootDirectory = new Directory("root");
    tree = new NonBinaryTree<>(rootDirectory);
    currentNode = tree.getRoot();
  }

  public void run() {
    System.out.println("CLI File System Simulator");
    System.out.println("Type 'exit' to quit");

    while (running) {
      System.out.print("> ");
      String input = scanner.nextLine().trim();

      if (input.equalsIgnoreCase("exit")) {
        running = false;
        continue;
      }

      if (!input.isEmpty()) {
        try {
          Result<FileSystem> result = CommandExecutor.executeCommand(input, tree, currentNode);

          // Update tree and current node
          tree = result.getTree();
          currentNode = result.getCurrentNode();

          // Display result message
          System.out.println(result.getMessage());
        } catch (Exception e) {
          System.out.println("Error: " + e.getMessage());
        }
      }
    }

    System.out.println("Exiting CLI");
    scanner.close();
  }

  public static void main(String[] args) {
    new Cli().run();
  }
}