package edu.austral.ingsis.clifford.commands;

import edu.austral.ingsis.clifford.commands.types.LsOrder;
import edu.austral.ingsis.clifford.filesystem.FileSystem;
import edu.austral.ingsis.clifford.result.Result;
import edu.austral.ingsis.clifford.tree.structure.Tree;
import edu.austral.ingsis.clifford.tree.structure.TreeNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record Ls(Tree<FileSystem> tree, TreeNode<FileSystem> currentNode, LsOrder orderType)
    implements Command<FileSystem> {

  @Override
  public Result<FileSystem> execute() {
    List<String> childrenNames = getChildrenNames(currentNode);
    sort(childrenNames);
    String message = childrenNames.isEmpty() ? "" : String.join(" ", childrenNames);
    return createResult(tree, message);
  }

  private void sort(List<String> childrenNames) {
    switch (orderType) {
      case ASCENDING -> childrenNames.sort(String::compareTo);
      case DESCENDING -> childrenNames.sort(Collections.reverseOrder());
      case DEFAULT -> {}
    }
  }

  private List<String> getChildrenNames(TreeNode<FileSystem> currentNode) {
    List<String> childrenNames = new ArrayList<>();
    for (TreeNode<FileSystem> child : currentNode.getChildren()) {
      childrenNames.add(child.getName());
    }
    return childrenNames;
  }
}
