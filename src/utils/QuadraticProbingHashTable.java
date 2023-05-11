package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 
 *
 * QuadraticProbing HashTable
 * 
 * QuadraticProbingHashTable(int n) : n is the initial size  or default size of 101
 * 
 *  Public Operations :-
 *  insert(AnyType x)   -> boolean // Where x is the element to insert into the hashTable.
 *  remove(AnyType x)   -> boolean // Where x is the element to be removed.
 *  contains(AnyType x) -> boolean // Check if given element is present in the hashTable.
 *  size()              -> Integer // return the size of the hashTable.
 *  capacity()          -> Integer // return the internal table size.
 *  empty()                        // Logically empty hashTable.
 *  printFrequencyTable()          // Print the Frequency table for the HashTable and save to the path if required in CSV.
 */


/**
* Probing table implementation of hash tables.
* Note that all "matching" is based on the equals method.
* @author Mark Allen Weiss
*/
public class QuadraticProbingHashTable<AnyType> {
	
	private static final int DEFAULT_TABLE_SIZE = 101; // Default size
	
	private HashEntry<AnyType>[] array; // Array of elements
	private int occupied;				// The number occupied cells
	private int size;					// Current total size
	
	
	public QuadraticProbingHashTable() {
		this(DEFAULT_TABLE_SIZE);
	}
	
	public QuadraticProbingHashTable(int n) {
		allocateArray(n);
		doClear();
	}
	

	/**
	 * resize the HashTable array
	 * @param n : Integer : the new prime number that will be the size of array;
	 */
	@SuppressWarnings("unchecked")
	private void allocateArray(int n) {
		array = new HashEntry[nextPrime(n)];
	}
	
	/**
	 * Clear the HashTable array
	 * replace each value with null
	 */
	private void doClear() {
		occupied = 0;
		
		for (int i = 0; i < size; i++) {
			array[i] = null;
		}
	}
	
	/**
	 * DataType to store the hash Metadata
	 * @param <AnyType>
	 */
	private static class HashEntry<AnyType> {
		public AnyType element;
		public boolean isActive;
		public int frequency;
		
		public HashEntry(AnyType element, boolean isActive) {
			this(element, isActive, 1);
		}
		
		public HashEntry(AnyType element, boolean isActive, int frequency) {
			this.element   = element;
			this.isActive  = isActive;
			this.frequency = frequency;
		}
	}
	
	/**
	 * Method to find a prime number >= the value provided n
	 * @param n : Integer : the value from which the prime number should be > or =
	 * @return Integer : a prime number >= n 
	 */
	private static int nextPrime(int n) {
		if (n % 2 == 0) {
			n++;
		}
		for (; !isPrime(n); n+=2)
			;
		return n;
	}
	
	/**
	 * Method to check if a number is prime
	 * @param n : Integer : the number to check
	 * @return boolean: is the n prime or not
	 */
	private static boolean isPrime(int n) {
		if (n == 2 || n == 3)
			return true;
		if (n == 1 || n % 2 == 0)
			return false;
		for ( int i = 3; i * i <= n; i+= 2) {
			if (n % i == 0)
				return false;
		}
		return true;
	}
	
	/**
	 * Method to hash the values.
	 * @param x : element to hash
	 * @return Integer : the hash value
	 */
	private int hash(AnyType x) {
		int hashValue = x.hashCode();
		
		hashValue %= array.length;
		
		if (hashValue < 0)
			hashValue += array.length;

		return hashValue;
	}
	
	/**
	 * Method that perform quadratic probing resolution.
	 * @param x : the element to search for.
	 * @return Integer : the position where the search terminates.
	 */
	private int findPos(AnyType x) {
		int offset = 1;
		int currentPos = hash(x);
		
		while ( array[currentPos] != null && !array[currentPos].element.equals(x)) {
			currentPos += offset;
			offset += 2;
			if (currentPos >= array.length)
				currentPos -= array.length;
		}
		return currentPos;
	}
	
	/**
	 * Method to check if element exists and is active.
	 * @param currentPos : Integer : the position to check
	 * @return boolean : is Active and exists
	 */
	private boolean isActive(int currentPos) {
		return array[currentPos] != null && array[currentPos].isActive;
	}
	
	/**
	 * Method to rehash the old hashTable into a bigger size hashTable
	 */
	private void reHash( ) {
		HashEntry<AnyType>[] oldArray = array;
		
		allocateArray(2 * oldArray.length);
		occupied = 0;
		size = 0;
		for (HashEntry<AnyType> entry: oldArray) {
			if (entry != null && entry.isActive)
				insert(entry.element, entry.frequency);
		}
	}
	
	/**
	 * Insert into the hashTable if new
	 * @param x : element to insert in hashTable
	 * @return : boolean : operation success
	 */
	public boolean insert(AnyType x) {
		int currentPos = findPos(x);
		if (isActive(currentPos)) {
			array[currentPos].frequency++;
			//System.out.println("present Already : " + x + " : " + array[currentPos].frequency);
			return false;
		}
		
		array[currentPos] = new HashEntry<>( x, true);
		size++;
		
		if (++occupied > array.length/2)
			reHash(); // To maintain the load factor below (0.5)
		
		return true;
	}
	
	/**
	 * This method is needed to fix the frequency rest while reHash
	 * @param x : element to insert in hashTable
	 * @param frequency : Integer : the frequency from the previous array
	 * @return : boolean : operation success
	 */
	private boolean insert(AnyType x, int frequency) {
		int currentPos = findPos(x);
		if (isActive(currentPos)) {
			array[currentPos].frequency++;
			return false;
		}
		array[currentPos] = new HashEntry<>(x, true, frequency);
		size++;
		
		if (++occupied > array.length/2)
			reHash(); // To maintain the load factor below (0.5)
		
		return true;
	}
	
	/**
	 * Remove from the hashTable
	 * @param x : element to remove
	 * @return : boolean : operation success 
	 */
	public boolean remove(AnyType x) {
		int currentPos = findPos(x);
		if (isActive(currentPos)) {
			array[currentPos].isActive = false;
			size--;
			return true;
		} else 
			return false;
	}
	
	/**
	 * Method to find element in hashTable.
	 * @param x : element to check for
	 * @return boolean : is present and active
	 */
	public boolean contains(AnyType x) {
		int currentPos = findPos(x);
		return isActive(currentPos);
	}
	
	/**
	 * Method to return current size
	 * @return : Integer : size
	 */
	public int size() { return this.size; }
	
	/**
	 * Method to return total internal Table length
	 * @return : Integer :  size
	 */
	public int capacity() { return array.length;}
	
	/**
	 * Method to logically empty hashTable
	 */
	public void empty() { doClear(); }
	
	/**
	 * Method to print the frequency table for the hashTable
	 * save to path if required
	 */
	public void printFrequencyTable(String path) {
		if (path.equals("")) {
			for (int  i = 0 ; i < array.length; i++) {
				HashEntry<AnyType> element = array[i];
				if (element == null)
					continue;
				System.out.println(String.format("%-30s:   %s", element.element, element.frequency));
			}
			return;
		}
		File file = new File(path);
		try {
			if (file.createNewFile()) {
				System.out.println("Created Frequency Table file : " + path);
			} else {
				System.out.println("File present : " + path);
			}
			FileWriter fileWriter = new FileWriter(path);
			fileWriter.append("word,frequency\n");
			for (int  i = 0 ; i < array.length; i++) {
				HashEntry<AnyType> element = array[i];
				if (element == null)
					continue;
				fileWriter.append(element.element + "," + element.frequency + "\n");
				System.out.println(String.format("%-30s:   %s", element.element, element.frequency));
			}
			fileWriter.close();
			return;
		} catch (IOException e) {
			System.out.println("File Saving Error : " + e.getMessage());
		}
	}
	
}