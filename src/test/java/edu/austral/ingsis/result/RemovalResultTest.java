package edu.austral.ingsis.result;

import static org.junit.jupiter.api.Assertions.*;

import edu.austral.ingsis.clifford.filesystem.FileSystem;
import edu.austral.ingsis.clifford.result.RemovalResult;
import org.junit.jupiter.api.Test;

class RemovalResultTest {

  @Test
  void getMessageReturnsCorrectMessageWhenNodeIsNotFound() {
    RemovalResult<FileSystem> result = new RemovalResult<>(null, false);
    assertEquals("Node was not found", result.getMessage());
  }

  @Test
  void getTreeReturnsNullWhenTreeIsNotProvided() {
    RemovalResult<FileSystem> result = new RemovalResult<>(null, false);
    assertNull(result.getTree());
  }
}
