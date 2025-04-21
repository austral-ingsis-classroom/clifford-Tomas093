package edu.austral.ingsis.clifford;

public class CLIHolder {
  private static CLI instance;

  public static void setInstance(CLI cli) {
    instance = cli;
  }

  public static CLI getInstance() {
    return instance;
  }
}
