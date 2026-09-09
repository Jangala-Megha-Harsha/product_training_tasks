import java.util.*;

class Solution {
    int[][] dp;
    int total;
    public int lastStoneWeightII(int[] stones) {
        total = 0;
        for (int stone : stones) total += stone;
        dp = new int[stones.length][total + 1];
        for (int[] row : dp) Arrays.fill(row, -1);
        return solve(stones, 0, 0);
    }
    
    int solve(int[] stones, int index, int sum) {
        if (index == stones.length) return Math.abs(total - 2 * sum);
        if (dp[index][sum] != -1) return dp[index][sum];
        
        int take = solve(stones, index + 1, sum + stones[index]);
        int nottake = solve(stones, index + 1, sum);
        return dp[index][sum] = Math.min(take, nottake);
    }
}