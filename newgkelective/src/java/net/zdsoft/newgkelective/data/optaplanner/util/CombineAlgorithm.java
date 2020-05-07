package net.zdsoft.newgkelective.data.optaplanner.util;

public class CombineAlgorithm {
	/* src数组的长度 */
	private int m;

	/* 需要获取N个数 */
	private int n;

	// 临时变量，obj的行数
	private int objLineIndex;

	/* 存放结果的二维数组 */
	public String[][] obj;
	
	public static void main(String[] args) {
		String[] str = {"1","2","3","4","5","6","7","8","9","10","11","12","13"};
		CombineAlgorithm com = new CombineAlgorithm(str,11);
	}

	public CombineAlgorithm(String[] src, int getNum) {
		m = src.length;
		n = getNum;

		/* 初始化 */
		objLineIndex = 0;
		obj = new String[combination(m, n)][n];

		String[] tmp = new String[n];
		combine(src, 0, 0, n, tmp);
		// System.out.println(src.length+"取"+getNum+"共有："+objLineIndex);
	}

	/**
	 * <p>
	 * 计算 C(m,n)个数 = (m!)/(n!*(m-n)!)
	 * </p>
	 * 从M个数中选N个数，函数返回有多少种选法 参数 m 必须大于等于 n m = 0; n = 0; return 1;
	 * 
	 * @param m
	 * @param n
	 * @return
	 */
	public int combination(int m, int n) {
		if (m < n)
			return 0; // 如果总数小于取出的数，直接返回0

		Long k = 1l;
		Long j = 1l;
		// 该种算法约掉了分母的(m-n)!,这样分子相乘的个数就是有n个了
		for (int i = n; i >= 1; i--) {
			k = k * m;
			j = j * n;
			m--;
			n--;
		}
		return (int) (k / j);
	}

	/**
	 * <p>
	 * 递归算法，把结果写到obj二维数组对象
	 * </p>
	 * 
	 * @param src
	 * @param srcIndex
	 * @param i
	 * @param n
	 *            取几个
	 * @param tmp
	 *            大致思路 例如 1，2，3，4，5，6，7 数组取3个相当于C（7，3） 123 124 125 ... 134
	 *            135 ... 567
	 */
	private void combine(String src[], int srcIndex, int i, int n, String[] tmp) {
		int j;
		for (j = srcIndex; j < src.length - (n - 1); j++) {
			tmp[i] = src[j];
			if (n == 1) {
				// System.out.println(Arrays.toString(tmp));
				System.arraycopy(tmp, 0, obj[objLineIndex], 0, tmp.length);
				objLineIndex++;
			} else {
				n--;// 3---2---1
				i++;// 0---1---2
				combine(src, j + 1, i, n, tmp);
				n++;
				i--;
			}
		}

	}

	public String[][] getResut() {
		return obj;
	}
}
