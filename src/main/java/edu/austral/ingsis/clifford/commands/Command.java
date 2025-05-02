package edu.austral.ingsis.clifford.commands;

import edu.austral.ingsis.clifford.filesystem.FileSystem;
import edu.austral.ingsis.clifford.result.CommandResult;
import edu.austral.ingsis.clifford.result.Result;
import edu.austral.ingsis.clifford.tree.structure.Tree;

public sealed interface Command<T extends FileSystem>
    permits Cd, Ls, Mkdir, Touch, Rm, Pwd, Invalid {

  Result<T> execute();

  default Result<T> createResult(Tree<T> tree, String message) {
    return new CommandResult<T>(message, tree);
  }
}
