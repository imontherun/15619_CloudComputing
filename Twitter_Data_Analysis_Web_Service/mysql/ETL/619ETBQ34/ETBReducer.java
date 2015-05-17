import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.TreeSet;

/**
 * @author Hansi Mou
 *
 *         Sep 8, 2014
 */
public class ETBReducer {

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

//		 InputStreamReader read = new InputStreamReader(new FileInputStream(
//		 new File("sample.txt")), "UTF-8");
//		 BufferedReader br = new BufferedReader(read);
		// //
		// OutputStreamWriter write = new OutputStreamWriter(new
		// FileOutputStream(
		// new File("resultnew.txt")), "UTF-8");
		// BufferedWriter output = new BufferedWriter(write);

		String input;
		String first = null;
		String current = null;
		String currentids = "";
		String res;

		// not null
		while ((input = br.readLine()) != null) {
			String[] parts = input.split("\t");
			first = parts[0];
			String second = " " + parts[1];

			if (current != null && current.equals(first)) {
				currentids += second;

			} else {
				if (current != null) {
					if ((res = Result(current, currentids)) != null) {
						System.out.println(Result(current, currentids));
						// output.append(res + "\n");
					}
				}
				current = first;
				currentids = second;
			}
		}

		if (current != null && current.equals(first)) {
			if ((res = Result(current, currentids)) != null) {
				System.out.print(Result(current, currentids));
				// output.append(res + "\n");
			}
		}
		// output.close();
	}

	/**
	 * Description:
	 * 
	 * @param string
	 * @param resultskim
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static String Result(String one, String two) {
		// TODO Auto-generated method stub
		String twonew = two;
		if (two.startsWith(" ")) {
			twonew = two.substring(1);
		}
		int i = 0;
		String res = "";

		String[] partold = twonew.split(" ");
		String[] part = Unique(partold);
		for (i = 0; i < part.length; i++) {
			if (part[i].endsWith("a"))
				break;
		}
		if (i == part.length)
			return null;
		if (part.length == 1) {
			if (part[0].endsWith("b")) {
				return null;
			} else if (part[0].endsWith("a")) {
				return one + "\t" + part[0].substring(0, part[0].length() - 1);
			}
		}

		ArrayList bione = new ArrayList();
		ArrayList bitwo = new ArrayList();

		for (int ii = 0; ii < part.length; ii++) {
			if (ii == 0 && part[ii].endsWith("a")) {
				bione.add((part[ii].substring(0, part[ii].length() - 1)));
			} else if (ii != 0) {
				if (part[ii - 1].substring(0, part[ii - 1].length() - 1)
						.equals(part[ii].substring(0, part[ii].length() - 1))) {

					bione.remove((part[ii].substring(0, part[ii].length() - 1)));

					bitwo.add((part[ii].substring(0, part[ii].length() - 1)));
				} else if (part[ii].endsWith("a")) {
					bione.add((part[ii].substring(0, part[ii].length() - 1)));
				}
			}
		}

		bione.addAll(bitwo);

		BigInteger[] arrayone = new BigInteger[bione.size()];
		for (int i1 = 0; i1 < bione.size(); i1++) {
			arrayone[i1] = new BigInteger(bione.get(i1).toString());
		}

		BigInteger[] allSort = bubbleSort(arrayone);

		for (int k = 0; k < allSort.length; k++) {
			boolean flag = false;
			for (int kk = 0; kk < bitwo.size(); kk++) {
				if (allSort[k].compareTo(new BigInteger(bitwo.get(kk)
						.toString())) == 0) {
					res += " " + "(" + allSort[k] + ")";
					flag = true;
				}
			}
			if (flag == false) {
				res += " " + allSort[k];
			}
		}

		return (one + "\t" + res.trim()).trim();
	}

	/**
	 * Description:
	 * 
	 * @param partold
	 * @return
	 */
	private static String[] Unique(String[] partold) {
		// TODO Auto-generated method stub
		TreeSet<String> tr = new TreeSet<String>();
		for (int i = 0; i < partold.length; i++)
			tr.add(partold[i]);
		String[] s2 = new String[tr.size()];
		for (int i = 0; i < s2.length; i++) {
			s2[i] = tr.pollFirst();
		}
		return s2;
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
