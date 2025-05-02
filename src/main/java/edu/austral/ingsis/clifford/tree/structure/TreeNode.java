package edu.austral.ingsis.clifford.tree.structure;

import edu.austral.ingsis.clifford.filesystem.FileSystem;
import java.util.List;

public interface TreeNode<T extends FileSystem> {

  T getData();

  List<? extends TreeNode<T>> getChildren();

  String getName();

  TreeNode<T> withChild(T childData);

  TreeNode<T> withUpdatedChild(int i, TreeNode<T> updatedChild);
}
