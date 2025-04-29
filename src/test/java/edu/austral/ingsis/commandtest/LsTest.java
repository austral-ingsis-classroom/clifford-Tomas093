package edu.austral.ingsis.commandtest;

import edu.austral.ingsis.clifford.commands.Ls;
import edu.austral.ingsis.clifford.filesystem.File;
import edu.austral.ingsis.clifford.filesystem.FileSystem;
import edu.austral.ingsis.clifford.filesystem.Directory;
import edu.austral.ingsis.clifford.result.Result;
import edu.austral.ingsis.clifford.tree.structure.NonBinaryTree;
import edu.austral.ingsis.clifford.tree.structure.Tree;
import edu.austral.ingsis.clifford.tree.structure.TreeNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LsTest {

    private Tree<FileSystem> fileSystem;
    private Ls ls;
    private TreeNode<FileSystem> docsNode;
    private TreeNode<FileSystem> emptyFolderNode;

    @BeforeEach
    void setUp() {
        Directory root = new Directory("root");
        Directory docs = new Directory("docs");
        Directory reports = new Directory("reports");
        Directory pictures = new Directory("pictures");
        FileSystem file = new File("file.txt");

        fileSystem = new NonBinaryTree<>(root);
        ls = new Ls();

        // Add 'docs' to root
        fileSystem = fileSystem.withChildAddedTo(fileSystem.getRoot(), docs, fileSystem.getRoot());
        TreeNode<FileSystem> docsNodeLocal = fileSystem.findNode(docs);
        docsNode = docsNodeLocal;

        // Add 'reports' as child of 'docs'
        fileSystem = fileSystem.withChildAddedTo(docsNodeLocal, reports, docsNodeLocal);

        // Add 'pictures' as child of 'root'
        fileSystem = fileSystem.withChildAddedTo(fileSystem.getRoot(), pictures, fileSystem.getRoot());

        // Add 'file.txt' as child of 'root'
        fileSystem = fileSystem.withChildAddedTo(fileSystem.getRoot(), file, fileSystem.getRoot());

        // Add 'anotherFile.md' as child of 'root'
        FileSystem anotherFile = new File("anotherFile.md");
        fileSystem = fileSystem.withChildAddedTo(fileSystem.getRoot(), anotherFile, fileSystem.getRoot());

        // Add empty folder as child of 'root'
        Directory emptyDirectory = new Directory("empty");
        fileSystem = fileSystem.withChildAddedTo(fileSystem.getRoot(), emptyDirectory, fileSystem.getRoot());
        emptyFolderNode = fileSystem.findNode(emptyDirectory);
    }

    @Test
    @DisplayName("Test ls in root directory")
    void testLsInRootDirectory() {
        Result<FileSystem> result = ls.execute(fileSystem, "", fileSystem.getRoot());
        assertEquals("docs pictures file.txt anotherFile.md empty", result.getMessage());
    }

    @Test
    @DisplayName("Test ls in docs directory")
    void testLsInDocsDirectory() {
        docsNode = fileSystem.findNode(docsNode.getData());
        Result<FileSystem> result = ls.execute(fileSystem, "", docsNode);
        assertEquals("reports", result.getMessage());
    }

    @Test
    @DisplayName("Test ls in empty directory")
    void testLsInEmptyDirectory() {
        Result<FileSystem> result = ls.execute(fileSystem, "", emptyFolderNode);
        assertEquals("", result.getMessage());
    }

    @Test
    @DisplayName("Test ls with ascending order")
    void testLsWithAscendingOrder() {
        Result<FileSystem> result = ls.execute(fileSystem, "--ord=asc", fileSystem.getRoot());
        assertEquals("anotherFile.md docs empty file.txt pictures", result.getMessage());
    }

    @Test
    @DisplayName("Test ls with descending order")
    void testLsWithDescendingOrder() {
        Result<FileSystem> result = ls.execute(fileSystem, "--ord=desc", fileSystem.getRoot());
        assertEquals("pictures file.txt empty docs anotherFile.md", result.getMessage());
    }

    @Test
    @DisplayName("Test ls with invalid order argument")
    void testLsWithInvalidOrderArgument() {
        Result<FileSystem> result = ls.execute(fileSystem, "--ord=invalid", fileSystem.getRoot());
        // Should behave like no sorting argument
        assertEquals("docs pictures file.txt anotherFile.md empty", result.getMessage());
    }

    @Test
    @DisplayName("Test ls with null argument")
    void testLsWithNullArgument() {
        Result<FileSystem> result = ls.execute(fileSystem, null, fileSystem.getRoot());
        assertEquals("docs pictures file.txt anotherFile.md empty", result.getMessage());
    }
}