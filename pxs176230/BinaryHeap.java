/**
 * @author Akhila Perabe (axp178830), Pooja Srinivasan (pxs176230), Shreeya Girish Degaonkar (sxd174830)
 * 
 *  Binary Heap implementation using arrays
 */
package pxs176230;


import java.util.NoSuchElementException;

public class BinaryHeap<T extends Comparable<? super T>> {
    Comparable[] pq;
    int size;

    /**
     * Constructor for building an empty priority queue using natural ordering of T
     * @param maxCapacity
     */
    public BinaryHeap(int maxCapacity) {
        pq = new Comparable[maxCapacity];
        size = 0;
    }

    /**
     * Add method
     * Resize pq when full
     * @param x
     * @return	Returns true always
     */
    public boolean add(T x){
    	if(size==pq.length) {
    		resize();
    	}
    	move(size,x);
    	percolateUp(size);
    	size++;
		return true;
    }

    /**
     * Adds x the priority queue
     * @param x
     * @return Returns true always
     */
    public boolean offer(T x) {
		return add(x);
    }
    
    /**
     * Remove top element from queue
     * Throws exception if pq is empty
     * @return
     * @throws NoSuchElementException
     */
    public T remove() throws NoSuchElementException {
        T result = poll();
        if(result == null) {
            throw new NoSuchElementException("Priority queue is empty");
        } else {
            return result;
        }
    }

    /**
     * Remove top element from queue if not empty
     * Returns null if pq is empty
     * @return
     */
    public T poll() {
    	if(size==0) {
    		return null;
    	}
    	
    	T min= (T)pq[0];
     	size--;
    	move(0,pq[size]);
    	percolateDown(0);
    	return min;
    }

    /**
     * Returns top element if not empty else null
     */
    public T peek() {
    	if(size==0) {
    		return null;
    	}
    	return (T) pq[0];
    }
    
    /**
     * Returns parent index of element at i
     * @param i
     * @return
     */
    int parent(int i) {
        return (i-1)/2;
    }

    /**
     * Return left child index of element at i
     * @param i
     * @return
     */
    int leftChild(int i) {
        return 2*i + 1;
    }

    /**
     * Checks if pq[index] violates heap order with parent
     * If violates moves it up the tree
     * @param index
     */
    void percolateUp(int index) {
		T x = (T) pq[index];
    	while(index>0 && (compare(x, pq[parent(index)])==-1)) {
    		move(index,pq[parent(index)]);
    		index=parent(index);
    	}
    	move(index,x);
    }

    /**
     * Checks if pq[index] violates heap order with children
     * If violates moves it to down the tree
     * @param i
     */
    void percolateDown(int i) {
		T x = (T) pq[i];
		int c = leftChild(i);
		while (c <= (size - 1)) {
			if ((c + 1) < size && (compare(pq[c], pq[c + 1]) == 1)) {
				c = c + 1;
			}
			if (compare(x, pq[c]) == 0 || compare(x, pq[c]) == -1) {
				break;
			}
			move(i, pq[c]);
			i = c;
			c = leftChild(i);
		}
		move(i, x);
    }

    /**
     * Move x to index dest
     * @param dest
     * @param x
     */
    void move(int dest, Comparable x) {
        pq[dest] = x;
    }

    /**
     * Compare a and b
     * @param a
     * @param b
     * @return
     */
    int compare(Comparable a, Comparable b) {
        return ((T) a).compareTo((T) b);
    }

    /**
     * Create a heap.
     * Precondition: none.
     */
    void buildHeap() {
        for(int i=parent(size-1); i>=0; i--) {
            percolateDown(i);
        }
    }

    /**
     * Returns true if empty
     * @return
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns number of elements in queue
     * @return
     */
    public int size() {
        return size;
    }

    /**
     * Resizes array to double the current size
     */
    void resize() {
    	int oldSize = pq.length;
    	Comparable[] oldArr = pq;
    	
    	pq = new Comparable[oldSize*2];
    	System.arraycopy(oldArr, 0, pq, 0, oldSize);
    }

    /**
     * Interface for Indexed Heap
     */
    public interface Index {
        public void putIndex(int index);
        public int getIndex();
    }

    /**
     * Class : Indexed Heap
     */
    public static class IndexedHeap<T extends Index & Comparable<? super T>> extends BinaryHeap<T> {
        
    	/**
    	 * Build a priority queue with a given array
    	 * @param capacity
    	 */
        IndexedHeap(int capacity) {
            super(capacity);
        }

        /**
         * Restores heap order property after the priority of x has decreased
         * @param x
         */
        void decreaseKey(T x) {
        	//Decrease x key
        	int i = x.getIndex();
        	percolateUp(i);
        }

        /**
         * Moves x to index i
         * Updates the index of the item x
         */
        @Override
        void move(int i, Comparable x) {
            super.move(i, x);
            T item = (T) x;
            item.putIndex(i);
        }
    }

    /**
     * Sample driver program
     * @param args
     */
    public static void main(String[] args) {
        Integer[] arr = {0,9,7,5,3,1,8,6,4,2};
        BinaryHeap<Integer> h = new BinaryHeap(arr.length);

        System.out.print("Before:");
        for(Integer x: arr) {
            h.offer(x);
            System.out.print(" " + x);
        }
        System.out.println();

        for(int i=0; i<arr.length; i++) {
            arr[i] = h.poll();
        }

        System.out.print("After :");
        for(Integer x: arr) {
            System.out.print(" " + x);
        }
        System.out.println();
    }
}
