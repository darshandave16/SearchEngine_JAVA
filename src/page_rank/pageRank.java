package page_rank;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import model.RestaurantItem;
import restaurant_items.RestaurantItems;
import reverse_indexing.ReverseIndex;
import reverse_indexing.WordData;
import spellcheck.EditDistance;

class StockComparator implements Comparator<RestaurantItem> {
  
    public int compare(RestaurantItem s1, RestaurantItem s2)
    {
        if (s1.price == s2.price)
            return 0;
        else if (s1.price > s2.price)
            return 1;
        else
            return -1;
    }
}
public class pageRank {
    
	public static ArrayList<RestaurantItem> rankPage(String keyword, Map<String, Set<RestaurantItem>> map1) throws Exception {		
        keyword = keyword.toLowerCase();
        String[] keywords = keyword.split(" ");
        Map<String,Integer> map = new HashMap<String, Integer>();
        for (String key : keywords){
            ArrayList<WordData> list_of_restaurents =  ReverseIndex.SearchTrie(key);
            for(int i=0; i<list_of_restaurents.size();i++){
                WordData obj = list_of_restaurents.get(i);
                if(map.containsKey(obj.getRestaurant())){
                    map.put(obj.getRestaurant(),map.get(obj.getRestaurant()) + obj.getFrequency());
                }
                else{
                    map.put(obj.getRestaurant(), obj.getFrequency());
                }
            }   
        }
        Collection<Integer> lst = map.values();
        Iterator<Integer> itr = lst.iterator();
        List<Map.Entry<String, Integer> > list = new LinkedList<Map.Entry<String, Integer> >(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2)
            {
                return -1*(o1.getValue()).compareTo(o2.getValue());
            }
        });

        // printing first 10 ranked files
        ArrayList<RestaurantItem> finalList = new ArrayList<RestaurantItem>();
        ArrayList<RestaurantItem> temp = new ArrayList<RestaurantItem>();
        Map<RestaurantItem, Integer> tempMap = new HashMap<>();
        
        int n = list.size();
        
        
        for (int k = 0; k < Math.min(n, 20); k++) {
            if (list.get(k) != null && list.get(k).getKey() != null) {
                //System.out.println((k + 1) + ": " + list.get(k).getKey());
                int len = 5;
                tempMap = topDeals(keyword, list.get(k).getKey(),map1);
                
                Map<RestaurantItem, Integer> topTen = tempMap.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(10)
                        .collect(Collectors.toMap(
                           Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
                
                
                
                Iterator<RestaurantItem> iter = topTen.keySet().iterator();
                int count_c = 0;
                while (iter.hasNext()) {
                	finalList.add(iter.next());
                	count_c++;
                	if (count_c == 3) {
                		break;
                	}
                }
                
            }

        }
        Collections.sort(finalList, new StockComparator());
         return finalList;
	}
    
    public static Map<RestaurantItem, Integer> topDeals(String keyword, String rName, Map<String, Set<RestaurantItem>> map1) throws Exception{
        Set<RestaurantItem> setItem = map1.get(rName);
        Map<RestaurantItem, Integer> hMap = new HashMap<>();
        Iterator<RestaurantItem> itr = setItem.iterator();
        
        ArrayList<RestaurantItem> lstItems = new  ArrayList<RestaurantItem>();
        for(int i=0;i<setItem.size();i++){
            RestaurantItem item = itr.next();
            
            String[] itemNameKeyWord = item.itemName.toLowerCase().split(" ");
            String[] keys = keyword.split(" ");
            Set<String> keysSet = new HashSet<>(Arrays.asList(keys));
            Set<String> itemset = new HashSet<>(Arrays.asList(itemNameKeyWord));
            //System.out.println(item.itemName + " " +keyword);
            int score = 0;
            for (String key : itemset){
                for (String k : keysSet) {
                	 if(EditDistance.editDistance(k, key)<=1){
                         lstItems.add(item);
                         score++;
                     } 
                }
            }
            if(score >= keys.length) {
            	hMap.put(item, score);
            }
            
        }
        return hMap;
    }

}
