class NumArray {

    int n;
    int[] tree;
    
    public NumArray(int[] nums) {
        n = nums.length;
        tree = new int[4 * n];
        build(0, 0, n - 1, nums);
    }

    public void build(int node, int start, int end, int[] nums){
        if (start == end){
            tree[node] = nums[start];
            return;
        }

        int mid = (start + end) / 2;
        build(2 * node + 1, start, mid, nums);
        build(2 * node + 2, mid + 1, end, nums);
        tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
    }
    
    public int sumRange(int left, int right) {
        return query(0, 0, n - 1, left, right);
    }

    public int query(int node, int start, int end, int left, int right){
        if (start > right || end < left) return 0;

        if (start >= left && end <= right) return tree[node];

        int mid = (start + end) / 2;
        int leftvalue = query(2 * node + 1, start, mid, left, right);
        int rightvalue = query(2 * node + 2, mid + 1, end, left, right);
        return leftvalue + rightvalue;
    }
}

/**
 * Your NumArray object will be instantiated and called as such:
 * NumArray obj = new NumArray(nums);
 * int param_1 = obj.sumRange(left,right);
 */