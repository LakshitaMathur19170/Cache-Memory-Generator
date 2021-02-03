package Extra;

import java.util.Scanner;

public class Cache_CO {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		 DEFAULT TEST CASE
//		String[] tag = {"000" , "001","010" ,"011"};
//		long[][] data = {{2,3,4,7},{12,23,54,67},{22,11,67,98},{1,2,3,4}};
//		int blocksize= log2(data[0].length);
//		int cachelines= log2(data.length);
//		int sizeM=7;
//		Direct_mapping dm = new Direct_mapping(tag, data, blocksize, cachelines, sizeM);
//		dm.read("0000001");
//		System.out.println("--------");
//		dm.write("0000001", 2);
//		System.out.println("--------");
//		dm.read("1000001");
//		System.out.println("--------");
//		dm.write("1000001", 2);
//		System.out.println("--------");
		Scanner scn = new Scanner(System.in);
		int n = scn.nextInt();
		int blocksize =scn.nextInt();
		int cachelines =scn.nextInt();
		// Take input for tag
		String[] tag= new String[(int) Math.pow(2, cachelines)];
		for(int i=0;i<tag.length;i++) {
			tag[i]=scn.next();
		}
		long[][] data=new long[(int) Math.pow(2, cachelines)][(int) Math.pow(2, blocksize)];
		//Take input for data
		for(int i=0;i<data.length;i++) {
			for(int j=0;j<data[0].length;j++) {
				data[i][j]=scn.nextLong();
			}
		}
		Direct_mapping dm = new Direct_mapping(tag, data, blocksize, cachelines, n);
		//write the function you need to perform
		//address input as string with binary values
		String address= scn.next();
		long changedata =scn.nextLong();
		dm.read(address);
		dm.write(address, changedata);
		
		
	}

	public static int log2(int N) {

		int result = (int) (Math.log(N) / Math.log(2));

		return result;
	}

}

class Direct_mapping {

	String[] tag;
	long[][] data;
	int blocksize;
	int cachelines;
	int sizeM;

	public Direct_mapping(String[] tag, long[][] data, int blocksize, int cachelines, int sizeM) {
		this.tag = tag;
		this.data = data;
		this.blocksize = blocksize;
		this.cachelines = cachelines;

		this.sizeM = sizeM;
	}

	public void read(String address) {
//		System.out.println(address);
		String tagbits = address.substring(0,sizeM-blocksize-cachelines);
		String blockoffset1= address.substring(sizeM-blocksize);
		int blockoffset= Integer.parseInt(blockoffset1);
		String linesc1 = address.substring(sizeM-blocksize-cachelines, sizeM-blocksize);

		int linesc= (int)btoD(Long.parseLong(linesc1));
		
		if (tag[(int) linesc].equals(tagbits)) {
			System.out.println("Cache Hit");
			System.out.println("Data:" +data[(int) linesc][blockoffset]);
			
		} else {
			System.out.println("Cache Miss");
			tag[(int) linesc] = tagbits;
			tag[(int) linesc] = tagbits;
			for (int i = 0; i < data[0].length; i++) {
				data[(int) linesc][i] = -1;
			}
			
			System.out.println("New Data:" +data[(int) linesc][blockoffset]);


		}

	}

	public void write(String address, long changedata) {
//		System.out.println(address);
		String tagbits = address.substring(0,sizeM-blocksize-cachelines);
		String blockoffset1= address.substring(sizeM-blocksize);
		int blockoffset= Integer.parseInt(blockoffset1);
		String linesc1 = address.substring(sizeM-blocksize-cachelines, sizeM-blocksize);

		int linesc= (int)btoD(Long.parseLong(linesc1));

		if (tag[(int) linesc].equals(tagbits)) {

			System.out.println("Cache Hit");
			System.out.println("Current data:" + data[(int) linesc][blockoffset]);
			data[(int) linesc][blockoffset] = changedata;
			System.out.println("New Data:" + data[(int) linesc][blockoffset]);

		} else {
			System.out.println("Cache Miss");
			tag[(int) linesc] = tagbits;
			for (int i = 0; i < data[0].length; i++) {
				data[(int) linesc][i] = -1;
			}
			System.out.println("Current data:" + data[(int) linesc][blockoffset]);
			data[(int) linesc][blockoffset] = changedata;
			System.out.println("New Data:" + data[(int) linesc][blockoffset]);

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
