package edu.austral.ingsis.runner;

import edu.austral.ingsis.clifford.commands.executer.Cli;
import java.util.List;

public class FileSystemRunnerImp implements FileSystemRunner {
  Cli cli = new Cli();

  @Override
  public List<String> executeCommands(List<String> commands) {
    cli.executeCommands(commands);
    return cli.executeCommands(commands);
  }
}
