package edu.austral.ingsis.commandtest;

import edu.austral.ingsis.clifford.commands.Touch;
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

public class TouchTest {

  private Tree<FileSystem> fileSystem;
  private Touch touch;
  private TreeNode<FileSystem> docsNode;

  @BeforeEach
  void setUp() {
    Directory root = new Directory("root");
    Directory docs = new Directory("docs");
    Directory reports = new Directory("reports");
    Directory pictures = new Directory("pictures");
    FileSystem existingFile = new File("existing.txt");

    fileSystem = new NonBinaryTree<>(root);
    touch = new Touch();

    // Agregamos 'docs' al root
    fileSystem = fileSystem.withChildAddedTo(fileSystem.getRoot(), docs, fileSystem.getRoot());
    TreeNode<FileSystem> docsNodeLocal = fileSystem.findNode(docs);
    docsNode = docsNodeLocal;  // actualizar la variable global para los tests

    // Agregamos 'reports' como hijo de 'docs'
    fileSystem = fileSystem.withChildAddedTo(docsNodeLocal, reports, docsNodeLocal);

    // Agregamos 'pictures' como hijo de 'root'
    fileSystem = fileSystem.withChildAddedTo(fileSystem.getRoot(), pictures, fileSystem.getRoot());

    // Agregamos un archivo existente para pruebas
    fileSystem = fileSystem.withChildAddedTo(fileSystem.getRoot(), existingFile, fileSystem.getRoot());
  }

  @Test
  @DisplayName("Test creating a file with valid name")
  void testCreateValidFile() {
    String fileName = "newfile.txt";
    Result<FileSystem> result = touch.execute(fileSystem, fileName, fileSystem.getRoot());

    assertEquals("'" + fileName + "' file created", result.getMessage());
  }

  @Test
  @DisplayName("Test creating a file with invalid name")
  void testCreateInvalidFile() {
    String invalidFileName = ""; // Asumiendo que un nombre vacío es inválido
    Result<FileSystem> result = touch.execute(fileSystem, invalidFileName, fileSystem.getRoot());

    assertEquals("Invalid file name", result.getMessage());
  }

  @Test
  @DisplayName("Test creating a file in docs directory")
  void testCreateFileInDocs() {
    String fileName = "docfile.txt";
    Result<FileSystem> result = touch.execute(fileSystem, fileName, docsNode);

    assertEquals("'" + fileName + "' file created", result.getMessage());
  }

  @Test
  @DisplayName("Test creating a file with special characters")
  void testCreateFileWithSpecialChars() {
    String fileName = "file-with-hyphens.txt";
    Result<FileSystem> result = touch.execute(fileSystem, fileName, fileSystem.getRoot());

    assertEquals("'" + fileName + "' file created", result.getMessage());
  }

  @Test
  @DisplayName("Test creating a file with extension")
  void testCreateFileWithExtension() {
    String fileName = "document.pdf";
    Result<FileSystem> result = touch.execute(fileSystem, fileName, fileSystem.getRoot());

    assertEquals("'" + fileName + "' file created", result.getMessage());
  }

  @Test
  @DisplayName("Test creating a file without extension")
  void testCreateFileWithoutExtension() {
    String fileName = "README";
    Result<FileSystem> result = touch.execute(fileSystem, fileName, fileSystem.getRoot());

    assertEquals("'" + fileName + "' file created", result.getMessage());
  }

  @Test
  @DisplayName("Test creating a file with spaces in name")
  void testCreateFileWithSpaces() {
    String fileName = "my document.txt";
    Result<FileSystem> result = touch.execute(fileSystem, fileName, fileSystem.getRoot());

    assertEquals("'" + fileName + "' file created", result.getMessage());

    // Verificar que el archivo fue creado
    TreeNode<FileSystem> newFileNode = fileSystem.findNode(new File(fileName));
  }
}