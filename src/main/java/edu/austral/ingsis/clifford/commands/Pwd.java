package edu.austral.ingsis.clifford.commands;

import edu.austral.ingsis.clifford.tree.structure.Tree;
import edu.austral.ingsis.clifford.tree.structure.TreeNode;
import edu.austral.ingsis.clifford.filesystem.FileSystem;
import edu.austral.ingsis.clifford.result.Result;

public record Pwd<T extends FileSystem>() implements Command<T> {

  @Override
  public Result<T> execute(Tree<T> tree, String argument, TreeNode<T> currentNode) {
    String absolutePath = getAbsolutePath(tree, currentNode);
    return createResult(tree, absolutePath);
  }

  private String getAbsolutePath(Tree<T> tree, TreeNode<T> currentNode) {
    if (currentNode.equals(tree.getRoot())) {
      return "/";
    }

    StringBuilder path = new StringBuilder();
    TreeNode<T> node = currentNode;

    java.util.List<String> pathParts = new java.util.ArrayList<>();

    while (node != null && !node.equals(tree.getRoot())) {
      pathParts.add(0, node.getData().name());
      node = tree.getParentNode(node.getData());
    }

    for (String part : pathParts) {
      path.append("/").append(part);
    }

    return path.toString();
  }
}