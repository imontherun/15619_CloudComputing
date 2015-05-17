
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;

/**
 * @author Hansi Mou
 *
 *         Sep 8, 2014
 */
public class ETBMapper22 {
	public static void main(String[] args) throws IOException, ParseException {
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in,
				"UTF-8"));

//		InputStreamReader read = new InputStreamReader(new FileInputStream(
//				new File("sample.txt")), "UTF-8");

//		BufferedReader br = new BufferedReader(read);
//		OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(
//				new File("resultfour2.txt")), "UTF-8");
//		BufferedWriter output = new BufferedWriter(write);

		String input;
		while ((input = br.readLine()) != null) {
			if (!input.equals("")) {
				String[] parts = input.split("\t");
				
				System.out.println(parts[0] + "\t" + parts[1] + ":" + parts[2]
						+ "%" + parts[3] + "%" + parts[4] + "%" + parts[5]);
//				output.append(parts[0] + "\t" + parts[1] + ":" + parts[2]
//						+ "%" + parts[3] + "%" + parts[4] + "\n");
			}
		}
//		output.close();
	}
}
