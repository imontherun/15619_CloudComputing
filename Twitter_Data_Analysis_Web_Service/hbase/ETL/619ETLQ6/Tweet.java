
/**
 * @author Hansi Mou
 * @date Oct 21, 2014
 * @version 1.0
 */

public class Tweet {
    private User user;  
    private Tweet retweeted_status = null;
    private String id_str;  
    private String created_at;  
    public Entities entities;
    public Places place;
    
    public Entities getEntities(){
    	return entities;
    }
    
    public String getTweetID() {  
        return id_str;  
    }  
  
    public void setTweetId(String id_str) {  
        this.id_str = id_str;  
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
    
    public User getUser() {  
        return user;  
    }  
    
    public Tweet getTweet() {  
        return retweeted_status;  
    }  
    
    public class Entities {
    	private Hashtags[] hashtags = null;
    	private Media[] media = null;
    	public Hashtags[] getHash(){
    		return hashtags;
    	}
    	public Media[] getMedia(){
    		return media;
    	}
    }
    
    public class Media{
    	private String type;
    	public String getType(){
    		return type;
    	}
    }
    
    public class Hashtags {
    	private String text = null;
    	private String[] indices;
    	public String getText(){
    		return text;
    	}
    	public String[] getIndices(){
    		return indices;
    	}
    }
    
    public class Places {
    	private String name = null;
    	public String getName(){
    		return name;
    	}
    }

}
