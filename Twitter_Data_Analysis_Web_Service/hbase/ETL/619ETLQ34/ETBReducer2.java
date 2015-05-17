import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;

/**
 * @author Hansi Mou
 *
 *         Sep 8, 2014
 */
public class ETBReducer2 {
	static int whichone;

	/**
	 * Description:
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in,
				"UTF-8"));

//		InputStreamReader read = new InputStreamReader(new FileInputStream(
//				new File("sample.txt")), "UTF-8");
//		BufferedReader br = new BufferedReader(read);
		//
		// OutputStreamWriter write = new OutputStreamWriter(new
		// FileOutputStream(
		// new File("finalfour.txt")), "UTF-8");
		// BufferedWriter output = new BufferedWriter(write);

		String input;
		String first = null;
		String current = null;
		String currentids = "";
		String currentin = "";
		String currentin2 = "";
		String[] parts = null;
		int currentcount = 0;
		

		// not null
		while ((input = br.readLine()) != null) {
			if (!input.equals("")) {
				parts = input.split("\t");
				first = parts[0];
				String ids = parts[1];
				String indices = parts[2];
				String indices2 = parts[3]; 

				if (current != null && current.equals(first)) {
					if (!currentids.contains(ids)) {
						currentcount++;
						currentids += "," + ids;
						currentin += "," + indices;
						currentin2 += "," + indices2;
					}

				} else {
					if (current != null) {
						System.out.println(current.split(" time ")[0] + "\t"
								+ current.split(" time ")[1] + "\t"
								+ Result(currentids) + "\t" + currentcount
								+ "\t" + currentin.split(",")[whichone] + "\t" + currentin2.split(",")[whichone]);
//						output.append(current.split(" time ")[0] + "\t"
//								+ current.split(" time ")[1] + "\t"
//								+ Result(currentids) + "\t" + currentcount
//								+ "\t" + currentin + "\n");
					}
					current = first;
					currentcount = 1;
					currentids = ids;
					currentin = indices;
					currentin2 = indices2;
				}
			}
		}

		if (current != null && current.equals(first)) {
			System.out.println(current.split(" time ")[0] + "\t"
					+ current.split(" time ")[1] + "\t" + Result(currentids)
					+ "\t" + currentcount + "\t" + currentin.split(",")[whichone] + "\t" + currentin2.split(",")[whichone]);

			// output.append(current.split(" time ")[0] + "\t"
			// + current.split(" time ")[1] + "\t" + Result(currentids)
			// + "\t" + currentcount + "\t" + currentin + "\n");
			// output.append(current.split(" time ")[0] + "\t"
			// +current.split(" time ")[1] + "\t" + currentids + "\t" +
			// currentcount + "\n");
		}
		// output.close();
	}

	/**
	 * Description:
	 * 
	 * @param currentids
	 * @return
	 */
	private static String Result(String a) {
		// TODO Auto-generated method stub
		String[] b = a.split(",");
		BigInteger[] c = new BigInteger[b.length];

		for (int i = 0; i < c.length; i++) {
			c[i] = new BigInteger(b[i]);
		}

		BigInteger[] d = bubbleSort(c);
		String res = "";
		for (int i = 0; i < d.length; i++) {
			if (i == 0) {
				res = d[i].toString();
			} else {
				res += "," + d[i].toString();
			}
		}

		for (int j=0; j < b.length; j++){
			if (b[j].equals(d[0].toString()))
				whichone = j;
		}
		
		return res;
	}

	public static BigInteger[] bubbleSort(BigInteger[] args) {
		for (int i = 0; i < args.length - 1; i++) {
			for (int j = i + 1; j < args.length; j++) {
				if (args[i].compareTo(args[j]) > 0) {
					BigInteger temp = args[i];
					args[i] = args[j];
					args[j] = temp;
				}
			}
		}
		return args;
	}

}