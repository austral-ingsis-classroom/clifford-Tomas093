package edu.austral.ingsis.treetest;

import edu.austral.ingsis.clifford.filesystem.File;
import edu.austral.ingsis.clifford.filesystem.FileSystem;
import edu.austral.ingsis.clifford.filesystem.Directory;
import edu.austral.ingsis.clifford.tree.structure.NonBinaryTree;
import edu.austral.ingsis.clifford.tree.structure.Node;
import edu.austral.ingsis.clifford.tree.structure.Tree;
import edu.austral.ingsis.clifford.tree.structure.TreeNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class NonBinaryTreeTest {

  private NonBinaryTree<FileSystem> tree;
  private Directory root;

  @BeforeEach
  void setUp() {
    root = new Directory("root");
    tree = new NonBinaryTree<>(root);
  }

  @Test
  void createTreeWithRootData() {
    assertNotNull(tree.getRoot());
    assertEquals(root, tree.getRoot().getData());
    assertEquals("root", tree.getRoot().getData().name());
    assertFalse(tree.isEmpty());
  }

  @Test
  void createTreeWithRootNode() {
    Node<FileSystem> rootNode = new Node<>(new Directory("documents"));
    NonBinaryTree<FileSystem> newTree = new NonBinaryTree<>(rootNode);

    assertNotNull(newTree.getRoot());
    assertEquals("documents", newTree.getRoot().getData().name());
    assertFalse(newTree.isEmpty());
  }

  @Test
  void withRootCreatesNewTreeWithDifferentRoot() {
    Node<FileSystem> newRootNode = new Node<>(new Directory("documents"));
    Tree<FileSystem> newTree = tree.withRoot(newRootNode);

    assertNotSame(tree, newTree);
    assertEquals("documents", newTree.getRoot().getData().name());
  }

  @Test
  void withCurrentNodeCreatesNewTreeWithDifferentCurrentNode() {
    FileSystem docs = new Directory("docs");
    Tree<FileSystem> treeWithChild = tree.withChildAddedTo(root, docs, tree.getCurrentNode());
    TreeNode<FileSystem> childNode = treeWithChild.findNode(docs);

    Tree<FileSystem> treeWithCurrentNode = treeWithChild.withCurrentNode(childNode);

    assertNotSame(treeWithChild, treeWithCurrentNode);
    assertEquals(childNode, ((NonBinaryTree<FileSystem>)treeWithCurrentNode).getCurrentNode());
    assertEquals(treeWithChild.getRoot(), treeWithCurrentNode.getRoot());
  }

  @Test
  void withChildAddedToParentDataCreatesTreeWithNewChild() {
    FileSystem docs = new Directory("docs");
    Tree<FileSystem> newTree = tree.withChildAddedTo(root, docs, tree.getCurrentNode());

    assertNotSame(tree, newTree);
    assertNotNull(newTree.findNode(docs));
    assertEquals(root, newTree.getParentNode(docs).getData());
  }

  @Test
  void withChildAddedToParentNodeCreatesTreeWithNewChild() {
    FileSystem docs = new Directory("docs");
    Tree<FileSystem> newTree = tree.withChildAddedTo(tree.getRoot(), docs, tree.getCurrentNode());

    assertNotSame(tree, newTree);
    assertNotNull(newTree.findNode(docs));
    assertEquals(root, newTree.getParentNode(docs).getData());
  }

  @Test
  void addFileAndFolderToTree() {
    FileSystem docs = new Directory("docs");
    FileSystem readme = new File("readme.txt");

    Tree<FileSystem> treeWithDocs = tree.withChildAddedTo(root, docs, tree.getCurrentNode());
    Tree<FileSystem> treeWithBoth = treeWithDocs.withChildAddedTo(root, readme, ((NonBinaryTree<FileSystem>)treeWithDocs).getCurrentNode());

    assertNotNull(treeWithBoth.findNode(docs));
    assertNotNull(treeWithBoth.findNode(readme));

    List<String> childrenNames = treeWithBoth.getRoot().getChildren().stream()
            .map(child -> child.getData().name())
            .collect(Collectors.toList());

    assertTrue(childrenNames.contains("docs"));
    assertTrue(childrenNames.contains("readme.txt"));
  }

  @Test
  void createNestedFolderStructure() {
    FileSystem docs = new Directory("docs");
    FileSystem reports = new Directory("reports");
    FileSystem quarterly = new File("quarterly.pdf");

    Tree<FileSystem> treeWithDocs = tree.withChildAddedTo(root, docs, tree.getCurrentNode());
    TreeNode<FileSystem> docsNode = treeWithDocs.findNode(docs);

    Tree<FileSystem> treeWithReports = treeWithDocs.withChildAddedTo(docs, reports,
            ((NonBinaryTree<FileSystem>)treeWithDocs).getCurrentNode());
    TreeNode<FileSystem> reportsNode = treeWithReports.findNode(reports);

    Tree<FileSystem> treeWithQuarterly = treeWithReports.withChildAddedTo(reports, quarterly,
            ((NonBinaryTree<FileSystem>)treeWithReports).getCurrentNode());

    assertNotNull(treeWithQuarterly.findNode(docs));
    assertNotNull(treeWithQuarterly.findNode(reports));
    assertNotNull(treeWithQuarterly.findNode(quarterly));

    assertEquals(docs, treeWithQuarterly.getParentNode(reports).getData());
    assertEquals(reports, treeWithQuarterly.getParentNode(quarterly).getData());
  }

  @Test
  void withNodeRemovedByDataRemovesNode() {
    FileSystem docs = new Directory("docs");
    Tree<FileSystem> treeWithDocs = tree.withChildAddedTo(root, docs, tree.getCurrentNode());

    Tree<FileSystem> treeWithoutDocs = treeWithDocs.withNodeRemoved(docs);

    assertNotSame(treeWithDocs, treeWithoutDocs);
    assertNull(treeWithoutDocs.findNode(docs));
  }

  @Test
  void withNodeRemovedByNodeReferenceRemovesNode() {
    FileSystem docs = new Directory("docs");
    Tree<FileSystem> treeWithDocs = tree.withChildAddedTo(root, docs, tree.getCurrentNode());
    TreeNode<FileSystem> docsNode = treeWithDocs.findNode(docs);

    Tree<FileSystem> treeWithoutDocs = treeWithDocs.withNodeRemoved(docsNode);

    assertNotSame(treeWithDocs, treeWithoutDocs);
    assertNull(treeWithoutDocs.findNode(docs));
  }

  @Test
  void findNodeReturnsCorrectNode() {
    FileSystem docs = new Directory("docs");
    Tree<FileSystem> treeWithDocs = tree.withChildAddedTo(root, docs, tree.getCurrentNode());

    TreeNode<FileSystem> foundNode = treeWithDocs.findNode(docs);

    assertNotNull(foundNode);
    assertEquals(docs, foundNode.getData());
  }

  @Test
  void getParentNodeReturnsCorrectParent() {
    FileSystem docs = new Directory("docs");
    FileSystem reports = new Directory("reports");

    Tree<FileSystem> treeWithDocs = tree.withChildAddedTo(root, docs, tree.getCurrentNode());
    Tree<FileSystem> treeWithReports = treeWithDocs.withChildAddedTo(docs, reports,
            ((NonBinaryTree<FileSystem>)treeWithDocs).getCurrentNode());

    TreeNode<FileSystem> parentNode = treeWithReports.getParentNode(reports);

    assertNotNull(parentNode);
    assertEquals(docs, parentNode.getData());
  }

  @Test
  void equalsAndHashCodeWorkCorrectly() {
    NonBinaryTree<FileSystem> tree1 = new NonBinaryTree<>(root);
    NonBinaryTree<FileSystem> tree2 = new NonBinaryTree<>(root);
    NonBinaryTree<FileSystem> tree3 = new NonBinaryTree<>(new Directory("different"));

    assertEquals(tree1, tree2);
    assertEquals(tree1.hashCode(), tree2.hashCode());
    assertNotEquals(tree1, tree3);
    assertNotEquals(tree1.hashCode(), tree3.hashCode());
  }

  @Test
  void findNodeReturnsNullForNonExistentNode() {
    assertNull(tree.findNode(new File("nonexistent.txt")));
  }

  @Test
  void removeNonExistentNodeReturnsOriginalTree() {
    FileSystem nonExistent = new File("nonexistent.txt");
    Tree<FileSystem> resultTree = tree.withNodeRemoved(nonExistent);

    assertSame(tree, resultTree);
  }
}