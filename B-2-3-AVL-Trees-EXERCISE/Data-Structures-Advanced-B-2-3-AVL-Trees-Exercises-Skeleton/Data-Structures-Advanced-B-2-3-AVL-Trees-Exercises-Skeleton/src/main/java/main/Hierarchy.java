package main;

import java.util.*;
import java.util.stream.Collectors;

public class Hierarchy<T> implements IHierarchy<T> {

    private Map<T, HierarchyNode<T>> data;
    private HierarchyNode<T> root;

    public Hierarchy(T value) {
        this.data = new HashMap<>();

        HierarchyNode<T> tHierarchyNode = new HierarchyNode<>(value);
        this.root = tHierarchyNode;
        this.data.put(value, tHierarchyNode);
    }

    @Override
    public int getCount() {
        return this.data
                .size();
    }

    @Override
    public void add(T element, T child) {
        HierarchyNode<T> tHierarchyNode = existAndGetElement(element);
        childExists(tHierarchyNode, child);
        HierarchyNode<T> childNode = new HierarchyNode<>(child);
        childNode.setParent(tHierarchyNode);
        this.data.put(child, childNode);
        tHierarchyNode.getChildren().add(childNode);
    }

    @Override
    public void remove(T element) {
        HierarchyNode<T> parent = existAndGetElement(element).getParent();
        if (parent == null) {
            throw new IllegalStateException();
        }
        HierarchyNode<T> toBeRemoved = existAndGetElement(element);
        List<HierarchyNode<T>> children = toBeRemoved.getChildren();
        children.forEach(c -> c.setParent(parent));
        parent.getChildren().addAll(children);
        parent.getChildren().remove(toBeRemoved);
        this.data.remove(element);

    }

    @Override
    public Iterable<T> getChildren(T element) {
        return existAndGetElement(element) == null ? null : this.data.get(element).getChildren().stream().map(HierarchyNode::getValue).collect(Collectors.toList());
    }

    @Override
    public T getParent(T element) {
        return this.existAndGetElement(element).getParent() == null ? null : this.existAndGetElement(element).getParent().getValue();
    }

    @Override
    public boolean contains(T element) {
        return this.data.containsKey(element);
    }

    @Override
    public Iterable<T> getCommonElements(IHierarchy<T> other) {
        return this.data.values().stream().filter(e -> other.contains(e.getValue())).map(HierarchyNode::getValue).collect(Collectors.toList());
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            Deque<HierarchyNode<T>> deque = new ArrayDeque<>(Collections.singletonList(root));

            @Override
            public boolean hasNext() {
                return deque.size() > 0;
            }

            @Override
            public T next() {
                HierarchyNode<T> nextElement = deque.poll();
                deque.addAll(nextElement.getChildren());
                return nextElement.getValue();
            }
        };
    }

    private HierarchyNode<T> existAndGetElement(T element) {
        if (!this.data
                .containsKey(element)) {
            throw new IllegalArgumentException();
        }

        if (this.data.values().contains(element)) {
            throw new IllegalArgumentException();
        }

        return this.data.get(element);
    }

    private void childExists(HierarchyNode<T> tHierarchyNode, T child) {
        if (this.data.values().stream().map(HierarchyNode::getValue).collect(Collectors.toList()).contains(child)) {
            throw new IllegalArgumentException();
        }
    }
}
