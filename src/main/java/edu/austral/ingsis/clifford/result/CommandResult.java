package edu.austral.ingsis.clifford.result;

import edu.austral.ingsis.clifford.filesystem.FileSystem;
import edu.austral.ingsis.clifford.tree.structure.Tree;
import edu.austral.ingsis.clifford.tree.structure.TreeNode;

public record CommandResult<T extends FileSystem>(String message, Tree<T> tree)
    implements Result<T> {

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public TreeNode<T> getCurrentNode() {
    return tree.getCurrentNode();
  }

  @Override
  public Tree<T> getTree() {
    return tree;
  }
}
