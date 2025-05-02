package edu.austral.ingsis.clifford.parser;

import edu.austral.ingsis.clifford.commands.Command;
import edu.austral.ingsis.clifford.commands.Invalid;
import edu.austral.ingsis.clifford.commands.Mkdir;
import edu.austral.ingsis.clifford.filesystem.FileSystem;
import edu.austral.ingsis.clifford.tree.structure.Tree;
import edu.austral.ingsis.clifford.tree.structure.TreeNode;

public record MkdirParser() implements CommandParser {
  @Override
  public Command<FileSystem> parse(
      String command, Tree<FileSystem> tree, TreeNode<FileSystem> currentNode) {
    String flags = getFlags(command);
    return createMkdirCommand(flags, tree, currentNode);
  }

  @Override
  public Boolean canHandle(String command) {
    return command.startsWith("mkdir");
  }

  private String getFlags(String command) {
    String[] parts = command.split(" ");
    if (parts.length > 1) {
      return parts[1];
    }
    return "";
  }

  private Command<FileSystem> createMkdirCommand(
      String flags, Tree<FileSystem> tree, TreeNode<FileSystem> currentNode) {
    if (flags.isEmpty()) {
      return new Invalid<>(tree, tree.getCurrentNode(), "No directory name provided");
    }
    return new Mkdir<>(tree, flags, currentNode);
  }
}
