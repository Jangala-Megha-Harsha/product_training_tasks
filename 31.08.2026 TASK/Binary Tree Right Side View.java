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
    public List<Integer> rightSideView(TreeNode root) {
        if (root == null) return new ArrayList<>();

        ArrayList<ArrayList<Integer>> ans = new ArrayList<>();
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);

        while (!q.isEmpty()){
            int n = q.size();
            ArrayList<Integer> al = new ArrayList<>();

            for (int i = 0; i<n; i++){
                TreeNode curr = q.peek();
                if (curr != null){
                    if (curr.left != null) q.add(curr.left);
                    if (curr.right != null) q.add(curr.right);
                    al.add(curr.val);
                    q.poll();
                }
            }
            ans.add(al);
        }

        List<Integer> result = new ArrayList<>();
        for (ArrayList<Integer> i: ans){
            int len = i.size() - 1;
            result.add(i.get(len));
        }
        return result;
    }
}