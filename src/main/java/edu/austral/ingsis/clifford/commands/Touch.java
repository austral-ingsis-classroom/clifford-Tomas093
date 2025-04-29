package edu.austral.ingsis.clifford.commands;

import edu.austral.ingsis.clifford.tree.structure.Tree;
import edu.austral.ingsis.clifford.filesystem.File;
import edu.austral.ingsis.clifford.filesystem.FileSystem;
import edu.austral.ingsis.clifford.result.Result;
import edu.austral.ingsis.clifford.tree.structure.TreeNode;

public record Touch<T extends FileSystem>() implements Command<T>, CreatorCommand {

  @Override
  public Result<T> execute(Tree<T> tree, String name, TreeNode<T> currentNode) {
    if (isValidName(name)) {
      String message = "'" + name + "'" + " file created";
      Tree<T> newTree = createFile(tree, name, currentNode);
      return createResult(newTree.withCurrentNode(newTree.findNode(currentNode.getData())), message);
    } else {
      String message = "Invalid file name";
      return createResult(tree, message);
    }
  }

  private Tree<T> createFile(Tree<T> tree, String name, TreeNode<T> currentNode) {
    FileSystem file = new File(name);
    return tree.withChildAddedTo(currentNode, (T) file, currentNode);
  }
}