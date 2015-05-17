import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
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
public class ETBMapper2 {
	public static void main(String[] args) throws IOException, ParseException {
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in,
				"UTF-8"));

//		 InputStreamReader read = new InputStreamReader(new FileInputStream(
//		 new File("try.txt")), "UTF-8");
//		
//		 BufferedReader br = new BufferedReader(read);
		// OutputStreamWriter write = new OutputStreamWriter(new
		// FileOutputStream(
		// new File("resultfour.txt")), "UTF-8");
		// BufferedWriter output = new BufferedWriter(write);
		//
		// long startTime = System.currentTimeMillis();

		String hashtag;
		String time;
		String loc = null;
		String indices;
		String indices2;
		String input;
		Gson gson = new Gson();
		SimpleDateFormat sdf = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss z yyyy", Locale.US);
		DateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		Date date;

		while ((input = br.readLine()) != null) {
			Tweet tweet = gson.fromJson(input, Tweet.class);
			if (tweet != null && tweet.entities.getHash() != null) {
				int len = tweet.entities.getHash().length;
				if (tweet != null && len > 0) {
					for (int i = 0; i < len; i++) {
						if (Unique(tweet, i)) {

							hashtag = tweet.entities.getHash()[i].getText();
							time = sdf2.format(sdf.parse(tweet.getTweetTime())
									.getTime());
							indices = tweet.entities.getHash()[i].getIndices()[0];
							indices2 = tweet.entities.getHash()[i].getIndices()[1];
							
							if (tweet.place != null) {
								loc = tweet.place.getName();
							} else if ((loc = tweet.getUser().getTimezZone()) != null) {
								if (loc.toLowerCase().contains("time")) {
									loc = null;
								}
							}
							if (loc != null) {
								System.out.println(time + loc + " time "
										+ hashtag + "\t" + tweet.getTweetID()
										+ "\t" + indices + "\t" + indices2);
								// output.append(time + loc + " time " + hashtag
								// + "\t" + tweet.getTweetID() + "\t"
								// + indices + "\n");
							}

						}
					}
				}
			}
		}
		// output.close();
	}

	/**
	 * Description: 
	 * @param hash
	 * @param i
	 * @return
	 */
	private static boolean Unique(Tweet tweet, int i) {
		// TODO Auto-generated method stub
		for (int j=0; j<i; j++){
			if (tweet.entities.getHash()[i].getText().equals(tweet.entities.getHash()[j].getText()))
				return false;
		}
		
		return true;
	}
}
