import java.util.function.Consumer;

public class AVL<T extends Comparable<T>> {

    private Node<T> root;

    public Node<T> getRoot() {
        return this.root;
    }

    public boolean contains(T item) {
        Node<T> node = this.search(this.root, item);
        return node != null;
    }

    public void insert(T item) {
        this.root = this.insert(this.root, item);
    }

    public void eachInOrder(Consumer<T> consumer) {
        this.eachInOrder(this.root, consumer);
    }

    private void eachInOrder(Node<T> node, Consumer<T> action) {
        if (node == null) {
            return;
        }

        this.eachInOrder(node.left, action);
        action.accept(node.value);
        this.eachInOrder(node.right, action);
    }

    private Node<T> insert(Node<T> node, T item) {
        if (node == null) {
            return new Node<>(item);
        }

        int cmp = item.compareTo(node.value);
        if (cmp < 0) {
            node.left = this.insert(node.left, item);
        } else if (cmp > 0) {
            node.right = this.insert(node.right, item);
        }
        updateHeight(node);
        node =balance(node);
        return node;
    }

    private Node<T> search(Node<T> node, T item) {
        if (node == null) {
            return null;
        }

        int cmp = item.compareTo(node.value);
        if (cmp < 0) {
            return search(node.left, item);
        } else if (cmp > 0) {
            return search(node.right, item);
        }

        return node;
    }


    private int height(Node<T> node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    private void updateHeight(Node<T> node) {
        node.height = Math.max(height(node.left), height(node.right)) + 1;
    }

    private Node<T> rotateRight(Node<T> node) {
        Node<T> newNode = node.left;
        node.left = newNode.right;
        newNode.right = node;

        updateHeight(node);
        updateHeight(newNode);

        return newNode;
    }

    private Node<T> rotateLeft(Node<T> node) {
        Node<T> newNode = node.right;
        node.right = newNode.left;
        newNode.left = node;

        updateHeight(node);
        updateHeight(newNode);

        return newNode;
    }


    private Node<T> balance(Node<T> node) {
        int balance = this.height(node.left) - this.height(node.right);

        if (balance < -1) {
            int childBalance = this.height(node.right.left) - this.height(node.right.right);
            if (childBalance > 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        } else if (balance > 1) {
            int childBalance = this.height(node.left.left) - this.height(node.left.right);
            if (childBalance < 0) {
                node.right = rotateRight(node.right);
            }
            return rotateRight(node);
        }
        return node;
    }
}
