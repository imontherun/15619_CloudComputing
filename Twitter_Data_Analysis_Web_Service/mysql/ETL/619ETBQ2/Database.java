import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author Hansi Mou
 * @date Oct 23, 2014
 * @version 1.0
 */

/**
 * @author Hansi Mou
 *
 *         Oct 23, 2014
 */
public class Database {

	static String TEAMID;
	static String AWS_ACCOUNT_ID1;
	static String AWS_ACCOUNT_ID2;
	static String AWS_ACCOUNT_ID3;

	static String driver = "com.mysql.jdbc.Driver";
	static String url;
	static String user = "root";
	static String password = "db15319root";
	static Connection conn;

	public Database(String url) throws Exception {
		this.url = url;
		Class.forName(driver);
		conn = DriverManager.getConnection(url, user, password);
		if (!conn.isClosed())
			System.out.println("Succeeded connecting to the Database!");
	}

	public static String getResult(String a, String b) throws Exception {
		Statement stmt;
		stmt = conn.createStatement();
		String selectSql = "select tweet_id,sen_score,censored_text from tweet where user_id='"
				+ a + "' and tweet_time='" + b + "'";
		ResultSet selectRes = stmt.executeQuery(selectSql);
		while (selectRes.next()) {
			System.out.println(TEAMID + "," + AWS_ACCOUNT_ID1 + ","
					+ AWS_ACCOUNT_ID2 + "," + AWS_ACCOUNT_ID3);
			System.out.println(selectRes.getString("tweet_id") + ":"
					+ selectRes.getString("sen_score") + ":"
					+ selectRes.getString("censored_text"));
		}

		return null;
	}

	public void close() throws Exception {
		conn.close();
	}
}
