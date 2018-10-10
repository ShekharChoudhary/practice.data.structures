package binary.tree;

/*
 Check two trees are identical or not
Problem:
Given two binary trees, write a function to check if they are equal or not.

Solution:
Two binary trees are considered equal if they are structurally identical and the nodes have the same value.

Algorithm:
isTreesIdenticalRec(Node root1, Node root2) :
1. Termination Condition: If both trees are empty then return true.
2. else if both trees are non-empty
     (a) Check data of the root nodes (root1->data ==  root2->data)
     (b) Check left subtrees recursively
                   call isTreesIdenticalRec(root1->left, root2->left)
     (c) Check right subtrees recursively
                   call isTreesIdenticalRec(root1->right, root2->right)
     (d) If a,b and c are true then return true.
3. else return false(=one is empty and another is not). 
 */

/**
 * 
 * Class to check whether two binary trees are identical.
 * 
 * @author choudshe
 *
 */
public class IdenBinaryTree {
	 /**
     * @author choudshe
     * Node of the Tree.
     */
    private static class Node {
          int data;
          Node left, right;

          Node(int item) {
               data = item;
               left = right = null;
          }
    }
   
    Node root1, root2;
   
    /**
     * To check whether Trees are Identical or not.
     * @param root1
     * @param root2
     * @return true/false
     */
    private static boolean isTreesIdenticalRec(Node root1, Node root2) {
         
          if(root1==null && root2==null) {
               return true;
          } else if(root1!=null && root2!=null) {
               return (root1.data == root2.data
                          && isTreesIdenticalRec(root1.left, root2.left)
                          && isTreesIdenticalRec(root1.right, root2.right));
          } else {
               return false;
          }
    }

    /**
     * Main method: Program start point.
     * @param args
     */
    public static void main(String[] args) {
         
          IdenBinaryTree tree1 = new IdenBinaryTree();
          tree1.root1 = new Node(1);
          tree1.root1.left = new Node(2);
          tree1.root1.right = new Node(3);
          tree1.root1.left.left = new Node(4);
          tree1.root1.left.right = new Node(5);

          IdenBinaryTree tree2 = new IdenBinaryTree();
          tree2.root2 = new Node(1);
          tree2.root2.left = new Node(2);
          tree2.root2.right = new Node(3);
          tree2.root2.left.left = new Node(4);
          tree2.root2.left.right = new Node(5);

          if (isTreesIdenticalRec(tree1.root1, tree2.root2)) {
               System.out.println("Trees are identical");
          } else {
               System.out.println("Trees are not identical");
          }
    }
}
