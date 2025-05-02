package edu.austral.ingsis.commandtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import edu.austral.ingsis.clifford.commands.Invalid;
import edu.austral.ingsis.clifford.filesystem.Directory;
import edu.austral.ingsis.clifford.filesystem.File;
import edu.austral.ingsis.clifford.filesystem.FileSystem;
import edu.austral.ingsis.clifford.result.Result;
import edu.austral.ingsis.clifford.tree.structure.NonBinaryTree;
import edu.austral.ingsis.clifford.tree.structure.Tree;
import edu.austral.ingsis.clifford.tree.structure.TreeNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class InvalidTest {

  private Tree<FileSystem> fileSystem;
  private TreeNode<FileSystem> docsNode;

  @BeforeEach
  void setUp() {
    Directory root = new Directory("root");
    Directory docs = new Directory("docs");
    Directory reports = new Directory("reports");
    Directory pictures = new Directory("pictures");
    FileSystem file = new File("file.txt");

    fileSystem = new NonBinaryTree<>(root);

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
    Invalid<FileSystem> invalid = new Invalid<>(fileSystem, fileSystem.getRoot(), "");
    Result<FileSystem> result = invalid.execute();
    assertEquals("Invalid command", result.getMessage());
    assertSame(fileSystem, result.getTree());
  }

  @Test
  @DisplayName("Test invalid command with argument")
  void testInvalidCommandWithArgument() {
    String invalidArg = "xyz";
    Invalid<FileSystem> invalid = new Invalid<>(fileSystem, fileSystem.getRoot(), invalidArg);
    Result<FileSystem> result = invalid.execute();
    assertEquals("Invalid command" + invalidArg, result.getMessage());
    assertSame(fileSystem, result.getTree());
  }

  @Test
  @DisplayName("Test invalid command with path-like argument")
  void testInvalidCommandWithPath() {
    String invalidArg = "/some/path";
    Invalid<FileSystem> invalid = new Invalid<>(fileSystem, fileSystem.getRoot(), invalidArg);
    Result<FileSystem> result = invalid.execute();
    assertEquals("Invalid command" + invalidArg, result.getMessage());
    assertSame(fileSystem, result.getTree());
  }

  @Test
  @DisplayName("Test invalid command from non-root directory")
  void testInvalidCommandFromNonRootDirectory() {
    String invalidArg = "command";
    Invalid<FileSystem> invalid = new Invalid<>(fileSystem, docsNode, invalidArg);
    Result<FileSystem> result = invalid.execute();
    assertEquals("Invalid command" + invalidArg, result.getMessage());
    assertSame(fileSystem, result.getTree());
  }

  @Test
  @DisplayName("Test invalid command with special characters")
  void testInvalidCommandWithSpecialChars() {
    String invalidArg = "--help";
    Invalid<FileSystem> invalid = new Invalid<>(fileSystem, fileSystem.getRoot(), invalidArg);
    Result<FileSystem> result = invalid.execute();
    assertEquals("Invalid command" + invalidArg, result.getMessage());
    assertSame(fileSystem, result.getTree());
  }
}
