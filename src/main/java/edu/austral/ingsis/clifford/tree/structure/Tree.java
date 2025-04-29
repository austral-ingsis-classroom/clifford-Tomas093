package edu.austral.ingsis.clifford.tree.structure;

import edu.austral.ingsis.clifford.filesystem.FileSystem;

public interface Tree<T extends FileSystem> {

  TreeNode<T> getRoot();

  boolean isEmpty();

  Tree<T> withRoot(TreeNode<T> newRoot);

  Tree<T> withChildAddedTo(T parentNodeData, T childData, TreeNode<T> currentNode);

  Tree<T> withChildAddedTo(TreeNode<T> parentNode, T childData, TreeNode<T> currentNode);

  Tree<T> withNodeRemoved(T nodeData);

  Tree<T> withNodeRemoved(TreeNode<T> node);

  TreeNode<T> findNode(T nodeData);

  TreeNode<T> getParentNode(T nodeData);

  Tree<T> withCurrentNode(TreeNode<T> newRootData);

  TreeNode<T> getCurrentNode();
}