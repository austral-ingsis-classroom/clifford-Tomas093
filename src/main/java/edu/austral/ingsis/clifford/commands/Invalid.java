package edu.austral.ingsis.clifford.commands;

import edu.austral.ingsis.clifford.filesystem.FileSystem;
import edu.austral.ingsis.clifford.result.CommandResult;
import edu.austral.ingsis.clifford.result.Result;
import edu.austral.ingsis.clifford.tree.structure.Tree;
import edu.austral.ingsis.clifford.tree.structure.TreeNode;

public record Invalid<T extends FileSystem>(Tree<T> tree, TreeNode<T> currentNode, String argument)
    implements Command<T> {
  @Override
  public Result<T> execute() {
    return new CommandResult<>("Invalid command" + argument, tree);
  }
}
