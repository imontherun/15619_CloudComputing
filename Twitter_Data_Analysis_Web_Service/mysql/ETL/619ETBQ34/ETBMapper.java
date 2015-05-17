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
public class ETBMapper {
	public static void main(String[] args) throws IOException, ParseException {
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));

//		InputStreamReader read = new InputStreamReader(
//				new FileInputStream(
//						new File(
//								"/Users/hans/Downloads/s3cmd-1.5.0-rc1/15619f14twitter-parta-aa")),
//				"UTF-8");
//		 InputStreamReader read = new InputStreamReader(new FileInputStream(
//		 new File("sample.txt")), "UTF-8");
//
//		BufferedReader br = new BufferedReader(read);
//		OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(
//				new File("result3.txt")), "UTF-8");
//		BufferedWriter output = new BufferedWriter(write);
//
//		long startTime = System.currentTimeMillis();

		String input;
		Gson gson = new Gson();
		while ((input = br.readLine()) != null) {
			Tweet tweet = gson.fromJson(input, Tweet.class);
			if (tweet != null && tweet.getTweet() != null) {
				System.out.println(tweet.getTweet().getUser().getUserID()+'\t'+tweet.getUser().getUserID()+"a");
				System.out.println(tweet.getUser().getUserID()+"\t"+tweet.getTweet().getUser().getUserID()+"b");
//				output.append(tweet.getTweet().getUser().getUserID()+"\t"+tweet.getUser().getUserID()+"a");
//				output.append("\n");
//				output.append(tweet.getUser().getUserID()+"\t"+tweet.getTweet().getUser().getUserID()+"b");
//				output.append("\n");
			}
		}
//		output.close();
	}
}
