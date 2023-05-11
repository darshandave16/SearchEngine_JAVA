package utils;

import java.util.ArrayList;
import java.util.List;

import model.RestaurantItem;


/**
 * 
 *
 * MinHeap Tree
 * 
 * MinHeap(int maxSize) : maxSize is the maximum size allocated to the tree.
 * 
 *  Public Operations :-
 *  insert(Data element) // Insert element to the heap and adjust the heap as per MinHeap
 *  remove()   -> Data   // Remove the minimum element from the heap and returns it.
 */


public class MaxHeap {
	
	private List<RestaurantItem> heap;
	public int size;
	private int maxSize;
	
	private static int TOP = 0;
	
	public MaxHeap(int maxSize) {
		this.maxSize = maxSize;
		this.size = 0;
		heap = new ArrayList<>();
	}
	
	/**
	 * Helper function for heap parent node.
	 */
	private int parent(int index) {return (index - 1)/2; }
	
	/**
	 * Helper function for heap Left Child node.
	 */
	private int left(int index) {return (2 * index) + 1; }
	
	/**
	 * Helper function for heap Right Child node.
	 */
	private int right(int index) {return (2 * index)+ 2; }
	
	/**
	 * Helper function to check if the current node is Leaf node.
	 */
	private boolean isLeaf(int index) { return (index > (size/2) && index <= size); }
	
	/**
	 * Helper function that helps to swap elements 
	 */
	private void swap(int index1, int index2) {
		RestaurantItem temp = heap.get(index1);
		heap.set(index1, heap.get(index2));
		heap.set(index2, temp);
	}
	
	/**
	 * Insert function helps to insert element to Heap and adjust it to MinHeap rules.
	 * @param element : Data : the Object to be added to the heap
	 */
	public void insert(RestaurantItem element) {
		if (size >= maxSize) {
			System.out.println("Max size exceeded");
			return;
		}
		
		heap.add(element);
		
		int current = size;
		
		while (heap.get(current).price > heap.get(parent(current)).price) {
			swap(current, parent(current));
			current = parent(current);
		}
		size++;
	}
	
	/**
	 * Method helps to remove minimum element from the heap
	 * @return : Data : the Data object removed from the heap, which have the min score value.
	 */
	public RestaurantItem remove() {
		RestaurantItem removed = heap.get(TOP);
		heap.set(TOP, heap.get(--size));
		heapify(TOP);
		return removed;
	}
	
	/**
	 * Helper function to adjust the current heap to MinHeap rules
	 */
	private void heapify(int index) {
		if (isLeaf(index)) {
			return;
		}	
		
		try {
			if (heap.get(index).price < heap.get(left(index)).price || heap.get(index).price < heap.get(right(index)).price) {
				if (heap.get(left(index)).price > heap.get(right(index)).price) {
					swap(index, left(index));
					heapify(left(index));
				}
			} else {
				swap(index, right(index));
				heapify(right(index));
			}
		} catch( Exception e) {
			
		}
	}
	
}