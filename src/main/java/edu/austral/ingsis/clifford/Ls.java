package edu.austral.ingsis.clifford;

import java.util.ArrayList;
import java.util.Comparator;

public class Ls implements Command {

  @Override
  public void execute(Folder folder, String flag) {
    if (folder != null) {
      ArrayList<FileSystem> children = folder.getChildren();

      if (flag != null) {
        if (flag.equals("asc")) {
          children.sort(Comparator.comparing(FileSystem::getName));
        } else if (flag.equals("desc")) {
          children.sort(Comparator.comparing(FileSystem::getName).reversed());
        }
      }

      StringBuilder result = new StringBuilder();
      for (int i = 0; i < children.size(); i++) {
        result.append(children.get(i).getName());
        if (i < children.size() - 1) {
          result.append(" ");
        }
      }

      System.out.println(result);
    }
  }
}
