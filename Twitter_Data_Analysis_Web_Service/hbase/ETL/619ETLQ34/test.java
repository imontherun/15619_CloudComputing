/**
 * @author Hansi Mou
 * @date Oct 20, 2014
 * @version 1.0
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class test {
	/**
	 * Description:
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		// Query a = new Query();
		// System.out.println(a.getTypeOneResult("20630300497055296189489132603428150008912572451445788755351067609550255501160184017902946173672156459"));
		// Data a = new Data();
		// System.out.println(a.getWeight("flop"));
		// Query a = new Query();
		// a.getTypeTwoResult("jisd\njkasdf");
		//
		//
		// // DataCensor a = new DataCensor();
		// System.out.println(a.censoredtext.replace("\n",";"));
		// Database a = new
		// Database("jdbc:mysql://54.172.157.193:3306/twitter_db");
		// a.getResult("2306027542", "2014-03-21+17:02:14");
		// a.close();
//		String time = "2014-03-21+17:02:14";
//		Date date = null;
//		DateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd+HH:mm:ss");
//
//		date = sdf2.parse(time);
		InputStreamReader read = new InputStreamReader(new FileInputStream(
				new File("finalfour4.txt")), "UTF-8");
		BufferedReader br = new BufferedReader(read);
		String input;
		while ((input = br.readLine()) != null) {
			if (!input.startsWith("2014")){
				System.out.println(input);
			}
		}

	}

	private static String Result(String dateloc, String in) {
		// TODO Auto-generated method stub
		String res = "";
		String input = in;
		if (in.endsWith(";"))
			 input = in.substring(0, in.length()-1);
		
		if (!input.contains(";"))
			return input.split("%")[0];
		
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