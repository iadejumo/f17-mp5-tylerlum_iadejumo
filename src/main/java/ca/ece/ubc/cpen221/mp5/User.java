package ca.ece.ubc.cpen221.mp5;

import java.util.Collections;
import java.util.Map;
import java.util.Random;

import org.json.simple.JSONObject;

public class User {

	private String url;
	private Map<String,Integer> votes;
	private long review_count;
	private String type;
	private String user_id;
	private String name;
	private Double average_stars;
	private String jsonString;
	
	public User(JSONObject user) {
		this.url = (String) user.get("url");
		this.votes = (Map<String,Integer>) user.get("votes");
		this.review_count = (long) user.get("review_count");
		this.type = (String) user.get("type");
		this.user_id = (String) user.get("user_id");
		this.name = (String) user.get("name");
		this.average_stars = (Double) user.get("average_stars");
		this.jsonString = user.toString();
	}
	
	public User(String name) {
		
	}
	
	private void generateUniqueFields() {
		String 
	}
	
	private int randomNumberGenrator() {
		Random rand = new Random(); 
		return rand.nextInt(10000);
	}
	
	public String getUrl() {
		return url;
	}
	public Map<String,Integer> getVotes() {
		return Collections.unmodifiableMap(votes);
	}
	public long getReview_count() {
		return review_count;
	}
	public String getType() {
		return type;
	}
	public String getUser_id() {
		return user_id;
	}
	public String getName() {
		return name;
	}
	public Double getAverage_stars() {
		return average_stars;
	}
	
	public void updateRating(long newReview) {
		average_stars = (average_stars*review_count + newReview)/(review_count + 1); 
		review_count++;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof User) {
			User otherRes = (User) other;
			return (this.user_id.equals(otherRes.user_id));
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return user_id.hashCode();
	}
	
	@Override
	public String toString() {
		return jsonString;
	}
}
