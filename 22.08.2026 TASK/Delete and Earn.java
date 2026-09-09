class Solution {
    public int deleteAndEarn(int[] nums) {
        int n = 10001;
        int[] values = new int[n];
        for (int num : nums)
            values[num] += num;

        int take = 0, nottake = 0;
        for (int i = 0; i < n; i++) {
            int takei = nottake + values[i];
            int nottakei = Math.max(nottake, take);
            take = takei;
            nottake = nottakei;
        }
        return Math.max(take, nottake);
    }
}