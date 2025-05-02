package edu.austral.ingsis.parsertest;

import static org.junit.jupiter.api.Assertions.*;

import edu.austral.ingsis.clifford.commands.Command;
import edu.austral.ingsis.clifford.commands.Invalid;
import edu.austral.ingsis.clifford.commands.Rm;
import edu.austral.ingsis.clifford.filesystem.Directory;
import edu.austral.ingsis.clifford.filesystem.FileSystem;
import edu.austral.ingsis.clifford.parser.RmParser;
import edu.austral.ingsis.clifford.tree.structure.Node;
import edu.austral.ingsis.clifford.tree.structure.NonBinaryTree;
import edu.austral.ingsis.clifford.tree.structure.Tree;
import edu.austral.ingsis.clifford.tree.structure.TreeNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RmParserTest {

  private RmParser rmParser;
  private Tree<FileSystem> tree;
  private TreeNode<FileSystem> currentNode;
  private FileSystem rootDirectory;

  @BeforeEach
  void setUp() {
    rmParser = new RmParser();
    rootDirectory = new Directory("root");
    currentNode = new Node<>(rootDirectory);
    tree = new NonBinaryTree<>(currentNode);
  }

  @Test
  void canHandleAcceptsRmCommand() {
    assertTrue(rmParser.canHandle("rm file.txt"));
  }

  @Test
  void canHandleAcceptsRmCommandWithFlags() {
    assertTrue(rmParser.canHandle("rm --recursive directory"));
  }

  @Test
  void canHandleRejectsNonRmCommand() {
    assertFalse(rmParser.canHandle("ls -a"));
    assertFalse(rmParser.canHandle("cd /home"));
  }

  @Test
  void parseReturnsInvalidCommandWhenNoFilenameProvided() {
    Command<FileSystem> command = rmParser.parse("rm", tree, currentNode);
    assertNotNull(command);
    assertTrue(command instanceof Invalid);
  }

  @Test
  void parseReturnsRmCommandWithFilename() {
    Command<FileSystem> command = rmParser.parse("rm file.txt", tree, currentNode);
    assertNotNull(command);
    assertTrue(command instanceof Rm);
  }

  @Test
  void parseReturnsRmCommandWithRecursiveFlag() {
    Command<FileSystem> command = rmParser.parse("rm --recursive directory", tree, currentNode);
    assertNotNull(command);
    assertTrue(command instanceof Rm);
  }

  @Test
  void parseWithMultipleArgumentsUsesFirstArgument() {
    Command<FileSystem> command = rmParser.parse("rm file1.txt file2.txt", tree, currentNode);
    assertNotNull(command);
    assertTrue(command instanceof Rm);
  }
}
