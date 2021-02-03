package Extra;

import java.util.Scanner;

public class CO_SetAssociative {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		String[][] tag = { { "0010", "0101" }, { "0001", "0000" } };
//		long[][][] data = { { { 1, 2, 4, 7 }, { 3, 4, 10, 11 } }, { { 5, 6, 22, 31 }, { 7, 21, 7, 8 } } };
//		int[][] LSU = { { 1, 0 }, { 2, 1 } };
//		int sizeM = 7;
//		int blockoffset = log2(data[0][0].length);
////		System.out.println(blockoffset);
//		int set =log2(tag.length);
////		System.out.println(set);
//		SetAssociative sa = new SetAssociative(tag, data, LSU, blockoffset, set, sizeM);
//		sa.read("0010000");
//		sa.write("0010000", 23);
		Scanner scn = new Scanner(System.in);
		int n = scn.nextInt();
		int set = scn.nextInt();
		int blocksize = scn.nextInt();
		int cachelines = scn.nextInt();
		// Take input for tag
		String[][] tag = new String[set][(int) Math.pow(2, cachelines) / set];
		for (int i = 0; i < tag.length; i++) {
			for (int j = 0; j < tag[0].length; j++) {
				tag[i][j] = scn.next();
			}
		}
		long[][][] data = new long[set][(int) Math.pow(2, cachelines)][(int) Math.pow(2, blocksize)];
		// Take input for data
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[0].length; j++) {
				for (int k = 0; k < data[0][0].length; k++) {
					data[i][j][k] = scn.nextLong();
				}
			}
		}
		// counter for least used tag value
		int[][] LSU = new int[set][(int) Math.pow(2, cachelines) / set];
		for (int i = 0; i < tag.length; i++) {
			for (int j = 0; j < tag[0].length; j++) {
				LSU[i][j] = scn.nextInt();
			}
		}
		SetAssociative sa = new SetAssociative(tag, data, LSU, blocksize, set, n);
		// write the function you need to perform
		// address input as string with binary values
		String address = scn.next();
		long changedata = scn.nextLong();
		sa.read(address);
		sa.write(address, changedata);

	}

	public static int log2(int N) {

		int result = (int) (Math.log(N) / Math.log(2));

		return result;
	}

}

class SetAssociative {
	String[][] tag;
	long[][][] data;
	int[][] LSU;
	int blockoffset;
	int set;
	int sizeM;

	public SetAssociative(String[][] tag, long[][][] data, int[][] LSU, int blockoffset, int set, int sizeM) {
		this.tag = tag;
		this.data = data;
		this.LSU = LSU;
		this.blockoffset = blockoffset;
		this.set = set;
		this.sizeM = sizeM;
	}

	public void read(String address) {
		String tagbits = address.substring(0, sizeM - (blockoffset + set));
//		System.out.println(tagbits);
		String setindex1 = address.substring(sizeM - set - blockoffset, sizeM - blockoffset);
		int setindex = (int) btoD(Long.parseLong(setindex1));
//		System.out.println(setindex);
		String blockindex1 = address.substring(sizeM - blockoffset);

		int blockindex = Integer.parseInt(blockindex1);
		int index = -1;
		int minLSU = Integer.MAX_VALUE;
		int minLSU_Index = -1;
		for (int i = 0; i < tag[setindex].length; i++) {
			if (tagbits.equals(tag[setindex][i])) {
				index = i;
				System.out.println(index);
			}
			if (LSU[setindex][i] < minLSU) {
				minLSU_Index = i;
			}
		}
		if (index != -1) {
			System.out.println("Cache Hit");
			System.out.println("Data:" + data[setindex][index][blockindex]);
			LSU[setindex][index] += 1;

		} else {
			System.out.println("Cache Miss");
			
			tag[setindex][minLSU_Index] = tagbits;
			LSU[setindex][minLSU_Index] = 0;
			for (int i = 0; i < data[setindex][minLSU_Index].length; i++) {
				data[setindex][minLSU_Index][i] = -1;
			}
			System.out.println("Data:" + data[setindex][minLSU_Index][blockindex]);
		}
	}

	public void write(String address, long changedata) {
		String tagbits = address.substring(0, sizeM - (blockoffset + set));
//		System.out.println(tagbits);
		String setindex1 = address.substring(sizeM - set - blockoffset, sizeM - blockoffset);
		int setindex = (int) btoD(Long.parseLong(setindex1));
//		System.out.println(setindex);
		String blockindex1 = address.substring(sizeM - blockoffset);

		int blockindex = Integer.parseInt(blockindex1);
		int index = -1;
		int minLSU = Integer.MAX_VALUE;
		int minLSU_Index = -1;
		for (int i = 0; i < tag[setindex].length; i++) {
			if (tagbits.equals(tag[setindex][i])) {
				index = i;
			}
			if (LSU[setindex][i] < minLSU) {
				minLSU_Index = i;
			}
		}
		if (index != -1) {
			System.out.println("Cache Hit");
			System.out.println("Current Data:" + data[setindex][index][blockindex]);
			data[setindex][index][blockindex] = changedata;
			System.out.println("New Data:" + data[setindex][index][blockindex]);
			LSU[setindex][index] += 1;

		} else {
			System.out.println("Cache Miss");
			tag[setindex][minLSU_Index] = tagbits;
			for (int i = 0; i < data[setindex][minLSU_Index].length; i++) {
				data[setindex][minLSU_Index][i] = -1;
			}
			System.out.println("Replaced the whole block");
			System.out.println("Current Data:" + data[setindex][minLSU_Index][blockindex]);
			LSU[setindex][minLSU_Index] = 0;
			data[setindex][minLSU_Index][blockindex] = changedata;
			System.out.println("New Data:" + data[setindex][minLSU_Index][blockindex]);
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
