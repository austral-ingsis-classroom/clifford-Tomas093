package edu.austral.ingsis;

import static org.junit.jupiter.api.Assertions.*;

import edu.austral.ingsis.clifford.File;
import edu.austral.ingsis.clifford.Folder;
import org.junit.jupiter.api.Test;

class FileTest {

  @Test
  void fileNameShouldBeReturnedCorrectly() {
    File file = new File("example.txt", null);
    assertEquals("example.txt", file.getName());
  }

  @Test
  void fileParentShouldBeNullWhenNoParentIsSet() {
    File file = new File("example.txt", null);
    assertNull(file.getParent());
  }

  @Test
  void fileParentShouldBeSetCorrectly() {
    Folder parent = new Folder("parent", null);
    File file = new File("example.txt", parent);
    assertEquals(parent, file.getParent());
  }

  @Test
  void filePathShouldReturnDefaultPathWhenNotOverridden() {
    File file = new File("example.txt", null);
    assertEquals("/example.txt", file.getPath());
  }

  @Test
  void fileShouldBeDeletedFromParentFolder() {
    Folder parent = new Folder("parent", null);
    File file = new File("example.txt", parent);
    parent.addChild(file);
    file.delete();
    assertNull(parent.getChild("example.txt"));
  }

  @Test
  void deletingFileWithoutParentShouldNotThrowException() {
    File file = new File("example.txt", null);
    assertDoesNotThrow(file::delete);
  }
}
