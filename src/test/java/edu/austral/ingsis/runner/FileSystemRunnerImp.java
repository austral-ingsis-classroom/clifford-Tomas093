package edu.austral.ingsis.runner;

import edu.austral.ingsis.clifford.commands.executer.CommandExecutor;
import edu.austral.ingsis.clifford.filesystem.FileSystem;
import edu.austral.ingsis.clifford.filesystem.Directory;
import edu.austral.ingsis.clifford.result.Result;
import edu.austral.ingsis.clifford.tree.structure.NonBinaryTree;
import edu.austral.ingsis.clifford.tree.structure.Tree;
import edu.austral.ingsis.clifford.tree.structure.TreeNode;

import java.util.ArrayList;
import java.util.List;

public class FileSystemRunnerImp implements FileSystemRunner {

  @Override
  public List<String> executeCommands(List<String> commands) {
    List<String> results = new ArrayList<>();
    Tree<FileSystem> tree = new NonBinaryTree<>(new Directory("/"));
    TreeNode<FileSystem> currentNode = tree.getRoot();

    for (String commandString : commands) {
      Result<FileSystem> result = CommandExecutor.executeCommand(commandString, tree, currentNode);

      results.add(result.getMessage());

      tree = result.getTree();
      currentNode = result.getCurrentNode();
    }

    return results;
  }
}
