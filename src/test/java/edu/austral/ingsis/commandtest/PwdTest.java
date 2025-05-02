package edu.austral.ingsis.commandtest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.austral.ingsis.clifford.commands.Pwd;
import edu.austral.ingsis.clifford.filesystem.Directory;
import edu.austral.ingsis.clifford.filesystem.FileSystem;
import edu.austral.ingsis.clifford.result.Result;
import edu.austral.ingsis.clifford.tree.structure.NonBinaryTree;
import edu.austral.ingsis.clifford.tree.structure.Tree;
import edu.austral.ingsis.clifford.tree.structure.TreeNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PwdTest {

  private Tree<FileSystem> fileSystem;
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
    Pwd<FileSystem> pwd = new Pwd<>(fileSystem, "", fileSystem.getRoot());
    Result<FileSystem> result = pwd.execute();
    assertEquals("/", result.getMessage());
  }

  @Test
  @DisplayName("Test pwd in first-level directory")
  void testPwdInFirstLevelDirectory() {
    Pwd<FileSystem> pwd = new Pwd<>(fileSystem, "", docsNode);
    Result<FileSystem> result = pwd.execute();
    assertEquals("/docs", result.getMessage());
  }

  @Test
  @DisplayName("Test pwd in nested directory")
  void testPwdInNestedDirectory() {
    Pwd<FileSystem> pwd = new Pwd<>(fileSystem, "", reportsNode);
    Result<FileSystem> result = pwd.execute();
    assertEquals("/docs/reports", result.getMessage());
  }

  @Test
  @DisplayName("Test pwd in deeply nested directory")
  void testPwdInDeeplyNestedDirectory() {
    Pwd<FileSystem> pwd = new Pwd<>(fileSystem, "", deepFolderNode);
    Result<FileSystem> result = pwd.execute();
    assertEquals("/docs/reports/deep", result.getMessage());
  }

  @Test
  @DisplayName("Test pwd ignores arguments")
  void testPwdIgnoresArguments() {
    // Pwd should ignore arguments and return the same result
    Pwd<FileSystem> pwd = new Pwd<>(fileSystem, "someArgument", docsNode);
    Result<FileSystem> result = pwd.execute();
    assertEquals("/docs", result.getMessage());
  }
}
