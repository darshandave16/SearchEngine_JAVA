package model;

/*
 * Class RestaurantItem: Stores information about a menu item from any given restaurant
 */
public class RestaurantItem {
	public float price;
	public String itemName;
	public String restaurant;
	
	public RestaurantItem(float price, String item, String restaurantName) {
		this.price = price;
		this.itemName = item;
		this.restaurant = restaurantName;
	}
}
