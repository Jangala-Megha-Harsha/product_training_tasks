/*
class Node {
    int data;
    Node left, right;

    public Node(int d) {
        data = d;
        left = right = null;
    }
}
*/

class Solution {
    public void leftfun(Node root, ArrayList<Integer> a){
        if (root == null) return;
        if (root.left == null && root.right == null) return;
        
        if (root.left != null){
            a.add(root.data);
            leftfun(root.left, a);
        } else {
            a.add(root.data);
            leftfun(root.right, a);
        }
    }
    
    public void rightfun(Node root, ArrayList<Integer> a){
        if (root == null) return;
        if (root.left == null && root.right == null) return;
        
        if (root.right != null) rightfun(root.right, a);
        else rightfun(root.left, a);
        
        a.add(root.data);
    }
    
    public void leaffun(Node root, ArrayList<Integer> a){
        if (root == null) return;
        if (root.left == null && root.right == null) a.add(root.data);
        leaffun(root.left, a);
        leaffun(root.right, a);
    }
    
    ArrayList<Integer> boundaryTraversal(Node root) {
        // code here
        ArrayList<Integer> a = new ArrayList<>();
        a.add(root.data);
        leftfun(root.left, a);
        leaffun(root.left, a);
        leaffun(root.right, a);
        rightfun(root.right, a);
        return a;
    }
}