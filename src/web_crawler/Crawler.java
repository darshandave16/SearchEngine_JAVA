package web_crawler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


/**
* Crawler class is used to get page source code after it is rendered by the selenium library.
* Crawler can save these files in from of HTML or in text based on the parameter passed.
*/

public class Crawler {
	
	private String pageSource; 
	private String title = "";
	private static String pathDir = System.getProperty("user.dir") + "/menus/";
	
	/**
	 * Get Title of the page by splitting it
	 * @param var actual title of page
	 * @return the new shorter title
	 */
	private String getPageTitle(String var) {
		System.out.println(var);
		return var.split(" ")[0];
	}
	
	/**
	 * Saves the page source into the local storage
	 * @param fName : name of the file to be saved on local
	 * @param raw : save it as text page source or html content
	 * @throws IOException : if unable to store
	 */
	public void saveToFile(String fName, boolean raw) throws IOException {
		String text = pageSource;
		if (!raw) {
			Document doc = Jsoup.parse(pageSource);
			text = doc.body().text();
			System.out.println(title);
			System.out.println(text);
		} else {
			fName = "/html/" + fName;
		}
		
		if (fName == "" && title == "") {
			throw new IOException("Title is missing, plz provide in the function arguments.");
		} else if (fName != "") {
		} else {
			fName = title;
		}
		BufferedWriter writer = new BufferedWriter(new FileWriter(pathDir + fName + ".txt"));
	    writer.write(text);
	    writer.close();
	}
	
	/**
	 * Saves the page source into the local storage
	 * @param fName : name of the file to be saved on local
	 */
	public void saveToFile(String fName) throws IOException {
		saveToFile(fName, false);
	}
	
	/**
	 * Saves the page source into the local storage with default file name
	 */
	public void saveToFile() throws IOException {
		saveToFile("", false);
	}
	
	/**
	 * Get the parsed page source
	 * @param url : the url of the page to be crawled
	 * @return : the parsed data
	 */
	public String getParsedSource(String url) {
		Document d = getSource(url);
		return d.body().text();
	}
	
	/**
	 * Get the html page source
	 * @param url : the url of the page to be crawled
	 * @return : the parsed data
	 */
	public Document getSource(String url) {
		return getSource(url, 0);
	}
	
	/**
	 * This method runs the selenium driver and then render 
	 * @param url : the url of the page to be crawled
	 * @param wait : the time to delay the web page rendering
	 * @return The Jsoup Document that contains the parsed data
	 */
	public Document getSource(String url, int wait) {
		String driverPath = "/opt/homebrew/bin/chromedriver";
		String binaryPath = "/Applications/Brave Browser.app/Contents/MacOS/Brave Browser";
		Document doc = new Document("Error");
		try {
			System.setProperty("webdriver.chrome.driver", driverPath);
			System.setProperty("chrome.binary", binaryPath);
			ChromeOptions options = new ChromeOptions();
			options.setBinary(binaryPath);
			WebDriver driver = new ChromeDriver(options);
			
			// GET Request
			driver.get(url);
			
			// Scroll to the bottom of the page
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
			Thread.sleep(wait);
			
			pageSource = driver.getPageSource();
			title = getPageTitle(driver.getTitle());
			
			driver.close();
			
			doc = Jsoup.parse(pageSource);
		} catch (Exception e) {
			System.out.println("Error : " + e.getMessage());
			return doc;
		}
		return doc;
	}
}
