package edu.austral.ingsis.clifford.commands.executer;

import edu.austral.ingsis.clifford.commands.Command;
import edu.austral.ingsis.clifford.filesystem.FileSystem;
import edu.austral.ingsis.clifford.parser.CommandParser;
import edu.austral.ingsis.clifford.parser.StringParser;
import edu.austral.ingsis.clifford.result.Result;
import edu.austral.ingsis.clifford.tree.structure.Tree;
import edu.austral.ingsis.clifford.tree.structure.TreeNode;

public record CommandExecutor() {

  public static Result<FileSystem> executeCommand(String input, Tree<FileSystem> tree, TreeNode<FileSystem> currentNode) {
    CommandParser commandParser = new CommandParser();
    String[] parsedInput = StringParser.parseCommandArgument(input);
    Command<FileSystem> command = commandParser.parse(parsedInput[0]);
    return command.execute(tree, parsedInput[1], currentNode);
  }
}
