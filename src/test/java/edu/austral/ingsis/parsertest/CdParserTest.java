package edu.austral.ingsis.parsertest;

import static org.junit.jupiter.api.Assertions.*;

import edu.austral.ingsis.clifford.commands.Cd;
import edu.austral.ingsis.clifford.commands.Command;
import edu.austral.ingsis.clifford.filesystem.Directory;
import edu.austral.ingsis.clifford.filesystem.FileSystem;
import edu.austral.ingsis.clifford.parser.CdParser;
import edu.austral.ingsis.clifford.tree.structure.Node;
import edu.austral.ingsis.clifford.tree.structure.NonBinaryTree;
import edu.austral.ingsis.clifford.tree.structure.Tree;
import edu.austral.ingsis.clifford.tree.structure.TreeNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CdParserTest {

  private CdParser cdParser;
  private Tree<FileSystem> tree;
  private TreeNode<FileSystem> currentNode;
  private FileSystem rootDirectory;

  @BeforeEach
  void setUp() {
    cdParser = new CdParser();
    rootDirectory = new Directory("root");
    currentNode = new Node<>(rootDirectory);
    tree = new NonBinaryTree<>(currentNode);
  }

  @Test
  void canHandleAcceptsSimpleCdCommand() {
    assertTrue(cdParser.canHandle("cd"));
  }

  @Test
  void canHandleAcceptsCdCommandWithPath() {
    assertTrue(cdParser.canHandle("cd /home"));
  }

  @Test
  void canHandleRejectsNonCdCommand() {
    assertFalse(cdParser.canHandle("ls -a"));
    assertFalse(cdParser.canHandle("mkdir dir"));
  }

  @Test
  void parseReturnsCommandForSimpleCdCommand() {
    Command<FileSystem> command = cdParser.parse("cd", tree, currentNode);
    assertNotNull(command);
    assertTrue(command instanceof Cd);
  }

  @Test
  void parseReturnsCdCommandWithPath() {
    Command<FileSystem> command = cdParser.parse("cd /home", tree, currentNode);
    assertNotNull(command);
    assertTrue(command instanceof Cd);
  }

  @Test
  void parseReturnsCdCommandWithRelativePath() {
    Command<FileSystem> command = cdParser.parse("cd ./docs", tree, currentNode);
    assertNotNull(command);
    assertTrue(command instanceof Cd);
  }

  @Test
  void parseReturnsCdCommandWithParentPath() {
    Command<FileSystem> command = cdParser.parse("cd ..", tree, currentNode);
    assertNotNull(command);
    assertTrue(command instanceof Cd);
  }

  @Test
  void parseWithNoPathArgument() {
    Command<FileSystem> command = cdParser.parse("cd", tree, currentNode);
    assertNotNull(command);
    assertTrue(command instanceof Cd);
  }

  @Test
  void parseWithMultiplePathSegments() {
    Command<FileSystem> command = cdParser.parse("cd /home/user/docs", tree, currentNode);
    assertNotNull(command);
    assertTrue(command instanceof Cd);
  }
}
