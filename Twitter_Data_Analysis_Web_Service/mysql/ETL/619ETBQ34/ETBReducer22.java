import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author Hansi Mou
 *
 *         Sep 8, 2014
 */
public class ETBReducer22 {

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
//		 
//		 OutputStreamWriter write = new OutputStreamWriter(new
//		 FileOutputStream(
//		 new File("finalfour3.txt")), "UTF-8");
//		 BufferedWriter output = new BufferedWriter(write);

		String input;
		String first = null;
		String current = null;
		String currentids = "";
		int currentcount = 0;

		// not null
		while ((input = br.readLine()) != null) {
			String[] parts = input.split("\t");
			first = parts[0];
			String ids = parts[1];

			if (current != null && current.equals(first)) {
				if (!currentids.contains(ids)) {
					currentcount++;
					currentids += ";" + ids;
				}

			} else {
				if (current != null) {
					System.out.println(Result(current, currentids));
//					output.append(Result(current, currentids) + "\n");
				}
				current = first;
				currentcount = 1;
				currentids = ids;
			}
		}

		if (current != null && current.equals(first)) {
			System.out.println(Result(current, currentids));
//			output.append(Result(current, currentids) + "\n");
		}
//		 output.close();
	}

	/**
	 * Description:
	 * 
	 * @param currentids
	 * @return
	 */
	private static String Result(String dateloc, String in) {
		// TODO Auto-generated method stub
		String res = "";
		String input = in;
		if (in.endsWith(";"))
			 input = in.substring(0, in.length()-1);
		
		if (!input.contains(";"))
			return dateloc + "\t" +input.split("%")[0];
		
		String[] item = input.split(";");
		List<Item> list = new ArrayList();
		for (int i = 0; i < item.length; i++) {
			Item ii = new Item(item[i]);
			list.add(ii);
		}
		Collections.sort(list, new MyComparator());
		Iterator itr = list.iterator();
		while (itr.hasNext()) {
		    Item a = (Item)itr.next();
		    res += a.toString();
		}
		if (res.endsWith("\\n"))
			res = res.substring(0,res.length()-2);
		return (dateloc + "\t" + res);
	}

}