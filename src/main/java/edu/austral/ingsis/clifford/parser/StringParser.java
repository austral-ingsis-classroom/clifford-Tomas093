package edu.austral.ingsis.clifford.parser;

public record StringParser() {
  public static String[] parseCommandArgument(String input) {
    String[] parts = input.split(" ", 2);
    String command = parts[0];
    String arguments = parts.length > 1 ? parts[1] : "";
    return new String[]{command, arguments};
  }

  public static String parseCommand(String input) {
    String[] parts = input.split(" ", 2);
    return parts[0];
  }


}
