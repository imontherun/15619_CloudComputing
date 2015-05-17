
/**
 * @author Hansi Mou
 * @date Oct 21, 2014
 * @version 1.0
 */

public class Tweet {
	private String id_str;  
    private User user;  
    private String created_at;  
    private int Sen_Score = 0;
    private String text;
    
  
    public String getTweetID() {  
        return id_str;  
    }  
  
    public void setTweetId(String id_str) {  
        this.id_str = id_str;  
    }  
  
    public User getUser() {  
        return user;  
    }  
  
    public void setUser(User user) {  
        this.user = user;  
    }  
  
    public String getTweetTime() {  
        return created_at;  
    }  
  
    public void setTweetTime(String created_at) {  
        this.created_at = created_at;  
    }  
    
    public int getSenScore() {  
        return Sen_Score;  
    }  
  
    public void setSenScore(int Sen_Score) {  
        this.Sen_Score = Sen_Score;  
    }  
    
    public String getText() {  
        return text;  
    }  
  
    public void setText(String text) {  
        this.text = text;  
    }
    
    public String toString() {  
        return "Tweet [created_at=" + created_at + ", id_str=" + id_str + ", user="  
                + user.getUserID()+ ", Sen_Score="  + Sen_Score+ ", text=" + text+"]";  
    }  
}
