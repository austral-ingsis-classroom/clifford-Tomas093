package edu.austral.ingsis.clifford.result;

import edu.austral.ingsis.clifford.filesystem.FileSystem;
import edu.austral.ingsis.clifford.tree.structure.Tree;
import edu.austral.ingsis.clifford.tree.structure.TreeNode;

public record RemovalResult<T extends FileSystem>(TreeNode<T> node, boolean found, Tree<T> tree)
    implements Result<T> {

  public RemovalResult(TreeNode<T> node, boolean found) {
    this(node, found, null);
  }

  @Override
  public String getMessage() {
    return found ? "Node was found and processed" : "Node was not found";
  }

  @Override
  public TreeNode<T> getCurrentNode() {
    return node;
  }

  @Override
  public Tree<T> getTree() {
    return tree;
  }
}
