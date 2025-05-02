package edu.austral.ingsis.parsertest;

import static org.junit.jupiter.api.Assertions.*;

import edu.austral.ingsis.clifford.commands.Command;
import edu.austral.ingsis.clifford.commands.Ls;
import edu.austral.ingsis.clifford.filesystem.Directory;
import edu.austral.ingsis.clifford.filesystem.FileSystem;
import edu.austral.ingsis.clifford.parser.LsParser;
import edu.austral.ingsis.clifford.tree.structure.Node;
import edu.austral.ingsis.clifford.tree.structure.NonBinaryTree;
import edu.austral.ingsis.clifford.tree.structure.Tree;
import edu.austral.ingsis.clifford.tree.structure.TreeNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LsParserTest {

  private LsParser lsParser;
  private Tree<FileSystem> tree;
  private TreeNode<FileSystem> currentNode;
  private FileSystem rootDirectory;

  @BeforeEach
  void setUp() {
    lsParser = new LsParser();
    rootDirectory = new Directory("root");
    currentNode = new Node<>(rootDirectory);
    tree = new NonBinaryTree<>(currentNode);
  }

  @Test
  void canHandleAcceptsSimpleLsCommand() {
    assertTrue(lsParser.canHandle("ls"));
  }

  @Test
  void canHandleAcceptsLsCommandWithFlags() {
    assertTrue(lsParser.canHandle("ls --ord=name"));
  }

  @Test
  void canHandleRejectsNonLsCommand() {
    assertFalse(lsParser.canHandle("cd /path"));
    assertFalse(lsParser.canHandle("mkdir dir"));
  }

  @Test
  void parseReturnsLsCommandForSimpleCommand() {
    Command<FileSystem> command = lsParser.parse("ls", tree, currentNode);
    assertNotNull(command);
    assertTrue(command instanceof Ls);
  }

  @Test
  void parseReturnsLsCommandWithNameOrder() {
    Command<FileSystem> command = lsParser.parse("ls --ord=name", tree, currentNode);
    assertNotNull(command);
    assertTrue(command instanceof Ls);
  }

  @Test
  void parseReturnsLsCommandWithDateOrder() {
    Command<FileSystem> command = lsParser.parse("ls --ord=date", tree, currentNode);
    assertNotNull(command);
    assertTrue(command instanceof Ls);
  }

  @Test
  void parseReturnsLsCommandWithTypeOrder() {
    Command<FileSystem> command = lsParser.parse("ls --ord=type", tree, currentNode);
    assertNotNull(command);
    assertTrue(command instanceof Ls);
  }

  @Test
  void parseReturnsLsCommandWithSizeOrder() {
    Command<FileSystem> command = lsParser.parse("ls --ord=size", tree, currentNode);
    assertNotNull(command);
    assertTrue(command instanceof Ls);
  }

  @Test
  void parseWithMultipleArguments() {
    Command<FileSystem> command = lsParser.parse("ls -a --ord=name", tree, currentNode);
    assertNotNull(command);
    assertTrue(command instanceof Ls);
  }

  @Test
  void parseWithInvalidOrderUsesDefault() {
    Command<FileSystem> command = lsParser.parse("ls --ord=invalid", tree, currentNode);
    assertNotNull(command);
    assertTrue(command instanceof Ls);
  }
}
