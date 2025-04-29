package edu.austral.ingsis.parsertest;

import edu.austral.ingsis.clifford.parser.StringParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringParserTest {

  @Test
  void createStringParserInstance() {
    StringParser stringParser = new StringParser();
    assertEquals(StringParser.class, stringParser.getClass());
  }

  @Test
  void parseCommandArgumentSplitsInputIntoCommandAndArguments() {
    String[] result = StringParser.parseCommandArgument("mkdir new_folder");
    assertArrayEquals(new String[]{"mkdir", "new_folder"}, result);
  }

  @Test
  void parseCommandArgumentReturnsCommandAndEmptyStringWhenNoArguments() {
    String[] result = StringParser.parseCommandArgument("pwd");
    assertArrayEquals(new String[]{"pwd", ""}, result);
  }

  @Test
  void parseCommandArgumentReturnsEmptyCommandAndEmptyStringWhenInputIsEmpty() {
    String[] result = StringParser.parseCommandArgument("");
    assertArrayEquals(new String[]{"", ""}, result);
  }

  @Test
  void parseCommandExtractsCommandFromInput() {
    String result = StringParser.parseCommand("ls -la");
    assertEquals("ls", result);
  }

  @Test
  void parseCommandReturnsEmptyStringWhenInputIsEmpty() {
    String result = StringParser.parseCommand("");
    assertEquals("", result);
  }

}