package edu.austral.ingsis.executortest;

import static org.junit.jupiter.api.Assertions.*;

import edu.austral.ingsis.clifford.commands.executer.CommandExecutor;
import edu.austral.ingsis.clifford.filesystem.Directory;
import edu.austral.ingsis.clifford.filesystem.FileSystem;
import edu.austral.ingsis.clifford.result.Result;
import edu.austral.ingsis.clifford.tree.structure.NonBinaryTree;
import edu.austral.ingsis.clifford.tree.structure.Tree;
import edu.austral.ingsis.clifford.tree.structure.TreeNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommandExecutorTest {

  private Tree<FileSystem> tree;
  private TreeNode<FileSystem> currentNode;

  @BeforeEach
  void setup() {
    tree = new NonBinaryTree<>(new Directory("root"));
    currentNode = tree.getRoot();
  }

  @Test
  void testMkdirCommand() {
    Result<FileSystem> result = CommandExecutor.executeCommand("mkdir test", tree, currentNode);
    tree = result.getTree();
    currentNode = result.getCurrentNode();
    assertEquals("'test' directory created", result.getMessage());

    // Check directly in the root children (where we can see the directory exists from debug output)
    boolean dirFound = false;
    for (TreeNode<FileSystem> child : tree.getRoot().getChildren()) {
      if (child.getData().name().equals("test")) {
        dirFound = true;
        break;
      }
    }
    assertTrue(dirFound);
  }

  @Test
  void testCdCommand() {
    // Create directory first
    Result<FileSystem> mkdirResult =
        CommandExecutor.executeCommand("mkdir test", tree, currentNode);
    tree = mkdirResult.getTree();
    currentNode = mkdirResult.getCurrentNode();

    System.out.println(mkdirResult.getCurrentNode().getName());

    Result<FileSystem> cdResult = CommandExecutor.executeCommand("cd test", tree, currentNode);

    assertEquals("moved to directory 'test'", cdResult.getMessage());
    assertEquals("test", cdResult.getCurrentNode().getData().name());
  }

  @Test
  void testCdToNonExistentDirectory() {
    Result<FileSystem> result = CommandExecutor.executeCommand("cd nonexistent", tree, currentNode);

    assertEquals("'nonexistent' directory does not exist", result.getMessage());
    assertEquals(currentNode, result.getCurrentNode());
  }

  @Test
  void testTouchCommand() {
    Result<FileSystem> result = CommandExecutor.executeCommand("touch file.txt", tree, currentNode);

    assertEquals("'file.txt' file created", result.getMessage());

    // Verify file was created
    boolean fileFound = false;
    for (TreeNode<FileSystem> child : result.getCurrentNode().getChildren()) {
      if (child.getData().name().equals("file.txt")) {
        fileFound = true;
        break;
      }
    }
    assertTrue(fileFound);
  }

  @Test
  void testLsCommand() {
    // Create a file and directory
    Result<FileSystem> touchResult =
        CommandExecutor.executeCommand("touch file1.txt", tree, currentNode);
    tree = touchResult.getTree();
    currentNode = touchResult.getCurrentNode();

    Result<FileSystem> mkdirResult =
        CommandExecutor.executeCommand("mkdir dir1", tree, currentNode);
    tree = mkdirResult.getTree();
    currentNode = mkdirResult.getCurrentNode();

    Result<FileSystem> lsResult = CommandExecutor.executeCommand("ls", tree, currentNode);

    assertEquals("file1.txt dir1", lsResult.getMessage());
  }

  @Test
  void testLsWithSortOrderCommand() {
    // Create a file and directory
    Result<FileSystem> touchResult =
        CommandExecutor.executeCommand("touch file1.txt", tree, currentNode);
    tree = touchResult.getTree();
    currentNode = touchResult.getCurrentNode();

    Result<FileSystem> mkdirResult =
        CommandExecutor.executeCommand("mkdir dir1", tree, currentNode);
    tree = mkdirResult.getTree();
    currentNode = mkdirResult.getCurrentNode();

    Result<FileSystem> lsResult =
        CommandExecutor.executeCommand("ls --ord=desc", tree, currentNode);

    assertEquals("file1.txt dir1", lsResult.getMessage());
  }

  @Test
  void testRmCommand() {
    // Create a file first
    Result<FileSystem> touchResult =
        CommandExecutor.executeCommand("touch file1.txt", tree, currentNode);
    tree = touchResult.getTree();
    currentNode = touchResult.getCurrentNode();

    Result<FileSystem> rmResult = CommandExecutor.executeCommand("rm file1.txt", tree, currentNode);

    assertEquals("'file1.txt' removed", rmResult.getMessage());

    // Verify file was removed
    boolean fileFound = false;
    for (TreeNode<FileSystem> child : rmResult.getCurrentNode().getChildren()) {
      if (child.getData().name().equals("file1.txt")) {
        fileFound = true;
        break;
      }
    }
    assertFalse(fileFound);
  }

  @Test
  void testRmDirectoryError() {
    // Create directory first
    Result<FileSystem> mkdirResult =
        CommandExecutor.executeCommand("mkdir dir1", tree, currentNode);
    tree = mkdirResult.getTree();
    currentNode = mkdirResult.getCurrentNode();

    Result<FileSystem> rmResult = CommandExecutor.executeCommand("rm dir1", tree, currentNode);

    assertEquals("cannot remove 'dir1', is a directory", rmResult.getMessage());
  }

  @Test
  void testRmDirectoryRecursive() {
    // Create directory first
    Result<FileSystem> mkdirResult =
        CommandExecutor.executeCommand("mkdir dir1", tree, currentNode);
    tree = mkdirResult.getTree();
    currentNode = mkdirResult.getCurrentNode();

    Result<FileSystem> rmResult =
        CommandExecutor.executeCommand("rm --recursive dir1", tree, currentNode);

    assertEquals("'dir1' removed", rmResult.getMessage());

    // Verify directory was removed
    boolean dirFound = false;
    for (TreeNode<FileSystem> child : rmResult.getCurrentNode().getChildren()) {
      if (child.getData().name().equals("dir1")) {
        dirFound = true;
        break;
      }
    }
    assertFalse(dirFound);
  }

  @Test
  void testPwdCommand() {
    // Create and navigate to directory
    Result<FileSystem> mkdirResult =
        CommandExecutor.executeCommand("mkdir dir1", tree, currentNode);
    tree = mkdirResult.getTree();
    currentNode = mkdirResult.getCurrentNode();

    Result<FileSystem> cdResult = CommandExecutor.executeCommand("cd dir1", tree, currentNode);
    tree = cdResult.getTree();
    System.out.println("root=" + tree.getRoot().getData().name());
    currentNode = cdResult.getCurrentNode();

    Result<FileSystem> pwdResult = CommandExecutor.executeCommand("pwd", tree, currentNode);

    assertEquals("/dir1", pwdResult.getMessage());
  }

  @Test
  void testComplexPathNavigation() {
    // Create nested directory structure
    Result<FileSystem> result = CommandExecutor.executeCommand("mkdir docs", tree, currentNode);
    tree = result.getTree();
    currentNode = result.getCurrentNode();

    result = CommandExecutor.executeCommand("cd docs", tree, currentNode);
    tree = result.getTree();
    currentNode = result.getCurrentNode();

    result = CommandExecutor.executeCommand("mkdir reports", tree, currentNode);
    tree = result.getTree();
    currentNode = result.getCurrentNode();

    result = CommandExecutor.executeCommand("cd ..", tree, currentNode);
    tree = result.getTree();
    currentNode = result.getCurrentNode();

    // Navigate using complex path
    result = CommandExecutor.executeCommand("cd docs/reports", tree, currentNode);

    assertEquals("moved to directory 'reports'", result.getMessage());
    assertEquals("reports", result.getCurrentNode().getData().name());
  }

  @Test
  void testCommandSequence() {
    // Execute "mkdir horace" and verify
    Result<FileSystem> result = CommandExecutor.executeCommand("mkdir horace", tree, currentNode);
    assertEquals("'horace' directory created", result.getMessage());
    tree = result.getTree();
    currentNode = result.getCurrentNode();

    // Execute "mkdir emily" and verify
    result = CommandExecutor.executeCommand("mkdir emily", tree, currentNode);
    assertEquals("'emily' directory created", result.getMessage());
    tree = result.getTree();
    currentNode = result.getCurrentNode();

    // Execute "mkdir jetta" and verify
    result = CommandExecutor.executeCommand("mkdir jetta", tree, currentNode);
    assertEquals("'jetta' directory created", result.getMessage());
    tree = result.getTree();
    currentNode = result.getCurrentNode();

    // Execute "ls" and verify
    result = CommandExecutor.executeCommand("ls", tree, currentNode);
    assertEquals("horace emily jetta", result.getMessage());
    tree = result.getTree();
    currentNode = result.getCurrentNode();

    // Execute "cd emily" and verify
    result = CommandExecutor.executeCommand("cd emily", tree, currentNode);
    assertEquals("moved to directory 'emily'", result.getMessage());
    tree = result.getTree();
    currentNode = result.getCurrentNode();

    // Execute "pwd" and verify
    result = CommandExecutor.executeCommand("pwd", tree, currentNode);
    assertEquals("/emily", result.getMessage());
    tree = result.getTree();
    currentNode = result.getCurrentNode();

    // Execute "touch elizabeth.txt" and verify
    result = CommandExecutor.executeCommand("touch elizabeth.txt", tree, currentNode);
    assertEquals("'elizabeth.txt' file created", result.getMessage());
    tree = result.getTree();
    currentNode = result.getCurrentNode();

    // Execute "mkdir t-bone" and verify
    result = CommandExecutor.executeCommand("mkdir t-bone", tree, currentNode);
    assertEquals("'t-bone' directory created", result.getMessage());
    tree = result.getTree();
    currentNode = result.getCurrentNode();

    // Execute "ls" and verify
    result = CommandExecutor.executeCommand("ls", tree, currentNode);
    assertEquals("elizabeth.txt t-bone", result.getMessage());
  }

  @Test
  void testFileSystemOperationsSequence() {
    // Create directory "horace"
    Result<FileSystem> result = CommandExecutor.executeCommand("mkdir horace", tree, currentNode);
    assertEquals("'horace' directory created", result.getMessage());
    tree = result.getTree();
    currentNode = result.getCurrentNode();

    // Create directory "emily"
    result = CommandExecutor.executeCommand("mkdir emily", tree, currentNode);
    assertEquals("'emily' directory created", result.getMessage());
    tree = result.getTree();
    currentNode = result.getCurrentNode();

    // Create directory "jetta"
    result = CommandExecutor.executeCommand("mkdir jetta", tree, currentNode);
    assertEquals("'jetta' directory created", result.getMessage());
    tree = result.getTree();
    currentNode = result.getCurrentNode();

    // Navigate to "emily"
    result = CommandExecutor.executeCommand("cd emily", tree, currentNode);
    assertEquals("moved to directory 'emily'", result.getMessage());
    tree = result.getTree();
    currentNode = result.getCurrentNode();

    // Create file "elizabeth.txt"
    result = CommandExecutor.executeCommand("touch elizabeth.txt", tree, currentNode);
    assertEquals("'elizabeth.txt' file created", result.getMessage());
    tree = result.getTree();
    currentNode = result.getCurrentNode();

    // Create directory "t-bone"
    result = CommandExecutor.executeCommand("mkdir t-bone", tree, currentNode);
    assertEquals("'t-bone' directory created", result.getMessage());
    tree = result.getTree();
    currentNode = result.getCurrentNode();

    // List contents
    result = CommandExecutor.executeCommand("ls", tree, currentNode);
    assertEquals("elizabeth.txt t-bone", result.getMessage());
    tree = result.getTree();
    currentNode = result.getCurrentNode();

    // Try to remove directory without recursive flag
    result = CommandExecutor.executeCommand("rm t-bone", tree, currentNode);
    assertEquals("cannot remove 't-bone', is a directory", result.getMessage());
    tree = result.getTree();
    currentNode = result.getCurrentNode();

    // Remove directory with recursive flag
    result = CommandExecutor.executeCommand("rm --recursive t-bone", tree, currentNode);
    assertEquals("'t-bone' removed", result.getMessage());
    tree = result.getTree();
    currentNode = result.getCurrentNode();

    // List contents
    result = CommandExecutor.executeCommand("ls", tree, currentNode);
    assertEquals("elizabeth.txt", result.getMessage());
    tree = result.getTree();
    currentNode = result.getCurrentNode();

    // Remove file
    result = CommandExecutor.executeCommand("rm elizabeth.txt", tree, currentNode);
    assertEquals("'elizabeth.txt' removed", result.getMessage());
    tree = result.getTree();
    currentNode = result.getCurrentNode();

    // List contents
    result = CommandExecutor.executeCommand("ls", tree, currentNode);
    assertEquals("", result.getMessage());
  }
}
