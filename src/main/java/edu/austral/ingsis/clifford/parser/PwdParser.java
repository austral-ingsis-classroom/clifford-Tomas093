package edu.austral.ingsis.clifford.parser;

import edu.austral.ingsis.clifford.commands.Command;
import edu.austral.ingsis.clifford.commands.Pwd;
import edu.austral.ingsis.clifford.filesystem.FileSystem;
import edu.austral.ingsis.clifford.tree.structure.Tree;
import edu.austral.ingsis.clifford.tree.structure.TreeNode;

public record PwdParser() implements CommandParser {
  @Override
  public Command<FileSystem> parse(
      String command, Tree<FileSystem> tree, TreeNode<FileSystem> currentNode) {
    return new Pwd<>(tree, "", currentNode);
  }

  @Override
  public Boolean canHandle(String command) {
    return command.trim().startsWith("pwd");
  }
}
