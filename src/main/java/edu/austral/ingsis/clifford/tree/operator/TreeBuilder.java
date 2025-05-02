package edu.austral.ingsis.clifford.tree.operator;

import edu.austral.ingsis.clifford.filesystem.FileSystem;
import edu.austral.ingsis.clifford.tree.structure.NonBinaryTree;
import edu.austral.ingsis.clifford.tree.structure.Tree;
import edu.austral.ingsis.clifford.tree.structure.TreeNode;

public record TreeBuilder<T extends FileSystem>(TreeNode<T> root) {

  public Tree<T> withRoot(TreeNode<T> newRoot) {
    return new NonBinaryTree<>(newRoot);
  }

  public Tree<T> withChildAddedTo(T parentNodeData, T childData, TreeNode<T> currentNode) {
    TreeNode<T> newRoot = addChildToNodeRecursive(root, parentNodeData, childData);
    return new NonBinaryTree<>(newRoot, currentNode);
  }

  public Tree<T> withChildAddedTo(TreeNode<T> parentNode, T childData, TreeNode<T> currentNode) {
    TreeNode<T> newRoot = addChildToNodeRecursive(root, parentNode, childData);
    return new NonBinaryTree<>(newRoot, currentNode);
  }

  private TreeNode<T> addChildToNodeRecursive(TreeNode<T> current, T parentNodeData, T childData) {
    if (current.getData().equals(parentNodeData)) {
      return current.withChild(childData);
    }

    return traverseAndUpdateChildren(
        current, node -> addChildToNodeRecursive(node, parentNodeData, childData));
  }

  private TreeNode<T> addChildToNodeRecursive(
      TreeNode<T> current, TreeNode<T> parentNode, T childData) {
    if (current.equals(parentNode)) {
      return current.withChild(childData);
    }

    return traverseAndUpdateChildren(
        current, node -> addChildToNodeRecursive(node, parentNode, childData));
  }

  private TreeNode<T> traverseAndUpdateChildren(
      TreeNode<T> current,
      java.util.function.Function<TreeNode<T>, TreeNode<T>> recursiveOperation) {
    TreeNode<T> updatedCurrent = current;
    for (int i = 0; i < current.getChildren().size(); i++) {
      TreeNode<T> child = current.getChildren().get(i);
      TreeNode<T> updatedChild = recursiveOperation.apply(child);
      if (updatedChild != child) {
        updatedCurrent = updatedCurrent.withUpdatedChild(i, updatedChild);
      }
    }
    return updatedCurrent;
  }
}
