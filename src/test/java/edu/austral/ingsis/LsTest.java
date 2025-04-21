package edu.austral.ingsis;

import static org.junit.jupiter.api.Assertions.*;

import edu.austral.ingsis.clifford.File;
import edu.austral.ingsis.clifford.Folder;
import edu.austral.ingsis.clifford.Ls;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LsTest {
  @BeforeEach
  public void setUp() {
    System.setOut(new PrintStream(new ByteArrayOutputStream()));
  }

  @AfterEach
  public void tearDown() {
    System.setOut(System.out);
  }

  @Test
  public void executeShouldPrintChildrenInAscendingOrderWhenFlagIsAsc() {
    Folder folder = new Folder("root", null);
    folder.addChild(new File("b.txt", folder));
    folder.addChild(new File("a.txt", folder));

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));

    new Ls().execute(folder, "asc");

    assertEquals("a.txt b.txt", outputStream.toString().trim());
  }

  @Test
  public void executeShouldPrintChildrenInDescendingOrderWhenFlagIsDesc() {
    Folder folder = new Folder("root", null);
    folder.addChild(new File("a.txt", folder));
    folder.addChild(new File("b.txt", folder));

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));

    new Ls().execute(folder, "desc");

    assertEquals("b.txt a.txt", outputStream.toString().trim());
  }

  @Test
  public void executeShouldHandleEmptyFolderGracefully() {
    Folder folder = new Folder("root", null);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));

    new Ls().execute(folder, null);

    assertEquals("", outputStream.toString().trim());
  }

  @Test
  public void executeShouldPrintChildrenWithoutSortingWhenFlagIsNull() {
    Folder folder = new Folder("root", null);
    folder.addChild(new File("a.txt", folder));
    folder.addChild(new File("b.txt", folder));

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));

    new Ls().execute(folder, null);

    assertEquals("a.txt b.txt", outputStream.toString().trim());
  }

  @Test
  public void executeShouldHandleNullFolderGracefully() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));

    new Ls().execute(null, "asc");

    assertEquals("", outputStream.toString().trim());
  }
}
