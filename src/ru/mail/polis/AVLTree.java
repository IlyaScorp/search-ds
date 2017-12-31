package ru.mail.polis;

import java.util.AbstractSet;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.SortedSet;

public class AVLTree<E extends Comparable<E>> extends AbstractSet<E> implements BalancedSortedSet<E> {

    private final Comparator<E> comparator;

    private BinarySearchTree.Node root; //todo: Создайте новый класс если нужно. Добавьте новые поля, если нужно.
    private int size;
    private boolean booleanAdd;
    private boolean booleanRemove;
    //todo: добавьте дополнительные переменные и/или методы если нужно

    public AVLTree() {
        this(null);
    }

    public AVLTree(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    /**
     * Вставляет элемент в дерево.
     * Инвариант: на вход всегда приходит NotNull объект, который имеет корректный тип
     *
     * @param value элемент который необходимо вставить
     * @return true, если элемент в дереве отсутствовал
     */
    @Override
    public boolean add(E value) {
        //todo: следует реализовать
        if (value == null){
            throw new NullPointerException();
        }
        booleanAdd = true;
        root = add(root, value);
        return booleanAdd;
    }

    private BinarySearchTree.Node add(BinarySearchTree.Node node, E value) {
        if (node == null){
            return new BinarySearchTree.Node(value);
        }
        int cmp = compare(value, (E) node.value);
        if (cmp < 0) {
            node.left = add(node.left, value);
        } else if (cmp > 0) {
            node.right = add(node.right, value);
        } else {
            booleanAdd = false;
        }
        node.height = 1 + Math.max(height(node.left), height(node.right));
        return balance(node);
    }

    private int height(BinarySearchTree.Node node){
        return node == null ? -1 : node.height;
    }

    /**
     * Удаляет элемент с таким же значением из дерева.
     * Инвариант: на вход всегда приходит NotNull объект, который имеет корректный тип
     *
     * @param object элемент который необходимо вставить
     * @return true, если элемент содержался в дереве
     */
    @Override
    public boolean remove(Object object) {
        @SuppressWarnings("unchecked")
        E value = (E) object;
        //todo: следует реализовать
        if (value == null){
            throw new NullPointerException();
        }
        booleanRemove = false;
        root = remove(root, value);
        return booleanRemove;
    }

    private BinarySearchTree.Node remove(BinarySearchTree.Node node, E value) {
        if (node == null){
            return null;
        }
        int cmp = compare(value, (E) node.value);
        if (cmp < 0) {
            node.left = remove(node.left, value);
        } else if (cmp > 0) {
            node.right = remove(node.right, value);
        } else {
            booleanRemove = true;
            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            } else {
                BinarySearchTree.Node node2 = node;
                node = min(node2.right);
                node.right = deleteMin(node2.right);
                node.left = node2.left;
            }
        }
        node.height = 1 + Math.max(height(node.left), height(node.right));
        return balance(node);
    }

    /**
     * Ищет элемент с таким же значением в дереве.
     * Инвариант: на вход всегда приходит NotNull объект, который имеет корректный тип
     *
     * @param object элемент который необходимо поискать
     * @return true, если такой элемент содержится в дереве
     */
    @Override
    public boolean contains(Object object) {
        @SuppressWarnings("unchecked")
        E value = (E) object;
        //todo: следует реализовать
        if (value == null){
            throw new NullPointerException();
        }
        return contains((E) object, root);
    }

    private boolean contains(E value, BinarySearchTree.Node node){
        boolean found = false;
        while ((node != null) && !found){
            E nodeval = (E) node.value;
            if (compare(value, nodeval) < 0){
                node = node.left;
            } else if (compare(value, nodeval) > 0){
                node = node.right;
            } else {
                found = true;
                break;
            }
            found = contains(value, node);
        }
        return found;
    }

    /**
     * Ищет наименьший элемент в дереве
     * @return Возвращает наименьший элемент в дереве
     * @throws NoSuchElementException если дерево пустое
     */
    @Override
    public E first() {
        //todo: следует реализовать
        if (isEmpty()){
            throw new java.util.NoSuchElementException();
        }
        return (E) min(root).value;
    }

    /**
     * Ищет наибольший элемент в дереве
     * @return Возвращает наибольший элемент в дереве
     * @throws NoSuchElementException если дерево пустое
     */
    @Override
    public E last() {
        //todo: следует реализовать
        if (isEmpty()){
            throw new java.util.NoSuchElementException();
        }
        return (E) max(root).value;
    }

    private BinarySearchTree.Node deleteMin(BinarySearchTree.Node node) {
        if (node.left == null) {
            return node.right;
        }
        node.left = deleteMin(node.left);
        node.height = 1 + Math.max(height(node.left), height(node.right));
        return balance(node);
    }

    private BinarySearchTree.Node min(BinarySearchTree.Node node) {
        return (node.left == null) ? node : min(node.left);
    }

    private BinarySearchTree.Node max(BinarySearchTree.Node node) {
        return (node.right == null) ? node : max(node.right);
    }

    private int compare(E v1, E v2) {
        return comparator == null ? v1.compareTo(v2) : comparator.compare(v1, v2);
    }

    @Override
    public Comparator<? super E> comparator() {
        return comparator;
    }

    @Override
    public int size() {
        return size(root);
    }

    private int size(BinarySearchTree.Node node){
        if (node == null){
            return 0;
        } else {
            return (int) Math.min((long) (1 + size(node.left)) + size(node.right), Integer.MAX_VALUE);
        }
    }

    @Override
    public String toString() {
        return "AVLTree{" +
                "tree=" + root +
                "size=" + size + ", " +
                '}';
    }

    @Override
    public SortedSet<E> subSet(E fromElement, E toElement) {
        throw new UnsupportedOperationException("subSet");
    }

    @Override
    public SortedSet<E> headSet(E toElement) {
        throw new UnsupportedOperationException("headSet");
    }

    @Override
    public SortedSet<E> tailSet(E fromElement) {
        throw new UnsupportedOperationException("tailSet");
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("iterator");
    }

    /**
     * Обходит дерево и проверяет что высоты двух поддеревьев
     * различны по высоте не более чем на 1
     *
     * @throws NotBalancedTreeException если высоты отличаются более чем на один
     */
    @Override
    public void checkBalanced() throws NotBalancedTreeException {
        traverseTreeAndCheckBalanced(root);
    }

    private int balanceFactor(BinarySearchTree.Node node) {
        return height(node.left) - height(node.right);
    }

    private BinarySearchTree.Node balance(BinarySearchTree.Node node) {
        if (balanceFactor(node) < -1) {
            if (balanceFactor(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            node = rotateLeft(node);
        } else if (balanceFactor(node) > 1) {
            if (balanceFactor(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            node = rotateRight(node);
        }
        return node;
    }

    private BinarySearchTree.Node rotateRight(BinarySearchTree.Node node) {
        BinarySearchTree.Node node2 = node.left;
        node.left = node2.right;
        node2.right = node;
        node.height = 1 + Math.max(height(node.left), height(node.right));
        node2.height = 1 + Math.max(height(node2.left), height(node2.right));
        return node2;
    }

    private BinarySearchTree.Node rotateLeft(BinarySearchTree.Node node) {
        BinarySearchTree.Node node2 = node.right;
        node.right = node2.left;
        node2.left = node;
        node.height = 1 + Math.max(height(node.left), height(node.right));
        node2.height = 1 + Math.max(height(node2.left), height(node2.right));
        return node2;
    }

    private int traverseTreeAndCheckBalanced(BinarySearchTree.Node curr) throws NotBalancedTreeException {
        if (curr == null) {
            return 1;
        }
        int leftHeight = traverseTreeAndCheckBalanced(curr.left);
        int rightHeight = traverseTreeAndCheckBalanced(curr.right);
        if (Math.abs(leftHeight - rightHeight) > 1) {
            throw NotBalancedTreeException.create("The heights of the two child subtrees of any node must be differ by at most one",
                    leftHeight, rightHeight, curr.toString());
        }
        return Math.max(leftHeight, rightHeight) + 1;
    }

}
