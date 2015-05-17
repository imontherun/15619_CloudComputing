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
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in,
				"UTF-8"));
//
//		 InputStreamReader read = new InputStreamReader(
//		 new FileInputStream(
//		 new File(
//		 "/Users/hans/Downloads/s3cmd-1.5.0-rc1/15619f14twitter-parta-aa")),
//		 "UTF-8");
//		InputStreamReader read = new InputStreamReader(new FileInputStream(
//				new File("sample.txt")), "UTF-8");
		//
//		BufferedReader br = new BufferedReader(read);
//		OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(
//				new File("q6map.txt")), "UTF-8");
//		BufferedWriter output = new BufferedWriter(write);
		//
		// long startTime = System.currentTimeMillis();

		String input;
		Gson gson = new Gson();
		while ((input = br.readLine()) != null) {
			Tweet tweet = gson.fromJson(input, Tweet.class);
			if (tweet != null && tweet.getEntities() != null
					&& tweet.getEntities().getMedia() != null) {
				
				int count = getPhotoNum(tweet.getEntities().getMedia());
				System.out.println(tweet.getUser().getUserID() + "\t"
						+ tweet.getTweetID() + ":" + count);
//				output.append(tweet.getUser().getUserID() + "\t"
//						+ tweet.getTweetID() + ":" + count + "\n");
			}
		}
//		output.close();
	}

	/**
	 * Description:
	 * 
	 * @param medias
	 * @return
	 */
	private static int getPhotoNum(Tweet.Media[] medias) {
		// TODO Auto-generated method stub
		int res = 0;
		for (int i = 0; i < medias.length; i++) {
			if (medias[i].getType().equals("photo"))
				res++;
		}
		return res;
	}
}
