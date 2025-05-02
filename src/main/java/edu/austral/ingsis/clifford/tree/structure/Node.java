package edu.austral.ingsis.clifford.tree.structure;

import edu.austral.ingsis.clifford.filesystem.FileSystem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class Node<T extends FileSystem> implements TreeNode<T> {
  private final T fileSystem;
  private final List<Node<T>> children;

  public Node(T fileSystem) {
    this.fileSystem = Objects.requireNonNull(fileSystem, "FileSystem cannot be null");
    this.children = Collections.emptyList();
  }

  public Node(T fileSystem, List<Node<T>> children) {
    this.fileSystem = Objects.requireNonNull(fileSystem, "FileSystem cannot be null");
    this.children = List.copyOf(children);
  }

  @Override
  public Node<T> withChild(T childData) {
    Node<T> childNode = new Node<>(childData);
    List<Node<T>> newChildren = List.copyOf(this.children);
    newChildren = new ArrayList<>(newChildren);
    newChildren.add(childNode);
    return new Node<>(this.fileSystem, newChildren);
  }

  @Override
  public Node<T> withUpdatedChild(int index, TreeNode<T> updatedChild) {
    if (index < 0 || index >= children.size()) {
      throw new IndexOutOfBoundsException("Child index out of bounds: " + index);
    }

    List<Node<T>> newChildren = new ArrayList<>(this.children);
    newChildren.set(index, (Node<T>) updatedChild);
    return new Node<>(this.fileSystem, newChildren);
  }

  @Override
  public T getData() {
    return fileSystem;
  }

  @Override
  public List<Node<T>> getChildren() {
    return Collections.unmodifiableList(children);
  }

  @Override
  public String getName() {
    return fileSystem.name();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Node<?> node = (Node<?>) o;
    return fileSystem.equals(node.fileSystem) && children.equals(node.children);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fileSystem, children);
  }
}
