import java.util.*;

public class Main {
	public static long tree[];
	
	public static long gcd(long a, long b) {
		while(b != 0) {
			long temp = a % b;
			a = b;
			b = temp;
		}
		return a;
	}
	
	public static void build(int index, int st, int ed, long arr[]) {
		if(st == ed) {
			tree[index] = arr[st];
			return;
		}
		int mid = (st + ed) / 2;
		build(2 * index + 1, st, mid, arr);
		build(2 * index + 2, mid + 1, ed, arr);
		tree[index] = gcd(tree[2 * index + 1], tree[2 * index + 2]);
	}
	
	public static void update(int index, int st, int ed, long arr[], int x, long val) {
		if(st == ed) {
			arr[x] = val;
			tree[index] = val;
			return;
		}
		int mid = (st + ed) / 2;
		if(x <= mid) update(2 * index + 1, st, mid, arr, x, val);
		else update(2 * index + 2, mid + 1, ed, arr, x, val);
		tree[index] = gcd(tree[2 * index + 1], tree[2 * index + 2]);
	}
	
	public static long query(int index, int st, int ed, int l, int r) {
		if(l > ed || r < st) return 0;
		if(l <= st && ed <= r) return tree[index];
		int mid = (st + ed) / 2;
		return gcd(query(2 * index + 1, st, mid, l, r), query(2 * index + 2, mid + 1, ed, l, r));
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		long arr[] = new long[n];
		for(int i = 0; i < n; i++) arr[i] = sc.nextLong();
		tree = new long[4 * n];
		build(0, 0, n - 1, arr);
		int q = sc.nextInt();
		for(int i = 0; i < q; i++) {
			int type = sc.nextInt();
			if(type == 1) {
				int index = sc.nextInt();
				long value = sc.nextLong();
				update(0, 0, n - 1, arr, index, value);
			} else {
				int l = sc.nextInt();
				int r = sc.nextInt();
				System.out.println(query(0, 0, n - 1, l, r));
			}
		}
	}
}