/**
 * @author Akhila Perabe (axp178830), Pooja Srinivasan (pxs176230), Shreeya Girish Degaonkar (sxd174830)
 * 
 *  Minimum Spanning Tree implementation using Prim1, Prim2, Prim3, Kruskal algorithms
 */

package pxs176230;

import pxs176230.BinaryHeap.Index;
import pxs176230.BinaryHeap.IndexedHeap;
import rbk.Graph;
import rbk.Graph.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

public class MST extends GraphAlgorithm<MST.MSTVertex> {
    String algorithm;	// Algorithm used to get MST
    public long wmst;	// Weight of the MST
    List<Edge> mst;		// Edges in the resulting MST

    /**
     * Constructor
     * @param g
     */
    MST(Graph g) {
        super(g, new MSTVertex((Vertex) null));
    }

    public static class MSTVertex implements Index, Comparable<MSTVertex>, Factory {
        boolean seen;
        MSTVertex parent;
        long d;
        Vertex ref;
        int index;		// Used only by Prim3
        long rank;		// Used only by Kruskal
        Edge mstEdge;	// Used only by Prim2, Prim3

        /**
         * Constructor
         * @param u
         */
        MSTVertex(Vertex u) {
            seen = false;
            parent = null;
            d = Long.MAX_VALUE;
            ref = u;
            rank = 0;
            mstEdge = null;
        }

        /**
         * Constructor used only Prim2
         * @param u
         */
        MSTVertex(MSTVertex u) {
           this.seen = u.seen;
           this.parent = u.parent;
           this.d = u.d;
           this.ref = u.ref;
           this.rank = u.rank;
           this.mstEdge = u.mstEdge;
        }

        /**
         * Make the factory vertex for the given vertex
         */
        public MSTVertex make(Vertex u) { return new MSTVertex(u); }

        /**
         * Set the index for the vertex
         * Used by Prim3 only
         */
        public void putIndex(int index) { 
        	this.index = index;
        }

        /**
         * Get index for the vertex
         * Used by Prim3 only
         */
        public int getIndex() { return index; }

        /**
         * Compares the vertex based on distance (d)
         */
        public int compareTo(MSTVertex other) {
            if(this.d < other.d) return -1;
            else if(this.d > other.d) return 1;
            return 0;
        }
        
        /**
         * Finds the root ancestor
         * Used only by Kruskal
         * @return
         */
        MSTVertex find() {
        	if(this != parent) {
        		parent = parent.find();
        	}
        	return parent;
        }
        
        /**
         * Merges the tree rooted at rv to the current vertex
         * Used only by Kruskal
         * @param rv
         */
        void union(MSTVertex rv) {
        	if (this.rank > rv.rank) {
        		rv.parent = this;
        	}
        	else if (this.rank < rv.rank) {
        		this.parent = rv;
        	}
        	else {
        		this.rank ++ ;
        		rv.parent = this;
        	}
        }
    }

    /**
     * Builds MST using Kruskal algorithm
     * @return
     */
    public long kruskal() {
        algorithm = "Kruskal";
        Edge[] edgeArray = g.getEdgeArray();
        mst = new LinkedList<>();
        wmst = 0;
        
        // Initialize parent for each vertex
        for(Vertex u : g) {
        	MSTVertex mv = get(u);
        	mv.parent = mv; 
        }
        
        Arrays.sort(edgeArray);
        for (Edge e : edgeArray) {
        	MSTVertex ru = get(e.fromVertex()).find();
        	MSTVertex rv = get(e.toVertex()).find();
        	
        	// If the vertices do not have the same ancestor
        	if(ru != rv) {
        		wmst += e.getWeight();
        		mst.add(e);
        		ru.union(rv);
        	}
        }
        return wmst;
    }

    /**
     * Builds MST using Prim3 algorithm
     * @param s
     * @return
     */
    public long prim3(Vertex s) {
        algorithm = "indexed heaps";
        mst = new LinkedList<>();
        wmst = 0;
        IndexedHeap<MSTVertex> q = new IndexedHeap<>(g.size());
        
        // Initialize
        get(s).d = 0;
        for(Vertex u : g) {
        	q.add(get(u));
        }
        
        while(!q.isEmpty()) {
        	MSTVertex u = q.remove();
        	
        	// Add u to MST
        	u.seen = true;
        	wmst += u.d;
        	if (u.mstEdge != null) mst.add(u.mstEdge);
        	
        	// For each edge (u,v)
        	for(Edge e: g.incident(u.ref)) {
        		MSTVertex v = get(e.otherEnd(u.ref));
        		
        		// If v has minimal distance from u then update v
        		if(!v.seen && e.getWeight() < v.d) {
        			v.d = e.getWeight();
        			v.parent = u;
        			v.mstEdge = e;
        			
        			// Update the priority of v
        			// in the priority queue
        			q.decreaseKey(v);
        		}
        	}
        }
        return wmst;
    }

    /**
     * Builds MST using Prim2 algorithm
     * @param s
     * @return
     */
    public long prim2(Vertex s) {
        algorithm = "PriorityQueue<Vertex>";
        mst = new LinkedList<>();
        wmst = 0;
        PriorityQueue<MSTVertex> q = new PriorityQueue<>();
        
        // Initialize
        get(s).d = 0;
        q.add(new MSTVertex(get(s)));
        
        while(!q.isEmpty()){
        	
            MSTVertex u = q.poll();
            u = get(u.ref);
            if(!u.seen){
            	
            	// Add u to the MST
                u.seen = true;
                wmst += u.d;
                if (u.mstEdge != null) mst.add(u.mstEdge);
                
                // For each edge (u,v)
                for(Edge e : g.outEdges(u.ref)){
                    MSTVertex v = get(e.otherEnd(u.ref));

                    // If v has minimal distance from u then update v
                    if(!v.seen && (e.getWeight() < v.d)){
                          v.d = e.getWeight();
                          v.parent = u;
                          v.mstEdge = e;
                          q.add(new MSTVertex(v));
                    }
                }
            }
        }
        return wmst;
    }

    /**
     * Builds MST using Prim1 algorithm
     * @param s
     * @return
     */
    public long prim1(Vertex s) {
        algorithm = "PriorityQueue<Edge>";
        mst = new LinkedList<>();
        wmst = 0;
        PriorityQueue<Edge> q = new PriorityQueue<>();
        
        // Initialize
        get(s).seen = true;
        for(Edge e : g.outEdges(s)){
            q.add(e);
        }
        
        while(!q.isEmpty()){
            Edge e = q.remove();
            MSTVertex u = get(e.fromVertex());
            MSTVertex v = get(e.toVertex());
            
            // Let u be the seen vertex and v be not seen
            if(!u.seen){
                MSTVertex temp = u;
                u = v;
                v = temp;
            }
            
            if(v.seen) continue;
            
            // Add the edge(u,v) to MST
            v.seen = true;
            v.parent = u;
            wmst += e.getWeight();
            mst.add(e);
            
            // For all edge from v, add them to queue
            for(Edge e2 : g.outEdges(v.ref)){
                if(!get(e2.otherEnd(v.ref)).seen) q.add(e2);
            }
        }
        return wmst;
    }

    /**
     * Get MST for the given graph and source
     * @param g
     * @param s
     * @param choice
     * @return
     */
    public static MST mst(Graph g, Vertex s, int choice) {
        MST m = new MST(g);
        switch(choice) {
            case 0:
                m.kruskal();
                break;
            case 1:
                m.prim1(s);
                break;
            case 2:
                m.prim2(s);
                break;
            default:
                m.prim3(s);
                break;
        }
        return m;
    }

    /**
     * Sample Driver program
     * @param args
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
		Scanner in;
		int choice = 0; // Kruskal
		if (args.length == 0 || args[0].equals("-")) {
			in = new Scanner(System.in);
		} else {
			File inputFile = new File(args[0]);
			in = new Scanner(inputFile);
		}

		if (args.length > 1) {
			choice = Integer.parseInt(args[1]);
		}

		// Read graph from input
		Graph g = Graph.readGraph(in);

		Vertex s = g.getVertex(1);

		Timer timer = new Timer();
		MST m = mst(g, s, choice);
		System.out.println(m.algorithm + "\n" + m.wmst);
		System.out.println(timer.end());
    }
}