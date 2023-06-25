import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;

public abstract class GeneralizedDijkstra {

    /**
     * Runs dijkstra algorithm to find shortest paths to the given vertex
     * @param g generalized graph that wil lbe used
     * @param start starting vertex
     * @param property to select which weight will be used
     * @param op operator that will be used in computing
     * @param d distance array
     * @param p parents array
     */
    public static void dijkstra(GeneralizedGraph g, int start, int property, OperatorCreator op, double[] d, int[] p) {
        HashSet<Integer> used = new HashSet<>();

        for (int i = 0; i < g.getNumV(); ++i)
            p[i] = -1;

        for (int i = 0; i < g.getNumV(); ++i)
            d[i] = Double.POSITIVE_INFINITY;
        d[start] = 0;

        Iterator<GeneralizedEdge> iter = g.edgeIterator(start);

        while (iter.hasNext()) {
            GeneralizedEdge e = iter.next();
            d[e.getDest()] = e.getWeight()[property];
            p[e.getDest()] = e.getSource();
        }

        used.add(start);

        PriorityQueue<Pair<Integer, Double>> wait = new PriorityQueue<>(new Comparator<Pair<Integer, Double>>(){

            @Override
            public int compare(Pair<Integer, Double> o1, Pair<Integer, Double> o2) {
                return o1.getSecond().compareTo(o2.getSecond());
            }
            
        });

        for(int i = 0; i < g.getNumV(); ++i)
            wait.offer(new Pair<>(i, d[i]));

        while (!wait.isEmpty()) {

            int vertex = wait.poll().getFirst();
            iter = g.edgeIterator(vertex);
            

            while (!used.contains(vertex) && iter.hasNext()) {
                GeneralizedEdge e = iter.next();
                if(op.operation(e.getWeight()[property], d[e.getSource()]) < d[e.getDest()]){
                    p[e.getDest()] = e.getSource();
                    d[e.getDest()] = op.operation(e.getWeight()[property], d[e.getSource()]);
                    wait.offer(new Pair<>(e.getDest(), d[e.getDest()]));
                }
            }

            used.add(vertex);
        }
    }
}