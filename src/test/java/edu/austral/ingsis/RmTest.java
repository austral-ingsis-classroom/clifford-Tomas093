package edu.austral.ingsis;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.austral.ingsis.clifford.File;
import edu.austral.ingsis.clifford.Folder;
import edu.austral.ingsis.clifford.Rm;
import org.junit.jupiter.api.Test;

public class RmTest {
  @Test
  void executeShouldNotRemoveFileWhenArgumentIsEmpty() {
    Folder parent = new Folder("parent", null);
    Rm rm = new Rm();

    assertDoesNotThrow(() -> rm.execute(parent, ""));
    assertEquals(0, parent.getChildren().size());
  }

  @Test
  void executeShouldNotRemoveFileWhenArgumentIsNull() {
    Folder parent = new Folder("parent", null);
    Rm rm = new Rm();

    assertDoesNotThrow(() -> rm.execute(parent, null));
    assertEquals(0, parent.getChildren().size());
  }

  @Test
  void executeShouldNotRemoveFileWhenFileSystemIsEmpty() {
    Folder parent = new Folder("parent", null);
    Rm rm = new Rm();

    rm.execute(parent, "file.txt");

    assertEquals(0, parent.getChildren().size());
  }

  @Test
  void executeShouldNotRemoveFileWhenArgumentHasInvalidFormat() {
    Folder parent = new Folder("parent", null);
    File file = new File("file.txt", parent);
    parent.addChild(file);
    Rm rm = new Rm();

    rm.execute(parent, "--invalid-format file.txt");

    assertEquals(1, parent.getChildren().size());
  }

  @Test
  void executeShouldNotRemoveFileWhenArgumentIsOnlyRecursiveFlag() {
    Folder parent = new Folder("parent", null);
    File file = new File("file.txt", parent);
    parent.addChild(file);
    Rm rm = new Rm();

    rm.execute(parent, "--recursive");

    assertEquals(1, parent.getChildren().size());
  }

  @Test
  void executeShouldRemoveFileWhenArgumentIsValid() {
    Folder parent = new Folder("parent", null);
    File file = new File("file.txt", parent);
    parent.addChild(file);
    Rm rm = new Rm();

    rm.execute(parent, "file.txt");

    assertEquals(0, parent.getChildren().size());
  }

  @Test
  void executeShouldRemoveDirectoryRecursivelyWhenArgumentIsValid() {
    Folder parent = new Folder("parent", null);
    Folder childDir = new Folder("child-dir", parent);
    File file = new File("file.txt", childDir);
    childDir.addChild(file);
    parent.addChild(childDir);
    Rm rm = new Rm();

    rm.execute(parent, "--recursive child-dir");

    assertEquals(0, parent.getChildren().size());
  }

  @Test
  void executeShouldNotRemoveDirectoryWhenRecursiveFlagIsMissing() {
    Folder parent = new Folder("parent", null);
    Folder childDir = new Folder("child-dir", parent);
    parent.addChild(childDir);
    Rm rm = new Rm();

    rm.execute(parent, "child-dir");

    assertEquals(1, parent.getChildren().size());
  }
}
