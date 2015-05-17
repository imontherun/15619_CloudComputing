/**
 * @author Hansi Mou
 * @date Oct 21, 2014
 * @version 1.0
 */

public class User {
	private String id_str;
	private String time_zone;
	
	public String getUserID(){
		return id_str;
	}
	public void setUserID(String id_str){
		this.id_str = id_str;
	}
	public String getTimezZone(){
		return time_zone;
	}
}
