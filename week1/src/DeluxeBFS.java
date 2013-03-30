import java.util.HashMap;

/**
 * User: adrian
 * Date: 3/27/13
 * Time: 8:09 PM
 */
public class DeluxeBFS {


    /**
     * **********************************************************************
     * Compilation:  javac BreadthFirstDirectedPaths.java
     * Execution:    java BreadthFirstDirectedPaths V E
     * Dependencies: Digraph.java Queue.java Stack.java
     * <p/>
     * Run breadth first search on a digraph.
     * Runs in O(E + V) time.
     * <p/>
     * % java BreadthFirstDirectedPaths tinyDG.txt 3
     * 3 to 0 (2):  3->2->0
     * 3 to 1 (3):  3->2->0->1
     * 3 to 2 (1):  3->2
     * 3 to 3 (0):  3
     * 3 to 4 (2):  3->5->4
     * 3 to 5 (1):  3->5
     * 3 to 6 (-):  not connected
     * 3 to 7 (-):  not connected
     * 3 to 8 (-):  not connected
     * 3 to 9 (-):  not connected
     * 3 to 10 (-):  not connected
     * 3 to 11 (-):  not connected
     * 3 to 12 (-):  not connected
     * <p/>
     * ***********************************************************************
     */

    private static final int INFINITY = Integer.MAX_VALUE;

    private boolean[] marked;  // marked[v] = is there an s->v path?
    private int[] edgeTo;      // edgeTo[v] = last edge on shortest s->v path
    private int[] distTo;      // distTo[v] = length of shortest s->v path
    private HashMap<Integer, Integer> markedIdxs = new HashMap<Integer, Integer>();
    private Digraph G;

    // single source
    public DeluxeBFS(Digraph G) {
        this.G = G;
        reset();
    }

    private void reset() {
        reinit();
        markedIdxs.clear();
    }


    private void reinit() {

        if (null == distTo) {
            distTo = new int[G.V()];
            for (int v = 0; v < G.V(); v++) distTo[v] = INFINITY;
        }
        if (null == edgeTo) edgeTo = new int[G.V()];

        if (null == marked) {
            marked = new boolean[G.V()];
        } else {
            for (int i : markedIdxs.keySet()) {
                marked[i] = false;
                distTo[i] = INFINITY;
                edgeTo[i] = 0;
            }
        }

    }

    public Integer[] findCommonAncestor(int v, int w) {
        reset();
        bfs(G, v);
        reinit();
        return bfs(G, w);
    }

    public Integer[] findCommonAncestor(Iterable<Integer> v, Iterable<Integer> w) {
        reset();
        bfs(G, v);
        reinit();
        return bfs(G, w);
    }

    // BFS from single source
    private Integer[] bfs(Digraph G, int s) {

        int bestDistanceSoFar = INFINITY;
        int bestAncestorSoFar = -1;

        boolean secondRun = false;
        if (!markedIdxs.isEmpty()) {
            secondRun = true;
        }
        Queue<Integer> q = new Queue<Integer>();
        marked[s] = true;
        distTo[s] = 0;
        q.enqueue(s);
        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : G.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;

                    if (secondRun) {
                        //no need to keep searching if we are farther out than something
                        //we found already

                        if (distTo[w] > bestDistanceSoFar) {
                            secondRun = false;
                            markedIdxs.clear();
                            break;
                        }

                        if (markedIdxs.containsKey(w)) {

                            //both distances combined
                            bestDistanceSoFar = distTo[w] + markedIdxs.get(w);
                            bestAncestorSoFar = w;


                        }

                    } else {

                        markedIdxs.put(w, distTo[w]);
                    }


                    q.enqueue(w);
                }
            }
        }
        return new Integer[]{bestDistanceSoFar, bestAncestorSoFar};
    }

    // BFS from multiple sources
    private Integer[] bfs(Digraph G, Iterable<Integer> sources) {


        int bestDistanceSoFar = INFINITY;
        int bestAncestorSoFar = -1;
        boolean secondRun = false;
        if (!markedIdxs.isEmpty()) {
            secondRun = true;
        }

        Queue<Integer> q = new Queue<Integer>();
        for (int s : sources) {
            marked[s] = true;
            distTo[s] = 0;
            q.enqueue(s);
        }
        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : G.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;
                    if (secondRun) {
                        //no need to keep searching if we are farther out than something
                        //we found already
                        if (distTo[w] >= bestDistanceSoFar) {
                            secondRun = false;
                            markedIdxs.clear();
                            break;
                        }
                        if (markedIdxs.containsKey(w)) {

                            //both distances combined
                            bestDistanceSoFar = distTo[w] + markedIdxs.get(w);
                            bestAncestorSoFar = w;


                        }

                    } else {

                        markedIdxs.put(w, distTo[w]);
                    }

                    q.enqueue(w);
                }
            }
        }
        return new Integer[]{bestDistanceSoFar, bestAncestorSoFar};
    }

    // length of shortest path from s (or sources) to v
    public int distTo(int v) {
        return distTo[v];
    }

    // is there a directed path from s (or sources) to v?
    public boolean hasPathTo(int v) {
        return marked[v];
    }

    // shortest path from s (or sources) to v; null if no such path
    public Iterable<Integer> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<Integer> path = new Stack<Integer>();
        int x;
        for (x = v; distTo[x] != 0; x = edgeTo[x])
            path.push(x);
        path.push(x);
        return path;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        // StdOut.println(G);

        int s = Integer.parseInt(args[1]);
        DeluxeBFS bfs = new DeluxeBFS(G);

        for (int v = 0; v < G.V(); v++) {
            if (bfs.hasPathTo(v)) {
                StdOut.printf("%d to %d (%d):  ", s, v, bfs.distTo(v));
                for (int x : bfs.pathTo(v)) {
                    if (x == s) StdOut.print(x);
                    else StdOut.print("->" + x);
                }
                StdOut.println();
            } else {
                StdOut.printf("%d to %d (-):  not connected\n", s, v);
            }

        }
    }


}
