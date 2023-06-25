import java.security.InvalidParameterException;
import java.util.Iterator;

public abstract class ImportanceValue {
    private static int min;
    private static Graph g;
    private static boolean[] visited;

    /**
     * Calculates importance value for each vertex
     * @param graph graph that will be used
     * @return returns answers as double array
     * @throws NullPointerException
     * @throws InvalidParameterException
     */
    public static double[] importanceValueForEach(Graph graph) throws NullPointerException, InvalidParameterException{
        if(graph == null)
            throw new NullPointerException();

        if(graph.isDirected())
            throw new InvalidParameterException();

        double[] ans = new double[graph.getNumV()];

        for(int i = 0; i < graph.getNumV(); ++i)
            ans[i] = importanceValue(graph, i);

        return ans;
    }

    /**
     * To find importance value of a vertex
     * @param graph graph that will be used
     * @param vertex verex will be used for calculation of importance value
     * @return returns importance value of given vertex
     * @throws NullPointerException
     * @throws InvalidParameterException
     */
    public static double importanceValue(Graph graph, int vertex) throws NullPointerException, InvalidParameterException{
        if(graph == null)
            throw new NullPointerException();

        if(graph.isDirected())
            throw new InvalidParameterException();

        double ans = 0.0;
        g = graph;

        int[] component = DepthFirstSearch.depthFirstSearch(g, vertex); //to find connected component
        int size = 0;

        for (int i = 0; i < component.length && component[i] != -1; ++i) {
            for (int j = i + 1; component[i] != vertex && j < component.length && component[j] != -1; ++j) {
                if (component[j] != vertex) {
                    int[] count;
                    visited = new boolean[graph.getNumV()];
                    min = Integer.MAX_VALUE;
                    count = numOfPaths(component[i], vertex, component[j]);
                    if (count[0] != 0)
                        ans += ((double) count[1] / (double) count[0]);
                }
            }
            size++;
        }
        if(size != 0)
            ans /= (size * size);
            
        return ans;
    }

    private static int[] numOfPaths(int start, int vertex, int end) {
        int[] returnVal = new int[2];
        numOfPathsRec(vertex, end, start, false, 0, returnVal);
        return returnVal;
    }

    private static void numOfPathsRec(int vertex, int end, int curr, boolean found, int length, int[] ans) {
        if (curr == end) {
            if (length < min) {
                ans[0] = 0;
                ans[1] = 0;
                min = length;
            }

            if (length == min) {
                if (found)
                    ans[1]++;
                ans[0]++;
            }
            return;
        }

        if (curr == vertex)
            found = true;

        Iterator<Edge> iter = g.edgeIterator(curr);
        visited[curr] = true;

        while (iter.hasNext()) {
            Edge e = iter.next();
            if (!visited[e.getDest()]) {
                numOfPathsRec(vertex, end, e.getDest(), found, length + 1, ans);
            }
        }

        visited[curr] = false;
    }
}
