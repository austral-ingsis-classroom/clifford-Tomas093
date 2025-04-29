package edu.austral.ingsis.clifford.commands;

public interface CreatorCommand {

  default boolean isValidName(String name) {
    return name != null && !name.trim().isEmpty() && !name.contains("/");
  }
}
