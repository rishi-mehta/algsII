import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

/**
 * User: adrian
 * Date: 3/27/13
 * Time: 7:50 PM
 */
public class SAP {

    private Digraph graph;
    private HashMap<String, Integer[]> cache = new HashMap<String, Integer[]>();

    /**
     * All methods should throw a java.lang.IndexOutOfBoundsException if one (or more)
     * of the input arguments is not between 0 and G.V() - 1. You may assume that the iterable
     * arguments contain at least one integer. All methods (and the constructor) should take time
     * at most proportional to E + V in the worst case, where E and V are the number of edges and
     * vertices in the digraph, respectively. Your data type should use space proportional to E + V.
     * <p/>
     * <p/>
     * How can I make SAP immutable? You can (and should) save the associated digraph
     * in an instance variable. However, because our Digraph data type is mutable,
     * you must first make a defensive copy by calling the copy constructor.
     *
     * @param G
     */
    public SAP(Digraph G) {
        graph = new Digraph(G);

    }

    private String makeKey(int v, int w) {
        String key = String.format("%d-%d", v, w);
        return key;
    }
    private String makeKey(Iterable<Integer> v, Iterable<Integer> w){
        ArrayList<Integer> stuff=new ArrayList<Integer>();
        for(Integer i:v){
            stuff.add(i);
        }

        for(Integer i:w){
            stuff.add(i);
        }
        //sort so if the keys are in different order they still evaluate as the same
        Collections.sort(stuff);
        StringBuilder builder=new StringBuilder();
        for(Integer i:stuff){
            builder.append(i);
            builder.append("-");
        }
        return  builder.toString();
    }

    private static int ANCESTOR = 1;
    private static int LENGTH = 0;

    private Integer[] getValueArray(String key) {
        Integer[] val = cache.get(key);
        return val;

    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (!checkInputs(v, w)) throw new IndexOutOfBoundsException();

        String key = makeKey(v, w);
        Integer[] cached = getValueArray(key);
        if (null == cached) {
            cached = runBfs(v,w);
            cache.put(key, cached);
        }
        return cached[LENGTH];

    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path

    public int ancestor(int v, int w) {
        if (!checkInputs(v, w)) throw new IndexOutOfBoundsException();
        String key = makeKey(v, w);
        Integer[] cached = getValueArray(key);
        if (null == cached) {
            cached = runBfs(v, w);
            cache.put(key, cached);
        }
        return cached[ANCESTOR];
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if(!checkInputs(v,w)) throw new IndexOutOfBoundsException();


        String key = makeKey(v, w);
        Integer[] cached = getValueArray(key);
        if (null == cached) {
            cached = runBfs(v, w);
            cache.put(key, cached);
        }
        return cached[LENGTH];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if(!checkInputs(v,w)) throw new IndexOutOfBoundsException();
        String key = makeKey(v, w);
        Integer[] cached = getValueArray(key);
        if (null == cached) {
            cached = runBfs(v, w);
            cache.put(key, cached);
        }
        return cached[ANCESTOR];

    }

    // for unit testing of this class (such as the one below)
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }

    /**
     * The idea is to run bfs on both inputs simultaneously, find intersections,
     * and find the one with the lowest path cost
     * The bottleneck operation is reinitializing arrays of length V to perform the BFS computations.
     * This must be done once for the first BFS computation, but if you keep track of which array
     * entries change, you can reuse the same array from computation to computation
     * (reinitializing only those entries that changed in the previous computation).
     * This leads to a dramatic savings when only a small number of entries change
     * (which is the typical case for the wordnet digraph). Note that if you have any other
     * loops that iterates through all of the vertices, then you must eliminate those loops too in order
     * to achieve a sublinear running time.
     * If you run the breadth-first searches from v and w simultaneously,
     * then you can terminate the BFS from v (or w) as soon as the distance exceeds the length
     * of the best ancestral path found so far.
     */
    private Integer[] runBfs(int v, int w) {

        Integer[] ret = null;
        DeluxeBFS bfs1 = new DeluxeBFS(graph);

        //BreadthFirstDirectedPaths bfs=new BreadthFirstDirectedPaths(graph,v);
        //BreadthFirstDirectedPaths bfs2=new BreadthFirstDirectedPaths(graph,w);


        ret = bfs1.findCommonAncestor(v, w);
        if (ret[LENGTH] == Integer.MAX_VALUE) ret[LENGTH] = -1;
        return ret;
    }

    private Integer[] runBfs(Iterable<Integer> v, Iterable<Integer> w) {

        Integer[] ret = null;
        DeluxeBFS bfs1 = new DeluxeBFS(graph);
        ret = bfs1.findCommonAncestor(v, w);
        if (ret[LENGTH] == Integer.MAX_VALUE) ret[LENGTH] = -1;
        return ret;
    }
    private boolean checkInputs(int v, int w) {
        return !(v < 0 || w < 0 || v > graph.V() - 1 || w > graph.V() - 1);
    }
    private boolean checkInputs(Iterable<Integer>v, Iterable<Integer>w){
        for(Integer i:v){
            if(i<0 || i>graph.V() -1)return false;
        }
        for(Integer i:w){
            if(i<0 || i>graph.V() -1)return false;
        }
        return true;
    }
}
