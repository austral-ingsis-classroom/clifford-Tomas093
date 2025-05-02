package edu.austral.ingsis.clifford.commands;

import edu.austral.ingsis.clifford.filesystem.FileSystem;
import edu.austral.ingsis.clifford.filesystem.FileSystemType;
import edu.austral.ingsis.clifford.result.Result;
import edu.austral.ingsis.clifford.tree.structure.Tree;
import edu.austral.ingsis.clifford.tree.structure.TreeNode;

public record Cd<T extends FileSystem>(Tree<T> tree, String argument, TreeNode<T> currentNode)
    implements Command<T> {

  @Override
  public Result<T> execute() {
    if (isCurrentDirectory(argument)) {
      return handleCurrentDirectory(tree, currentNode);
    }

    if (isParentDirectory(argument)) {
      return handleParentDirectory(tree, currentNode);
    }

    return handlePathNavigation(tree, argument, currentNode);
  }

  private boolean isCurrentDirectory(String argument) {
    return ".".equals(argument) || argument.isEmpty();
  }

  private boolean isParentDirectory(String argument) {
    return "..".equals(argument);
  }

  private Result<T> handleCurrentDirectory(Tree<T> tree, TreeNode<T> currentNode) {
    return createResult(tree.withCurrentNode(currentNode), "");
  }

  private Result<T> handleParentDirectory(Tree<T> tree, TreeNode<T> currentNode) {
    TreeNode<T> parentNode = tree.getParentNode(currentNode.getData());
    if (parentNode == null) {
      return createResult(tree, "moved to directory '/'");
    }
    return createResult(
        tree.withCurrentNode(parentNode),
        "moved to directory '" + parentNode.getData().name() + "'");
  }

  private Result<T> handlePathNavigation(Tree<T> tree, String path, TreeNode<T> currentNode) {
    TreeNode<T> targetNode = resolvePath(tree, path, currentNode);

    if (targetNode == null) {
      return createResult(tree, "'" + path + "' directory does not exist");
    }

    if (!isDirectory(targetNode.getData())) {
      return createResult(tree, "'" + targetNode.getData().name() + "' is not a directory");
    }

    return createResult(
        tree.withCurrentNode(targetNode),
        "moved to directory '" + targetNode.getData().name() + "'");
  }

  private TreeNode<T> resolvePath(Tree<T> tree, String path, TreeNode<T> currentNode) {
    if (!path.contains("/")) {
      return findDirectChild(currentNode, path);
    }

    return navigatePathSegments(tree, path, currentNode);
  }

  private TreeNode<T> findDirectChild(TreeNode<T> node, String childName) {
    for (TreeNode<T> child : node.getChildren()) {
      if (child.getName().equals(childName)) {
        return child;
      }
    }
    return null;
  }

  private TreeNode<T> navigatePathSegments(Tree<T> tree, String path, TreeNode<T> currentNode) {
    TreeNode<T> targetNode = initializeTargetNode(tree, path, currentNode);
    String processedPath = processInitialPath(path);

    if (processedPath.isEmpty() && path.startsWith("/")) {
      return targetNode;
    }

    return navigateSegments(tree, processedPath.split("/"), targetNode);
  }

  private TreeNode<T> initializeTargetNode(Tree<T> tree, String path, TreeNode<T> currentNode) {
    return path.startsWith("/") ? tree.getRoot() : currentNode;
  }

  private String processInitialPath(String path) {
    return path.startsWith("/") ? path.substring(1) : path;
  }

  private TreeNode<T> navigateSegments(Tree<T> tree, String[] pathParts, TreeNode<T> startNode) {
    TreeNode<T> currentNode = startNode;

    for (String part : pathParts) {
      currentNode = navigateSingleSegment(tree, part, currentNode);
      if (currentNode == null) {
        return null;
      }
    }

    return currentNode;
  }

  private TreeNode<T> navigateSingleSegment(Tree<T> tree, String segment, TreeNode<T> current) {
    if ("..".equals(segment)) {
      return tree.getParentNode(current.getData());
    }

    return findDirectChild(current, segment);
  }

  private boolean isDirectory(T fileSystem) {
    return fileSystem.getType() == FileSystemType.FOLDER;
  }
}
