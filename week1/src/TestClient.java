/**
 * User: adrian
 * Date: 3/27/13
 * Time: 7:53 PM
 */
public class TestClient {
   // Test client. The following test client takes the name of a digraph input file as as a command-line argument,
   // constructs the digraph, reads in vertex pairs from standard input, and prints out the length of the shortest
   // ancestral path between the two vertices and a common ancestor that participates in that path:

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
    /*
    Here is a sample execution:
            % more digraph1.txt             % java SAP digraph1.txt
    13                              3 11
            11                              length = 4, ancestor = 1
            7  3
            8  3                           9 12
            3  1                           length = 3, ancestor = 5
            4  1
            5  1                           7 2
            9  5                           length = 4, ancestor = 0
            10  5
            11 10                           1 6
            12 10                           length = -1, ancestor = -1
            1  0
            2  0
*/
}
