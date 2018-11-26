// Starter code for LP5

package pxs176230;

import pxs176230.BinaryHeap.Index;
import pxs176230.BinaryHeap.IndexedHeap;
import rbk.Graph;
import rbk.Graph.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

public class MST extends GraphAlgorithm<MST.MSTVertex> {
    String algorithm;
    public long wmst;
    List<Edge> mst;

    MST(Graph g) {
        super(g, new MSTVertex((Vertex) null));
    }

    public static class MSTVertex implements Index, Comparable<MSTVertex>, Factory {
        boolean seen;
        Vertex parent;
        long d;
        Vertex ref;

        MSTVertex(Vertex u) {
            seen = false;
            parent = null;
            d = Long.MAX_VALUE;
            ref = u;
        }

        MSTVertex(MSTVertex u) {  // for prim2
           this.seen = u.seen;
           this.parent = u.parent;
           this.d = u.d;
           this.ref = u.ref;
        }

        public MSTVertex make(Vertex u) { return new MSTVertex(u); }

        public void putIndex(int index) { }

        public int getIndex() { return 0; }

        public int compareTo(MSTVertex other) {
            if(this.d < other.d) return -1;
            else if(this.d > other.d) return 1;
            return 0;
        }
    }

    public long kruskal() {
        algorithm = "Kruskal";
        Edge[] edgeArray = g.getEdgeArray();
        mst = new LinkedList<>();
        wmst = 0;
        return wmst;
    }

    public long prim3(Vertex s) {
        algorithm = "indexed heaps";
        mst = new LinkedList<>();
        wmst = 0;
        IndexedHeap<MSTVertex> q = new IndexedHeap<>(g.size());
        return wmst;
    }

    public long prim2(Vertex s) {
        algorithm = "PriorityQueue<Vertex>";
        mst = new LinkedList<>();
        wmst = 0;
        PriorityQueue<MSTVertex> q = new PriorityQueue<>();
        get(s).d = 0;
        q.add(new MSTVertex(get(s)));
        while(!q.isEmpty()){
            MSTVertex u = q.poll();
            if(!get(u.ref).seen){
                get(u.ref).seen = true;
                wmst+=u.d;
                for(Edge e : g.outEdges(u.ref)){
                    Vertex v = e.otherEnd(u.ref);
                    if(!get(v).seen && e.getWeight() < get(v).d){
                          get(v).d = e.getWeight();
                          get(v).parent = u.ref;
                          q.add(new MSTVertex(get(v)));
                    }
                }
            }
        }
        return wmst;
    }

    public long prim1(Vertex s) {
        algorithm = "PriorityQueue<Edge>";
        mst = new LinkedList<>();
        wmst = 0;
        PriorityQueue<Edge> q = new PriorityQueue<>();
        get(s).seen = true;
        for(Edge e : g.outEdges(s)){
            q.add(e);
        }
        while(!q.isEmpty()){
            Edge e = q.remove();
            mst.add(e);
            Vertex u = e.fromVertex();
            Vertex v = e.toVertex();
            if(!get(u).seen){
                Vertex temp = u;
                u = v;
                v = temp;
            }
            if(get(v).seen) continue;
            get(v).seen = true;
            get(v).parent = u;
            wmst += e.getWeight();
            for(Edge e2 : g.outEdges(v)){
                if(!get(e2.otherEnd(v)).seen) q.add(e2);
            }
        }
        return wmst;
    }


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

    public static void main(String[] args) throws FileNotFoundException {
      Scanner in;
        int choice = 0;  // Kruskal
        if (args.length == 0 || args[0].equals("-")) {
            in = new Scanner(System.in);
        } else {
            File inputFile = new File(args[0]);
            in = new Scanner(inputFile);
        }

        if (args.length > 1) { choice = Integer.parseInt(args[1]); }
        /**
        String string = "4 5  1 2 10   2 3 12   2 4 2   1 4 1   4 3 2 ";
        Scanner in;
        // If there is a command line argument, use it as file from which
        // input is read, otherwise use input from string.
        in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(string);

        // Read graph from input
        Graph g = Graph.readGraph(in);
       **/
        Graph g = Graph.readGraph(in);
        Vertex s = g.getVertex(1);

        Timer timer = new Timer();
        MST m = mst(g, s, 1);
        System.out.println(m.algorithm + "\n" + m.wmst);
        System.out.println(timer.end());
    }
}