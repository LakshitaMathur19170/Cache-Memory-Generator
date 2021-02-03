package Extra;

import java.util.Scanner;

public class Cache_CO_FAssociative {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		 DEFAULT TEST CASE
//		String[] tag = { "00100", "01001", "10001", "00000" };
//		int[] LSU = { 1, 0, 2, 1 };
//		long[][] data = { { 2, 3, 4, 7 }, { 12, 23, 54, 67 }, { 22, 11, 67, 98 }, { 1, 2, 3, 4 } };
//		int sizeM = 7;
//		int blockoffset = log2(data.length);
//
//		FullAssociative fa = new FullAssociative(tag, data, blockoffset, LSU, sizeM);
//
//		fa.read("0010000");
//		System.out.println("------");
//		fa.write("0010000", 25);
//		System.out.println("------");
////		fa.read("1000000");
////		System.out.println("------");
//		fa.write("1000000", 43);
//		System.out.println("------");
		Scanner scn = new Scanner(System.in);
		int n = scn.nextInt();
		int blocksize = scn.nextInt();
		int cachelines = scn.nextInt();
		// Take input for tag
		String[] tag = new String[(int) Math.pow(2, cachelines)];
		for (int i = 0; i < tag.length; i++) {
			tag[i] = scn.next();
		}
		long[][] data = new long[(int) Math.pow(2, cachelines)][(int) Math.pow(2, blocksize)];
		// Take input for data
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[0].length; j++) {
				data[i][j] = scn.nextLong();
			}
		}
		// counter for least used tag value
		int[] LSU = new int[(int) Math.pow(2, cachelines)];
		FullAssociative fa = new FullAssociative(tag, data, blocksize, LSU, n);
		// write the function you need to perform
		// address input as string with binary values
		String address = scn.next();
		long changedata= scn.nextLong();
		fa.read(address);
		fa.write(address, changedata);

	}

	public static int log2(int N) {

		int result = (int) (Math.log(N) / Math.log(2));

		return result;
	}

}

class FullAssociative {
	String[] tag;
	long[][] data;
	int blockoffset;
	int[] LSU;
	int sizeM;

	public FullAssociative(String[] tag, long[][] data, int blockoffset, int[] LSU, int sizeM) {
		this.tag = tag;
		this.data = data;
		this.blockoffset = blockoffset;
		this.LSU = LSU;
		this.sizeM = sizeM;

	}

	public void read(String address) {

		String tagbits = address.substring(0, sizeM - blockoffset);

		String blockindex1 = address.substring(sizeM - blockoffset);

		int blockindex = Integer.parseInt(blockindex1);

		int min_LRU = Integer.MAX_VALUE;
		int min_LRU_Index = -1;
		int index = -1;
		for (int i = 0; i < tag.length; i++) {
			if (tagbits.equals(tag[i])) {
				index = i;
			}
			if (LSU[i] < min_LRU) {
				min_LRU_Index = i;
				min_LRU = (int) LSU[i];
			}
		}
		if (index != -1) {
			System.out.println("Cache Hit");
			System.out.println("Data:" + data[index][blockindex]);
			LSU[index] += 1;

		} else {
			System.out.println("Cache Miss");
			tag[min_LRU_Index] = tagbits;
			LSU[min_LRU_Index] = 0;
			
			for (int i = 0; i < data[min_LRU_Index].length; i++) {
				data[min_LRU_Index][(int) blockindex] = -1;
			}
			
			System.out.println("New Data:" + data[min_LRU_Index][blockindex]);

		}

	}

	public void write(String address, long changedata) {

		String tagbits = address.substring(0, sizeM - blockoffset);

		String blockindex1 = address.substring(sizeM - blockoffset);

		int blockindex = Integer.parseInt(blockindex1);
		int min_LRU = Integer.MAX_VALUE;
		int min_LRU_Index = -1;
		int index = -1;
		for (int i = 0; i < tag.length; i++) {
			if (tagbits.equals(tag[i])) {
				index = i;
			}
			if (LSU[i] < min_LRU) {
				min_LRU_Index = i;
				min_LRU = (int) LSU[i];
			}
		}
		if (index != -1) {
			System.out.println("Cache Hit");
			System.out.println("Current Data:" + data[index][(int) blockindex]);
			data[index][(int) blockindex] = changedata;
			System.out.println("New Data:" + data[index][(int) blockindex]);
			LSU[index] += 1;

		} else {
			System.out.println("Cache Miss");
			tag[min_LRU_Index] = tagbits;
			for (int i = 0; i < data[min_LRU_Index].length; i++) {
				data[min_LRU_Index][(int) blockindex] = -1;
			}
			LSU[min_LRU_Index] = 0;
			System.out.println("Replaced the whole block");
			System.out.println("Current Data:" + data[min_LRU_Index][(int) blockindex]);
			data[min_LRU_Index][(int) blockindex] = changedata;
			System.out.println("New Data:" + data[min_LRU_Index][(int) blockindex]);

		}
	}

	public long btoD(long n) {
		long ans = 0;

		long mult = 1; // 2^0
		while (n > 0) {

			long rem = n % 10;

			ans = ans + rem * mult;

			mult = mult * 2;
			n = n / 10;

		}
		return ans;
	}
}
