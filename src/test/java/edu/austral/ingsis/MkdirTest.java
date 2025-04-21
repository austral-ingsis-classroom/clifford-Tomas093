package edu.austral.ingsis;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.austral.ingsis.clifford.Folder;
import edu.austral.ingsis.clifford.Mkdir;
import org.junit.jupiter.api.Test;

public class MkdirTest {
  @Test
  void executeShouldCreateNewFolderWithValidName() {
    Folder parent = new Folder("parent", null);
    Mkdir mkdir = new Mkdir();

    mkdir.execute(parent, "newFolder");

    assertEquals(1, parent.getChildren().size());
    assertEquals("newFolder", parent.getChildren().get(0).getName());
  }

  @Test
  void executeShouldNotCreateFolderWithInvalidName() {
    Folder parent = new Folder("parent", null);
    Mkdir mkdir = new Mkdir();

    mkdir.execute(parent, "");

    assertEquals(0, parent.getChildren().size());
  }

  @Test
  void executeShouldHandleNullParentGracefully() {
    Mkdir mkdir = new Mkdir();
    assertDoesNotThrow(() -> mkdir.execute(null, "newFolder"));
  }

  @Test
  void executeShouldNotCreateFolderWhenNameIsNull() {
    Folder parent = new Folder("parent", null);
    Mkdir mkdir = new Mkdir();

    mkdir.execute(parent, null);

    assertEquals(0, parent.getChildren().size());
  }
}
