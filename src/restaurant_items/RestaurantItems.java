package restaurant_items;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import model.RestaurantItem;


/*
 * Class RestaurantItems: Class to parse all the restaurants and create RestaurantItem objects 
 */
public class RestaurantItems {
	public static void main(String[] args) throws Exception {
		Set<RestaurantItem> items = createAllRestaurantItems();
		for(RestaurantItem item : items) {
			if(item.restaurant.equals("HT Grill and Chill")) {
				System.out.println("*********");
				System.out.println("Price: " + item.price);
				System.out.println("Item Name: " + item.itemName);
				System.out.println("Restaurant Name: " + item.restaurant);
				
				System.out.println("*********");
			}
			
		}
		Map<String, Set<RestaurantItem>> map = getItemsByRestaurant(items);
		
	}
	
	/*
	 * Parses through all the webpages and creates a set of all restaurant menu items for all the restaurants
	 */
	public static Set<RestaurantItem> createAllRestaurantItems() throws Exception {
		Set<RestaurantItem> ans = new HashSet<>();
		String pathDir = System.getProperty("user.dir") + "/menus/html";
		File dir = new File(pathDir);
		File[] rpaths = dir.listFiles();
		for(File rpath : rpaths) {
			if(!rpath.getName().contains(".txt")) {
                continue;
            }
			String restaurant = rpath.getName().substring(0, rpath.getName().lastIndexOf(".txt"));
			String content = FileUtils.readFileToString(rpath, "UTF-8");
			Document doc = Jsoup.parse(content); 
			Elements items = doc.getElementsByClass("styles__PositionContainer-sc-1xl58bi-0");
			for(Element e : items) {
				
				String itemName = e.getElementsByClass("styles__Title-sc-1xl58bi-5").first().text();
				String price = e.getElementsByClass("styles__Price-sc-1xl58bi-6").first().text();
				if(!itemName.matches("[0-9]*")) {
					if(price.matches("\\$[0-9]*\\.[0-9]*")) {
						price = price.substring(1);
						float p = Float.parseFloat(price);
						RestaurantItem item = new RestaurantItem(p, itemName, restaurant);
						ans.add(item);
					} else if(price.matches("\\$[0-9]*\\.[0-9]* Offer")) {
						price = price.substring(1);
						price = price.substring(0, price.lastIndexOf(" Offer"));
						float p = Float.parseFloat(price);
						RestaurantItem item = new RestaurantItem(p, itemName, restaurant);
						ans.add(item);
					}
				}
				
				
			}
		}
		return ans;
		
	}
	
	/*
	 * Creates a Map of restaurant to its food items it is serving.
	 */
	public static Map<String, Set<RestaurantItem>> getItemsByRestaurant(Set<RestaurantItem> items) {
		Map<String, Set<RestaurantItem>> ans = new HashMap<>();
		Set<String> restaurants = new HashSet<>();
		for(RestaurantItem item : items) {
			if(ans.containsKey(item.restaurant)) {
				Set<RestaurantItem> is = ans.get(item.restaurant);
				is.add(item);
				ans.put(item.restaurant, is);
			} else {
				Set<RestaurantItem> is = new HashSet<>();
				is.add(item);
				ans.put(item.restaurant, is);
			}
		}
		
		return ans;
	}
	
}
