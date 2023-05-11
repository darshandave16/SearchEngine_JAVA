package word_completion;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import dictionary.Frequency;

/*
 * Trie: Creates a trie, inserts words in trie and gives auto-complete recommendations
 */
public class AutoCompleteTrie {
    Node root;
    
    /*
     * Default constructor to initialsie the trie
     */
    public AutoCompleteTrie() {
	    root = new Node();
	    root.value = '/';
	    root.end = false;
	    root.children = new Node[26];
	    root.cur = null;
	    for(int i = 0 ; i < 26 ; i++) {
	        root.children[i] = null;
	    }
    }
    
    /*
     * adds a word to the trie
     */
    public void insert(String word) {
        Node temp = root;
        int n = word.length();
        for(int i = 0 ; i < n ; i++) {
            char x = word.charAt(i);
            if(temp.children[x - 'a'] == null) {
                Node node = new Node();
                node.value = x;
                node.children = new Node[26];
                node.end = false;
                node.cur = null;
                for(int j = 0 ; j < 26 ; j++) {
                    node.children[j] = null;
                }
                temp.children[x-'a'] = node;
                temp = node;
            } else {
                temp = temp.children[x - 'a'];
            }
        }
        temp.cur = word;
        temp.end = true;
    }
    
    /*
     * returns true if the trie contains the given word
     */
    public boolean search(String word) {
        Node temp = root;
        int n = word.length();
        for(int i = 0 ; i < n ; i++) {
            char x = word.charAt(i);
            if(temp.children[x - 'a'] == null) {
                return false;
            } else {
                temp = temp.children[x - 'a'];
            }
        }
        if(temp.end == true) {
            return true;
        } else {
            return false;
        }
    }
    
    /*
     * returns true if the trie contains words with the given prefix
     */
    public boolean hasPrefix(String prefix) {
        Node temp = root;
        int n = prefix.length();
        for(int i = 0 ; i < n ; i++) {
            char x = prefix.charAt(i);
            if(temp.children[x - 'a'] == null) {
                return false;
            } else {
                temp = temp.children[x - 'a'];
            }
        }
        return true;
    }
    
    /*
     * returns set of words with given prefix.
     * This method can be used for auto complete suggestions
     */
    public Set<String> startsWith(String prefix) {
    	Set<String> ans = new HashSet<>();
    	Node temp = root;
        int n = prefix.length();
        for(int i = 0 ; i < n ; i++) {
            char x = prefix.charAt(i);
            if(temp.children[x - 'a'] == null) {
                return ans;
            } else {
                temp = temp.children[x - 'a'];
            }
        }
        
	    Queue<Node> q = new LinkedList<>();
	    q.add(temp);
	    while(!q.isEmpty()) {
	    	Node cc = q.remove();
	    	if(cc.end) {
	    		ans.add(cc.cur);
	    	}
	    	for(int i = 0 ; i < 26 ; i++) {
	    		if(cc.children[i] != null) {
	    			q.add(cc.children[i]);
	    		}
	    	}
	    }
	    return ans;
    }
    
    /*
     * main(String[] args)
     * to test out Trie methods and to play around with the Trie
     */
    public static void main(String[] args) {
    	AutoCompleteTrie trie = new AutoCompleteTrie();
    	Frequency.createGlobalDictionaryAndMap();
    	Set<String> dictionary = Frequency.getGlobalDictionary();
    	for(String word : dictionary) {
    		trie.insert(word);
    	}
    	
    	System.out.println(trie.startsWith("burg"));
    	
    	System.out.println(trie.startsWith("egg"));
    }
	    
}

/*
 * Class Node which is contained inside Trie
 */
class Node {
    Node[] children;
    char value;
    boolean end;
    String cur;
}