import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;

import com.google.gson.Gson;

/**
 * @author Hansi Mou
 *
 *         Sep 8, 2014
 */
public class ETLMapper {
	public static void main(String[] args) throws IOException, ParseException {
		// TODO Auto-generated method stub
		 BufferedReader br = new BufferedReader(new
		 InputStreamReader(System.in, "UTF-8"));

//		InputStreamReader read = new InputStreamReader(
//				new FileInputStream(
//						new File(
//								"/Users/hans/Downloads/s3cmd-1.5.0-rc1/15619f14twitter-parta-aa")),
//				"UTF-8");
//		InputStreamReader read = new InputStreamReader(new FileInputStream(
//				new File("sample.txt")), "UTF-8");
		//
//		BufferedReader br = new BufferedReader(read);
//		OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(
//				new File("q5map.txt")), "UTF-8");
//		BufferedWriter output = new BufferedWriter(write);
		//
		// long startTime = System.currentTimeMillis();

		String input;
		Gson gson = new Gson();
		while ((input = br.readLine()) != null) {
			Tweet tweet = gson.fromJson(input, Tweet.class);
			if (tweet != null) {
				// for score 1
				System.out.println(tweet.getUser().getUserID() + "\t" + "s1:"
						+ tweet.getTweetID());
//				output.append(tweet.getUser().getUserID() + "\t" + "s1:"
//						+ tweet.getTweetID() + "\n");
			}
			if (tweet != null && tweet.getTweet() != null) {
				// for score2
				System.out.println(tweet.getTweet().getUser().getUserID()
						+ "\t" + "s2:" + tweet.getTweetID());
//				output.append(tweet.getTweet().getUser().getUserID() + "\t"
//						+ "s2:" + tweet.getTweetID() + "\n");

				// for score3
				System.out
						.println(tweet.getTweet().getUser().getUserID() + "\t"
								+ "s3:"
								+ tweet.getUser().getUserID());
//				output.append(tweet.getTweet().getUser().getUserID() + "\t"
//						+ "s2:" + tweet.getTweet().getUser().getUserID() + "\n");
			}
		}
//		 output.close();
	}
}
