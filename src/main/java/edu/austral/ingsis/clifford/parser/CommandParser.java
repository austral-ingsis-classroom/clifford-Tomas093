package edu.austral.ingsis.clifford.parser;

import edu.austral.ingsis.clifford.commands.*;
import edu.austral.ingsis.clifford.filesystem.FileSystem;

public record CommandParser<T extends  FileSystem>() {

  public Command<T> parse(String command) {
    return switch (command) {
      case "ls" -> new Ls<T>();
      case "cd" -> new Cd<T>();
      case "mkdir" -> new Mkdir<T>();
      case "touch" -> new Touch<T>();
      case "rm" -> new Rm<T>();
      case "pwd" -> new Pwd<T>();
      default -> new Invalid<T>();
    };
  }
}