class Solution {
    public int findTargetSumWays(int[] nums, int target) {
        int n = nums.length;

        int total = 0;
        for (int num: nums) total += num;

        if (total + target < 0 || (total + target)%2 != 0) return 0;

        int subsetSum = (total + target) / 2;
        int[][] dp = new int[n + 1][subsetSum + 1];
        dp[0][0] = 1;

        for (int i = 1; i <= n; i++){
            for (int j = 0; j <= subsetSum; j++){
                dp[i][j] = dp[i - 1][j];

                if (j >= nums[i - 1]) dp[i][j] += dp[i - 1][j - nums[i - 1]];
            }
        }
        return dp[n][subsetSum];
    }
}