package spellcheck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import dictionary.Frequency;

public class SpellCheck {

	private static HashMap<String,Integer> possibleOptions; 
	public static void init() {
		possibleOptions = new HashMap<String,Integer>();
		for(String word : Frequency.getGlobalDictionary()) {
			possibleOptions.put(word,0);
		}
	}

	public static ArrayList<String> SpellCheckSuggesions(String strWord) 
	{
		ArrayList<String> lst = new ArrayList<String> ();
		ArrayList<Integer> lstDistance = new ArrayList<Integer>();
		lst.add("");
		lst.add("");
		lst.add("");
		lst.add("");
		lst.add("");
		lstDistance.add(1000);
		lstDistance.add(1000);
		lstDistance.add(1000);
		lstDistance.add(1000);
		lstDistance.add(1000);
		int distance = 0;
		
		Set<String> setString = possibleOptions.keySet();
		Iterator<String> itter = setString.iterator(); 
		
		for(int i = 0; i < setString.size(); i++) 
		{
			String dictWord = itter.next();
			//System.out.println(dictWord);
			distance = EditDistance.editDistance(strWord, dictWord);
			if(distance == 0)
				return new ArrayList<String>();
			else 
			{
				if(distance < lstDistance.get(4)) 
				{
					for(int j = 0; j < 5; j++) 
					{
						if(distance <= lstDistance.get(j)) 
						{
							lstDistance.add(j,distance);
							lstDistance.remove(5);
							
							lst.add(j,dictWord);
							lst.remove(5);
							break;
						}
					}
				}
			}
				
		}
		return lst;
	}
}
