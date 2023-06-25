public abstract class ConnectedComponentFinders {

    /**
     * Finds connected component count with BFS
     * @param graph graph that will be searched
     * @return returns connected component count
     * @throws NullPointerException
     */
    public static int findWithBFS(Graph graph) throws NullPointerException{
        if(graph == null)
            throw new NullPointerException();

        int count = 0;
        boolean[] found = new boolean[graph.getNumV()];

        for (int i = 0; i < graph.getNumV(); ++i) {
            if (!found[i]) {
                int[] parent = BreadthFirstSearch.breadthFirstSearch(graph, i);
                for (int j = 0; j < graph.getNumV(); ++j) {
                    if (parent[j] != -1) {
                        found[j] = true;
                        found[i] = true;
                    }
                }
                ++count;
            }
        }

        return count;
    }

    /**
     * Finds connected component count with DFS
     * @param graph graph that will be searched
     * @return returns connected component count
     * @throws NullPointerException
     */
    public static int findWithDFS(Graph graph) throws NullPointerException{
        if(graph == null)
            throw new NullPointerException();

        int count = 0;
        boolean[] found = new boolean[graph.getNumV()];

        for (int i = 0; i < graph.getNumV(); ++i) {
            if (!found[i]) {
                int[] finish = DepthFirstSearch.depthFirstSearch(graph, i);
                found[i] = true;
                for (int j = 0; j < finish.length; ++j)
                    if (finish[j] != -1)
                        found[finish[j]] = true;
                ++count;
            }
        }

        return count;
    }
}