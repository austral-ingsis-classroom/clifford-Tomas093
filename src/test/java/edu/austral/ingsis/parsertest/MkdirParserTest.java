package edu.austral.ingsis.parsertest;

import static org.junit.jupiter.api.Assertions.*;

import edu.austral.ingsis.clifford.commands.Command;
import edu.austral.ingsis.clifford.commands.Invalid;
import edu.austral.ingsis.clifford.commands.Mkdir;
import edu.austral.ingsis.clifford.filesystem.Directory;
import edu.austral.ingsis.clifford.filesystem.FileSystem;
import edu.austral.ingsis.clifford.parser.MkdirParser;
import edu.austral.ingsis.clifford.tree.structure.Node;
import edu.austral.ingsis.clifford.tree.structure.NonBinaryTree;
import edu.austral.ingsis.clifford.tree.structure.Tree;
import edu.austral.ingsis.clifford.tree.structure.TreeNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MkdirParserTest {

  private MkdirParser mkdirParser;
  private Tree<FileSystem> tree;
  private TreeNode<FileSystem> currentNode;
  private FileSystem rootDirectory;

  @BeforeEach
  void setUp() {
    mkdirParser = new MkdirParser();
    rootDirectory = new Directory("root");
    currentNode = new Node<>(rootDirectory);
    tree = new NonBinaryTree<>(currentNode);
  }

  @Test
  void canHandleAcceptsMkdirCommand() {
    assertTrue(mkdirParser.canHandle("mkdir docs"));
  }

  @Test
  void canHandleAcceptsSimpleMkdirCommand() {
    assertTrue(mkdirParser.canHandle("mkdir"));
  }

  @Test
  void canHandleRejectsNonMkdirCommand() {
    assertFalse(mkdirParser.canHandle("ls -a"));
    assertFalse(mkdirParser.canHandle("cd /home"));
  }

  @Test
  void parseReturnsMkdirCommandWithDirectoryName() {
    Command<FileSystem> command = mkdirParser.parse("mkdir docs", tree, currentNode);
    assertNotNull(command);
    assertTrue(command instanceof Mkdir);
  }

  @Test
  void parseReturnsInvalidCommandWhenNoDirectoryNameProvided() {
    Command<FileSystem> command = mkdirParser.parse("mkdir", tree, currentNode);
    assertNotNull(command);
    assertTrue(command instanceof Invalid);
  }

  @Test
  void parseWithMultipleArgumentsUsesFirstArgument() {
    Command<FileSystem> command = mkdirParser.parse("mkdir docs temp", tree, currentNode);
    assertNotNull(command);
    assertTrue(command instanceof Mkdir);
  }

  @Test
  void parseWithDirectoryNameIncludingDash() {
    Command<FileSystem> command = mkdirParser.parse("mkdir my-docs", tree, currentNode);
    assertNotNull(command);
    assertTrue(command instanceof Mkdir);
  }

  @Test
  void parseWithDirectoryNameIncludingUnderscore() {
    Command<FileSystem> command = mkdirParser.parse("mkdir my_docs", tree, currentNode);
    assertNotNull(command);
    assertTrue(command instanceof Mkdir);
  }
}
