package edu.austral.ingsis.clifford.parser;

import edu.austral.ingsis.clifford.commands.*;
import edu.austral.ingsis.clifford.filesystem.FileSystem;
import edu.austral.ingsis.clifford.tree.structure.Tree;
import edu.austral.ingsis.clifford.tree.structure.TreeNode;

public record CommandsParser<T extends FileSystem>() {
  private static final CommandParser[] parsers =
      new CommandParser[] {
        new LsParser(),
        new CdParser(),
        new MkdirParser(),
        new TouchParser(),
        new RmParser(),
        new PwdParser()
      };

  public Command<FileSystem> parse(
      String command, Tree<FileSystem> tree, TreeNode<FileSystem> currentNode) {
    for (CommandParser parser : parsers) {
      if (parser.canHandle(command)) {
        return parser.parse(command, tree, currentNode);
      }
    }
    return new Invalid<>(tree, currentNode, command);
  }
}
