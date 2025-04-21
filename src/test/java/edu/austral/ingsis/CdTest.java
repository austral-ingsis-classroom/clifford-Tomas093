package edu.austral.ingsis;

import static org.junit.jupiter.api.Assertions.*;

import edu.austral.ingsis.clifford.CLI;
import edu.austral.ingsis.clifford.CLIHolder;
import edu.austral.ingsis.clifford.Cd;
import edu.austral.ingsis.clifford.Folder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CdTest {

  private CLI cli;
  private Folder rootFolder;
  private Folder subFolder;
  private Folder nestedFolder;
  private Cd cdCommand;

  @BeforeEach
  void setUp() {
    rootFolder = new Folder("root", null);
    subFolder = new Folder("sub", rootFolder);
    nestedFolder = new Folder("nested", subFolder);

    rootFolder.addChild(subFolder);
    subFolder.addChild(nestedFolder);

    cli = new CLI(rootFolder);
    CLIHolder.setInstance(cli);

    cdCommand = new Cd();
  }

  @Test
  void executeMovesToParentFolderWhenArgumentIsDoubleDot() {
    cli.setCurrentFolder(subFolder);

    cdCommand.execute(cli.getCurrentFolder(), "..");

    assertEquals(rootFolder, cli.getCurrentFolder());
  }

  @Test
  void executeDoesNotMoveWhenAlreadyAtRootAndArgumentIsDoubleDot() {
    cli.setCurrentFolder(rootFolder);

    cdCommand.execute(cli.getCurrentFolder(), "..");

    assertEquals(rootFolder, cli.getCurrentFolder());
  }

  @Test
  void executeMovesToAbsolutePathWhenArgumentStartsWithSlash() {
    cli.setCurrentFolder(subFolder);

    cdCommand.execute(cli.getCurrentFolder(), "/sub/nested");

    assertEquals(nestedFolder, cli.getCurrentFolder());
  }

  @Test
  void executeDoesNotMoveWhenAbsolutePathIsInvalid() {
    cli.setCurrentFolder(subFolder);

    cdCommand.execute(cli.getCurrentFolder(), "/invalid");

    assertEquals(subFolder, cli.getCurrentFolder());
  }

  @Test
  void executeMovesToRelativePathWhenArgumentIsValid() {
    cli.setCurrentFolder(subFolder);

    cdCommand.execute(cli.getCurrentFolder(), "nested");

    assertEquals(nestedFolder, cli.getCurrentFolder());
  }

  @Test
  void executeDoesNotMoveWhenRelativePathIsInvalid() {
    cli.setCurrentFolder(subFolder);

    cdCommand.execute(cli.getCurrentFolder(), "invalid");

    assertEquals(subFolder, cli.getCurrentFolder());
  }
}
