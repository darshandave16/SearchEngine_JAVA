package countItems;


import java.io.BufferedWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

//Calculating the frequency from existing saved file and updating the map and file after each search
public class CountItems {
	// To read the Searched words map from the file
	// returns a map consisting of the word and the number of times it has been searched.
	public static Map<String, Integer> readMapFromFile() {
		Map<String, Integer> map = new HashMap<>();
		try {
	      File myObj = new File(System.getProperty("user.dir")+"/resources/countOfWords.csv");
	      Scanner myReader = new Scanner(myObj);
	      while (myReader.hasNextLine()) {
	        String data = myReader.nextLine();
	        int  val, index;
			String val1;
	        if(data.contains(",")) {
	        	index = data.indexOf(",");
	            val = Integer.parseInt(data.substring(index+1, data.length()));// number of time dish has been searched
            	val1 = data.substring(0,index);// name of the dish
				map.put(val1, val);
	        }
	        	
	      }
	      myReader.close();
	    } catch (IOException e) {
	      System.out.println("An error occurred while reading map from file.");
	      e.printStackTrace();
	    }
		return map;
	}
	// To update the map
    //returns the new count and appends if word has not been previously searched

	public static void updateMap(Set<String> words, Map<String, Integer>m1) {
		for(String input : words) {
			if(m1.containsKey(input)) {
				m1.put(input, m1.get(input) + 1);
			} else {
				m1.put(input, 1);
			}
		}
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(System.getProperty("user.dir")+"/resources/countOfWords.csv"));
			for (Map.Entry<String, Integer> entry : m1.entrySet()) {
				writer.write(entry.getKey()+","+entry.getValue()+"\n");
			}
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		
		
	}
	 // Sorts the map based on frequency of the word
	public static Map<String,Integer> sortByFrequency(Map<String, Integer> m10){
    	List<Map.Entry<String, Integer> > list = new LinkedList<Map.Entry<String, Integer> >(m10.entrySet());
    	Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2)
            {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });
    	
    	HashMap<String, Integer> temp = new HashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
        
    }
	//Prints all the searched words from highest to lowest frequency
	public static void print(Map<String, Integer> m1) {
		System.out.println("Search Frequency for all the words in descending order:");
		System.out.println(StringUtils.rightPad("Word ", 20)+ ": Frequency");
		Stream<Map.Entry<String, Integer>> sortedMap = sortByFrequency(m1).entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()));
		sortedMap.forEach(e -> System.out.println(StringUtils.rightPad(e.getKey(), 20)+ ": " + StringUtils.rightPad(" " + e.getValue(), 15)));
	}
	 // Returns the "k" most searched words
	public static void showTopK(Map<String, Integer> m1, int k) {
		List<Q1> st = new ArrayList<>();
		PriorityQueue<Q1> queue = new PriorityQueue<Q1>(new Q1());
		for(Map.Entry<String, Integer> e : m1.entrySet()) {
			if(queue.size() > 0) {
			}
			Q1 q = new Q1(e.getKey(), e.getValue());
			if(queue.size() < k) {
				queue.add(q);
			} else if(queue.peek().score < q.score) {
				queue.poll();
				queue.add(q);
			}
		}
		System.out.println("\nTop " + k + " most searched words:");
		System.out.println(StringUtils.rightPad("Word ", 20)+ ": Frequency");
		while(!queue.isEmpty()) {
			st.add(queue.poll());
			//queue.poll();
		}
		int n = st.size();
		for(int i = n - 1 ; i >= 0 ; i--) {
			System.out.println(StringUtils.rightPad(st.get(i).word, 20)+ ": " + StringUtils.rightPad(" " + st.get(i).score, 15));
		}
		
	}
	
	
	public static void main(String[] args) throws IOException {
	    
		Map<String, Integer> m1 = readMapFromFile();
		System.out.println(m1);
		Set<String> set = new HashSet<>();
//		set.add("burger");
		set.add("pizza");
//		
//		updateMap(set, m1);
//		print(m1);
//		set.remove("pizza");
//		
		updateMap(set, m1);
		print(m1);
		showTopK(m1, 1); // top k
		
	}
}

class Q1 implements Comparator<Q1> {
	String word;
	int score;
	public Q1() {
		
	}
	public Q1(String w, int s) {
		word = w;
		score = s;
	}
	public int compare(Q1 s1, Q1 s2) {
		return s1.score - s2.score;
	}
}


