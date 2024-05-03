package bstmap;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class BSTMap<K extends Comparable, V> implements Map61B<K, V> {
    private int size;
    private BSTNode root;

    private class BSTNode {
        private K key;
        private V value;
        BSTNode right, left;

        private BSTNode(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public BSTMap() {


    }

    @Override
    public void clear() {
        clear(root);
        size = 0;
        root = null;
    }

    public void clear(BSTNode node) {
        if (node == null) {
            return;
        } else {
            clear(node.left);
            clear(node.right);
        }
        node.right = node.left = null;
    }

    @Override
    public boolean containsKey(K key) {
        return containsKey(this.root, key);
    }

    private boolean containsKey(BSTNode node, K key) {
        if (node == null) {
            return false;
        } else if (key.compareTo(node.key) > 0) {
            return containsKey(node.right, key);
        } else if (key.compareTo(node.key) < 0) {
            return containsKey(node.left, key);
        } else {
            return true;
        }
    }

    @Override
    public V get(K key) {
        return get(root, key);
    }

    private V get(BSTNode node, K key) {
        if (node == null) {
            return null;
        } else if (key.compareTo(node.key) > 0) {
            return get(node.right, key);
        } else if (key.compareTo(node.key) < 0) {
            return get(node.left, key);
        } else {
            return node.value;
        }
    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        if (root == null) {
            root = new BSTNode(key, value);
            size = 1;
        } else if (put(root, key, value)) {
            size = size + 1;
        }

    }

    private boolean put(BSTNode node, K key, V value) {
        if (key.compareTo(node.key) == 0) {
            return false;
        } else if (key.compareTo(node.key) < 0) {
            if (node.left == null) {
                node.left = new BSTNode(key, value);
                return true;
            } else {
                return put(node.left, key, value);
            }
        } else {
            if (node.right == null) {
                node.right = new BSTNode(key, value);
                return true;
            } else {
                return put(node.right, key, value);
            }
        }
    }

    @Override
    public Set<K> keySet() {
        TreeSet<V> resultSet = new TreeSet<>();
        keySet(resultSet, root);
        return (Set) resultSet;

    }

    private void keySet(TreeSet<V> set, BSTNode node) {
        if (node == null) {
            return;
        }
        set.add(node.value);
        keySet(set, node.left);
        keySet(set, node.right);
    }

    @Override
    public V remove(K key) throws UnsupportedOperationException {
        return null;
    }


    @Override
    public V remove(K key, V value) throws UnsupportedOperationException {
        return null;
    }

    @Override
    public Iterator<K> iterator() throws UnsupportedOperationException {
        return null;
    }
}
