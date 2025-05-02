package edu.austral.ingsis.clifford.tree.operator;

import edu.austral.ingsis.clifford.filesystem.FileSystem;
import edu.austral.ingsis.clifford.result.RemovalResult;
import edu.austral.ingsis.clifford.tree.structure.Node;
import edu.austral.ingsis.clifford.tree.structure.NonBinaryTree;
import edu.austral.ingsis.clifford.tree.structure.Tree;
import edu.austral.ingsis.clifford.tree.structure.TreeNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public record TreeCleaner<T extends FileSystem>(Tree<T> tree) {

  private TreeNode<T> getRoot() {
    if (tree.isEmpty()) {
      return null;
    }
    return tree.getRoot();
  }

  private boolean isEmpty() {
    return getRoot() == null || getRoot().getData() == null;
  }

  public Tree<T> withNodeRemoved(T nodeData) {
    if (tree.isEmpty()) {
      return tree;
    }

    if (Objects.requireNonNull(getRoot()).getData() != null
        && getRoot().getData().equals(nodeData)) {
      return tree;
    }

    RemovalResult<T> result =
        removeNodeRecursive(
            getRoot(), child -> child.getData() != null && child.getData().equals(nodeData));

    if (result.found()) {
      return new NonBinaryTree<>(result.node());
    }
    return tree;
  }

  public Tree<T> withNodeRemoved(TreeNode<T> node) {
    if (isEmpty()) {
      return tree;
    }

    if (getRoot().equals(node)) {
      return tree;
    }

    RemovalResult<T> result = removeNodeRecursive(getRoot(), child -> child.equals(node));

    if (result.found()) {
      return new NonBinaryTree<>(result.node());
    }
    return tree;
  }

  private RemovalResult<T> removeNodeRecursive(
      TreeNode<T> current, Predicate<TreeNode<T>> matchesNode) {
    List<? extends TreeNode<T>> currentChildren = current.getChildren();
    List<TreeNode<T>> newChildren = new ArrayList<>();
    boolean found = false;

    for (TreeNode<T> child : currentChildren) {
      if (matchesNode.test(child)) {
        found = true;
      } else {
        newChildren.add(child);
      }
    }

    if (found) {
      if (newChildren.size() == currentChildren.size()) {
        return new RemovalResult<>(current, true);
      }

      TreeNode<T> result = new Node<>(current.getData());
      for (TreeNode<T> child : newChildren) {
        result = result.withChild(child.getData());
      }
      return new RemovalResult<>(result, true);
    }

    for (int i = 0; i < currentChildren.size(); i++) {
      TreeNode<T> child = currentChildren.get(i);
      RemovalResult<T> childResult = removeNodeRecursive(child, matchesNode);

      if (childResult.found()) {
        if (childResult.node() != child) {
          return new RemovalResult<>(current.withUpdatedChild(i, childResult.node()), true);
        }
        return new RemovalResult<>(current, true);
      }
    }

    return new RemovalResult<>(current, false);
  }
}
