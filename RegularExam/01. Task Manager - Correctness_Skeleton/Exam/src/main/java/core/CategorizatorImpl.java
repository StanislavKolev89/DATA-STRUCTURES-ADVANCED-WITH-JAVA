package core;

import models.Category;

import java.util.*;
import java.util.stream.Collectors;

public class CategorizatorImpl implements Categorizator {

    private final Map<String, Category> categoryMap;
    private final Map<String, Set<Category>> parentsOfMap;
    private final Map<String, Category> childOfMap;

    public CategorizatorImpl() {
        this.categoryMap = new LinkedHashMap<>();
        this.parentsOfMap = new LinkedHashMap<>();
        this.childOfMap = new LinkedHashMap<>();
    }

    @Override
    public void addCategory(Category category) {
        if (this.contains(category)) {
            throw new IllegalArgumentException();
        }
        this.categoryMap.put(category.getId(), category);

    }

    @Override
    public void assignParent(String childCategoryId, String parentCategoryId) {
        Category child = this.categoryMap.get(childCategoryId);
        Category parent = this.categoryMap.get(parentCategoryId);
        if (child == null || parent == null) {
            throw new IllegalArgumentException();
        }

        if (this.childOfMap.containsKey(child.getId())) {
            Category parentOfChild = this.childOfMap.get(child.getId());
            if (parentOfChild.equals(parent)) {
                throw new IllegalArgumentException();
            }
        }
        this.parentsOfMap.putIfAbsent(parentCategoryId, new LinkedHashSet<>());
        this.parentsOfMap.get(parentCategoryId).add(child);
        this.childOfMap.put(child.getId(), parent);


    }

    @Override
    public void removeCategory(String categoryId) {
        Category category = this.categoryMap.remove(categoryId);
        if (category == null) {
            throw new IllegalArgumentException();
        }
        Set<Category> categories = this.parentsOfMap.get(category.getId());

        if (categories.isEmpty()) {
            throw new IllegalArgumentException();
        }
        categories.forEach(c -> this.categoryMap.remove(c));


    }

    @Override
    public boolean contains(Category category) {
        return this.categoryMap.containsKey(category.getId());
    }

    @Override
    public int size() {
        return this.categoryMap.size();
    }

    @Override
    public Iterable<Category> getChildren(String categoryId) {
        if (categoryId == null) {
            throw new IllegalArgumentException();
        }
        Category parent = this.categoryMap.get(categoryId);
        if (parent == null) {
            throw new IllegalArgumentException();
        }

        List<Category> allChildren = this.getAllChildren(parent);
        return allChildren;
    }

    @Override
    public Iterable<Category> getHierarchy(String categoryId) {
        if (categoryId == null) {
            throw new IllegalArgumentException();
        }

        List<Category> parents = new ArrayList<>();
        Category child = this.categoryMap.get(categoryId);
        Deque<Category> deque = new ArrayDeque<>();

        if (child == null) {
            throw new IllegalArgumentException();
        }

        if (this.childOfMap.containsKey(child.getId())) {
            parents.add(child);
            deque.offer(child);
            while (!deque.isEmpty()) {
                Category polledChild = deque.poll();
                Category parent = this.childOfMap.get(polledChild.getId());
                if (parent == null) {

                    Collections.reverse(parents);
                    return parents;
                }

                parents.add(parent);
                deque.offer(parent);
            }
        }

        return null;
    }

    @Override
    public Iterable<Category> getTop3CategoriesOrderedByDepthOfChildrenThenByName() {


        return this.categoryMap.values().stream().sorted((f, s) -> {
            if (this.getDepth(f) != this.getDepth(s)) {
                return this.getDepth(s) - this.getDepth(f);
            }
            return f.getName().compareTo(s.getName());
        }).limit(3).collect(Collectors.toList());
    }

    private int getDepth(Category parent) {

        return getAllChildren(parent).size();
    }

    private List<Category> getAllChildren(Category parent) {

        List<Category> cate = new ArrayList<>();
        Set<Category> categories = this.parentsOfMap.get(parent.getId());

        Deque<Category> deque = new ArrayDeque<>();
        if (categories != null && !categories.isEmpty()) {

            for (Category category : categories) {
                cate.add(category);
                deque.offer(category);
            }
        }

        while (!deque.isEmpty()) {
            Category poll = deque.poll();
            Set<Category> categories1 = this.parentsOfMap.get(poll.getId());
            if (categories1 != null && !categories1.isEmpty()) {
                for (Category category : categories1) {
                    cate.add(category);
                    deque.offer(category);
                }
            }

        }

        return cate;

    }

}
