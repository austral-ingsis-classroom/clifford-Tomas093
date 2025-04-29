package edu.austral.ingsis.clifford.commands;

import edu.austral.ingsis.clifford.tree.structure.Tree;
import edu.austral.ingsis.clifford.filesystem.FileSystem;
import edu.austral.ingsis.clifford.result.Result;
import edu.austral.ingsis.clifford.tree.structure.TreeNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record Ls<T extends FileSystem>() implements Command<T> {

  @Override
  public Result<T> execute(Tree<T> tree, String argument, TreeNode<T> currentNode) {
    List<String> childrenNames = getChildrenNames(currentNode);
    sort(argument, childrenNames);
    String message = childrenNames.isEmpty() ? "" : String.join(" ", childrenNames);
    return createResult(tree, message);
  }

  private static void sort(String argument, List<String> childrenNames) {
    if (argument != null && argument.startsWith("--ord=")) {
      String order = argument.substring(6);
      if ("asc".equals(order)) {
        Collections.sort(childrenNames);
      } else if ("desc".equals(order)) {
        childrenNames.sort(Collections.reverseOrder());
      }
    }
  }

  private List<String> getChildrenNames(TreeNode<T> currentNode) {
    List<String> childrenNames = new ArrayList<>();
    for (TreeNode<T> child : currentNode.getChildren()) {
      childrenNames.add(child.getName());
    }
    return childrenNames;
  }

}
