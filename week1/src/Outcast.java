import java.util.ArrayList;

/**
 * User: adrian
 * Date: 3/27/13
 * Time: 7:55 PM
 */
public class Outcast {
    // constructor takes a WordNet object
    private WordNet wordnet;

    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;

    }

    private ArrayList<int[]> allTuples(Integer[] arr, int startIdx, ArrayList<int[]> ret) {
        if (startIdx > arr.length - 1) {
            return ret;
        }
        for (int i = startIdx; i < arr.length - 1; ++i) {
            ret.add(new int[]{arr[i], arr[i + 1]});
        }
        return ret;

    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {

        int[] sums = new int[nouns.length];
        for (int i = 0; i < nouns.length; ++i) {

            String n1 = nouns[i];
            int acc = 0;
            for (int j = 0; j < nouns.length; ++j) {
                if(i==j)continue;
                String n2 = nouns[j];
                int dist=wordnet.distance(n1, n2);
                System.out.println("n1 = " + n1);
                System.out.println("n2 = " + n2);
                System.out.println("dist = " + dist);
                acc += dist;
            }
            sums[i] = acc;


        }

        int idx = -1;
        int biggest=0;
        for (int i = 0; i < sums.length; i++) {
            int sum = sums[i];
            if(sum > biggest){
                biggest=sum;
                idx=i;
            }
        }
        return nouns[idx];
    }

    // for unit testing of this class (such as the one below)
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            String[] nouns = In.readStrings(args[t]);
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
