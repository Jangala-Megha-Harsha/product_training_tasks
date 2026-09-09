class Solution {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int m = nums1.length;
        int n = nums2.length;

        int arr[] = new int[m+n];
        int idx = 0;
        for (int i: nums1) arr[idx++] = i;
        for (int i: nums2) arr[idx++] = i;

        Arrays.sort(arr);

        if ((m+n) % 2 == 1){
            return arr[(m+n) / 2];
        }
        return ((arr[(m+n)/2 - 1] + arr[(m+n)/2]) / 2.0);
    }
}