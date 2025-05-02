package edu.austral.ingsis.clifford.parser;

import edu.austral.ingsis.clifford.commands.Cd;
import edu.austral.ingsis.clifford.commands.Command;
import edu.austral.ingsis.clifford.filesystem.FileSystem;
import edu.austral.ingsis.clifford.tree.structure.Tree;
import edu.austral.ingsis.clifford.tree.structure.TreeNode;

public record CdParser() implements CommandParser {
  @Override
  public Command<FileSystem> parse(
      String command, Tree<FileSystem> tree, TreeNode<FileSystem> currentNode) {
    String flags = getFlags(command);
    return createCdCommand(flags, tree, currentNode);
  }

  @Override
  public Boolean canHandle(String command) {
    return command.startsWith("cd");
  }

  private String getFlags(String command) {
    String[] parts = command.split(" ");
    if (parts.length > 1) {
      return parts[1];
    }
    return "";
  }

  private Command<FileSystem> createCdCommand(
      String flags, Tree<FileSystem> tree, TreeNode<FileSystem> currentNode) {
    return new Cd<>(tree, flags, currentNode);
  }
}
