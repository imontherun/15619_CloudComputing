import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.gson.Gson;


/**
 * @author Hansi Mou
 *
 *         Sep 8, 2014
 */
public class dodo {
	public static void main(String[] args) throws IOException, ParseException {
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));

//		InputStreamReader read = new InputStreamReader(
//				new FileInputStream(
//						new File(
//								"/Users/hans/Downloads/s3cmd-1.5.0-rc1/15619f14twitter-parta-aa")),
//				"UTF-8");
//		 InputStreamReader read = new InputStreamReader(new FileInputStream(
//		 new File("result3c.txt")), "UTF-8");
//
//		BufferedReader reader = new BufferedReader(read);
//		OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(
//				new File("result3.txt")), "UTF-8");
//		BufferedWriter output = new BufferedWriter(write);
//
//		long startTime = System.currentTimeMillis();

		String input;
		Gson gson = new Gson();
		int i = 0;
		Query aa = new Query();
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss z yyyy", Locale.US);
		DateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd+HH:mm:ss");

		char t = 9; // \t
		char b = 10; // \n
		char zero = 0;
		char w = 6;
		char r = 13;
		while ((input = br.readLine()) != null) {
			Tweet tweet = gson.fromJson(input, Tweet.class);
			if (tweet != null) {
				aa.getTypeTwoResult(tweet.getText());
				date = sdf.parse(tweet.getTweetTime());

				
				System.out.println(tweet.getTweetID()
						+ t
						+ tweet.getUser().getUserID()
						+ t
						+ sdf2.format(new Date(date.getTime() + 4 * 3600 * 1000))
						+ t + aa.score + t
						+ aa.censoredtext.replace(b, zero));
			}
		}
	}
}
