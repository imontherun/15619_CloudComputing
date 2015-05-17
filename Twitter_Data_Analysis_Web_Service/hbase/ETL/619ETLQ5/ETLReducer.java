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
import java.util.HashSet;
import java.util.TreeSet;

/**
 * @author Hansi Mou
 *
 *         Sep 8, 2014
 */
public class ETLReducer {

	/**
	 * Description:
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		 BufferedReader br = new BufferedReader(new
		 InputStreamReader(System.in,
		 "UTF-8"));

//		InputStreamReader read = new InputStreamReader(new FileInputStream(
//				new File("sample2.txt")), "UTF-8");
//		BufferedReader br = new BufferedReader(read);
//
//		OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(
//				new File("q5reduce.txt")), "UTF-8");
//		BufferedWriter output = new BufferedWriter(write);

		String input;
		String first = null;
		String current = null;
		String currentids = "";
		String res;

		// not null
		while ((input = br.readLine()) != null) {
			String[] parts = input.split("\t");
			first = parts[0];
			String second = ";" + parts[1];

			if (current != null && current.equals(first)) {
				currentids += second;

			} else {
				if (current != null) {
					System.out.println(getResult(current, currentids));
//					output.append(getResult(current, currentids) + "\n");
				}
				current = first;
				currentids = second;
			}
		}

		if (current != null && current.equals(first)) {
			System.out.print(getResult(current, currentids));
//			output.append(getResult(current, currentids) + "\n");
		}
//		output.close();
	}

	/**
	 * Description:
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	private static String getResult(String a, String bb) {
		// TODO Auto-generated method stub
		String b = bb;
		if (bb.startsWith(";"))
			b = bb.substring(1);
		String[] scores = b.split(";");

		HashSet<String> s1 = new HashSet<String>();  
		HashSet<String> s2 = new HashSet<String>();  
		HashSet<String> s3 = new HashSet<String>();  
		for (int i = 0; i < scores.length; i++) {
			String[] part = scores[i].split(":");
			if (part[0].contains("s1"))
				s1.add(part[1]);
			else if (part[0].contains("s2"))
				s2.add(part[1]);
			else if (part[0].contains("s3"))
				s3.add(part[1]);
		}
		int score1 = s1.size();
		int score2 = s2.size()*3;
		int score3 = s3.size()*10;
		int all = score1 + score2 + score3;
		return a + "\t" + score1 + "," + score2 + "," + score3 + "," + all;
	}

}