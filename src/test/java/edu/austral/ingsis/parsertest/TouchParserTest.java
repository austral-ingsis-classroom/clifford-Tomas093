package edu.austral.ingsis.parsertest;

import static org.junit.jupiter.api.Assertions.*;

import edu.austral.ingsis.clifford.commands.Command;
import edu.austral.ingsis.clifford.commands.Invalid;
import edu.austral.ingsis.clifford.commands.Touch;
import edu.austral.ingsis.clifford.filesystem.Directory;
import edu.austral.ingsis.clifford.filesystem.FileSystem;
import edu.austral.ingsis.clifford.parser.TouchParser;
import edu.austral.ingsis.clifford.tree.structure.Node;
import edu.austral.ingsis.clifford.tree.structure.NonBinaryTree;
import edu.austral.ingsis.clifford.tree.structure.Tree;
import edu.austral.ingsis.clifford.tree.structure.TreeNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TouchParserTest {

  private TouchParser touchParser;
  private Tree<FileSystem> tree;
  private TreeNode<FileSystem> currentNode;
  private FileSystem rootDirectory;

  @BeforeEach
  void setUp() {
    touchParser = new TouchParser();
    rootDirectory = new Directory("root");
    currentNode = new Node<>(rootDirectory);
    tree = new NonBinaryTree<>(currentNode);
  }

  @Test
  void canHandleAcceptsTouchCommand() {
    assertTrue(touchParser.canHandle("touch file.txt"));
  }

  @Test
  void canHandleAcceptsSimpleTouchCommand() {
    assertTrue(touchParser.canHandle("touch"));
  }

  @Test
  void canHandleRejectsNonTouchCommand() {
    assertFalse(touchParser.canHandle("ls -a"));
    assertFalse(touchParser.canHandle("cd /home"));
  }

  @Test
  void parseReturnsInvalidCommandWhenNoFilenameProvided() {
    Command<FileSystem> command = touchParser.parse("touch", tree, currentNode);
    assertNotNull(command);
    assertTrue(command instanceof Invalid);
  }

  @Test
  void parseReturnsTouchCommandWithFilename() {
    Command<FileSystem> command = touchParser.parse("touch file.txt", tree, currentNode);
    assertNotNull(command);
    assertTrue(command instanceof Touch);
  }

  @Test
  void parseWithMultipleArgumentsUsesFirstArgument() {
    Command<FileSystem> command = touchParser.parse("touch file1.txt file2.txt", tree, currentNode);
    assertNotNull(command);
    assertTrue(command instanceof Touch);
  }

  @Test
  void parseWithComplexFilename() {
    Command<FileSystem> command = touchParser.parse("touch my-file_1.2.txt", tree, currentNode);
    assertNotNull(command);
    assertTrue(command instanceof Touch);
  }
}
