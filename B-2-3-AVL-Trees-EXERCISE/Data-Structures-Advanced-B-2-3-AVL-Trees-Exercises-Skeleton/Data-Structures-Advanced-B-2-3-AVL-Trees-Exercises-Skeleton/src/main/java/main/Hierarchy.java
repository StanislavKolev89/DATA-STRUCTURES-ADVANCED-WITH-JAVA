package main;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Hierarchy<T> implements IHierarchy<T> {

    private Map<T, HierarchyNode<T>> data;

    public Hierarchy(T value) {
        this.data = new HashMap<>();
        HierarchyNode<T> tHierarchyNode = new HierarchyNode<>(value);
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
        if(parent == null){
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
        return existAndGetElement(element) == null ? null: this.data.get(element).getChildren().stream().map(HierarchyNode::getValue).collect(Collectors.toList());
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
        return null;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public T next() {
                return null;
            }
        };
    }

    private HierarchyNode<T> existAndGetElement(T element) {
        if (!this.data
                .containsKey(element)) {
            throw new IllegalArgumentException();
        }

    if(this.data.values().contains(element)){
        throw new IllegalArgumentException();
    }

        return this.data.get(element);
    }

    private void childExists(HierarchyNode<T> tHierarchyNode, T child) {
        if(this.data.values().stream().map(HierarchyNode::getValue).collect(Collectors.toList()).contains(child)){
            throw new IllegalArgumentException();
        }
    }
}
