import java.util.*;

/**
 * Class to implement the depth-first search algorithm.
 * 
 * @author Koffman and Wolfgang
 */

public abstract class DepthFirstSearch {

  /** The index that indicates the discovery order. */
  private static int discoverIndex = 0;

  /** The index that indicates the finish order. */
  private static int finishIndex = 0;

  /**
   * Construct the depth-first search of a Graph selecting the start vertices in
   * the specified order. The first vertex visited is order[0].
   * 
   * @param graph The graph
   * @param order The array giving the order in which the start vertices should be
   *              selected
   */
  public static int[] depthFirstSearch(Graph g, int current) {
    boolean[] visited = new boolean[g.getNumV()];
    int[] finishOrder = new int[g.getNumV()];
    int[] discoveryOrder = new int[g.getNumV()];
    discoverIndex = 0;
    finishIndex = 0;

    for (int i = 0; i < g.getNumV(); ++i)
      finishOrder[i] = -1;

    depthFirstSearchRec(current, g, visited, finishOrder, discoveryOrder);
    return finishOrder;
  }

  /**
   * Recursively depth-first search the graph starting at vertex current.
   * 
   * @param current The start vertex
   */
  public static void depthFirstSearchRec(int current, Graph graph, boolean[] visited, int[] finishOrder,
      int[] discoveryOrder) {
    /* Mark the current vertex visited. */
    visited[current] = true;
    discoveryOrder[discoverIndex++] = current;
    /* Examine each vertex adjacent to the current vertex */
    Iterator<Edge> itr = graph.edgeIterator(current);
    while (itr.hasNext()) {
      int neighbor = itr.next().getDest();
      /* Process a neighbor that has not been visited */
      if (!visited[neighbor]) {
        /*
         * Recursively apply the algorithm starting at neighbor.
         */
        depthFirstSearchRec(neighbor, graph, visited, finishOrder, discoveryOrder);
      }
    }
    /* Mark current finished. */
    finishOrder[finishIndex++] = current;
  }

  /**** END EXERCISE ****/
}
