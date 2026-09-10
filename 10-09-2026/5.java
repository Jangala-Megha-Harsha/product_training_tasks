import java.util.*;

public class Main {
	public static int tree[];
	
	public static void build(int index, int st, int ed, int arr[]) {
		if(st == ed) {
			tree[index] = arr[st];
			return;
		}
		int mid = (st + ed) / 2;
		build(2 * index + 1, st, mid, arr);
		build(2 * index + 2, mid + 1, ed, arr);
		tree[index] = Math.max(tree[2 * index + 1], tree[2 * index + 2]);
	}
	
	public static void update(int index, int st, int ed, int arr[], int x, int val) {
		if(st == ed) {
			arr[x] = val;
			tree[index] = val;
			return;
		}
		int mid = (st + ed) / 2;
		if(x <= mid) update(2 * index + 1, st, mid, arr, x, val);
		else update(2 * index + 2, mid + 1, ed, arr, x, val);
		tree[index] = Math.max(tree[2 * index + 1], tree[2 * index + 2]);
	}
	
	public static int query(int index, int st, int ed, int l, int r, int x) {
		if(l > ed || r < st || tree[index] < x) return -1;
		if(st == ed) return st;
		int mid = (st + ed) / 2;
		int left = query(2 * index + 1, st, mid, l, r, x);
		if(left != -1) return left;
		return query(2 * index + 2, mid + 1, ed, l, r, x);
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int arr[] = new int[n];
		for(int i = 0; i < n; i++) arr[i] = sc.nextInt();
		tree = new int[4 * n];
		build(0, 0, n - 1, arr);
		int q = sc.nextInt();
		for(int i = 0; i < q; i++) {
			int type = sc.nextInt();
			if(type == 1) {
				int index = sc.nextInt();
				int value = sc.nextInt();
				update(0, 0, n - 1, arr, index, value);
			} else {
				int l = sc.nextInt();
				int r = sc.nextInt();
				int x = sc.nextInt();
				System.out.println(query(0, 0, n - 1, l, r, x));
			}
		}
	}
}