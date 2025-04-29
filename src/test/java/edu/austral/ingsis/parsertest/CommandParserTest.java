package edu.austral.ingsis.parsertest;

import edu.austral.ingsis.clifford.commands.*;
import edu.austral.ingsis.clifford.filesystem.FileSystem;
import edu.austral.ingsis.clifford.parser.CommandParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommandParserTest {

  @Test
  void parseReturnsLsCommandWhenInputIsLs() {
    CommandParser parser = new CommandParser();
    Command<FileSystem> command = parser.parse("ls");
    assertTrue(command instanceof Ls);
  }

  @Test
  void parseReturnsCdCommandWhenInputIsCd() {
    CommandParser parser = new CommandParser();
    Command<FileSystem> command = parser.parse("cd");
    assertTrue(command instanceof Cd);
  }

  @Test
  void parseReturnsMkdirCommandWhenInputIsMkdir() {
    CommandParser parser = new CommandParser();
    Command<FileSystem> command = parser.parse("mkdir");
    assertTrue(command instanceof Mkdir);
  }

  @Test
  void parseReturnsTouchCommandWhenInputIsTouch() {
    CommandParser parser = new CommandParser();
    Command<FileSystem> command = parser.parse("touch");
    assertTrue(command instanceof Touch);
  }

  @Test
  void parseReturnsRmCommandWhenInputIsRm() {
    CommandParser parser = new CommandParser();
    Command<FileSystem> command = parser.parse("rm");
    assertTrue(command instanceof Rm);
  }

  @Test
  void parseReturnsPwdCommandWhenInputIsPwd() {
    CommandParser parser = new CommandParser();
    Command<FileSystem> command = parser.parse("pwd");
    assertTrue(command instanceof Pwd);
  }

  @Test
  void parseReturnsInvalidCommandWhenInputIsUnknown() {
    CommandParser parser = new CommandParser();
    Command<FileSystem> command = parser.parse("unknown");
    assertTrue(command instanceof Invalid);
  }

  @Test
  void parseReturnsInvalidCommandWhenInputIsEmpty() {
    CommandParser parser = new CommandParser();
    Command<FileSystem> command = parser.parse("");
    assertTrue(command instanceof Invalid);
  }

}