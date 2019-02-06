package edu.elte.thesis.model.graph;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents a tree node.
 *
 * @author Viktoria Sinkovics
 */
public class Node<T> {

    private static final Logger LOGGER = LogManager.getLogger(Node.class);

    protected boolean root;

    protected Node<T> parent;

    protected List<Node<T>> children;

    protected final T entity;

    public Node(Node<T> parent, List<Node<T>> children, T entity) {
        Assert.notNull(entity, "entity should not be null.");

        this.parent = parent;
        this.children = children;
        this.entity = entity;
        this.root = false;
    }

    public Node(Node<T> parent, T entity) {
        Assert.notNull(entity, "entity should not be null.");

        this.parent = parent;
        this.entity = entity;
        this.root = false;
        this.children = new ArrayList<>();
    }

    public Node(T entity) {
        Assert.notNull(entity, "entity should not be null.");

        this.entity = entity;
        this.root = false;
        this.parent = null;
        this.children = new ArrayList<>();
    }

    public Optional<Node<T>> getParent() {
        return Optional.ofNullable(parent);
    }

    public List<Node<T>> getChildren() {
        return children;
    }

    public T getEntity() {
        return entity;
    }

    public boolean isRoot() {
        return root;
    }

    public void makeRoot() {
        this.parent = null;
        this.root = true;
    }

    public void setParent(Node<T> parent) {
        if (this.root) {
            if (Objects.nonNull(parent)) {
                LOGGER.info("This node is the root.");
            }
        } else {
            if (Objects.isNull(this.parent) && Objects.nonNull(parent)) {
                this.parent = parent;
            } else {
                throw new IllegalStateException("Trying to remove parent on a non-root cell or to change the parent."
                        + " [Cell: " + this + "];[Parent: " + parent + "]");
            }
        }
    }

    public Node<T> addChild(Node<T> child) {
        Assert.notNull(child, "child should not be null.");
        Assert.isTrue(this.children.size() < 4, "Node can't have more than 4 children.");

        this.children.add(child);

        return child;
    }

    public List<Node<T>> addChildren(List<Node<T>> children) {
        Assert.notNull(children, "child should not be null.");

        this.children.addAll(children);

        Assert.isTrue(this.children.size() < 5, "Node can't have more than 4 children.");

        return children;
    }

    @Override public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Node<?> node = (Node<?>) other;
        return root == node.root
                && Objects.equals(entity, node.entity);
    }

    @Override public int hashCode() {
        return Objects.hash(root, entity);
    }

    @Override
    public String toString() {
        return "Node{"
                + "parent=" + parent
                + ", children=" + children
                + ", entity=" + entity
                + '}';
    }

}
