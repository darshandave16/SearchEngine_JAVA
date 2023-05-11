package web_crawler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
* GetMenus class is used to get page source code after it is rendered by the selenium library.
* This class gets the menus of each Restaurant from the given csv file and store it in a .html
*  or .txt file, as required by the user. 
*/
public class GetMenus {
	
	// The URL of the Web-Page to crawl
	private static String PARENT_URL = "https://www.skipthedishes.com";
	
	// The local path of the directory
	private static String pathDir = System.getProperty("user.dir") + "/menus/new/";
	
	// The file that contains all the restraunts.
	private static String PATH_REST_DB = pathDir + "restaurants_data.csv";
	
	// finalize the URL
	private static String getUrl(String route) {
		return PARENT_URL + route.trim();
	}
	
	/**
	 * Get the latest restaurant list from the web-page and then crawl each for their menu
	 */
	public static void crawlLatestDataFromAgrigator() {
		GetRestaurant.main(new String[] {});
		main(new String[] {});
	}
	
	/**
	 * This method do the main crawling, rendering of the Web-Page and the delays between each calls
	 * @param args : None
	 */
	public static void main(String[] args) {
		String URL = "https://www.skipthedishes.com/Macro%20sentdonalds-93-wyandotte";
		Crawler crawler = new Crawler();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(PATH_REST_DB));
			String row = reader.readLine();
			boolean start = true;
			while(row != null) {
				String[] rowEntries = row.split(",");
				System.out.println(rowEntries[0] + " : " + rowEntries[1]);
				if (start) {
					crawler.getSource(getUrl(rowEntries[1]), 3000);
					crawler.saveToFile(rowEntries[0], true);
				}
				row = reader.readLine();
			}
		} catch (IOException e) {
			System.out.println("Error : " + e.getMessage());		
		}
		
	}
	
}