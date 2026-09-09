import java.util.*;

class Solution {
    int[] dp;
    public int mincostTickets(int[] days, int[] costs) {
        dp = new int[days.length];
        Arrays.fill(dp, -1);
        return solve(days, costs, 0);
    }

    int solve(int[] days, int[] costs, int index) {
        if (index >= days.length) return 0;
        
        if (dp[index] != -1) return dp[index];
        
        int oneDay = costs[0] + solve(days, costs, index + 1);
        int next = index;
        while (next < days.length && days[next] < days[index] + 7) {
            next++;
        }
        
        int sevenDay = costs[1] + solve(days, costs, next);
        next = index;
        while (next < days.length && days[next] < days[index] + 30) {
            next++;
        }
        
        int thirtyDay = costs[2] + solve(days, costs, next);
        return dp[index] = Math.min(
            oneDay,
            Math.min(sevenDay, thirtyDay)
        );
    }
}