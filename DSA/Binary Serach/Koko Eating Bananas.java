class Solution {
    public int minEatingSpeed(int[] piles, int h) {
        int n = piles[0];
        
        int low = 1, high = piles[0];
        for (int ele: piles) high = Math.max(high, ele);

        int result = high;
        while (low <= high){
            int mid = low + (high - low)/2;
            if (isPossible(piles, h, mid)){
                result = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return result;
    }

    public boolean isPossible(int[] piles, int h, int mid){
        long hours = 0;
        for (int ele: piles) hours += (ele + mid - 1) / mid;
        return hours <= h;
    }
}