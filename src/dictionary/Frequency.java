package dictionary;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.*;

/*
 * Creates a Global dictionary and map for words from webpages for spell check
 */
 public class Frequency {
	private static Set<String> dict;  
	private static Map <String,Map<String,Integer>> hm;
	public static Set<String> getGlobalDictionary() {
		return dict;
	}
	public static Map <String,Map<String,Integer>> getMap() {
		return hm;
	}
	
	public static void createGlobalDictionaryAndMap() {
		String[] pathnames;
		String s;
		String s1;
		String s2;
		String newline = System.lineSeparator();
		dict = new HashSet<>();
		File folder = new File(System.getProperty("user.dir") + "/menus/");
		pathnames = folder.list();
		hm = new HashMap<String, Map<String,Integer>>();
		for (String pathname : pathnames) {
			if(!pathname.contains(".txt")) {
                continue;
            }
			try {
			      File myObj = new File(System.getProperty("user.dir") + "/menus/" + pathname);
			      Scanner myReader = new Scanner(myObj);
			      while (myReader.hasNextLine()) {
			        String data = myReader.nextLine();
			        s = data;
			        s1 = s.toLowerCase();
			        s2 = s1.replaceAll("[^A-Za-z]+", " ");
			        count_freq(s2);
			 
//			        System.out.println(pathname);
			        hm.put(pathname.substring(0, pathname.lastIndexOf(".txt")), count_freq(s2));
			        
			      }
			      myReader.close();
			    } catch (FileNotFoundException e) {
			      System.out.println("An error occurred.");
			      e.printStackTrace();
			    }
		}
	}
	// returns a map comprising with dish search frequency
	static Map<String, Integer> count_freq(String str)
    {
        Map<String,Integer> mp=new HashMap<>();
        Map<String, Integer> mapreq = new HashMap<>();
 
        // Splitting to find the word
        String arr[]=str.split(" ");
 
        // Loop to iterate over the words
        for(int i=0;i<arr.length;i++)
        {
        	dict.add(arr[i]);
            // Condition to check if the
            // array element is present
            // the hash-map
            if(mp.containsKey(arr[i]))
            {
                mp.put(arr[i], mp.get(arr[i])+1);
            }
            else
            {
                mp.put(arr[i],1);
            }
        }
        
        // Loop to iterate over the
        // elements of the map
        for(Map.Entry<String,Integer> entry:
                    mp.entrySet())
        {
            mapreq.put(entry.getKey(),entry.getValue());
//            System.out.println(mapreq);
        }
        return mapreq;
    }
	public static void main(String[] args) {
		createGlobalDictionaryAndMap();
		for(Map.Entry<String,Map<String, Integer>> entry:
            hm.entrySet())
{
    System.out.println(entry.getKey() + entry.getValue());
//    System.out.println(mapreq);
}
		System.out.println(dict);
	}
 

}
