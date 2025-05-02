package edu.austral.ingsis.commandtest;

import static org.junit.jupiter.api.Assertions.*;

import edu.austral.ingsis.clifford.commands.Mkdir;
import edu.austral.ingsis.clifford.filesystem.Directory;
import edu.austral.ingsis.clifford.filesystem.FileSystem;
import edu.austral.ingsis.clifford.result.Result;
import edu.austral.ingsis.clifford.tree.structure.NonBinaryTree;
import edu.austral.ingsis.clifford.tree.structure.Tree;
import edu.austral.ingsis.clifford.tree.structure.TreeNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MkdirTest {

  private Tree<FileSystem> fileSystem;
  private TreeNode<FileSystem> docsNode;

  @BeforeEach
  void setUp() {
    Directory root = new Directory("root");
    Directory docs = new Directory("docs");

    fileSystem = new NonBinaryTree<>(root);

    // Add 'docs' to root
    fileSystem = fileSystem.withChildAddedTo(fileSystem.getRoot(), docs, fileSystem.getRoot());
    docsNode = fileSystem.findNode(docs);
  }

  @Test
  @DisplayName("Test creating directory in root")
  void testCreateDirectoryInRoot() {
    Mkdir<FileSystem> mkdir = new Mkdir<>(fileSystem, "photos", fileSystem.getRoot());
    Result<FileSystem> result = mkdir.execute();

    assertEquals("'photos' directory created", result.getMessage());
    fileSystem = result.getTree();

    // Verify the directory was created
    TreeNode<FileSystem> newDir = fileSystem.findNode(new Directory("photos"));
    assertNotNull(newDir);
    assertEquals("photos", newDir.getName());
  }

  @Test
  @DisplayName("Test creating directory in subdirectory")
  void testCreateDirectoryInSubdirectory() {
    Mkdir<FileSystem> mkdir = new Mkdir<>(fileSystem, "reports", docsNode);
    Result<FileSystem> result = mkdir.execute();

    assertEquals("'reports' directory created", result.getMessage());
    fileSystem = result.getTree();

    // Verify the directory was created in the correct location
    TreeNode<FileSystem> newDir = fileSystem.findNode(new Directory("reports"));
    assertNotNull(newDir);

    // Verify it's a child of docs
    TreeNode<FileSystem> updatedDocsNode = fileSystem.findNode(docsNode.getData());
    boolean isChild =
        updatedDocsNode.getChildren().stream().anyMatch(child -> child.getName().equals("reports"));
    assertTrue(isChild);
  }

  @Test
  @DisplayName("Test creating directory with invalid name")
  void testCreateDirectoryWithInvalidName() {
    Mkdir<FileSystem> mkdir = new Mkdir<>(fileSystem, "", fileSystem.getRoot());
    Result<FileSystem> result = mkdir.execute();

    assertEquals("Invalid directory name", result.getMessage());

    // Verify the tree wasn't modified
    Tree<FileSystem> originalTree = fileSystem;
    fileSystem = result.getTree();
    assertEquals(originalTree, fileSystem);
  }

  @Test
  @DisplayName("Test creating directory with special characters")
  void testCreateDirectoryWithSpecialCharacters() {
    Mkdir<FileSystem> mkdir = new Mkdir<>(fileSystem, "test-dir_01", fileSystem.getRoot());
    Result<FileSystem> result = mkdir.execute();

    assertEquals("'test-dir_01' directory created", result.getMessage());
    fileSystem = result.getTree();

    // Verify the directory was created
    TreeNode<FileSystem> newDir = fileSystem.findNode(new Directory("test-dir_01"));
    assertNotNull(newDir);
  }
}
