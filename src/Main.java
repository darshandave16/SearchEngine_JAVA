import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import countItems.CountItems;
import dictionary.Frequency;
import model.RestaurantItem;
import page_rank.pageRank;
import restaurant_items.RestaurantItems;
import reverse_indexing.ReverseIndex;
import reverse_indexing.WordData;
import spellcheck.SpellCheck;
import utils.MaxHeap;
import utils.PageMode;
import web_crawler.GetMenus;
import word_completion.AutoCompleteTrie;

/**
* The price Analysis menu are launched using this class 
*/

public class Main {
	
	// The state of the current menu is defined by mode variable
	private static PageMode mode = PageMode.MAIN_MENU;	
	
	// HashMap with the search history keywords and their frequency count
	private static Map <String, Integer> countMap;
	
	// Global dictionary that contain all words
	private static Set<String> globalDictionary;  
	
	// Trie of all the words from the dictionary
	private static AutoCompleteTrie trie = new AutoCompleteTrie();
	
	//
	private static Map<String, Set<RestaurantItem>> itemMap;
		
	/*
	 * The method used to Run the Application
	 */
	public static void main(String[] args) {		
		// Initiate the Global dictionary
		Frequency.createGlobalDictionaryAndMap();
		globalDictionary = Frequency.getGlobalDictionary();
		
		// Read previous search
		countMap = CountItems.readMapFromFile();
		
		
		// Initiate Reverse indexing
		ReverseIndex.init();
    	for(String word : globalDictionary) {
    		trie.insert(word);
    	}
    	SpellCheck.init();
    	try {
    		itemMap = RestaurantItems.getItemsByRestaurant(RestaurantItems.createAllRestaurantItems());
		} catch (Exception e1) {
			System.out.println("\nError : " + e1);
		}
    	
		int ch = 1;
		while (ch != 0) {
						
			try {
				switch (mode) {
				case SEARCH:
					ch = getSearchMenu();
					break;
				case MAIN_MENU:
					ch = getMainMenu();
					break;
				case TOP_10_SEARCH:
					ch = getTop10SearchMenus();
					break;
				case SEARCH_FREQUENCY:
					ch = getAllSearchFreqMenus();
					break;
				default:
					System.out.println("Erro while processing requesting, moving to main menu.");
					mode = PageMode.MAIN_MENU;
					break;
				}
			} catch (Exception e) {
				System.out.println("\nError : Invalid option! Please, pick option available in the menu. " + e.getMessage());
				ch = 1;
			}	
		}
		
	}
	
	/*
	 * Method that handles the menu route to display the options available in the main menu.
	 */
	private static int getMainMenu() {
		System.out.println("Find the Best deals\n");
		System.out.println(" [ 1 ] Search for top deals.");
		System.out.println(" [ 2 ] Show top 10 most searched items.");
		System.out.println(" [ 3 ] Show search frequency.");
		System.out.println(" [ 4 ] Crawl Latest Data. (This will take time)");
		System.out.println("\nSelect one option from the above menu: ");
		System.out.println("Or, Select 0 to exit.");
		Scanner scanner = new Scanner(System.in);
		
		int ch = scanner.nextInt();
		switch(ch) {
		case 1:
			mode = PageMode.SEARCH;
			break;
		case 2:
			mode = PageMode.TOP_10_SEARCH;
			break;
		case 3:
			mode = PageMode.SEARCH_FREQUENCY;
			break;
		case 4:
			GetMenus.crawlLatestDataFromAgrigator();
		case 0:
			ch = exitSystemApplication();
			return ch;
		default:
			System.out.println("\nError :Invalid option! Please, pick option available in the menu.");
		}
		return ch;
	}
	
	/*
	 * Method that handles the menu route for the search deal option from the main menu.
	 * This method handles all the important task suck as word completion suggestions and
	 * spell check suggestions. Based on the keywords entered by the user, the appropriate
	 * items with their price is displayed.
	 * return : state for the Menu loop
	 */
	private static int getSearchMenu() {
		System.out.println("\nEnter item to search deals for: ");
		System.out.println("Or, Type 0 to return to main menu.");
		Scanner scanner = new Scanner(System.in);
		String keyword = scanner.nextLine();
		
		// Check if exit condition is met
		if (keyword.trim().equals("0")) {
			//int ch = exitSystemApplication();
			mode = PageMode.MAIN_MENU;
			return 1;
		} else if (keyword.matches(".*[^A-Za-z].*")) { 
			// Check for Non-words in keyword and remove them
			keyword = keyword.replaceAll("[^a-zA-Z\\s]", "");
			System.out.println("\nRemoved invalid characters: " + keyword);
		}
		
		if (keyword.isEmpty()) {
			return 1;
		}
		
		// Convert to lower case 
		keyword = keyword.toLowerCase();
		
		String[] keywords = keyword.split(" ");
		
		Set<String> finalKeywords = new HashSet<String>();
		for (String key: keywords) {
			// check if present in the Dictionary 
			if (globalDictionary.contains(key)) {
				finalKeywords.add(key);
			} else {
				// check for auto completes
				Set<String> possibleOptions = trie.startsWith(key);
				if (possibleOptions.size() > 0) {
					
					System.out.println("\nDid you mean any of these: ");
					// select one from the possible auto complete options
					while (!globalDictionary.contains(key)) {
						String[] arry = new String[possibleOptions.size()];
						int c = 0;
						for (String autoStr: possibleOptions) {
							System.out.println("[ " + (c + 1)+ " ] " + autoStr);
							arry[c] = autoStr;
							c++;
						}
						
						System.out.println("\nSelect one option from the above menu: ");
						System.out.println("Or, Type 0 to exit.");
						try {
							Scanner scannerAuto = new Scanner(System.in);
							int opt = scannerAuto.nextInt();
							if (opt == 0) {
								mode = PageMode.MAIN_MENU;
								System.out.println("");
								return 1;
							} else if (opt > possibleOptions.size() +1 || opt < 1) {
								System.out.println("\nError: Invalid input try again.");
							} else {
								key = arry[opt-1];
								finalKeywords.add(key);
							}
						} catch (Exception e) {
							System.out.println("\nError: Invalid input try again.");
						}
						
					}
				} else {
			    	ArrayList<String> arry = SpellCheck.SpellCheckSuggesions(key);
			    	while(!globalDictionary.contains(key)) {
						System.out.println("\nUnable to find the requested items. \nSome other suggestions for you:");
			    		int c = 0;
				    	if (arry.size() > 0) {
				    		for (String autoStr: arry) {
								System.out.println("[ " + (c + 1)+ " ] " + autoStr);
								c++;
							}
				    	}
			    		System.out.println("\nSelect one option from the above menu: ");
			    		System.out.println("Or, Type 0 to exit.");
						try {
							Scanner scannerAuto = new Scanner(System.in);
							int opt = scannerAuto.nextInt();
							if (opt == 0) {
								mode = PageMode.MAIN_MENU;
								System.out.println("");
								return 1;
							} else if (opt > arry.size() +1 || opt < 1) {
								System.out.println("\nError: Invalid input try again.");
							} else {
								key = arry.get(opt - 1);
								finalKeywords.add(key);
							}
						} catch (Exception e) {
							System.out.println("\nError: Invalid input try again. ");
						}
			    	}
			    	
				}
			}
		}
		// save
		CountItems.updateMap(finalKeywords, countMap);
		
		System.out.println("Searching for : ");
		String keyW = "";
		for (String keys: finalKeywords) {
			System.out.print(keys + " ");
			keyW += keys + " ";
		}
		System.out.println("");	
		
		try {
			ArrayList<RestaurantItem> finalList = pageRank.rankPage(keyW.trim(), itemMap);			
			// convert to heap
	        PriorityQueue<RestaurantItem> maxheap = new PriorityQueue<RestaurantItem>((first, second) -> (int)(first.price * 100) - (int)(second.price * 100));
	        maxheap.addAll(finalList);
	        
			int key = 1;
			int count = 0;
			while (maxheap.size() > 0 && key == 1) {
				
				System.out.println(StringUtils.rightPad("Index " , 8) + StringUtils.rightPad("Restaurant", 40) + StringUtils.rightPad("Item Name", 65) + "Price");
		        for (int k = 0; k < Math.min(maxheap.size(), 10); k++) {
		        	count++;
		        	RestaurantItem item = maxheap.poll();
		     		System.out.println(StringUtils.rightPad(" " + (count), 8) + StringUtils.rightPad(item.restaurant, 40) + StringUtils.rightPad(item.itemName, 65) + "$ " + String.format("%02.2f",item.price));
		        }
		        if (Math.min(maxheap.size(), 10) < 10) {
		        	break;
		        }
				System.out.println("\nPress 1 to get next page, or 0 to exit.");
		        Scanner scan = new Scanner(System.in);
				key = scan.nextInt();
				
			}
			System.out.println("\nExit");
		} catch (Exception e) {
			System.out.println("\nError Page Rank: " + keyW + e);
		}
		
		System.out.println("");	
		return 1;
	}

	/*
	 * Method that handles the menu route to exit the application.
	 * return : state for the Menu loop
	 */
	private static int exitSystemApplication() {
		System.out.println("\nAre you sure you want to exit the application? (y/n) \n");
		Scanner scannerExit = new Scanner(System.in);
		String chExit = scannerExit.nextLine();
		if (!chExit.toLowerCase().equals("y")) {
			return 1;
		} else {
			System.out.println("\nApplication Closed.");
			return 0;
		}
	}
	
	/*
	 * Method that handles the menu route to get Top 10 search results from search history
	 * return : state for the Menu loop
	 */
	private static int getTop10SearchMenus() throws IOException {
		CountItems.showTopK(countMap, 10);
		System.out.println();
		mode = PageMode.MAIN_MENU;
		return 1;
	}
	
	/*
	 * Method that handles the menu route to get all search results from search history based
	 * on their frequency of search.
	 * return : state for the Menu loop
	 */
	private static int getAllSearchFreqMenus() throws IOException {
		CountItems.print(countMap);
		System.out.println();
		mode = PageMode.MAIN_MENU;
		return 1;
	}

}
