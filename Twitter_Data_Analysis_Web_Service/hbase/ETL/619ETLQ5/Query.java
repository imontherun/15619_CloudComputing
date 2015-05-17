

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Query {
	BigInteger pub = new BigInteger(
			"6876766832351765396496377534476050002970857483815262918450355869850085167053394672634315391224052153");
	BigInteger input;
	String TEAMID = "sudoCloud";
	String AWS_ACCOUNT_ID1 = "7760-2621-3295";
	String AWS_ACCOUNT_ID2 = "2";
	String AWS_ACCOUNT_ID3 = "3";

	String censoredtext = null;
	int score = 0;

	/**
	 * Description: the output for the type one question
	 * 
	 * @param X*Y
	 * @return the rest-based output
	 */
	public String getTypeOneResult(String a) {
		input = new BigInteger(a);
		BigInteger res = input.divide(pub);
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sdf.format(dt);

		return res.toString() + "\n" + TEAMID + "," + AWS_ACCOUNT_ID1 + ","
				+ AWS_ACCOUNT_ID2 + "," + AWS_ACCOUNT_ID3 + "\n" + date + "\n";
	}

	/**
	 * Description:
	 * 
	 * @param text
	 * @return
	 */
	public void getTypeTwoResult(String text) {
		DataAfinn data = new DataAfinn();
		int begin = 0;
		int score = 0;

		String res = "";

		DataCensor censor = new DataCensor();

		for (int i = 0; i < text.length(); i++) {
			// read a char that is not number or letter
			char ii = text.charAt(i);
			if (!Character.isLetterOrDigit(ii)
					|| ((Character.isLetterOrDigit(ii) && i == text
							.length() - 1))) {
				if (begin > i - 1 && i == text.length() - 1 && !Character.isLetterOrDigit(ii)){
					
				}
				else if (begin > i - 1 && i != text.length() - 1) {
					begin++;
				} else {
					String word;
					if (i == text.length() - 1 && Character.isLetterOrDigit(ii)) {
						word = text.substring(begin).toLowerCase();
					} else {
						word = text.substring(begin, i).toLowerCase();
					}
					int weight = data.getWeight(word);
					// word is in AFINN
					if (weight != 0) {
						score += weight;
					}

					// censor
					if (censor.contains(word)) {
						char[] wordOld;
						if (i == text.length() - 1
								&& Character.isLetterOrDigit(ii)) {
							wordOld = text.substring(begin).toCharArray();
						} else {
							wordOld = text.substring(begin, i).toCharArray();
						}
						for (int k = 1; k < wordOld.length - 1; k++) {
							wordOld[k] = '*';
						}
						String wword = String.valueOf(wordOld);
						res += wword.substring(0, wword.length());
						if (i == text.length() - 1
								&& Character.isLetterOrDigit(ii)) {
							res = res.substring(0, res.length() - 1);
						}
					} else {
						res += text.substring(begin, i);
					}
					begin = i + 1;
				}
				res += ii;
			}
		}
		this.score = score;
		this.censoredtext = res;
//		System.out.println(res);
//		System.out.println(score);
	}
}