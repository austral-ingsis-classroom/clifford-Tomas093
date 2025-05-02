package edu.austral.ingsis.clifford.parser;

import edu.austral.ingsis.clifford.commands.Command;
import edu.austral.ingsis.clifford.commands.Ls;
import edu.austral.ingsis.clifford.commands.types.LsOrder;
import edu.austral.ingsis.clifford.filesystem.FileSystem;
import edu.austral.ingsis.clifford.tree.structure.Tree;
import edu.austral.ingsis.clifford.tree.structure.TreeNode;

public record LsParser() implements CommandParser {

  @Override
  public Command<FileSystem> parse(
      String command, Tree<FileSystem> tree, TreeNode<FileSystem> currentNode) {
    LsOrder order = getFlags(command);
    if (order == null) {
      order = LsOrder.DEFAULT;
    }
    return createLsCommand(order, tree, currentNode);
  }

  @Override
  public Boolean canHandle(String command) {
    return command.startsWith("ls");
  }

  private LsOrder getFlags(String command) {
    String[] parts = command.split(" ");
    for (String part : parts) {
      if (part.startsWith("--ord=")) {
        String order = part.substring(6);
        return LsOrder.fromString(order);
      }
    }
    return null;
  }

  private Command<FileSystem> createLsCommand(
      LsOrder order, Tree<FileSystem> tree, TreeNode<FileSystem> currentNode) {
    return new Ls(tree, currentNode, order);
  }
}
