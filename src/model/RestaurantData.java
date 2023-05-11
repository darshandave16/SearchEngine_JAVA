package model;

public class RestaurantData {
	
	String name;
	String link;
	
	public RestaurantData(String name, String link) {
		this.name = name;
		this.link = link;
	}
	
	public String getName() { return this.name; }
	
	public String getLink() { return this.link; }

}
