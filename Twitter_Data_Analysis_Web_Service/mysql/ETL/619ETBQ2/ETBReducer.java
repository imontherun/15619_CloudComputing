

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

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
		 BufferedReader br = new BufferedReader(new
		 InputStreamReader(System.in,
		 "UTF-8"));

//		InputStreamReader read = new InputStreamReader(new FileInputStream(
//				new File("sorttest.txt")), "UTF-8");
//		BufferedReader br = new BufferedReader(read);
//
//		OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(
//				new File("reducetest.txt")), "UTF-8");
//		BufferedWriter output = new BufferedWriter(write);

		String input;
		String first = null;
		String current = null;
		String currentids = "";

		// not null
		while ((input = br.readLine()) != null) {
			String[] parts = input.split("\t");
			first = parts[0];
			String ids = parts[1];

			if (current != null && current.equals(first)) {
				if (!currentids.contains(ids)) {
					currentids += "\\n"+ids;
				}

			} else {
				if (current != null) {
					System.out.println(current + "\t" + currentids);
//					 output.append(current + "\t" + currentids + "\n");
				}
				current = first;
				currentids = ids;
			}
		}

		if (current != null && current.equals(first)) {
			System.out.println(current + "\t" + currentids);
//			 output.append(current + "\t" + currentids + "\n");
		}
//		output.close();
	}
}
