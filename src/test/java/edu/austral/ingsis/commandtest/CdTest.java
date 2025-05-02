package edu.austral.ingsis.commandtest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.austral.ingsis.clifford.commands.Cd;
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

public class CdTest {

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

    // Agregamos 'docs' al root
    fileSystem = fileSystem.withChildAddedTo(fileSystem.getRoot(), docs, fileSystem.getRoot());
    TreeNode<FileSystem> docsNodeLocal = fileSystem.findNode(docs);
    docsNode = docsNodeLocal; // actualizar la variable global para los tests

    // Agregamos 'reports' como hijo de 'docs'
    fileSystem = fileSystem.withChildAddedTo(docsNodeLocal, reports, docsNodeLocal);

    // Agregamos 'pictures' como hijo de 'root'
    fileSystem = fileSystem.withChildAddedTo(fileSystem.getRoot(), pictures, fileSystem.getRoot());

    // Agregamos 'file.txt' como hijo de 'root'
    fileSystem = fileSystem.withChildAddedTo(fileSystem.getRoot(), file, fileSystem.getRoot());
  }

  @Test
  void testEmptyArgument() {
    Cd<FileSystem> cd = new Cd<>(fileSystem, "", fileSystem.getRoot());
    Result<FileSystem> result = cd.execute();
    assertEquals("", result.getMessage());
  }

  @Test
  @DisplayName("Test cd with current directory (.)")
  void testCurrentDirectory() {
    Cd<FileSystem> cd = new Cd<>(fileSystem, ".", fileSystem.getRoot());
    Result<FileSystem> result = cd.execute();
    assertEquals("", result.getMessage());
  }

  @Test
  @DisplayName("Test cd with parent directory (..)")
  void testParentDirectory() {
    Cd<FileSystem> cd = new Cd<>(fileSystem, "..", docsNode);
    Result<FileSystem> result = cd.execute();
    assertEquals("moved to directory 'root'", result.getMessage());
    assertEquals(fileSystem.getRoot(), result.getTree().getCurrentNode());
  }

  @Test
  @DisplayName("Test cd from root to parent (..) stays at root")
  void testParentFromRoot() {
    Cd<FileSystem> cd = new Cd<>(fileSystem, "..", fileSystem.getRoot());
    Result<FileSystem> result = cd.execute();
    assertEquals("moved to directory '/'", result.getMessage());
    assertEquals(fileSystem.getRoot().getData(), result.getTree().getCurrentNode().getData());
  }

  @Test
  @DisplayName("Test cd with non-existent directory")
  void testNonExistentDirectory() {
    Cd<FileSystem> cd = new Cd<>(fileSystem, "nonexistent", fileSystem.getRoot());
    Result<FileSystem> result = cd.execute();
    assertEquals("'nonexistent' directory does not exist", result.getMessage());
    assertEquals(fileSystem.getRoot().getData(), result.getTree().getCurrentNode().getData());
  }

  @Test
  @DisplayName("Test cd to a valid directory")
  void testValidDirectory() {
    Cd<FileSystem> cd = new Cd<>(fileSystem, "docs", fileSystem.getRoot());
    Result<FileSystem> result = cd.execute();
    assertEquals("moved to directory 'docs'", result.getMessage());
    assertEquals(docsNode.getData(), result.getTree().getCurrentNode().getData());
  }

  @Test
  @DisplayName("Test cd to a file")
  void testNavigateToFile() {
    Cd<FileSystem> cd = new Cd<>(fileSystem, "file.txt", fileSystem.getRoot());
    Result<FileSystem> result = cd.execute();
    assertEquals("'file.txt' is not a directory", result.getMessage());
    assertEquals(fileSystem.getRoot().getData(), result.getTree().getCurrentNode().getData());
  }

  @Test
  @DisplayName("Test cd with nested path")
  void testNestedPath() {
    Cd<FileSystem> cd = new Cd<>(fileSystem, "docs/reports", fileSystem.getRoot());
    Result<FileSystem> result = cd.execute();
    assertEquals("moved to directory 'reports'", result.getMessage());
    assertEquals(
        fileSystem.findNode(new Directory("reports")).getData(),
        result.getTree().getCurrentNode().getData());
  }

  @Test
  @DisplayName("Test cd with absolute path")
  void testAbsolutePath() {
    Cd<FileSystem> cd = new Cd<>(fileSystem, "/docs/reports", docsNode);
    Result<FileSystem> result = cd.execute();
    assertEquals("moved to directory 'reports'", result.getMessage());
    assertEquals(
        fileSystem.findNode(new Directory("reports")).getData(),
        result.getTree().getCurrentNode().getData());
  }

  @Test
  @DisplayName("Test cd to root with absolute path")
  void testToRootPath() {
    Cd<FileSystem> cd = new Cd<>(fileSystem, "/", docsNode);
    Result<FileSystem> result = cd.execute();
    assertEquals("moved to directory 'root'", result.getMessage());
    assertEquals(fileSystem.getRoot().getData(), result.getTree().getCurrentNode().getData());
  }

  @Test
  @DisplayName("Test cd with complex path (../pictures)")
  void testComplexPath() {
    Cd<FileSystem> cd = new Cd<>(fileSystem, "../pictures", docsNode);
    Result<FileSystem> result = cd.execute();
    assertEquals("moved to directory 'pictures'", result.getMessage());
    assertEquals(
        fileSystem.findNode(new Directory("pictures")).getData(),
        result.getTree().getCurrentNode().getData());
  }
}
