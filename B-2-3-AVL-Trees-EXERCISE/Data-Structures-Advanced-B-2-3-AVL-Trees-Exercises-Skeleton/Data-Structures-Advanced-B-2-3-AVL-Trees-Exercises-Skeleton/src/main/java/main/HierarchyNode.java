package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HierarchyNode<T> {
    private T value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HierarchyNode<?> that = (HierarchyNode<?>) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    private HierarchyNode<T> parent;
    private List<HierarchyNode<T>> children;

    public HierarchyNode(T value) {
        this.value = value;
        this.parent = null;
        this.children = new ArrayList<>();
    }

    public T getValue() {
        return value;
    }

    public HierarchyNode<T> setValue(T value) {
        this.value = value;
        return this;
    }

    public HierarchyNode<T> getParent() {
        return parent;
    }

    public void setParent(HierarchyNode<T> parent) {
        this.parent = parent;
    }

    public List<HierarchyNode<T>> getChildren() {
        return children;
    }

    public HierarchyNode<T> setChildren(List<HierarchyNode<T>> children) {
        this.children = children;
        return this;
    }
}
