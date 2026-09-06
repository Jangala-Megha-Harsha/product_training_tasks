/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode() {}
 *     TreeNode(int val) { this.val = val; }
 *     TreeNode(int val, TreeNode left, TreeNode right) {
 *         this.val = val;
 *         this.left = left;
 *         this.right = right;
 *     }
 * }
 */
class Solution {
    boolean result;
    public void find(TreeNode root, long min, long max){
        if (root == null) return;

        if (root.val <= min || root.val >= max){
            result = false;
            return;
        }

        find(root.left, min, root.val);
        find(root.right, root.val, max);
    }
    public boolean isValidBST(TreeNode root) {
        result = true;
        find(root, Long.MIN_VALUE, Long.MAX_VALUE);
        return result;
    }
}