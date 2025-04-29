package edu.austral.ingsis.clifford.commands;

import edu.austral.ingsis.clifford.tree.structure.Tree;
import edu.austral.ingsis.clifford.filesystem.FileSystem;
import edu.austral.ingsis.clifford.filesystem.FileSystemType;
import edu.austral.ingsis.clifford.result.Result;
import edu.austral.ingsis.clifford.tree.structure.TreeNode;

import java.util.Optional;

public record Rm<T extends FileSystem>() implements Command<T> {

  @Override
  public Result<T> execute(Tree<T> tree, String name, TreeNode<T> currentNode) {
    CommandOptions options = parseOptions(name);
    Optional<? extends TreeNode<T>> nodeToRemove = findChildByName(options.name(), currentNode);

    if (nodeToRemove.isEmpty()) {
      return createNotFoundResult(tree, options.name());
    }

    Result<T> validationResult = validateRemoval(tree, nodeToRemove.get(), options);
    if (validationResult != null) return validationResult;

    return performRemoval(tree, options.name(), currentNode);
  }

  private CommandOptions parseOptions(String name) {
    boolean recursive = false;
    String cleanName = name;

    if (name.contains("--recursive")) {
      recursive = true;
      cleanName = name.replace("--recursive", "").trim();
    }

    return new CommandOptions(cleanName, recursive);
  }

  private Result<T> validateRemoval(Tree<T> tree, TreeNode<T> node, CommandOptions options) {
    if (node.getData().getType() == FileSystemType.DIRECTORY && !options.recursive()) {
      return createResult(tree, "cannot remove '" + options.name() + "', is a directory");
    }
    return null;
  }

  private Result<T> createNotFoundResult(Tree<T> tree, String name) {
    return createResult(tree, "'" + name + "' not found");
  }

  private Result<T> performRemoval(Tree<T> tree, String name, TreeNode<T> currentNode) {
    Tree<T> updatedTree = removeFileSystem(tree, name, currentNode);
    TreeNode<T> newCurrentNode = updateCurrentNode(updatedTree, currentNode);
    updatedTree = updatedTree.withCurrentNode(newCurrentNode);

    return createResult(updatedTree, "'" + name + "' removed");
  }

  private TreeNode<T> updateCurrentNode(Tree<T> tree, TreeNode<T> currentNode) {
    TreeNode<T> newNode = tree.findNode(currentNode.getData());
    return newNode != null ? newNode : tree.getRoot();
  }

  private Optional<? extends TreeNode<T>> findChildByName(String name, TreeNode<T> currentNode) {
    return currentNode.getChildren().stream().filter(child -> child.getName().equals(name)).findFirst();
  }

  private Tree<T> removeFileSystem(Tree<T> tree, String name, TreeNode<T> currentNode) {
    Optional<? extends TreeNode<T>> nodeToRemove = findChildByName(name, currentNode);
    return nodeToRemove.map(tree::withNodeRemoved).orElse(tree);
  }

  private record CommandOptions(String name, boolean recursive) {
  }
}