package edu.austral.ingsis.clifford.parser;

import edu.austral.ingsis.clifford.commands.Command;
import edu.austral.ingsis.clifford.filesystem.FileSystem;
import edu.austral.ingsis.clifford.tree.structure.Tree;
import edu.austral.ingsis.clifford.tree.structure.TreeNode;

public sealed interface CommandParser
    permits LsParser, CdParser, MkdirParser, TouchParser, RmParser, PwdParser {

  Command<FileSystem> parse(
      String command, Tree<FileSystem> tree, TreeNode<FileSystem> currentNode);

  Boolean canHandle(String command);
}
