package edu.austral.ingsis.clifford.parser;

import edu.austral.ingsis.clifford.commands.Command;
import edu.austral.ingsis.clifford.commands.Invalid;
import edu.austral.ingsis.clifford.commands.Touch;
import edu.austral.ingsis.clifford.filesystem.FileSystem;
import edu.austral.ingsis.clifford.tree.structure.Tree;
import edu.austral.ingsis.clifford.tree.structure.TreeNode;

public record TouchParser() implements CommandParser {
  @Override
  public Command<FileSystem> parse(
      String command, Tree<FileSystem> tree, TreeNode<FileSystem> currentNode) {
    String flags = getFlags(command);
    return createTouchCommand(flags, tree, currentNode);
  }

  @Override
  public Boolean canHandle(String command) {
    return command.startsWith("touch");
  }

  private String getFlags(String command) {
    String[] parts = command.split(" ");
    if (parts.length > 1) {
      return parts[1];
    }
    return "";
  }

  private Command<FileSystem> createTouchCommand(
      String flags, Tree<FileSystem> tree, TreeNode<FileSystem> currentNode) {
    if (flags.isEmpty()) {
      return new Invalid<>(tree, tree.getCurrentNode(), "No file name provided");
    }
    return new Touch<>(tree, flags, currentNode);
  }
}
