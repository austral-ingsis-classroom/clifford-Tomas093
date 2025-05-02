package edu.austral.ingsis.parsertest;

import static org.junit.jupiter.api.Assertions.*;

import edu.austral.ingsis.clifford.commands.*;
import edu.austral.ingsis.clifford.filesystem.Directory;
import edu.austral.ingsis.clifford.filesystem.FileSystem;
import edu.austral.ingsis.clifford.parser.CommandsParser;
import edu.austral.ingsis.clifford.tree.structure.Node;
import edu.austral.ingsis.clifford.tree.structure.NonBinaryTree;
import edu.austral.ingsis.clifford.tree.structure.Tree;
import edu.austral.ingsis.clifford.tree.structure.TreeNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommandsParserTest {

  private CommandsParser commandsParser;
  private Tree<FileSystem> tree;
  private TreeNode<FileSystem> currentNode;
  private FileSystem rootDirectory;

  @BeforeEach
  void setUp() {
    commandsParser = new CommandsParser();
    rootDirectory = new Directory("root");
    currentNode = new Node<>(rootDirectory);
    tree = new NonBinaryTree<>(currentNode);
  }

  @Test
  void parseReturnsLsCommandForLsInput() {
    Command command = commandsParser.parse("ls", tree, currentNode);
    assertNotNull(command);
    assertTrue(command instanceof Ls);
  }

  @Test
  void parseReturnsCdCommandForCdInput() {
    Command command = commandsParser.parse("cd /", tree, currentNode);
    assertNotNull(command);
    assertTrue(command instanceof Cd);
  }

  @Test
  void parseReturnsMkdirCommandForMkdirInput() {
    Command<FileSystem> command = commandsParser.parse("mkdir newdir", tree, currentNode);
    assertNotNull(command);
    assertTrue(command instanceof Mkdir);
  }

  @Test
  void parseReturnsTouchCommandForTouchInput() {
    Command<FileSystem> command = commandsParser.parse("touch file.txt", tree, currentNode);
    assertNotNull(command);
    assertTrue(command instanceof Touch);
  }

  @Test
  void parseReturnsRmCommandForRmInput() {
    Command<FileSystem> command = commandsParser.parse("rm file.txt", tree, currentNode);
    assertNotNull(command);
    assertTrue(command instanceof Rm);
  }

  @Test
  void parseReturnsPwdCommandForPwdInput() {
    Command<FileSystem> command = commandsParser.parse("pwd ", tree, currentNode);
    assertNotNull(command);
    assertTrue(command instanceof Pwd);
  }

  @Test
  void parseReturnsInvalidCommandForUnknownCommand() {
    Command<FileSystem> command = commandsParser.parse("unknown-command", tree, currentNode);
    assertNotNull(command);
    assertTrue(command instanceof Invalid);
  }

  @Test
  void parseReturnsInvalidCommandForEmptyString() {
    Command<FileSystem> command = commandsParser.parse("", tree, currentNode);
    assertNotNull(command);
    assertTrue(command instanceof Invalid);
  }

  @Test
  void parseWithCommandAndArguments() {
    Command<FileSystem> command = commandsParser.parse("ls", tree, currentNode);
    assertNotNull(command);
    assertTrue(command instanceof Ls);
  }

  @Test
  void parseWithComplexCommand() {
    Command<FileSystem> command =
        commandsParser.parse("cd /home/user/documents", tree, currentNode);
    assertNotNull(command);
    assertTrue(command instanceof Cd);
  }
}
