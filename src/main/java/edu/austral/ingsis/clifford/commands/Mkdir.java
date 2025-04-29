package edu.austral.ingsis.clifford.commands;

import edu.austral.ingsis.clifford.filesystem.Folder;
import edu.austral.ingsis.clifford.tree.structure.Tree;
import edu.austral.ingsis.clifford.filesystem.FileSystem;
import edu.austral.ingsis.clifford.filesystem.Directory;
import edu.austral.ingsis.clifford.result.Result;
import edu.austral.ingsis.clifford.tree.structure.TreeNode;

public record Mkdir<T extends FileSystem>() implements Command<T>, CreatorCommand {

  @Override
  public Result<T> execute(Tree<T> tree, String name, TreeNode<T> currentNode) {
    if (isValidName(name)) {
      String message = "'" + name + "'" + " directory created";
      Tree<T> newTree = createFolder(tree, name, currentNode);
      return createResult(newTree.withCurrentNode(newTree.findNode(currentNode.getData())), message);
    }
    else {
      String message = "Invalid directory name";
      return createResult(tree, message);
    }
  }

  private Tree<T> createFolder(Tree<T> tree, String name, TreeNode<T> currentNode) {
    Folder directory = new Directory(name);
    return tree.withChildAddedTo(currentNode, (T) directory, currentNode);
  }
}