package com.wangy.algorithm.tree;


/**
 * 二分查找树 BST 实现
 *
 * @author wangy
 * @version 1.0
 * @date 2022/12/7 / 16:12
 */
public class BinarySearchTree<T extends Comparable<T>> {

    private TreeNode root;

    private int size;


    public BinarySearchTree() {
    }

    boolean find(T key) {
        TreeNode node = root.getNode(key);
        return node != null;
    }

    T findParent(T key) {
        TreeNode node = root.getParentNode(key);
        if (node == null) return null;
        return node.value;
    }

    int size() {
        return size;
    }


    T del(T key) {
        return root.deleteKey(key);
    }

    void insert(T key) {
        if (root == null) {
            root = new TreeNode(key);
            size++;
            return;
        }

        if (root.insertKey(key)) size++;
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

    void printPreOder(TreeNode node) {
        if (node == null) return;
        System.out.print(node.value + "\t");
        printPreOder(node.leftChild);
        printPreOder(node.rightChild);
    }

    private final class TreeNode {

        private T value;
        private TreeNode leftChild;
        private TreeNode rightChild;


        public TreeNode(T value) {
            this.value = value;
        }


        /**
         * 在BST中找值为key的节点
         *
         * @param key 节点的值
         * @return 符合条件的节点或者<code>null</code>
         */

        TreeNode getNode(T key) {
            TreeNode current = root;

            while (true) {
                if (key == current.value) return current;

                if (key.compareTo(current.value) > 0) {
                    // search right child
                    if (current.rightChild == null) return null;
                    current = current.rightChild;
                }

                if (key.compareTo(current.value) < 0) {
                    // search left child
                    if (current.leftChild == null) return null;
                    current = current.leftChild;
                }
            }
        }

        /**
         * 获取指定值的父节点
         *
         * @param key 节点值
         * @return 如果查找的节点是根节点，则返回根节点。否则返回节点的父节点，或者返回null
         */
        TreeNode getParentNode(T key) {
            if (key == root.value) return root; // 根节点返回自己
            TreeNode node = getNode(key);
            if (node == null) return null;
            TreeNode current = root;
            while (true) {
                if (key.compareTo(current.value) > 0) {
                    if (current.rightChild == null) return null;
                    else if (current.rightChild.value == key) return current;
                    current = current.rightChild;
                }
                if (key.compareTo(current.value) < 0) {
                    if (current.leftChild == null) return null;
                    else if (current.leftChild.value == key) return current;
                    current = current.leftChild;
                }
            }
        }


        /**
         * 循环方式插入节点
         *
         * @param key 待插入的值
         * @return true-插入成功 false-插入失败
         */
        boolean insertKey(T key) {
            TreeNode current = root;

            while (true) {
                if (key.compareTo(current.value) > 0) {
                    // right child
                    if (current.rightChild == null) {
                        current.rightChild = new TreeNode(key);
                        return true;
                    }
                    current = current.rightChild;
                }
                if (key.compareTo(current.value) < 0) {
                    // left child
                    if (current.leftChild == null) {
                        current.leftChild = new TreeNode(key);
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
        boolean insertKey(T key, TreeNode root) {

            TreeNode current = root;

            if (key.compareTo(current.value) > 0) {
                // right child
                if (current.rightChild == null) {
                    current.rightChild = new TreeNode(key);
                    return true;
                }
                current = current.rightChild;
                insertKey(key, current);
            }
            if (key.compareTo(current.value) < 0) {
                // left child
                if (current.leftChild == null) {
                    current.leftChild = new TreeNode(key);
                    return true;
                }
                current = current.leftChild;
                insertKey(key, current);
            } else {
                throw new RuntimeException("Same key is not allowed in binary search tree.");
            }
            return false;
        }

        /**
         * 删除一个节点 （树重组）
         *
         * @param key key to be deleted
         * @return key deleted or null if delete key fail
         */
        T deleteKey(T key) {
            TreeNode targetNode = getNode(key);
            if (targetNode == null) return null;

            TreeNode parentNode = getParentNode(key);
            assert parentNode != null;

            // case1 叶子节点直接删除
            if (targetNode.leftChild == null && targetNode.rightChild == null) {
                if (key.compareTo(parentNode.value) > 0) {
                    // 删除的节点是右孩子
                    parentNode.rightChild = null;
                } else {
                    parentNode.leftChild = null;
                }
                return key;
            }

            // case 2 非叶子节点，树重组
            // 找出左子树中最大的/右子树中最小的替换被删除的节点
            TreeNode _1stChild; // 目标节点的最近子节点
            TreeNode successor = null; // 上升节点
            TreeNode successorParent; // 上升节点的父节点
            // 注意，successor不一定为叶子节点
            // 但只能存在左孩子或者右孩子中的一种，successor不可能有2个孩子！
            TreeNode reserved = null ; // 后继节点（successor子节点）
            // leftChild != null || rightChild != null
            if (targetNode.leftChild != null) {
                _1stChild = targetNode.leftChild;
                successor = findMaxSuccessor(_1stChild);
                reserved = successor.leftChild;
                successorParent = getParentNode(successor.value);
                assert successorParent != null;
                // 设置上升节点的左右孩子
                if (successor.value != _1stChild.value) successor.leftChild = _1stChild;
                successor.rightChild = targetNode.rightChild;
            } else {
                _1stChild = targetNode.rightChild;
                successor = findMinSuccessor(_1stChild);
                reserved = successor.rightChild;
                successorParent = getParentNode(successor.value);
                assert successorParent != null;
                successor.leftChild = targetNode.leftChild;
                if (successor.value != _1stChild.value) successor.rightChild = _1stChild;
            }
            // 若删除的是根节点
            if (parentNode.value == root.value) {
                root = successor;
            } else {
                if (key.compareTo(parentNode.value) > 0) {
                    // 删除的节点是右孩子
                    parentNode.rightChild = successor;
                } else {
                    parentNode.leftChild = successor;
                }
            }

            if (successor.value.compareTo(successorParent.value) > 0) {
                // 上升节点是右孩子
                successorParent.rightChild = reserved;
            } else {
                successorParent.leftChild = reserved;
            }
            return key;
        }

        /**
         * 找出子树的最小节点
         */
        private TreeNode findMinSuccessor(TreeNode node) {
            if (node.leftChild == null) return node;
            return findMinSuccessor(node.leftChild);
        }


        /**
         * 找出子树的最大节点
         */
        private TreeNode findMaxSuccessor(TreeNode node) {
            if (node.rightChild == null) return node;
            return findMaxSuccessor(node.rightChild);
        }
    }

    static class Main {
        public static void main(String[] args) {
            BinarySearchTree<Integer> bst = new BinarySearchTree<>();

            System.out.print("[insert & traversal]\t");
            bst.insert(21);
            bst.insert(14);
            bst.insert(7);
            bst.insert(19);
            bst.insert(3);
            bst.insert(13);
            bst.insert(15);
            bst.insert(9);
            bst.insert(10);
            bst.insert(18);
            bst.insert(37);
            bst.insert(25);
            bst.insert(23);
            bst.insert(36);
            bst.insert(48);
            bst.insert(40);
            bst.insert(52);
            bst.insert(67);
            bst.insert(50);
            bst.insert(45);
            bst.insert(39);

            // 18 16 13 17 19
            bst.printPreOder(bst.root);

            System.out.println("\n[count]\t\t\t\t\ttree size: " + bst.size());

            System.out.println("[find parent]\t\t\tparent of 23: " + bst.findParent(23));

            System.out.println("[contains]\t\t\t\tcontains key 20: " + bst.find(20));

            System.out.println("[deletion]\t\t\t\tdelete key 23: " + bst.del(23));
            System.out.print("[traversal]\t\t\t\t");
            bst.printPreOder(bst.root);
            System.out.println();

            System.out.println("[deletion]\t\t\t\tdelete key 48: " + bst.del(48));
            System.out.print("[traversal]\t\t\t\t");
            bst.printPreOder(bst.root);
            System.out.println();

            System.out.println("[deletion]\t\t\t\tdelete key 19: " + bst.del(19));
            System.out.print("[traversal]\t\t\t\t");
            bst.printPreOder(bst.root);
            System.out.println();

            System.out.println("[deletion]\t\t\t\tdelete key 25: " + bst.del(25));
            System.out.print("[traversal]\t\t\t\t");
            bst.printPreOder(bst.root);
            System.out.println();

            System.out.println("[deletion]\t\t\t\tdelete key 45: " + bst.del(45));
            System.out.print("[traversal]\t\t\t\t");
            bst.printPreOder(bst.root);
            System.out.println();

            // delete root node
            System.out.println("[deletion]\t\t\t\tdelete key 21: " + bst.del(21));
            System.out.print("[traversal]\t\t\t\t");
            bst.printPreOder(bst.root);
            System.out.println();
        }
    }
}
