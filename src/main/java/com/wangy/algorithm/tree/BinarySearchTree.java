package com.wangy.algorithm.tree;

/**
 * 二分查找树 BST 实现
 *
 * @author wangy
 * @version 1.0
 * @date 2022/12/7 / 16:12
 */
public class BinarySearchTree<T extends Comparable<T>> {

    private TreeNode<T> root;

    private int size;


    public BinarySearchTree() {
    }

    boolean find(T key) {
        return false;
    }

    int size() {
        return size;
    }

    boolean del(T key) {
        return false;
    }

    void insert(T key) {
        if (root == null) {
            root = new TreeNode<>(key);
            size++;
            return;
        }

        // if (TreeNode.insertKey(key, this.root))
        //    size++;
        if (TreeNode.insertNode(new TreeNode<>(key), root)) size++;
    }

    /**
     * pre-order traversal 前序遍历
     * 先访问自己，再访问左子树，再访问右子树
     * <p>
     * in-order traversal 中序遍历
     * 先访问左子树，再访问自己，再访问右子树
     * <p>
     * post-order traversal 后序遍历
     * 先访问左子树，再访问右子树，再访问自己
     */

    void printPreOder(TreeNode<T> node) {
        if (node == null) return;
        System.out.print(node.value + "\t");
        printPreOder(node.leftChild);
        printPreOder(node.rightChild);
    }

    static class TreeNode<T extends Comparable<T>> {

        private T value;
        private TreeNode<T> leftChild;
        private TreeNode<T> rightChild;


        public TreeNode(T value) {
            this.value = value;
        }




        /**
         *  循环方式插入节点
         *
         * @param node 待插入的节点
         * @param root 根节点
         * @return true-插入成功 false-插入失败
         * @param <T> key类型
         */
        static <T extends Comparable<T>> boolean insertNode(TreeNode<T> node, TreeNode<T> root) {
            T key = node.value;
            TreeNode<T> current = root;

            while (true) {
                if (key.compareTo(current.value) > 0) {
                    // right child
                    if (current.rightChild == null) {
                        current.rightChild = node;
                        return true;
                    }
                    current = current.rightChild;
                }
                if (key.compareTo(current.value) < 0) {
                    // left child
                    if (current.leftChild == null) {
                        current.leftChild = node;
                        return true;
                    }
                    current = current.leftChild;
                }
                if (key.compareTo(current.value) == 0) {
                    System.out.println("Same key is not allowed.");
                    return false;
                }
            }
        }

        /**
         * 递归方式插入一个节点
         *
         * @param key  key
         * @param root BST root node
         * @return true-插入成功 false-插入失败
         */
        static <T extends Comparable<T>> boolean insertKey(T key, TreeNode<T> root) {

            TreeNode<T> current = root;

            if (key.compareTo(current.value) > 0) {
                // right child
                if (current.rightChild == null) {
                    current.rightChild = new TreeNode<>(key);
                    return true;
                }
                current = current.rightChild;
                insertKey(key, current);
            }
            if (key.compareTo(current.value) < 0) {
                // left child
                if (current.leftChild == null) {
                    current.leftChild = new TreeNode<>(key);
                    return true;
                }
                current = current.leftChild;
                insertKey(key, current);
            } else {
                throw new RuntimeException("Same key is not allowed in binary search tree.");
            }
            return false;
        }
    }

    static class Main {
        public static void main(String[] args) {
            BinarySearchTree<Integer> bst = new BinarySearchTree<>();

            bst.insert(18);
            bst.insert(16);
            bst.insert(17);
            bst.insert(19);
            bst.insert(13);


            // 18 16 13 17 19
            bst.printPreOder(bst.root);


        }
    }
}
