package edu.austral.ingsis.commandtest;

import static org.junit.jupiter.api.Assertions.*;

import edu.austral.ingsis.clifford.commands.Rm;
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

public class RmTest {

  private Tree<FileSystem> fileSystem;
  private TreeNode<FileSystem> docsNode;

  @BeforeEach
  void setUp() {
    Directory root = new Directory("root");
    Directory docs = new Directory("docs");
    Directory pictures = new Directory("pictures");
    FileSystem textFile = new File("file.txt");
    FileSystem imageFile = new File("image.png");
    FileSystem reportsFile = new File("reports.pdf");

    fileSystem = new NonBinaryTree<>(root);

    // Add directories to root
    fileSystem = fileSystem.withChildAddedTo(fileSystem.getRoot(), docs, fileSystem.getRoot());
    docsNode = fileSystem.findNode(docs);

    fileSystem = fileSystem.withChildAddedTo(fileSystem.getRoot(), pictures, fileSystem.getRoot());
    TreeNode<FileSystem> picturesNode = fileSystem.findNode(pictures);

    // Add files to root
    fileSystem = fileSystem.withChildAddedTo(fileSystem.getRoot(), textFile, fileSystem.getRoot());

    // Add files to docs
    fileSystem = fileSystem.withChildAddedTo(docsNode, reportsFile, docsNode);

    // Add files to pictures
    fileSystem = fileSystem.withChildAddedTo(picturesNode, imageFile, picturesNode);
  }

  @Test
  @DisplayName("Test removing a file from root")
  void testRemoveFileFromRoot() {
    Rm<FileSystem> rm = new Rm<>(fileSystem, "file.txt", fileSystem.getRoot(), false);
    Result<FileSystem> result = rm.execute();

    assertEquals("'file.txt' removed", result.getMessage());
    fileSystem = result.getTree();

    // Verify file was removed
    assertNull(fileSystem.findNode(new File("file.txt")));
  }

  @Test
  @DisplayName("Test removing a directory")
  void testRemoveDirectory() {
    Rm<FileSystem> rm = new Rm<>(fileSystem, "pictures", fileSystem.getRoot(), false);
    Result<FileSystem> result = rm.execute();

    assertEquals("cannot remove 'pictures', is a directory", result.getMessage());
    fileSystem = result.getTree();

    // Verify directory was not removed
    assertNotNull(fileSystem.findNode(new Directory("pictures")));
    // Verify files inside directory were not removed
    assertNotNull(fileSystem.findNode(new File("image.png")));
  }

  @Test
  @DisplayName("Test removing non-existent item")
  void testRemoveNonExistentItem() {
    Rm<FileSystem> rm = new Rm<>(fileSystem, "nonexistent.txt", fileSystem.getRoot(), false);
    Result<FileSystem> result = rm.execute();

    assertEquals("'nonexistent.txt' not found", result.getMessage());

    // Verify file system remained unchanged
    Tree<FileSystem> originalTree = fileSystem;
    fileSystem = result.getTree();
    assertEquals(originalTree, fileSystem);
  }

  @Test
  @DisplayName("Test removing a directory with recursive flag")
  void testRemoveDirectoryWithRecursiveFlag() {
    Rm<FileSystem> rm = new Rm<>(fileSystem, "--recursive pictures", fileSystem.getRoot(), true);
    Result<FileSystem> result = rm.execute();

    assertEquals("'pictures' removed", result.getMessage());
    fileSystem = result.getTree();

    // Verify directory was removed
    assertNull(fileSystem.findNode(new Directory("pictures")));
    // Verify files inside directory were also removed
    assertNull(fileSystem.findNode(new File("image.png")));
  }

  @Test
  @DisplayName("Test removing a file with recursive flag")
  void testRemoveFileWithRecursiveFlag() {
    Rm<FileSystem> rm = new Rm<>(fileSystem, "--recursive file.txt", fileSystem.getRoot(), true);
    Result<FileSystem> result = rm.execute();

    assertEquals("'file.txt' removed", result.getMessage());
    fileSystem = result.getTree();

    // Verify file was removed
    assertNull(fileSystem.findNode(new File("file.txt")));
  }

  @Test
  @DisplayName("Test removing directory while standing in it")
  void testRemoveDirectoryWhileStandingInIt() {
    // Test should fail because you can't remove the directory you are in
    Rm<FileSystem> rm = new Rm<>(fileSystem, "--recursive docs", docsNode, true);
    Result<FileSystem> result = rm.execute();

    assertEquals("'docs' not found", result.getMessage());
    fileSystem = result.getTree();

    // Verify directory was not removed
    assertNotNull(fileSystem.findNode(new Directory("docs")));
    // Verify files inside directory were not removed
    assertNotNull(fileSystem.findNode(new File("reports.pdf")));
    // Verify file system remained unchanged
    Tree<FileSystem> originalTree = fileSystem;
    fileSystem = result.getTree();
    assertEquals(originalTree, fileSystem);
  }
}
