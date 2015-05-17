import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Hansi Mou
 *
 *         Sep 8, 2014
 */
public class ETLReducer {

	private static int total = 0;
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
//				new File("q6mapsort.txt")), "UTF-8");
//		BufferedReader br = new BufferedReader(read);
//
//		OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(
//				new File("q6reduce.txt")), "UTF-8");
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
					String ss = getResult(current, currentids);
					System.out.println(ss);
//					output.append(ss + "\n");
				}
				current = first;
				currentids = second;
			}
		}

		if (current != null && current.equals(first)) {
			String ss = getResult(current, currentids);
			System.out.println(ss);
//			output.append(ss + "\n");
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
		String[] photos = b.split(";");
		Map<String, String> res = new HashMap<String, String>();
		
		for (int i = 0; i < photos.length; i++){
			String[] r = photos[i].split(":");
			res.put(r[0], r[1]);
		}
		
		int count = 0;
		
		Iterator iter = res.entrySet().iterator(); 
		while (iter.hasNext()) { 
		    Map.Entry entry = (Map.Entry) iter.next(); 
		    count += Integer.valueOf((String)entry.getValue());
		}
		
		total += count;
		
		return a + "\t" + count + "\t" + total;
	}

}