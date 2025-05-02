package edu.austral.ingsis.clifford.commands;

import edu.austral.ingsis.clifford.filesystem.FileSystem;
import edu.austral.ingsis.clifford.filesystem.FileSystemType;
import edu.austral.ingsis.clifford.result.CommandResult;
import edu.austral.ingsis.clifford.result.Result;
import edu.austral.ingsis.clifford.tree.structure.Tree;
import edu.austral.ingsis.clifford.tree.structure.TreeNode;
import java.util.Optional;

public record Rm<T extends FileSystem>(
    Tree<T> tree, String name, TreeNode<T> currentNode, Boolean recursive) implements Command<T> {

  @Override
  public Result<T> execute() {
    CommandOptions options = parseOptions(name);
    Optional<? extends TreeNode<T>> nodeToRemove = findChildByName(options.name(), currentNode);

    if (nodeToRemove.isEmpty()) {
      return createErrorResult("'" + options.name() + "' not found");
    }

    Result<T> validationResult = validateRemoval(nodeToRemove.get(), options);
    if (validationResult != null) return validationResult;

    return performRemoval(options.name());
  }

  private CommandOptions parseOptions(String name) {
    boolean recursive = this.recursive;
    String cleanName = name;

    if (cleanName.startsWith("--recursive ")) {
      recursive = true;
      cleanName = cleanName.substring("--recursive ".length()).trim();
    }

    return new CommandOptions(cleanName, recursive);
  }

  private Optional<? extends TreeNode<T>> findChildByName(String name, TreeNode<T> node) {
    return node.getChildren().stream()
        .filter(child -> child.getData().name().equals(name))
        .findFirst();
  }

  private Result<T> validateRemoval(TreeNode<T> node, CommandOptions options) {
    if (node.getData().getType() == FileSystemType.FOLDER && !options.recursive()) {
      return createErrorResult("cannot remove '" + options.name() + "', is a directory");
    }
    return null;
  }

  private Result<T> performRemoval(String name) {
    Tree<T> updatedTree = removeFileSystem(name);
    TreeNode<T> newCurrentNode = updateCurrentNode(updatedTree);
    updatedTree = updatedTree.withCurrentNode(newCurrentNode);

    return createSuccessResult(updatedTree, "'" + name + "' removed");
  }

  private Tree<T> removeFileSystem(String name) {
    Optional<? extends TreeNode<T>> nodeToRemove = findChildByName(name, currentNode);
    return nodeToRemove.map(tree::withNodeRemoved).orElse(tree);
  }

  private TreeNode<T> updateCurrentNode(Tree<T> updatedTree) {
    TreeNode<T> newNode = updatedTree.findNode(currentNode.getData());
    return newNode != null ? newNode : updatedTree.getRoot();
  }

  private Result<T> createErrorResult(String message) {
    return new CommandResult<>(message, tree);
  }

  private Result<T> createSuccessResult(Tree<T> updatedTree, String message) {
    return new CommandResult<>(message, updatedTree);
  }

  private record CommandOptions(String name, boolean recursive) {}
}
