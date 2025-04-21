package edu.austral.ingsis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.austral.ingsis.clifford.*;
import org.junit.jupiter.api.Test;

public class CLITest {
  @Test
  void startExecutesMkdirCommandSuccessfully() {
    Folder root = new Folder("root", null);
    CLI cli = new CLI(root);

    String simulatedInput = "mkdir newFolder\nexit\n";
    System.setIn(new java.io.ByteArrayInputStream(simulatedInput.getBytes()));

    cli.start();

    assertEquals(1, root.getChildren().size());
    assertTrue(root.getChildren().get(0) instanceof Folder);
    assertEquals("newFolder", root.getChildren().get(0).getName());
  }

  @Test
  void startExecutesTouchCommandSuccessfully() {
    Folder root = new Folder("root", null);
    CLI cli = new CLI(root);

    String simulatedInput = "touch newFile.txt\nexit\n";
    System.setIn(new java.io.ByteArrayInputStream(simulatedInput.getBytes()));

    cli.start();

    assertEquals(1, root.getChildren().size());
    assertTrue(root.getChildren().get(0) instanceof File);
    assertEquals("newFile.txt", root.getChildren().get(0).getName());
  }

  @Test
  void startHandlesUnknownCommandGracefully() {
    Folder root = new Folder("root", null);
    CLI cli = new CLI(root);

    String simulatedInput = "unknownCommand\nexit\n";
    System.setIn(new java.io.ByteArrayInputStream(simulatedInput.getBytes()));

    cli.start();

    assertEquals(0, root.getChildren().size());
  }

  @Test
  void startExecutesLsCommandSuccessfully() {
    Folder root = new Folder("root", null);
    Folder subFolder = new Folder("subFolder", root);
    root.addChild(subFolder);
    CLI cli = new CLI(root);

    String simulatedInput = "ls\nexit\n";
    System.setIn(new java.io.ByteArrayInputStream(simulatedInput.getBytes()));

    cli.start();

    assertEquals(1, root.getChildren().size());
    assertEquals("subFolder", root.getChildren().get(0).getName());
  }
}
