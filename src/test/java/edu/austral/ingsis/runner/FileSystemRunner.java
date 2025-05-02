package edu.austral.ingsis.runner;

import java.util.List;

public interface FileSystemRunner {
  List<String> executeCommands(List<String> commands);
}
