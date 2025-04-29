package edu.austral.ingsis.commandtest;

import edu.austral.ingsis.clifford.commands.Pwd;
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

public class PwdTest {

  private Tree<FileSystem> fileSystem;
  private Pwd pwd;
  private TreeNode<FileSystem> docsNode;
  private TreeNode<FileSystem> reportsNode;
  private TreeNode<FileSystem> deepFolderNode;

  @BeforeEach
  void setUp() {
    Directory root = new Directory("root");
    Directory docs = new Directory("docs");
    Directory reports = new Directory("reports");
    Directory deepDirectory = new Directory("deep");

    fileSystem = new NonBinaryTree<>(root);
    pwd = new Pwd();

    // Add 'docs' to root
    fileSystem = fileSystem.withChildAddedTo(fileSystem.getRoot(), docs, fileSystem.getRoot());
    docsNode = fileSystem.findNode(docs);

    // Add 'reports' as child of 'docs'
    fileSystem = fileSystem.withChildAddedTo(docsNode, reports, docsNode);
    reportsNode = fileSystem.findNode(reports);

    // Add 'deep' as child of 'reports'
    fileSystem = fileSystem.withChildAddedTo(reportsNode, deepDirectory, reportsNode);
    deepFolderNode = fileSystem.findNode(deepDirectory);
  }

  @Test
  @DisplayName("Test pwd at root directory")
  void testPwdAtRootDirectory() {
    Result<FileSystem> result = pwd.execute(fileSystem, "", fileSystem.getRoot());
    assertEquals("/", result.getMessage());
  }

  @Test
  @DisplayName("Test pwd in first-level directory")
  void testPwdInFirstLevelDirectory() {
    Result<FileSystem> result = pwd.execute(fileSystem, "", docsNode);
    assertEquals("/docs", result.getMessage());
  }

  @Test
  @DisplayName("Test pwd in nested directory")
  void testPwdInNestedDirectory() {
    Result<FileSystem> result = pwd.execute(fileSystem, "", reportsNode);
    assertEquals("/docs/reports", result.getMessage());
  }

  @Test
  @DisplayName("Test pwd in deeply nested directory")
  void testPwdInDeeplyNestedDirectory() {
    Result<FileSystem> result = pwd.execute(fileSystem, "", deepFolderNode);
    assertEquals("/docs/reports/deep", result.getMessage());
  }

  @Test
  @DisplayName("Test pwd ignores arguments")
  void testPwdIgnoresArguments() {
    // Pwd should ignore arguments and return the same result
    Result<FileSystem> result = pwd.execute(fileSystem, "someArgument", docsNode);
    assertEquals("/docs", result.getMessage());
  }
}