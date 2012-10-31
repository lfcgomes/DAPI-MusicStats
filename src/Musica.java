import java.util.HashMap;

public class Musica {
	
	private String artist;
	private String timestamp;
	private String track_id;
	private String title;
	private HashMap<String,Integer> similiars;
	private HashMap<String,Integer> tags;
	

	public String getArtist() {
		return artist;
	}
	public void setArtist(String nome) {
		this.artist = nome;
	}
	public void setTimestamp(String time){
		this.timestamp = time;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public String getTitle(){
		return this.title;
	}
	public void setTitle(String title){
		this.title = title;
	}
	public void setTrackid(String trackid){
		this.track_id = trackid;
	}
	public String getTrackid(){
		return this.track_id;
	}
	public HashMap getSimilars(){
		return this.similiars;
	}
	public HashMap getTags(){
		return this.tags;
	}
	

}