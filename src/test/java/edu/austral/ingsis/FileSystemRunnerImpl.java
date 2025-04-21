package edu.austral.ingsis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.austral.ingsis.clifford.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileSystemRunnerImpl implements FileSystemRunner {

  private final FileSystemRunner runner =
      commands -> {
        Folder root = new Folder("", null);
        CLI cli = new CLI(root);
        CLIHolder.setInstance(cli);

        List<String> results = new ArrayList<>();

        for (String command : commands) {
          ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
          PrintStream originalOut = System.out;
          System.setOut(new PrintStream(outputStream));

          String[] tokens = command.split(" ", 2);
          String cmd = tokens[0];
          String arguments = tokens.length > 1 ? tokens[1] : "";

          executeCommand(cli, cmd, arguments);

          System.setOut(originalOut);
          String output = outputStream.toString().trim();

          output = formatOutput(command, output);
          results.add(output);
        }

        return results;
      };

  private void executeCommand(CLI cli, String cmd, String arguments) {
    Folder currentFolder = cli.getCurrentFolder();

    switch (cmd) {
      case "mkdir":
        new Mkdir().execute(currentFolder, arguments);
        break;
      case "touch":
        new Touch().execute(currentFolder, arguments);
        break;
      case "ls":
        if (arguments.startsWith("--ord=")) {
          String order = arguments.substring(6);
          new Ls().execute(currentFolder, order);
        } else {
          new Ls().execute(currentFolder, "");
        }
        break;
      case "cd":
        new Cd().execute(currentFolder, arguments);
        break;
      case "rm":
        new Rm().execute(currentFolder, arguments);
        break;
      case "pwd":
        System.out.println(currentFolder.getPath());
        break;
      default:
        System.out.println("Unknown command: " + cmd);
    }
  }

  private String formatOutput(String command, String output) {

    if (output.isEmpty()) return "";

    if (command.startsWith("mkdir") && output.contains("directory  created")) {
      String[] parts = command.split(" ", 2);
      if (parts.length > 1) {
        return "'" + parts[1] + "' directory created";
      }
    }

    if (command.startsWith("touch") && output.contains("file created")) {
      String[] parts = command.split(" ", 2);
      if (parts.length > 1) {
        return "'" + parts[1] + "' file created";
      }
    }

    if (command.startsWith("cd")) {
      if (output.startsWith("Moved to:") && output.length() > 9) {
        String path = output.substring(9).trim(); // Fixed from 10 to 9

        if (command.equals("cd ..") && path.equals("/")) {
          return "moved to directory '/'";
        }

        if (path.equals("/")) {
          return "moved to directory '/'";
        }

        String dirName;

        if (command.equals("cd ..")) {
          dirName = "..";
        } else {
          String[] parts = command.split(" ", 2);
          if (parts.length > 1) {
            String argPath = parts[1].trim();

            if (argPath.contains("/")) {

              String[] pathParts = argPath.split("/");
              dirName = pathParts[pathParts.length - 1];

              if (dirName.isEmpty() && pathParts.length > 1) {
                dirName = pathParts[pathParts.length - 2];
              }
            } else {

              dirName = argPath;
            }
          } else {

            int lastSlashIndex = path.lastIndexOf("/");
            if (lastSlashIndex >= 0 && lastSlashIndex < path.length() - 1) {
              dirName = path.substring(lastSlashIndex + 1);
            } else {
              dirName = path;
            }
          }
        }

        return "moved to directory '" + dirName + "'";
      } else if (output.contains("Path not found")) {
        String[] parts = command.split(" ", 2);
        if (parts.length > 1) {
          return "'" + parts[1] + "' directory does not exist";
        }
      }
    }

    if (command.startsWith("rm")) {
      String[] parts = command.split(" ");
      if (parts.length > 1) {
        String arg;
        if (parts.length > 2 && parts[1].equals("--recursive")) {
          arg = parts[2];
        } else {
          arg = parts[1];
        }

        if (output.contains("Invalid directory name")) {
          return "cannot remove '" + arg + "', is a directory";
        }
        if (output.contains("removed")) {
          return "'" + arg + "' removed";
        }
      }
    }

    return output;
  }

  public void executeTest(List<Map.Entry<String, String>> commandsAndResults) {
    final List<String> commands = commandsAndResults.stream().map(Map.Entry::getKey).toList();
    final List<String> expectedResult =
        commandsAndResults.stream().map(Map.Entry::getValue).toList();

    final List<String> actualResult = runner.executeCommands(commands);

    assertEquals(expectedResult, actualResult);
  }

  @Override
  public List<String> executeCommands(List<String> commands) {
    return runner.executeCommands(commands);
  }
}
