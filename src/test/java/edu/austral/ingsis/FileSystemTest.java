package edu.austral.ingsis;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import edu.austral.ingsis.clifford.File;
import edu.austral.ingsis.clifford.Folder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FileSystemTest {
  @Test
  void filePathShouldIncludeParentFolderNames() {
    Folder parent = new Folder("parent", null);
    File file = new File("example.txt", parent);
    Assertions.assertEquals("/parent/example.txt", file.getPath());
  }

  @Test
  void filePathShouldReturnRootPathForRootFolder() {
    Folder root = new Folder("root", null);
    Assertions.assertEquals("/root", root.getPath());
  }

  @Test
  void filePathShouldHandleNestedFoldersCorrectly() {
    Folder root = new Folder("root", null);
    Folder subFolder = new Folder("subFolder", root);
    File file = new File("example.txt", subFolder);
    Assertions.assertEquals("/root/subFolder/example.txt", file.getPath());
  }

  @Test
  void deletingFileShouldNotAffectSiblingFiles() {
    Folder parent = new Folder("parent", null);
    File file1 = new File("file1.txt", parent);
    File file2 = new File("file2.txt", parent);
    parent.addChild(file1);
    parent.addChild(file2);
    file1.delete();
    assertNotNull(parent.getChild("file2.txt"));
  }
}
