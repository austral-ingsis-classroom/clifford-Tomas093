package edu.austral.ingsis;

import static org.junit.jupiter.api.Assertions.*;

import edu.austral.ingsis.clifford.File;
import edu.austral.ingsis.clifford.Folder;
import org.junit.jupiter.api.Test;

public class FolderTest {
  @Test
  void folderShouldAddChildCorrectly() {
    Folder parent = new Folder("parent", null);
    File file = new File("example.txt", null);
    parent.addChild(file);
    assertNotNull(parent.getChild("example.txt"));
  }

  @Test
  void folderShouldReturnNullForNonExistentChild() {
    Folder parent = new Folder("parent", null);
    assertNull(parent.getChild("nonexistent.txt"));
  }

  @Test
  void folderShouldReturnAllChildren() {
    Folder parent = new Folder("parent", null);
    File file1 = new File("file1.txt", null);
    File file2 = new File("file2.txt", null);
    parent.addChild(file1);
    parent.addChild(file2);
    assertEquals(2, parent.getChildren().size());
  }

  @Test
  void folderPathShouldIncludeParentFolderNames() {
    Folder root = new Folder("root", null);
    Folder subFolder = new Folder("subFolder", root);
    assertEquals("/root/subFolder", subFolder.getPath());
  }

  @Test
  void deletingFolderShouldRemoveItFromParent() {
    Folder parent = new Folder("parent", null);
    Folder child = new Folder("child", parent);
    parent.addChild(child);
    child.delete();
    assertNull(parent.getChild("child"));
  }

  @Test
  void getChildShouldReturnNullForNonExistentChild() {
    Folder parent = new Folder("parent", null);
    assertNull(parent.getChild("nonexistent.txt"));
  }

  @Test
  void getChildShouldReturnChildForExistingName() {
    Folder parent = new Folder("parent", null);
    File file = new File("example.txt", null);
    parent.addChild(file);
    assertEquals(file, parent.getChild("example.txt"));
  }
}
