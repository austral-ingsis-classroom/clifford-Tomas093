package edu.austral.ingsis.parsertest;

import static org.junit.jupiter.api.Assertions.*;

import edu.austral.ingsis.clifford.commands.Command;
import edu.austral.ingsis.clifford.commands.Pwd;
import edu.austral.ingsis.clifford.filesystem.Directory;
import edu.austral.ingsis.clifford.filesystem.FileSystem;
import edu.austral.ingsis.clifford.parser.PwdParser;
import edu.austral.ingsis.clifford.tree.structure.Node;
import edu.austral.ingsis.clifford.tree.structure.NonBinaryTree;
import edu.austral.ingsis.clifford.tree.structure.Tree;
import edu.austral.ingsis.clifford.tree.structure.TreeNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PwdParserTest {

  private PwdParser pwdParser;
  private Tree<FileSystem> tree;
  private TreeNode<FileSystem> currentNode;

  @BeforeEach
  void setUp() {
    pwdParser = new PwdParser();
    FileSystem rootDirectory = new Directory("root");
    currentNode = new Node<>(rootDirectory);
    tree = new NonBinaryTree<>(currentNode);
  }

  @Test
  void canHandleAcceptsPwdCommand() {
    assertTrue(pwdParser.canHandle("pwd"));
  }

  @Test
  void canHandleAcceptsPwdCommandWithTrailingSpace() {
    assertTrue(pwdParser.canHandle("pwd "));
  }

  @Test
  void canHandleRejectsNonPwdCommand() {
    assertFalse(pwdParser.canHandle("ls -a"));
    assertFalse(pwdParser.canHandle("cd /home"));
  }

  @Test
  void parseReturnsPwdCommandWithNoArguments() {
    Command<FileSystem> command = pwdParser.parse("pwd", tree, currentNode);
    assertNotNull(command);
    assertTrue(command instanceof Pwd);
  }

  @Test
  void parseReturnsPwdCommandWithTrailingSpace() {
    Command<FileSystem> command = pwdParser.parse("pwd ", tree, currentNode);
    assertNotNull(command);
    assertTrue(command instanceof Pwd);
  }

  @Test
  void parseIgnoresExtraArgumentsForPwd() {
    Command<FileSystem> command = pwdParser.parse("pwd --option", tree, currentNode);
    assertNotNull(command);
    assertTrue(command instanceof Pwd);
  }

  @Test
  void parseWithMultipleArgumentsStillWorks() {
    Command<FileSystem> command = pwdParser.parse("pwd --option1 --option2", tree, currentNode);
    assertNotNull(command);
    assertTrue(command instanceof Pwd);
  }

  @Test
  void parseWithComplexArgumentStillWorks() {
    Command<FileSystem> command = pwdParser.parse("pwd /path/to/directory", tree, currentNode);
    assertNotNull(command);
    assertTrue(command instanceof Pwd);
  }
}
