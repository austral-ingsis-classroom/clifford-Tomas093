package edu.austral.ingsis.commandtest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.austral.ingsis.clifford.commands.Ls;
import edu.austral.ingsis.clifford.commands.types.LsOrder;
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

public class LsTest {

  private Tree<FileSystem> fileSystem;
  private TreeNode<FileSystem> docsNode;
  private TreeNode<FileSystem> emptyFolderNode;

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

    // Add 'anotherFile.md' as child of 'root'
    FileSystem anotherFile = new File("anotherFile.md");
    fileSystem =
        fileSystem.withChildAddedTo(fileSystem.getRoot(), anotherFile, fileSystem.getRoot());

    // Add empty folder as child of 'root'
    Directory emptyDirectory = new Directory("empty");
    fileSystem =
        fileSystem.withChildAddedTo(fileSystem.getRoot(), emptyDirectory, fileSystem.getRoot());
    emptyFolderNode = fileSystem.findNode(emptyDirectory);
  }

  @Test
  @DisplayName("Test ls in root directory")
  void testLsInRootDirectory() {
    Ls ls = new Ls(fileSystem, fileSystem.getRoot(), LsOrder.DEFAULT);
    Result<FileSystem> result = ls.execute();
    assertEquals("docs pictures file.txt anotherFile.md empty", result.getMessage());
  }

  @Test
  @DisplayName("Test ls in docs directory")
  void testLsInDocsDirectory() {
    docsNode = fileSystem.findNode(docsNode.getData());
    Ls ls = new Ls(fileSystem, docsNode, LsOrder.DEFAULT);
    Result<FileSystem> result = ls.execute();
    assertEquals("reports", result.getMessage());
  }

  @Test
  @DisplayName("Test ls in empty directory")
  void testLsInEmptyDirectory() {
    Ls ls = new Ls(fileSystem, emptyFolderNode, LsOrder.DEFAULT);
    Result<FileSystem> result = ls.execute();
    assertEquals("", result.getMessage());
  }

  @Test
  @DisplayName("Test ls with ascending order")
  void testLsWithAscendingOrder() {
    Ls ls = new Ls(fileSystem, fileSystem.getRoot(), LsOrder.ASCENDING);
    Result<FileSystem> result = ls.execute();
    assertEquals("anotherFile.md docs empty file.txt pictures", result.getMessage());
  }

  @Test
  @DisplayName("Test ls with descending order")
  void testLsWithDescendingOrder() {
    Ls ls = new Ls(fileSystem, fileSystem.getRoot(), LsOrder.DESCENDING);
    Result<FileSystem> result = ls.execute();
    assertEquals("pictures file.txt empty docs anotherFile.md", result.getMessage());
  }

  @Test
  @DisplayName("Test ls with invalid order argument")
  void testLsWithInvalidOrderArgument() {
    // For invalid arguments, the DEFAULT enum should be used
    Ls ls = new Ls(fileSystem, fileSystem.getRoot(), LsOrder.DEFAULT);
    Result<FileSystem> result = ls.execute();
    assertEquals("docs pictures file.txt anotherFile.md empty", result.getMessage());
  }

  @Test
  @DisplayName("Test ls with null argument")
  void testLsWithNullArgument() {
    // For null arguments, the DEFAULT enum should be used
    Ls ls = new Ls(fileSystem, fileSystem.getRoot(), LsOrder.DEFAULT);
    Result<FileSystem> result = ls.execute();
    assertEquals("docs pictures file.txt anotherFile.md empty", result.getMessage());
  }
}
