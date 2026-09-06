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
    public List<String> binaryTreePaths(TreeNode root) {
        List<String> result = new ArrayList<>();
        if (root == null) return result;
        Queue<TreeNode> q = new LinkedList<>();
        Queue<String> paths = new LinkedList<>();
        q.add(root);
        paths.add(String.valueOf(root.val));
        while (!q.isEmpty()) {
            TreeNode curr = q.poll();
            String path = paths.poll();

            if (curr.left == null && curr.right == null) result.add(path);

            if (curr.left != null) {
                q.add(curr.left);
                paths.add(path + "->" + curr.left.val);
            }

            if (curr.right != null) {
                q.add(curr.right);
                paths.add(path + "->" + curr.right.val);
            }
        }
        return result;
    }
}