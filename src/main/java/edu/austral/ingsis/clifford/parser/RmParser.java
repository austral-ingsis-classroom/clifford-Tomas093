package edu.austral.ingsis.clifford.parser;

import edu.austral.ingsis.clifford.commands.Command;
import edu.austral.ingsis.clifford.commands.Invalid;
import edu.austral.ingsis.clifford.commands.Rm;
import edu.austral.ingsis.clifford.filesystem.FileSystem;
import edu.austral.ingsis.clifford.tree.structure.Tree;
import edu.austral.ingsis.clifford.tree.structure.TreeNode;

public record RmParser() implements CommandParser {
  @Override
  public Command<FileSystem> parse(
      String command, Tree<FileSystem> tree, TreeNode<FileSystem> currentNode) {
    String[] parts = command.trim().split(" ");

    if (parts.length < 2) {
      return new Invalid<>(tree, currentNode, "No file name provided");
    }

    boolean recursive = false;
    String fileName;

    if (parts.length > 2 && "--recursive".equals(parts[1])) {
      recursive = true;
      fileName = parts[2];
    } else {
      fileName = parts[1];
    }
    return new Rm<>(tree, fileName, currentNode, recursive);
  }

  @Override
  public Boolean canHandle(String command) {
    return command.trim().startsWith("rm");
  }
}
