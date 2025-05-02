package edu.austral.ingsis.clifford.commands.executer;

import edu.austral.ingsis.clifford.commands.Command;
import edu.austral.ingsis.clifford.filesystem.FileSystem;
import edu.austral.ingsis.clifford.parser.CommandsParser;
import edu.austral.ingsis.clifford.result.Result;
import edu.austral.ingsis.clifford.tree.structure.Tree;
import edu.austral.ingsis.clifford.tree.structure.TreeNode;

public record CommandExecutor() {

  public static Result<FileSystem> executeCommand(
      String input, Tree<FileSystem> tree, TreeNode<FileSystem> currentNode) {
    CommandsParser<FileSystem> parser = new CommandsParser<>();
    Command<FileSystem> command = parser.parse(input, tree, currentNode);
    return command.execute();
  }
}
