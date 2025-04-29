package edu.austral.ingsis.clifford.tree.structure;

import edu.austral.ingsis.clifford.filesystem.FileSystem;
import edu.austral.ingsis.clifford.tree.operator.TreeBuilder;
import edu.austral.ingsis.clifford.tree.operator.TreeCleaner;
import edu.austral.ingsis.clifford.tree.operator.TreeSearcher;

import java.util.Objects;


public final class NonBinaryTree<T extends FileSystem> implements Tree<T> {
  private final TreeNode<T> root;
  private final TreeSearcher<T> searcher;
  private final TreeNode<T> currentNode;

  public NonBinaryTree(T rootData) {
    this.root = rootData != null ? new Node<>(rootData) : null;
    this.searcher = new TreeSearcher<>(this);
    this.currentNode = root;
  }

  public NonBinaryTree(TreeNode<T> root) {
    this.root = root;
    this.searcher = new TreeSearcher<>(this);
    this.currentNode = root;
  }

  public NonBinaryTree(TreeNode<T> root, TreeNode<T> currentNode) {
    this.root = root;
    this.searcher = new TreeSearcher<>(this);
    this.currentNode = currentNode != null ? currentNode : root;
  }

  @Override
  public TreeNode<T> getRoot() {
    return root;
  }

  @Override
  public boolean isEmpty() {
    return root == null;
  }

  @Override
  public Tree<T> withRoot(TreeNode<T> newRoot) {
    return new NonBinaryTree<>(newRoot);
  }

  @Override
  public Tree<T> withCurrentNode(TreeNode<T> newCurrentNode) {
    return new NonBinaryTree<>(this.root, newCurrentNode);
  }

  @Override
  public Tree<T> withChildAddedTo(T parentNodeData, T childData, TreeNode<T> currentNode) {
    if (isEmpty() || parentNodeData == null || childData == null) {
      return this;
    }

    TreeBuilder<T> builder = new TreeBuilder<>(root);
    return builder.withChildAddedTo(parentNodeData, childData, currentNode);
  }

  public TreeNode<T> getCurrentNode() {
    return currentNode;
  }

  @Override
  public Tree<T> withChildAddedTo(TreeNode<T> parentNode, T childData, TreeNode<T> currentNode) {
    if (isEmpty() || parentNode == null || childData == null) {
      return this;
    }

    TreeBuilder<T> builder = new TreeBuilder<>(root);
    return builder.withChildAddedTo(parentNode, childData, currentNode);
  }

  @Override
  public Tree<T> withNodeRemoved(T nodeData) {
    if (isEmpty() || nodeData == null) {
      return this;
    }

    if (root.getData().equals(nodeData)) {
      return this;
    }

    TreeCleaner<T> cleaner = new TreeCleaner<>(this);
    return cleaner.withNodeRemoved(nodeData);
  }

  @Override
  public Tree<T> withNodeRemoved(TreeNode<T> node) {
    if (isEmpty() || node == null) {
      return this;
    }

    if (root.equals(node)) {
      return this;
    }

    TreeCleaner<T> cleaner = new TreeCleaner<>(this);
    return cleaner.withNodeRemoved(node);
  }

  @Override
  public TreeNode<T> findNode(T nodeData) {
    if (isEmpty() || nodeData == null) {
      return null;
    }
    return searcher.findNode(nodeData);
  }

  @Override
  public TreeNode<T> getParentNode(T nodeData) {
    if (isEmpty() || nodeData == null) {
      return null;
    }
    return searcher.getParentNode(nodeData);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    NonBinaryTree<?> that = (NonBinaryTree<?>) o;
    return Objects.equals(root, that.root);
  }

  @Override
  public int hashCode() {
    return Objects.hash(root);
  }
}