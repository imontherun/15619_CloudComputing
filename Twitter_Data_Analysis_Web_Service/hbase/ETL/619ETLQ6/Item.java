import java.math.BigInteger;


/**
 * @author Hansi Mou
 *
 *         Nov 4, 2014
 */
public class Item {
	public String hashtag;
	public String tweetid;
	public int count;
	public int indices;
	public int indices2;
	public BigInteger firsttweet;

	public Item(String input) {
		this.hashtag = input.split(":")[0];
		this.tweetid = input.split(":")[1].split("%")[0];
		this.indices = Integer.parseInt(input.split("%")[2]);
		this.count = Integer.parseInt(input.split("%")[1]);
		this.indices2 = Integer.parseInt(input.split("%")[3]);

		if (!tweetid.contains(","))
			this.firsttweet = new BigInteger(tweetid);
		else
			this.firsttweet = new BigInteger(tweetid.split(",")[0]);
	}

	public String toString() {
		return hashtag + ":" + tweetid + "\\n";
	}
}
