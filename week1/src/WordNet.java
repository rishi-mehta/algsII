import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * User: adrian
 * Date: 3/27/13
 * Time: 7:42 PM
 */
public class WordNet {
    // constructor takes the name of the two input files

    private HashMap<String, ArrayList<Integer>> nounToId = new HashMap<String, ArrayList<Integer>>(89);
    private HashMap<Integer, String> idToNoun = new HashMap<Integer, String>(89);
    private Digraph graph;
    private DeluxeBFS bfs;

    /**
     * The constructor should throw a java.lang.IllegalArgumentException if the
     * input does not correspond to a rooted DAG. The distance() and sap() methods
     * should throw a java.lang.IllegalArgumentException unless both of the noun arguments are WordNet nouns.
     * Your data type should use space linear in the input size (size of synsets
     * and hypernyms files). The constructor should take time linearithmic (or better)
     * in the input size. The method isNoun() should run in time logarithmic (or better) in
     * the number of nouns. The methods distance() and sap() should run in time linear in the
     * size of the WordNet digraph.
     *
     * @param synsets
     * @param hypernyms
     */
    public WordNet(String synsets, String hypernyms) {
        In in = new In(synsets);
        In in2 = new In(hypernyms);
        String line;
        int largestId = 0;
        int count = 0;
        while ((line = in.readLine()) != null) {
            String[] lineArr = line.split(",");
            //StdOut.println(line);
            int id = Integer.parseInt(lineArr[0]);
            if (id > largestId) largestId = id;
            String[] synset = lineArr[1].split(" ");
            ++count;
            for (String noun : synset) {

                ArrayList<Integer> ids = nounToId.get(noun);
                if (null == ids) {
                    ArrayList<Integer> l = new ArrayList<Integer>();
                    l.add(id);
                    nounToId.put(noun, l);
                    idToNoun.put(id, noun);


                } else {
                    ids.add(id);
                }

            }
        }

        graph = new Digraph(count);


        while ((line = in2.readLine()) != null) {
            String[] lineArr = line.split(",");
            Integer[] hnyms = new Integer[lineArr.length];
            for (int i = 0; i < lineArr.length; ++i) {
                hnyms[i] = Integer.parseInt(lineArr[i]);
                if (i != 0) {
                    graph.addEdge(hnyms[0], hnyms[i]);
                }
            }

        }

        bfs = new DeluxeBFS(graph);
        //System.out.println("done w/constructor");
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounToId.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return nounToId.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new java.lang.IllegalArgumentException();


        SAP sap = new SAP(graph);
        int d = 0;
        List<Integer> one = nounToId.get(nounA);
        List<Integer> two = nounToId.get(nounB);
        if (one.size() > 1 && two.size() > 1) {
            Iterable<Integer> v = nounToId.get(nounA);
            Iterable<Integer> w = nounToId.get(nounB);
            return sap.length(v, w);

        } else {
            Integer i1 = one.get(0);
            Integer i2 = two.get(0);
            return sap.length(i1, i2);
        }
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
// in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new java.lang.IllegalArgumentException();
        int a;
        SAP sap = new SAP(graph);

        List<Integer> one = nounToId.get(nounA);
        List<Integer> two = nounToId.get(nounB);
        if (one.size() > 1 && two.size() > 1) {
            Iterable<Integer> v = nounToId.get(nounA);
            Iterable<Integer> w = nounToId.get(nounB);

            a = sap.ancestor(v, w);
        } else {
            Integer i1 = one.get(0);
            Integer i2 = two.get(0);
            a = sap.ancestor(i1, i2);
        }

        return idToNoun.get(a);


    }

    // for unit testing of this class
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        //String anscestor = wordnet.sap("Black_Plague", "black_marlin");
        //System.out.println("anscestor = " + anscestor);
        int d = wordnet.distance("Black_Plague", "black_marlin");
        System.out.println("d = " + d);
    }
}
