package edu.austral.ingsis.commandtest;

import edu.austral.ingsis.clifford.commands.Invalid;
import edu.austral.ingsis.clifford.filesystem.File;
import edu.austral.ingsis.clifford.filesystem.FileSystem;
import edu.austral.ingsis.clifford.filesystem.Directory;
import edu.austral.ingsis.clifford.result.Result;
import edu.austral.ingsis.clifford.tree.structure.NonBinaryTree;
import edu.austral.ingsis.clifford.tree.structure.Tree;
import edu.austral.ingsis.clifford.tree.structure.TreeNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class InvalidTest {

  private Tree<FileSystem> fileSystem;
  private Invalid invalid;
  private TreeNode<FileSystem> docsNode;

  @BeforeEach
  void setUp() {
    Directory root = new Directory("root");
    Directory docs = new Directory("docs");
    Directory reports = new Directory("reports");
    Directory pictures = new Directory("pictures");
    FileSystem file = new File("file.txt");

    fileSystem = new NonBinaryTree<>(root);
    invalid = new Invalid();

    // Add 'docs' to root
    fileSystem = fileSystem.withChildAddedTo(fileSystem.getRoot(), docs, fileSystem.getRoot());
    TreeNode<FileSystem> docsNodeLocal = fileSystem.findNode(docs);
    docsNode = docsNodeLocal;

    // Add 'reports' as child of 'docs'
    fileSystem = fileSystem.withChildAddedTo(docsNodeLocal, reports, docsNodeLocal);

    // Add 'pictures' as child of 'root'
    fileSystem = fileSystem.withChildAddedTo(fileSystem.getRoot(), pictures, fileSystem.getRoot());

    // Add 'file.txt' as child of 'root'
    fileSystem = fileSystem.withChildAddedTo(fileSystem.getRoot(), file, fileSystem.getRoot());
  }

  @Test
  @DisplayName("Test invalid command with empty argument")
  void testInvalidCommandEmptyArgument() {
    Result<FileSystem> result = invalid.execute(fileSystem, "", fileSystem.getRoot());
    assertEquals("Invalid command", result.getMessage());
    assertSame(fileSystem, result.getTree());
  }

  @Test
  @DisplayName("Test invalid command with argument")
  void testInvalidCommandWithArgument() {
    String invalidArg = "xyz";
    Result<FileSystem> result = invalid.execute(fileSystem, invalidArg, fileSystem.getRoot());
    assertEquals("Invalid command" + invalidArg, result.getMessage());
    assertSame(fileSystem, result.getTree());
  }

  @Test
  @DisplayName("Test invalid command with path-like argument")
  void testInvalidCommandWithPath() {
    String invalidArg = "/some/path";
    Result<FileSystem> result = invalid.execute(fileSystem, invalidArg, fileSystem.getRoot());
    assertEquals("Invalid command" + invalidArg, result.getMessage());
    assertSame(fileSystem, result.getTree());
  }

  @Test
  @DisplayName("Test invalid command from non-root directory")
  void testInvalidCommandFromNonRootDirectory() {
    String invalidArg = "command";
    Result<FileSystem> result = invalid.execute(fileSystem, invalidArg, docsNode);
    assertEquals("Invalid command" + invalidArg, result.getMessage());
    assertSame(fileSystem, result.getTree());
  }

  @Test
  @DisplayName("Test invalid command with special characters")
  void testInvalidCommandWithSpecialChars() {
    String invalidArg = "--help";
    Result<FileSystem> result = invalid.execute(fileSystem, invalidArg, fileSystem.getRoot());
    assertEquals("Invalid command" + invalidArg, result.getMessage());
    assertSame(fileSystem, result.getTree());
  }
}