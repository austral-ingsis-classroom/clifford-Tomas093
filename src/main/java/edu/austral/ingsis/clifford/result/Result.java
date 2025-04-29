package edu.austral.ingsis.clifford.result;

  import edu.austral.ingsis.clifford.tree.structure.Tree;
  import edu.austral.ingsis.clifford.filesystem.FileSystem;
  import edu.austral.ingsis.clifford.tree.structure.TreeNode;

  public sealed interface Result<T extends FileSystem> permits RemovalResult, CommandResult {

    String getMessage();

    TreeNode<T> getCurrentNode();

    Tree<T> getTree();

  }