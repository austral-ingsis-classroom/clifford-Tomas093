package edu.austral.ingsis.clifford;

public interface Command {

  void execute(Folder fileSystem, String argument);
}
