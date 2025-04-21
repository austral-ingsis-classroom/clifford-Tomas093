package edu.austral.ingsis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.austral.ingsis.clifford.File;
import edu.austral.ingsis.clifford.Folder;
import edu.austral.ingsis.clifford.Touch;
import org.junit.jupiter.api.Test;

public class TouchTest {
  @Test
  void executeCreatesFileWhenNameIsValid() {
    Folder root = new Folder("root", null);
    Touch touch = new Touch();

    touch.execute(root, "newfile.txt");

    assertEquals(1, root.getChildren().size());
    assertTrue(root.getChildren().get(0) instanceof File);
    assertEquals("newfile.txt", root.getChildren().get(0).getName());
  }

  @Test
  void executeDoesNotCreateFileWhenNameIsInvalid() {
    Folder root = new Folder("root", null);
    Touch touch = new Touch();

    touch.execute(root, "invalid/name");

    assertEquals(0, root.getChildren().size());
  }

  @Test
  void executeCreatesFileWithCorrectParent() {
    Folder root = new Folder("root", null);
    Touch touch = new Touch();

    touch.execute(root, "file.txt");

    File createdFile = (File) root.getChildren().get(0);
    assertEquals(root, createdFile.getParent());
  }

  @Test
  void executeHandlesEmptyFileNameGracefully() {
    Folder root = new Folder("root", null);
    Touch touch = new Touch();

    touch.execute(root, "");

    assertEquals(0, root.getChildren().size());
  }

  @Test
  void executeHandlesNullFileNameGracefully() {
    Folder root = new Folder("root", null);
    Touch touch = new Touch();

    touch.execute(root, null);

    assertEquals(0, root.getChildren().size());
  }
}
