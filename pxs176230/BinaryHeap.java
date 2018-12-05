// Starter code for LP5

// Change to your netid
package pxs176230;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

public class BinaryHeap<T extends Comparable<? super T>> {
    Comparable[] pq;
    int size;
    T min;

    // Constructor for building an empty priority queue using natural ordering of T
    public BinaryHeap(int maxCapacity) {
        pq = new Comparable[maxCapacity];
        size = 0;
    }

    // add method: resize pq if needed
    public boolean add(T x) throws Exception{
    	if(size==pq.length) {
    		// call resize/
    		//return false;
    	}
    	move(size,x);
    	percolateUp(size);
    	size++;
		return true;
    }

    public boolean offer(T x) {
    	if(size==pq.length) {
    		return false;
    	}
    	move(size,x);
     	percolateUp(size);
    	size++;
    	
//		return add(x);
    	return true;
    }
    // throw exception if pq is empty
    public T remove() throws NoSuchElementException {
        T result = poll();
        if(result == null) {
            throw new NoSuchElementException("Priority queue is empty");
        } else {
            return result;
        }
    }

    // return null if pq is empty
    public T poll() {
    	if(size==0) {
    		return null;
    	}
    	min=(T) pq[0];
     	size--;
    	move(0,pq[size]);
    	percolateDown(0);
    	return min;
    }

    // return null if pq is empty
    public T peek() {
    	if(size==0) {
    		return null;
    	}
    	return (T) pq[0];
    }
    int parent(int i) {
        return (i-1)/2;
    }

    int leftChild(int i) {
        return 2*i + 1;
    }

    /** pq[index] may violate heap order with parent */
    void percolateUp(int index) {
	
	
	
		T x = (T) pq[index];
    	while(index>0 && (x.compareTo((T) pq[parent(index)])==-1)) {
    		move(index,pq[parent(index)]);
    		index=parent(index);
    	}
    	move(index,x);
	
	
	
	
	
    }

    /** pq[index] may violate heap order with children */
    void percolateDown(int i) {
	T x = (T) pq[i];
    int c= leftChild(i);
    while(c<=(size-1)) {
    	if((c+1)<size && (pq[c].compareTo(pq[c+1])==1)) {
    		c=c+1;
    	}
    	if(x.compareTo((T) pq[c])==0 || x.compareTo((T) pq[c])==-1) { break;}
    	move(i,pq[c]);
    	i=c;
    	c=leftChild(i);
    } }

    void move(int dest, Comparable x) {
        pq[dest] = x;
    }

    int compare(Comparable a, Comparable b) {
        return ((T) a).compareTo((T) b);
    }

    /** Create a heap.  Precondition: none. */
    void buildHeap() {
        for(int i=parent(size-1); i>=0; i--) {
            percolateDown(i);
        }
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return size;
    }

    // Resize array to double the current size
    void resize() {
    }

    public interface Index {
        public void putIndex(int index);
        public int getIndex();
    }

    public static class IndexedHeap<T extends Index & Comparable<? super T>> extends BinaryHeap<T> {
        /** Build a priority queue with a given array */
        IndexedHeap(int capacity) {
            super(capacity);
        }

        /** restore heap order property after the priority of x has decreased */
        void decreaseKey(T x) {
        }

        @Override
        void move(int i, Comparable x) {
            super.move(i, x);
        }
    }

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
