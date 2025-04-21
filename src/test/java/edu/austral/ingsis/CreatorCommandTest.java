package edu.austral.ingsis;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.austral.ingsis.clifford.CreatorCommand;
import org.junit.jupiter.api.Test;

public class CreatorCommandTest {

  @Test
  void isValidNameReturnsTrueForValidName() {
    CreatorCommand command = new CreatorCommand() {};
    assertTrue(command.isValidName("validName"));
  }

  @Test
  void isValidNameReturnsFalseForNullName() {
    CreatorCommand command = new CreatorCommand() {};
    assertFalse(command.isValidName(null));
  }

  @Test
  void isValidNameReturnsFalseForEmptyName() {
    CreatorCommand command = new CreatorCommand() {};
    assertFalse(command.isValidName(""));
  }

  @Test
  void isValidNameReturnsFalseForNameWithOnlySpaces() {
    CreatorCommand command = new CreatorCommand() {};
    assertFalse(command.isValidName("   "));
  }

  @Test
  void isValidNameReturnsFalseForNameContainingSlash() {
    CreatorCommand command = new CreatorCommand() {};
    assertFalse(command.isValidName("invalid/name"));
  }
}
