package reverse_indexing;
import java.util.*;

import dictionary.Frequency;


public class ReverseIndex{
	private static WordsTrie trie;
	
	private static HashMap<String, ArrayList<WordData>> mapReverseIndex;

	
	public static void init() {
		
		mapReverseIndex =  new HashMap<String, ArrayList<WordData>>();
		
		Map <String,Map<String,Integer>> map = Frequency.getMap();
		
		Set<String> lstKeys = map.keySet();
		 Iterator<String> itter = lstKeys.iterator();
		
		for (int i = 0; i < lstKeys.size(); i++) 
		{
			String restaurant = itter.next();
			//System.out.println(restaurant);
			
			Map<String,Integer> map1 = map.get(restaurant);
			
			Set<String> lstKeys1 = map1.keySet();
			 Iterator<String> itter1 = lstKeys1.iterator();
			
			 
			for (int j = 0; j < lstKeys1.size(); j++) 
			{
				String word = itter1.next();
				if(mapReverseIndex.containsKey(word)) 
				{
					ArrayList<WordData> lst =  mapReverseIndex.get(word);
					lst.add(new WordData (restaurant, map1.get(word)));
				}else 
				{
					ArrayList<WordData> lst = new ArrayList<WordData>();
					lst.add(new WordData (restaurant, map1.get(word)));
					
					mapReverseIndex.put(word,lst);
				}
			}
		}
		
		trie = new WordsTrie();
		
		Set<String> lstKeys3 = mapReverseIndex.keySet();
		 Iterator<String> itter3 = lstKeys3.iterator();
		 for (int i = 0; i < lstKeys3.size(); i++) 
			{
				
			 	String word = itter3.next();
			 	//System.out.println( i + " word : "+ word + "    *****");
			 	ArrayList<WordData> lst =  mapReverseIndex.get(word);
//			 	for(int j = 0; j < lst.size(); j++) 
//			 	{
//			 		System.out.println( j + "     Restaurant : "+ (lst.get(j)).getRestaurant() + 
//			 				"    freq : "+ (lst.get(j)).getFrequency());
//			 	}
				
				trie.insert(word,lst);
			}
	}
	
	
	// should call main method to construct trie first.
	
	public static boolean Contains(String word) 
	{
		return trie.search(word);
	}
	public static ArrayList<WordData> SearchTrie(String str)
	{
	 	ArrayList<WordData> lst1 =  trie.searchAndGetIndexing(str);
//	 	for(int j = 0; j < lst1.size(); j++) 
//	 	{
//	 		System.out.println( j + "     Restaurant : "+ (lst1.get(j)).getRestaurant() + 
//	 				"    freq : "+ (lst1.get(j)).getFrequency());
//	 	}
		return lst1;
	}

}
