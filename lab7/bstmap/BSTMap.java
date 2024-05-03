package bstmap;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
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

    private void clear(BSTNode node) {
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
        TreeSet<K> resultSet = new TreeSet<>();
        keySet(resultSet, root);
        return (Set) resultSet;

    }

    private void keySet(TreeSet<K> set, BSTNode node) {
        if (node == null) {
            return;
        }
        set.add(node.key);
        keySet(set, node.left);
        keySet(set, node.right);
    }

    public void printInOrder() {
        printInOrder(root);
    }

    private void printInOrder(BSTNode node) {
        if (node == null) {
            return;
        }
        printInOrder(node.left);
        System.out.format("[%s->%s]", node.key, node.value);
        printInOrder(node.right);
    }


    @Override
    public V remove(K key) {
        if (containsKey(key)) {
            V targetValue = get(key);
            root = remove(root, key);
            size = size - 1;
            return targetValue;
        }
        return null;
    }

    private BSTNode remove(BSTNode node, K key) {
        if (node == null) {
            return null;
        }
        int cmp = key.compareTo(node.key);
        if (cmp > 0) {
            node.right = remove(node.right, key);
        } else if (cmp < 0) {
            node.left = remove(node.left, key);
        } else {
            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            } else {
                BSTNode originalNode = node;
                node = getMaxChild(node.left);
                node.right = originalNode.right;
                node.left = remove(originalNode.left, node.key);
            }
        }
        return node;
    }

    private BSTNode getMaxChild(BSTNode node) {
        if (node.right == null) {
            return node;
        }
        return getMaxChild(node.right);
    }


    @Override
    public V remove(K key, V value) {
        if (containsKey(key)) {
            V targetValue = get(key);
            if (value.equals(targetValue)) {
                root = remove(root, key);
                size = size - 1;
                return targetValue;
            } else {
                return null;
            }
        }
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }


}
