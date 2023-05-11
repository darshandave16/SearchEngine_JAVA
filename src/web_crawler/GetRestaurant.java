package web_crawler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import model.RestaurantData;

/**
* GetRestaurant class is used to get page source code after it is rendered by the selenium library.
* This class gets the Restaurant from the given Web-Page and store it in a csv file 
*/
public class GetRestaurant {
	
	private static List<RestaurantData> array = new ArrayList<>();
	
	// The local path of the directory
	private static String pathDir = System.getProperty("user.dir") + "/menus/new/";
	
	/**
	 * Get the latest restaurant list from the web-page and then crawl each for their menu
	 */
	public static void saveToFile(String text) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(pathDir + "temp_2" + ".txt"));
	    writer.write(text);
	    writer.close();
	}
	
	/**
	 *  Check the Restaurant is already present or not
	 * @param array : list of all the restaurants fetched from the Web-Page
	 * @param name : the Restaurant to check for
	 * @return : bool : if tis present or not
	 */
	public static boolean contains(List<RestaurantData> array, String name){
	    return array.stream().filter(o -> o.getName().equals(name)).findFirst().isPresent();
	}
	
	/**
	 * Saves the file to the local Storage
	 * @param array : list of all the restaurants fetched from the Web-Page
	 * @throws IOException : If error while storing
	 */
	public static void saveToCSV(List<RestaurantData> array) throws IOException {
		FileWriter file = new FileWriter(pathDir + "restaurants_data.csv");
		String newLine = System.getProperty("line.separator");
		for (RestaurantData d: array) {
			System.out.println(d.getName() + " " + d.getLink());
			file.write(d.getName() + ", " + d.getLink() + newLine);
		}
		file.close();
	}
	
	/**
	 * This method do the main crawling, rendering of the Web-Page and the delays between each calls
	 * @param args : None
	 */
	public static void main(String[] args) {
		String URL = "https://www.skipthedishes.com/windsor/restaurants";
		Crawler crawler = new Crawler();
		Document doc = crawler.getSource(URL, 5000);
		Elements div = doc.select("#root > div > main > div > div > div > div > div.styles__ListWrapper-ybz2uy-0.btxZuN > div");
		
		for(Element child: div) {
			System.out.println("####");
			for(Element el: child.children()) {
				if (el.tagName().equals("li")) {
					String url = el.child(0).child(0).attr("href");
					String name = el.child(0).child(0).child(0).child(1).child(1).text();
					//System.out.println("# " + name + " : " + url);
					RestaurantData data = new RestaurantData(name, url);
					if (!contains(array, data.getName())) {
						array.add(data);
					}
				}
			}
		}
		
		for (RestaurantData d: array) {
			System.out.println(d.getName() + " " + d.getLink());
		}
		
		try {
			saveToCSV(array);
			crawler.saveToFile("", true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error: " + e.getMessage());
		}
	}

}
