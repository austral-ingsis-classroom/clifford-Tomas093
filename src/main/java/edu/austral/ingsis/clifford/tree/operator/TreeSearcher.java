package edu.austral.ingsis.clifford.tree.operator;

import edu.austral.ingsis.clifford.filesystem.FileSystem;
import edu.austral.ingsis.clifford.tree.structure.Tree;
import edu.austral.ingsis.clifford.tree.structure.TreeNode;
import java.util.Objects;

public record TreeSearcher<T extends FileSystem>(Tree<T> tree) {

  public boolean isEmpty() {
    return tree.isEmpty();
  }

  public TreeNode<T> getRoot() {
    if (isEmpty()) {
      return null;
    }
    return tree.getRoot();
  }

  public TreeNode<T> findNode(T nodeData) {
    if (isEmpty()) {
      return null;
    }
    return findNodeRecursive(Objects.requireNonNull(getRoot()), nodeData);
  }

  private TreeNode<T> findNodeRecursive(TreeNode<T> current, T nodeData) {
    if (current.getData().equals(nodeData)) {
      return current;
    }

    for (TreeNode<T> child : current.getChildren()) {
      TreeNode<T> found = findNodeRecursive(child, nodeData);
      if (found != null) {
        return found;
      }
    }

    return null;
  }

  public TreeNode<T> getParentNode(T nodeData) {
    if (isEmpty()) {
      return null;
    }
    return getParentNodeRecursive(Objects.requireNonNull(getRoot()), nodeData);
  }

  private TreeNode<T> getParentNodeRecursive(TreeNode<T> current, T nodeData) {
    for (TreeNode<T> child : current.getChildren()) {
      if (child.getData().equals(nodeData)) {
        return current;
      }
      TreeNode<T> found = getParentNodeRecursive(child, nodeData);
      if (found != null) {
        return found;
      }
    }
    return null;
  }
}
